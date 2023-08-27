package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class appStartTeacher extends AppCompatActivity {

    private FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button logout, profile, studentlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start_teacher);
        String username = getIntent().getStringExtra("user_uid");
        TextView Username = findViewById(R.id.Textname);
        Username.setText(username);
        logout = findViewById(R.id.LogOut);

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