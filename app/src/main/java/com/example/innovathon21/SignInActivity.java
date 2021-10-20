package com.example.innovathon21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

     TextView student_createAcc;
     Button student_login;
    EditText student_email,student_pass;
    String stu_email_data, stu_email_pass;
    FirebaseAuth fAuth;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        student_createAcc = (TextView) findViewById(R.id.textCreateNewAcc);
        student_login = (Button) findViewById(R.id.buttonSignIn);
        student_email = (EditText) findViewById(R.id.student_input_email);
        student_pass = (EditText) findViewById(R.id.student_password);

        fAuth = FirebaseAuth.getInstance();



        student_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });

        student_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });
    }

    private void userSignUp() {

        stu_email_data = student_email.getText().toString().trim();
        stu_email_pass = student_pass.getText().toString().trim();

        if (stu_email_data.isEmpty()) {
            student_email.setError("Email required");
            student_email.requestFocus();
            return;
        }
        if (stu_email_pass.isEmpty()) {
            student_pass.setError("Password Required");
            student_pass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(stu_email_data).matches()) {
            student_email.setError("Enter Valid email");
            student_email.requestFocus();
            return;
        }
        if (stu_email_pass.length() < 6) {
            student_pass.setError("Length short");
            student_pass.requestFocus();
            return;
        }

        fAuth.signInWithEmailAndPassword(stu_email_data,stu_email_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, " Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Student_Dashboard.class));
                }
                else {
                    Toast.makeText(SignInActivity.this, "You are not REGISTERED!, Please do register in our APP first ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}