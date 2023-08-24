package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class appstart extends AppCompatActivity {


    private FirebaseFirestore db;
    private Button logOut;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        String Username = getIntent().getStringExtra("user_uid");
        TextView username = findViewById(R.id.Textname);
        username.setText(Username);
        logOut = findViewById(R.id.LogOut);

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

