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

public class teacherlogin extends AppCompatActivity {

    EditText userName , passWord;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);
        Button button = findViewById(R.id.tlogin);
        userName = findViewById(R.id.tUsername);
        passWord = findViewById(R.id.tPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              sSignIn();
            }
        });
    }
    void sSignIn() {
        String user = userName.getText().toString();
        String pass = passWord.getText().toString();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            Toast.makeText(teacherlogin.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        } else {
            teacherLogFire(user, pass);
        }
    }

    public void teacherLogFire(String userName, String passWord) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(teacherlogin.this, new OnCompleteListener<AuthResult>() {
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
                                String s = documentSnapshot.getString("type");
                                if (s.equals("1"))
                                    Toast.makeText(teacherlogin.this, "Login failed ", Toast.LENGTH_SHORT).show();
                                else {
                                   // System.out.println("LALALALA");
                                    Intent intent = new Intent(teacherlogin.this, appStartTeacher.class);
                                    intent.putExtra("user_uid", uid);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        // Assuming display name is set during registration

                        // Create user profile document if not already created

                        // Proceed to the next activity

                    } else {
                        Toast.makeText(teacherlogin.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else  Toast.makeText(teacherlogin.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}