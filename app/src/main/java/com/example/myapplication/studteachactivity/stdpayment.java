package com.example.myapplication.studteachactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.addTeacher;
import com.example.myapplication.studentTeacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class stdpayment extends AppCompatActivity {

    Button donebutton, back;
    TextView paymentStatus;
    String std_id,teach_id;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdpayment);

        ImageButton bkashPaymentButton = findViewById(R.id.bkashPaymentButton);
        ImageButton nagadPaymentButton = findViewById(R.id.nagadPaymentButton);
        donebutton = findViewById(R.id.donebutton);
        back = findViewById(R.id.paymentback);
        paymentStatus = findViewById(R.id.paymentStatus);
        std_id = getIntent().getStringExtra("student_id");
        teach_id = getIntent().getStringExtra("teacher_id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stdpayment.this, studentTeacher.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id",teach_id);
                startActivity(intent);
                finish();
            }
        });

        // Handle bKash Payment Button Click
        bkashPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the bKash app is installed
                if (isAppInstalled("com.bKash.customerapp")) {
                    // Open the bKash app
                    openApp("com.bKash.customerapp");
                } else {
                    // Prompt the user to download the bKash app from Google Play
                    showDownloadPrompt("bKash");
                }
            }
        });

        // Handle Nagad Payment Button Click
        nagadPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the Nagad app is installed
                if (isAppInstalled("com.nagad.customerapp")) {
                    // Open the Nagad app
                    openApp("com.nagad.customerapp");
                } else {
                    // Prompt the user to download the Nagad app from Google Play
                    showDownloadPrompt("Nagad");
                }
            }
        });

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paymentStatus.setText("PAID");
                db = FirebaseFirestore.getInstance();


                Map<String, Object> request = new HashMap<>();
                request.put("teacherID", teach_id);
                request.put("message"," added a new Task");
                request.put("type", 4);

                db.collection("Teacher").document(teach_id).collection("notifications")
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                // Add your success message here
                                Toast.makeText(stdpayment.this, "Payment done successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(stdpayment.this, addTeacher.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                // Add your failure message here
                                Toast.makeText(stdpayment.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                            }
                        });
                // set notification

                Intent intent  = new Intent(stdpayment.this, studentTeacher.class);
                intent.putExtra("student_id",std_id);
                intent.putExtra("teacher_id",teach_id);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void openApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void showDownloadPrompt(String appName) {
        // You can customize the message and action here.
        String message = "The " + appName + " app is not installed. Would you like to download it from Google Play?";
        Uri uri = Uri.parse("market://details?id=" + getAppPackageName(appName));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Show a dialog or toast to ask the user to download the app
        // You can also use a library for a more sophisticated dialog.
    }

    private String getAppPackageName(String appName) {
        if ("bKash".equals(appName)) {
            return "com.bKash.customerapp";
        } else if ("Nagad".equals(appName)) {
            return "com.nagad.customerapp";
        }
        return "";
    }
}
