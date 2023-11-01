package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        boolean student = false;
        Button stdbutton = findViewById(R.id.studentbutton);
        Button teachbutton = findViewById(R.id.teacherbutton);
        stdbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setContentView(R.layout.signlogin);
                Button signUp = findViewById(R.id.signup);
                Button back = findViewById(R.id.back);
                Button login = findViewById(R.id.login);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gostdLogin();
                    }
                });
                signUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity2.this, studentSignup.class);
                        startActivity(intent);
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        teachbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.signlogin);
                Button login = findViewById(R.id.login);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goteachlogin();
                    }
                });
                Button signUp = findViewById(R.id.signup);
                signUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goTecSignup();
                    }
                });
                Button back = findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);

                    }
                });
            }

        });
    }
    public void gostdLogin(){
        Intent intent = new Intent(this, studentlogin.class);
        startActivity(intent);
    }
    public void goteachlogin(){
        Intent intent = new Intent(this, teacherlogin.class);
        startActivity(intent);
    }
    public void goTecSignup(){
        Intent intent = new Intent(this, TeacherSignUp.class);
        startActivity(intent);
    }


}