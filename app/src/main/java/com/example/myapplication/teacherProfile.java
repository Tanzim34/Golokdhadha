package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class teacherProfile extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageButton addPhotoButton;
    private ImageView profileImageView;
    private Button galleryButton;
    private TextView institution, address, semester, name, pay;
    private Button editProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        addPhotoButton = findViewById(R.id.addphoto);
        profileImageView = findViewById(R.id.imageView5);
        galleryButton = findViewById(R.id.galleryButton);
        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        semester = findViewById(R.id.semester);
        name = findViewById(R.id.email);
        editProfile = findViewById(R.id.button);
        pay = findViewById(R.id.payment);
        String userUid = getIntent().getStringExtra("user_id");
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherProfile.this, teacherEditProfile.class);
                intent.putExtra("user_id", userUid);
                startActivity(intent);
                finish();
            }
        });

        if (userUid != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userUid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String Name = documentSnapshot.getString("Name");
                    String Institution = documentSnapshot.getString("Institution");
                    String Address = documentSnapshot.getString("Address");
                    String CClass = documentSnapshot.getString("Semester");
                    String payment = documentSnapshot.getString("payment");
                    name.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    semester.setText(CClass);
                    pay.setText(payment);
                    String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                    if (profileImageUrl != null) {
                        // Load profile image using a library like Picasso or Glide
                        Picasso.get().load(profileImageUrl).into(profileImageView);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(teacherProfile.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Camera app not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();
            // Upload the image to Firebase Storage and update the user's profile
            uploadImageToStorage(selectedImageUri);
        }
    }

    private void uploadImageToStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String userId = getIntent().getStringExtra("user_id");
        if (userId != null) {
            StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            updateProfileImageInFirestore(userId, uri.toString());
                            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.e("FirebaseStorage", "Image upload failed", e);
                    });
        }
    }

    private void updateProfileImageInFirestore(String userId, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        // Update the 'profileImageUrl' field with the image URL
        userRef.update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Profile image URL updated in Firestore
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}
