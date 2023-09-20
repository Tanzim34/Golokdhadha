package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class studentlogin extends AppCompatActivity {

    private EditText userName , passWord;
    private TextView forgotPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        Button button = findViewById(R.id.slogin);
        userName = findViewById(R.id.sUsername);
        passWord = findViewById(R.id.sPassword);
        forgotPassword = findViewById(R.id.forgerPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tSignIn();
            }
        });
    }
    void tSignIn() {
        String user = userName.getText().toString();
        String pass = passWord.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            Toast.makeText(studentlogin.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        }
        else {
            teacherLogFire(user, pass);
        }
    }
    private void forgotPassword(){
        String email = userName.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(studentlogin.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(studentlogin.this, "Password reset email sent. Check your inbox.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(studentlogin.this, "Failed to send password reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void teacherLogFire(String userName, String passWord) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(studentlogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("users").document(uid);
                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String type = documentSnapshot.getString("type");
                                if(type.equals("2")) Toast.makeText(studentlogin.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                else {
                                    Intent intent = new Intent(studentlogin.this, appstart.class);
                                    intent.putExtra("user_uid", uid);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        // Assuming display name is set during registration

                        // Create user profile document if not already created

                        // Proceed to the next activity
                    }
                } else {
                    Toast.makeText(studentlogin.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}