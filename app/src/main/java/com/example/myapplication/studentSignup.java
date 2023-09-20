package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class studentSignup extends AppCompatActivity {

    EditText sUsername, sAddress, sInstitution, sClass, sPassword, sCpassword, sName;
    ImageView profileImageView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        Button button = findViewById(R.id.sSignup);
       // progressBar = findViewById(R.id.progressBar)
        sUsername = findViewById(R.id.sUsername);
        sPassword = findViewById(R.id.sPassword);
        sInstitution = findViewById(R.id.sInstitution);
        sClass = findViewById(R.id.sClass);
        sAddress = findViewById(R.id.sAddress);
       // sPassword = findViewById(R.id.sPassword);
        sCpassword = findViewById(R.id.sConfirm);
        sName = findViewById(R.id.sName);
        profileImageView = findViewById(R.id.sSignupScreen);

        db = FirebaseFirestore.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSignUp();
            }
        });
    }



   void sSignUp(){
        String name = sName.getText().toString();
        String user = sUsername.getText().toString();
        String pass = sPassword.getText().toString();
        String Institution = sInstitution.getText().toString();
        String Class = sClass.getText().toString();
        String Address = sAddress.getText().toString();
       // String password = sPassword.getText().toString();
        String confirm = sCpassword.getText().toString();

        if(TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(user) ||
                TextUtils.isEmpty(pass) ||
                TextUtils.isEmpty(Institution) ||
                TextUtils.isEmpty(Class) ||
                TextUtils.isEmpty(Address) ||
                TextUtils.isEmpty(confirm))
        {
            Toast.makeText(studentSignup.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        } else if (pass.equals(confirm)) {
            StudentSignUpFire(name, user,pass,Institution, Class, Address);
        }
        // check if the class is between 1 ans 12
        else if(pass.equals(confirm)){
          //  Toast.makeText(studentSignup.this, "asif", Toast.LENGTH_SHORT).show();
            Toast.makeText(studentSignup.this,"Password and confirm password is not matching", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(studentSignup.this, "Password and confirm password is not matching", Toast.LENGTH_LONG).show();

        }
    }

    public void StudentSignUpFire(String name, String username, String password, String institution, String Class, String address){
       FirebaseAuth auth = FirebaseAuth.getInstance();
       auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(studentSignup.this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(studentSignup.this, "successfully", Toast.LENGTH_SHORT).show();
                   FirebaseUser user = auth.getCurrentUser();

                   user.sendEmailVerification();
                   Map<String, Object> userProfile = new HashMap<>();
                   String UID = user.getUid();
                   userProfile.put("Name", name);
                   userProfile.put("Institution", institution);
                   userProfile.put("Address", address);
                   userProfile.put("Class", Class);
                   userProfile.put("type", "1");
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
                                   Toast.makeText(studentSignup.this,"Failed",Toast.LENGTH_SHORT).show();

                               }
                           });
                   Intent intent = new Intent(studentSignup.this, studentlogin.class);
                   intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK
                           |Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                   finish();
               }
               task.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(studentSignup.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

               //else                Toast.makeText(studentSignup.this, "Dibbyo", Toast.LENGTH_SHORT).show();

           }
       });
    }

}