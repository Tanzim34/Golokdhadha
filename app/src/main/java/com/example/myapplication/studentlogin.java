package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class studentlogin extends AppCompatActivity {
    EditText userName , passWord;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        Button button = findViewById(R.id.slogin);
        userName = findViewById(R.id.sUsername);
        passWord = findViewById(R.id.sPassword);
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
            Toast.makeText(studentlogin.this,"Successfull", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(studentlogin.this,"Invalid", Toast.LENGTH_SHORT).show();
    }

}
