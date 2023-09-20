package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class TeacherSignUp extends AppCompatActivity {

    private EditText name,email , institution, address, semester, password, confirmPassword;
    FirebaseFirestore db;
    private Button signUpButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        institution = findViewById(R.id.institution);
        semester = findViewById(R.id.semester);
        address = findViewById(R.id.address);
        confirmPassword = findViewById(R.id.confirmPassword);
        email = findViewById(R.id.email);
        signUpButton = findViewById(R.id.signUp);
        db = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }



    void signUp(){
        String getName = name.getText().toString();
        String getEmail = email.getText().toString();
        String getAddress = address.getText().toString();
        String getPassword = password.getText().toString();
        String getInstitution = institution.getText().toString();
        String getSemester = semester.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        if(TextUtils.isEmpty(getName) ||
                TextUtils.isEmpty(getEmail) ||
                TextUtils.isEmpty(getAddress) ||
                TextUtils.isEmpty(getInstitution) ||
                TextUtils.isEmpty(getSemester) ||
                TextUtils.isEmpty(getAddress) ||
                TextUtils.isEmpty(getPassword) ||
                TextUtils.isEmpty((getConfirmPassword))
                )

        {
            Toast.makeText(TeacherSignUp.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        } else if (getPassword.equals(getConfirmPassword)) {
            teacherSignUpFire(getName, getEmail,getAddress,getPassword, getInstitution, getSemester, getConfirmPassword);
        }
        else {
            Toast.makeText(TeacherSignUp.this, "Password and confirm password is not matching", Toast.LENGTH_LONG).show();
        }
        // check if the class is between 1 ans 12
    }

    public void teacherSignUpFire(String name, String email, String address, String password, String institution, String semester, String confirmPassword){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(TeacherSignUp.this, new OnCompleteListener<AuthResult>() {
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
                    userProfile.put("Semester", semester);
                    userProfile.put("type", "2");
                    db.collection("users").document(UID)
                            .set(userProfile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // comment
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