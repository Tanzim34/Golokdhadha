package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class teacherEditProfile extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageButton addPhotoButton;
    private ImageView profileImageView;
    private Button galleryButton;
    private EditText institution, address, Class, userName;
    private TextView name;
    private Button doneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        addPhotoButton = findViewById(R.id.addphoto);
        profileImageView = findViewById(R.id.imageView5);
        galleryButton = findViewById(R.id.galleryButton);
        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        Class = findViewById(R.id.Class);
        name = findViewById(R.id.email);
        userName = findViewById(R.id.name);
        doneButton = findViewById(R.id.doneButton);
        String userUid = getIntent().getStringExtra("user_id");
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
                    name.setText(Name);
                    userName.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    Class.setText(CClass);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(teacherEditProfile.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateUserName = userName.getText().toString();
                String updateInstitution = institution.getText().toString();
                String updateAddress = address.getText().toString();
                String updateClass = Class.getText().toString();
                if(userUid != null){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef =db.collection("users").document(userUid);
                    userRef.update(
                            "Name", updateUserName,
                            "Institution", updateInstitution,
                            "Address", updateAddress,
                            "Semester", updateClass
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(teacherEditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            // Navigate profile or any other desired activity
                            Intent intent = new Intent(teacherEditProfile.this, teacherProfile.class);
                            intent.putExtra("user_id", userUid);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(teacherEditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}