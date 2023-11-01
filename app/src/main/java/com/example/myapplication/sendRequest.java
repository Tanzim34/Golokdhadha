package com.example.myapplication;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class sendRequest extends AppCompatActivity {


    private TextView institution, address, semester, name, py;

    private Button sendRq;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);


        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        semester = findViewById(R.id.semester);
        name = findViewById(R.id.name);
        py = findViewById(R.id.payment);

        sendRq = findViewById(R.id.sendRequest);

        String userUid = getIntent().getStringExtra("user_id");
        String studentUid = getIntent().getStringExtra("student_id");
        sendRq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userUid != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(userUid);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> request = new HashMap<>();
                            request.put("studentID", studentUid);
                            request.put("message","send you a request");
                            request.put("type", 1);

                            db.collection("Teacher").document(userUid).collection("notifications")
                                    .add(request)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            // Add your success message here
                                            Toast.makeText(sendRequest.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                            // Add your failure message here
                                            Toast.makeText(sendRequest.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(sendRequest.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
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
                    String CClass = documentSnapshot.getString("Class");
                    String payment = documentSnapshot.getString("payment");


                    //Toast.makeText(sendRequest.this,Name+CClass, Toast.LENGTH_SHORT).show();
                    name.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    semester.setText(CClass);
                    py.setText(payment);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(sendRequest.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
