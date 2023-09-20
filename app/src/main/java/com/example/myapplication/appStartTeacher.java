package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class appStartTeacher extends AppCompatActivity {

    private FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button logout, profile, studentlist;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start_teacher);
        TextView username = findViewById(R.id.name);
        logout = findViewById(R.id.logOut);
        profile = findViewById(R.id.myProfile);
        studentlist = findViewById(R.id.myStudents);
        String userUid = getIntent().getStringExtra("user_uid");
        if(userUid != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userUid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString("Name");
                    username.setText(name);
                    // Toast.makeText(appstart.this, name, Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(appStartTeacher.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appStartTeacher.this, teacherProfile.class);
                intent.putExtra("user_id", userUid);
                startActivity(intent);
                finish();
            }
        });
        studentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appStartTeacher.this, teacherStudent.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(appStartTeacher.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}