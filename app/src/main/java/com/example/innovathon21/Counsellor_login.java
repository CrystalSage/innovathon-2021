package com.example.innovathon21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class Counsellor_login extends AppCompatActivity {
    Button couns_login;
    TextView couns_registeration_main;
    EditText couns_email,couns_pass;
    String email_data, email_pass;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_login);

        couns_email=(EditText)findViewById(R.id.couns_input_email);
        couns_pass=(EditText)findViewById(R.id.couns_password);
        couns_registeration_main=(TextView)findViewById(R.id.textCreateNewAcc_couns);
        couns_login=(Button)findViewById(R.id.buttonSignIn_couns);

        fAuth = FirebaseAuth.getInstance();

        couns_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });

        couns_registeration_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Counsellor_Registeration.class));
            }
        });


    }

    private void userSignUp() {

        email_data = couns_email.getText().toString().trim();
        email_pass = couns_pass.getText().toString().trim();

        if (email_data.isEmpty()) {
            couns_email.setError("Email required");
            couns_email.requestFocus();
            return;
        }
        if (email_pass.isEmpty()) {
            couns_pass.setError("Password Required");
            couns_pass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_data).matches()) {
            couns_email.setError("Enter Valid email");
            couns_email.requestFocus();
            return;
        }
        if (email_pass.length() < 6) {
            couns_pass.setError("Length short");
            couns_pass.requestFocus();
            return;
        }

        fAuth.signInWithEmailAndPassword(email_data,email_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Counsellor_login.this, " Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Counsellor_Dashboard.class));
                }
                else {
                    Toast.makeText(Counsellor_login.this, "You are not REGISTERED!, Please do register in our APP first ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}