package com.example.myapplication.teachstudactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

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

import com.example.myapplication.R;
import com.example.myapplication.sendRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class acceptRequest extends AppCompatActivity {


    private ImageView profileImageView;

    private TextView institution, address, Class, name;

    private Button acceptButton, rejectButton;
    private Button editProfile;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);


        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        Class = findViewById(R.id.Class);
        name = findViewById(R.id.email);
        acceptButton= findViewById(R.id.accept);
        rejectButton = findViewById(R.id.reject);


        String std_id = getIntent().getStringExtra("student_id");
        String teach_id = getIntent().getStringExtra("teacher_id");

       // Toast.makeText(acceptRequest.this, userUid, Toast.LENGTH_SHORT).show();


        if (std_id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(std_id);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String Name = documentSnapshot.getString("Name");
                    String Institution = documentSnapshot.getString("Institution");
                    String Address = documentSnapshot.getString("Address");
                    String CClass = documentSnapshot.getString("Class");
                    name.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    Class.setText(CClass);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(acceptRequest.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        db = FirebaseFirestore.getInstance();


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = db.collection("Student").document(std_id).collection("TeacherID").document(teach_id);
                Map<String, Object> emptyData = new HashMap<>();
// You don't need to add any data to the document

                documentReference.set(emptyData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Map<String, Object> request = new HashMap<>();
                                request.put("teacherID", teach_id);
                                request.put("message","accept your request");
                                request.put("type", 2);

                                db.collection("Student").document(std_id).collection("notifications")
                                        .add(request)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                // Add your success message here
                                                Toast.makeText(acceptRequest.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                                // Add your failure message here
                                                Toast.makeText(acceptRequest.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                                // Document with ID successfully created

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors
                            }
                        });
                DocumentReference docu = db.collection("Teacher").document(teach_id).collection("studentID").document(std_id);
                Map<String, Object> empty1Data = new HashMap<>();
// You don't need to add any data to the document

                docu.set(empty1Data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(acceptRequest.this,"Request Accepted", Toast.LENGTH_SHORT).show();
                                // Document with ID successfully created
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors
                            }
                        });

                db.collection("Teacher").document(teach_id).collection("notifications")
                        .whereEqualTo("studentID", std_id)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("Teacher").document(teach_id).collection("notifications")
                                            .document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                                            .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });

                Intent  intent = new Intent(acceptRequest.this, teachnotify.class);
                startActivity(intent);
            }


        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documentIdToRemove = "your_document_id"; // Replace with the ID you want to remove
                CollectionReference collectionReference = db.collection("Teacher").document(teach_id).collection("notifications");

                Toast.makeText(acceptRequest.this,"Request Removed", Toast.LENGTH_SHORT).show();



                db.collection("Teacher").document(teach_id).collection("notifications")
                        .whereEqualTo("studentID", std_id)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("Teacher").document(teach_id).collection("notifications")
                                            .document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                                            .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });

                Intent  intent = new Intent(acceptRequest.this, teachnotify.class);
                startActivity(intent);

            }
        });

    }


}
