package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.studteachactivity.stdcalender;
import com.example.myapplication.studteachactivity.stdchat;
import com.example.myapplication.studteachactivity.stdnotify;
import com.example.myapplication.studteachactivity.stdpayment;
import com.example.myapplication.studteachactivity.stdtask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class teacherStudent extends AppCompatActivity {

    ImageView home,chat,task,calender,payment, notify;
    FirebaseFirestore db;
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

        String std_id = getIntent().getStringExtra("student_id");
        String teach_id = getIntent().getStringExtra("teacher_id");

        TextView studentNm = findViewById(R.id.stuName);
        TextView teacherNm = findViewById(R.id.teachname);

        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(std_id);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Name = documentSnapshot.getString("Name");
                String Institution = documentSnapshot.getString("Institution");
                String Address = documentSnapshot.getString("Address");
                String CClass = documentSnapshot.getString("Class");
                studentNm.setText(Name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(teacherStudent.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
        userRef = db.collection("users").document(teach_id);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Name = documentSnapshot.getString("Name");
                String Institution = documentSnapshot.getString("Institution");
                String Address = documentSnapshot.getString("Address");
                String CClass = documentSnapshot.getString("Class");
                teacherNm.setText(Name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(teacherStudent.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, homeprofile.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, stdchat.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, stdtask.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, stdcalender.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, stdpayment.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherStudent.this, stdnotify.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id", teach_id);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
                finish();
            }
        });


    }
}