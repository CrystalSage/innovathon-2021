package com.example.innovathon21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

public class Counsellor_Registeration extends AppCompatActivity {

    RoundedImageView img_view;
    EditText name,email,pass,confirm_pass;
    Button sign_up_couns;
    String email_couns,password,name_couns,userid;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_registeration);

        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
/*
        if(fAuth.getCurrentUser() != null){
            Toast.makeText(this, "Already Registered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        name=(EditText)findViewById(R.id.student_name_signup_couns);
        email=(EditText)findViewById(R.id.student_email_signup_couns);
        pass=(EditText)findViewById(R.id.student_password_signup_couns);
        confirm_pass=(EditText)findViewById(R.id.student_password_signup_confirm_couns);
        img_view=(RoundedImageView)findViewById(R.id.imageprofile_couns);
        sign_up_couns=(Button)findViewById(R.id.buttonSignUp_couns);

        sign_up_couns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_couns=name.getText().toString().trim();
                email_couns = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(TextUtils.isEmpty(email_couns)){
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    pass.setError("Password Must be >= 6 Characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email_couns,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            userid=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("couns_accounts").document(userid);
                            Map<String,Object> user= new HashMap<>();
                            user.put("Email",email_couns);
                            user.put("Password",password);
                            user.put("First Name",name_couns);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Counsellor_Registeration.this, "Successful", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Counsellor_Registeration.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Counsellor_Dashboard.class));
                        }
                    }
                });
            }
        });




    }
}