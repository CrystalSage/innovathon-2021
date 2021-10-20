package com.example.innovathon21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Counsellor_Registeration extends AppCompatActivity {

    RoundedImageView img_view;
    EditText name,email,pass,confirm_pass;
    Button sign_up_couns;
    String email_couns,password,name_couns,userid;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    private Uri mImageUri;
    StorageReference storageReference;
    TextView tv;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_registeration);


        storageReference= FirebaseStorage.getInstance().getReference();
        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
/*
        if(fAuth.getCurrentUser() != null){
            Toast.makeText(this, "Already Registered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        tv=(TextView)findViewById(R.id.couns_edittext);
        name=(EditText)findViewById(R.id.student_name_signup_couns);
        email=(EditText)findViewById(R.id.student_email_signup_couns);
        pass=(EditText)findViewById(R.id.student_password_signup_couns);
        confirm_pass=(EditText)findViewById(R.id.student_password_signup_confirm_couns);
        img_view=(RoundedImageView)findViewById(R.id.imageprofile_couns);
        sign_up_couns=(Button)findViewById(R.id.buttonSignUp_couns);

        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setVisibility(View.INVISIBLE);
                openFileChooser();
            }
        });

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

                if(mImageUri==null){
                    Toast.makeText(Counsellor_Registeration.this, "PLS UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
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
                            startstorageprocessimage();
                            startActivity(new Intent(getApplicationContext(),Counsellor_Dashboard.class));
                        }
                    }
                });
            }
        });




    }

    private void startstorageprocessimage() {
        if(mImageUri==null){
            Toast.makeText(Counsellor_Registeration.this, "PLS UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
        }

        final StorageReference reference=storageReference.child("image_files/"+System.currentTimeMillis()+".jpeg");
        reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("couns_accounts").document(fAuth.getUid());
                        Map<String,Object> user= new HashMap<>();
                        user.put("imageUrl",uri.toString());
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Counsellor_Registeration.this, "Photo Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Counsellor_Registeration.this, "Photo Updatation Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        tv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(mImageUri).into(img_view);
        }
    }
}