package com.example.myapplication.studteachactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.R;
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
    Button button;
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Format the selected date to match your database structure
                stringDateSelected = String.format("%04d-%02d-%02d", year, month + 1, day);

                // Change the background color of the EditText to indicate selection
                editText.setBackgroundColor(getResources().getColor(R.color.lavender)); // Set the color you want
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
