package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class sendRequest extends AppCompatActivity {


    private TextView institution, address, semester, name, py;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);


        institution = findViewById(R.id.institution);
        address = findViewById(R.id.address);
        semester = findViewById(R.id.semester);
        name = findViewById(R.id.name);
        py = findViewById(R.id.payment);

        String userUid = getIntent().getStringExtra("user_id");


        if (userUid != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userUid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String Name = documentSnapshot.getString("Name");
                    String Institution = documentSnapshot.getString("Institution");
                    String Address = documentSnapshot.getString("Address");
                    String CClass = documentSnapshot.getString("Class");
                    String payment = documentSnapshot.getString("payment");


                    //Toast.makeText(sendRequest.this,Name+CClass, Toast.LENGTH_SHORT).show();
                    name.setText(Name);
                    institution.setText(Institution);
                    address.setText(Address);
                    semester.setText(CClass);
                    py.setText(payment);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(sendRequest.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
