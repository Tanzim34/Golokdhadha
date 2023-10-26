package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.studteachactivity.stdcalender;
import com.example.myapplication.studteachactivity.stdchat;
import com.example.myapplication.studteachactivity.stdnotify;
import com.example.myapplication.studteachactivity.stdpayment;
import com.example.myapplication.studteachactivity.stdtask;

public class studentTeacher extends AppCompatActivity {

ImageView home,chat,task,calender,payment, notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_teacher);
        home = findViewById(R.id.homebutton);
        chat = findViewById(R.id.chatbutton);
        task = findViewById(R.id.taskbutton);
        calender = findViewById(R.id.calenderbutton);
        payment = findViewById(R.id.paymentbutton);
        notify = findViewById(R.id.notifybutton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView studentNm = findViewById(R.id.stuName);
        TextView teacherNm = findViewById(R.id.teachname);

        String std_id = getIntent().getStringExtra("student_id");
        String teach_id = getIntent().getStringExtra("teacher_id");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, homeprofile.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, chatwindo.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, stdtask.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, stdcalender.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, stdpayment.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentTeacher.this, stdnotify.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });


    }
}