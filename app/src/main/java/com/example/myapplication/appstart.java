package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class appstart extends AppCompatActivity {


    private FirebaseFirestore db;
    private Button logOut, profile, teacherList;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        TextView username = findViewById(R.id.Textname);
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
                    Toast.makeText(appstart.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


        profile = findViewById(R.id.Profile);
        teacherList = findViewById(R.id.MyTeachers);
        logOut = findViewById(R.id.LogOut);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appstart.this, studentProfile.class);
                intent.putExtra("user_id",userUid);
                startActivity(intent);
            }
        });
        teacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appstart.this, studentTeacher.class);
                startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(appstart.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        }

    }

