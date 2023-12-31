package com.example.myapplication.studteachactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.studentTeacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class stdcalender extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText editText;
    Button button, back;
    private String stringDateSelected;
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

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Student").document(std_id).collection("TeacherID").document(teach_id).collection("Calender");

        button = findViewById(R.id.button);
        back = findViewById(R.id.calenderback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stdcalender.this, studentTeacher.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id",teach_id);
                startActivity(intent);
                finish();

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Format the selected date to match your database structure
                stringDateSelected = String.format("%04d-%02d-%02d", year, month + 1, day);

                // Reset the background color for all dates
                for (int i = 0; i < calendarView.getChildCount(); i++) {
                    View child = calendarView.getChildAt(i);
                    child.setBackgroundResource(0);
                }

                // Get the day of the month of the selected date
                int dayOfMonth = calendarView.getLayoutMode();

                // Find the child view for the selected date and change its background color
                for (int i = 0; i < calendarView.getChildCount(); i++) {
                    View child = calendarView.getChildAt(i);
                    if (child instanceof TextView) {
                        TextView dayView = (TextView) child;
                        if (dayView.getText().toString().equals(String.valueOf(dayOfMonth))) {
                            // Change the background color of the selected date
                            child.setBackgroundResource(R.drawable.selected_date_background); // Set the drawable you want
                        }
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a reference to the selected date in the collection
                DocumentReference documentReference = collectionReference.document(stringDateSelected);

                // Create a data map with the event details
                Map<String, Object> eventData = new HashMap<>();
                eventData.put("Date", stringDateSelected);
                eventData.put("EventDescription", editText.getText().toString());

                // Add the event to Firestore
                documentReference.set(eventData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added for date: " + stringDateSelected);
                                Toast.makeText(stdcalender.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(stdcalender.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
