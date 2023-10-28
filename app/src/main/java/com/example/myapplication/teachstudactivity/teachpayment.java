package com.example.myapplication.teachstudactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class teachpayment extends AppCompatActivity {

    TextView mobileNo, payment_amount, payment_status;
    EditText set_amount;
    Button done,cng_status;
    FirebaseFirestore db;
    String std_id, teach_id;
    String amount,status, amount1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachpayment);
        done = findViewById(R.id.donebutton);
        cng_status = findViewById(R.id.statusbutton);
        set_amount = findViewById(R.id.PaymentStatus);
        mobileNo = findViewById(R.id.mobile);
        payment_amount = findViewById(R.id.amount);
        payment_status = findViewById(R.id.paymentStatus);
        std_id = getIntent().getStringExtra("student_id");
        teach_id = getIntent().getStringExtra("teacher_id");

        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Student").document(std_id);
        documentReference = documentReference.collection("teacherID").document(teach_id);
        CollectionReference collectionReference = documentReference.collection("payment");
        DocumentReference paymentdocu = collectionReference.document("payment_" + std_id + "_" + teach_id);
        paymentdocu.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                amount = documentSnapshot.getString("Amount");
                status = documentSnapshot.getString("Status");
            }
        });
        if(amount!=null) payment_amount.setText(amount);
        else payment_amount.setText("10000");
        if(status==null) status = "DUE";
        if(status.equals("DUE")) payment_status.setTextColor(Color.RED);
        else payment_status.setTextColor(Color.GREEN);
        payment_status.setText(status);

        cng_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("Student").document(std_id);
                documentReference = documentReference.collection("teacherID").document(teach_id);
                CollectionReference collectionReference = documentReference.collection("payment");
                DocumentReference paymentdocu = collectionReference.document("payment_" + std_id + "_" + teach_id);

                paymentdocu.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        amount = documentSnapshot.getString("Amount");
                        status = documentSnapshot.getString("Status");

                        // Check if status is not null before comparing
                        if (status != null) {
                            if (status.equals("DUE")) {
                                status = "PAID";
                                payment_status.setTextColor(Color.RED); // Set text color to red
                            } else {
                                status = "DUE";
                                payment_status.setTextColor(Color.BLACK); // Set text color back to black (or any other default color)
                            }

                            // Create a map with the updated values
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("Amount", amount);
                            updateData.put("Status", status);

                            // Update the document with the new values
                            paymentdocu.update(updateData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document updated successfully
                                            // You can add code here to handle success
                                            // For example, display a success message
                                            Toast.makeText(teachpayment.this, "Payment status updated successfully.", Toast.LENGTH_SHORT).show();

                                            // You can also perform additional actions here, if needed
                                            // For example, you can update the UI to reflect the changes.
                                            payment_status.setText(status);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error, for example, displaying an error message
                                            Toast.makeText(teachpayment.this, "Failed to update payment status.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle the case where status is null
                        }
                    }
                });
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("Student").document(std_id);
                documentReference = documentReference.collection("teacherID").document(teach_id);
                CollectionReference collectionReference = documentReference.collection("payment");
                DocumentReference paymentdocu = collectionReference.document("payment_" + std_id + "_" + teach_id);

                paymentdocu.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        amount = documentSnapshot.getString("Amount");
                        status = documentSnapshot.getString("Status");
                        amount1 = set_amount.getText().toString();
                        if(!TextUtils.isEmpty(amount1)) amount = amount1;

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("Amount", amount);
                        updateData.put("Status", status);
                        paymentdocu.update(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Document updated successfully
                                       // Toast.makeText(teachpayment.this, "Payment status updated successfully.", Toast.LENGTH_SHORT).show();
                                        payment_status.setText(status);
                                        if(status.equals("DUE")) payment_status.setTextColor(Color.RED);
                                        else payment_status.setTextColor(Color.GREEN);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle the error, for example, displaying an error message
                                       // Toast.makeText(teachpayment.this, "Failed to update payment status.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
            }
        });


    }
}