package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class teacherlogin extends AppCompatActivity {

    EditText userName , passWord;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);
        Button button = findViewById(R.id.tlogin);
        userName = findViewById(R.id.tUsername);
        passWord = findViewById(R.id.tPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSignIn();
            }
        });
    }
    void sSignIn(){
        String user = userName.getText().toString();
        String pass = passWord.getText().toString();
        if(user.equals("admin") && pass.equals("admin")){
            Toast.makeText(teacherlogin.this,"Successfull", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(teacherlogin.this,"Invalid", Toast.LENGTH_SHORT).show();
    }

}