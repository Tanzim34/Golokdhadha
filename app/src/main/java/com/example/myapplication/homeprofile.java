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

public class homeprofile extends AppCompatActivity {


    private TextView institution, address, semester, name, py;

    Button back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeprofile);


        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        semester = findViewById(R.id.semester);
        name = findViewById(R.id.name);
        py = findViewById(R.id.payment);
        back = findViewById(R.id.profileback);

        String userUid = getIntent().getStringExtra("user_id");
        String student_id = getIntent().getStringExtra("student_id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeprofile.this, studentTeacher.class);
                intent.putExtra("student_id",student_id);
                intent.putExtra("teacher_id", userUid);
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
                    String CClass = documentSnapshot.getString("Class");
                    String payment = documentSnapshot.getString("payment");


                    Toast.makeText(homeprofile.this,Name+CClass, Toast.LENGTH_SHORT).show();
                    name.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    semester.setText("2020-21");
                    py.setText("1200");



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(homeprofile.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
