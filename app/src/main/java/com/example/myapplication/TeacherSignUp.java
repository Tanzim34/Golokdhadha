package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.StartupTime;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TeacherSignUp extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText tUsername, tAddress, tInstitution, tSemester, tPassword, tCpassword, tName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);
        Button button = findViewById(R.id.tSignup);
        // progressBar = findViewById(R.id.progressBar)
        tUsername = findViewById(R.id.tUsername);
        tPassword = findViewById(R.id.tPassword);
        tInstitution = findViewById(R.id.tInstitution);
        tSemester= findViewById(R.id.tSemester);
        tAddress = findViewById(R.id.tAddress);
        // sPassword = findViewById(R.id.sPassword);
        tCpassword = findViewById(R.id.tConfirm);
        tName = findViewById(R.id.tName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tSignUp();
            }
        });
    }
    void tSignUp(){
        String name = tName.getText().toString();
        String user = tUsername.getText().toString();
        String pass = tPassword.getText().toString();
        String Institution = tInstitution.getText().toString();
        String Class = tSemester.getText().toString();
        String Address = tAddress.getText().toString();
        // String password = sPassword.getText().toString();
        String confirm = tCpassword.getText().toString();

        if(TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(user) ||
                TextUtils.isEmpty(pass) ||
                TextUtils.isEmpty(Institution) ||
                TextUtils.isEmpty(Class) ||
                TextUtils.isEmpty(Address) ||
                TextUtils.isEmpty(confirm))
        {
            Toast.makeText(TeacherSignUp.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        }
        // check if the class is between 1 - 12
        else{
            //  Toast.makeText(studentSignup.this, "asif", Toast.LENGTH_SHORT).show();
            StudentSignUpFire(name,user,pass,Institution, Class, Address);
        }
    }

    public void StudentSignUpFire(String name, String username, String password, String institution, String Class, String address){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // studentInfo st = new studentInfo(name, institution,address, Class);

        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(TeacherSignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TeacherSignUp.this, "successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();

                    user.sendEmailVerification();
                    Map<String, Object> userProfile = new HashMap<>();
                    String UID = user.getUid();
                    userProfile.put("Name", name);
                    userProfile.put("Institution", institution);
                    userProfile.put("Address", address);
                    userProfile.put("Class", Class);
                    db.collection("users").document(UID)
                            .set(userProfile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TeacherSignUp.this,"Failed",Toast.LENGTH_SHORT).show();

                                }
                            });
                    Intent intent = new Intent(TeacherSignUp.this, studentlogin.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK
                            |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TeacherSignUp.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //else                Toast.makeText(studentSignup.this, "Dibbyo", Toast.LENGTH_SHORT).show();

            }
        });
    }
}