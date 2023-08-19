package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class studentSignup extends AppCompatActivity {

    EditText sUsername, sAddress, sInstitution, sClass, sPassword, sCpassword;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        Button button = findViewById(R.id.ssignup);
       // progressBar = findViewById(R.id.progressBar)
        sUsername = findViewById(R.id.sUsername);
        sPassword = findViewById(R.id.sPassword);
        sInstitution = findViewById(R.id.sInstitution);
        sClass = findViewById(R.id.sClass);
        sAddress = findViewById(R.id.sAddress);
       // sPassword = findViewById(R.id.sPassword);
        sCpassword = findViewById(R.id.sConfirm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSignUp();
            }
        });
    }
   void sSignUp(){
        String user = sUsername.getText().toString();
        String pass = sPassword.getText().toString();
        String Institution = sInstitution.getText().toString();
        String Class = sClass.getText().toString();
        String Address = sAddress.getText().toString();
       // String password = sPassword.getText().toString();
        String confirm = sCpassword.getText().toString();

        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(Institution) || TextUtils.isEmpty(Class) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(confirm))
        {
            Toast.makeText(studentSignup.this, "Please Fill Out All The Information", Toast.LENGTH_LONG).show();
        }
        // check if the class is between 1 ans 12
        else{
            StudentSignUpFire(user,pass,Institution, Class, Address);
        }
    }

    public void StudentSignUpFire(String username, String password, String institution, String Class, String address){
        FirebaseAuth auth = FirebaseAuth.getInstance();
       auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(studentSignup.this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(studentSignup.this, "successfull", Toast.LENGTH_SHORT).show();
                   FirebaseUser user = auth.getCurrentUser();
                   user.sendEmailVerification();
                   Intent intent = new Intent(studentSignup.this, studentlogin.class);
                   intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK
                   |Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                   finish();
               }
           }
       });
    }

}