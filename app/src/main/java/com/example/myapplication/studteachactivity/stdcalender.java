package com.example.myapplication.studteachactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.sendRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class stdcalender extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText editText;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public String std_id, teach_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdcalender);

        calendarView = findViewById(R.id.calendarView);
        editText = findViewById(R.id.editText);
        std_id = getIntent().getStringExtra("student_id");
        teach_id = getIntent().getStringExtra("teacher_id");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                stringDateSelected = i + Integer.toString(i1+1) + i2;
                calendarClicked();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Calender");
        if(databaseReference==null)  Toast.makeText(stdcalender.this, "Error in database", Toast.LENGTH_SHORT).show();
        databaseReference = databaseReference.child("stdID_" + std_id + "teachID_" + teach_id);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Student").document(std_id).collection("TeacherID").document(teach_id).collection("Calender");

    }

    private void calendarClicked(){

        DocumentReference documentReference = collectionReference.document(stringDateSelected);


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> request = new HashMap<>();
                request.put("Date", stringDateSelected);

                collectionReference
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                // Add your success message here
                                Toast.makeText(stdcalender.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                // Add your failure message here
                                Toast.makeText(stdcalender.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(stdcalender.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void buttonSaveEvent(View view){
        databaseReference.child(stringDateSelected).setValue(editText.getText().toString());
    }
}