package com.example.myapplication.teachstudactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;

public class teachcalender extends AppCompatActivity {

    String std_id, teach_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachcalender);

        std_id = getIntent().getStringExtra("student_id");
        teach_id = getIntent().getStringExtra("teacher_id");
    }
}