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

public class SignUpActivity extends AppCompatActivity {
private TextView signIn;

    RoundedImageView img_view_student;
    EditText name,email,pass,confirm_pass;
    TextView addImage;
    Button sign_up_stu;
    String email_stu,password,name_stu,userid;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    private Uri mImageUri;
    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signIn = (TextView) findViewById(R.id.textSignIn);
        addImage=(TextView)findViewById(R.id.addImage);
        name=(EditText)findViewById(R.id.student_name_signup);
        email=(EditText)findViewById(R.id.student_email_signup);
        pass=(EditText)findViewById(R.id.student_password_signup);
        confirm_pass=(EditText)findViewById(R.id.student_password_signup_confirm);
        img_view_student=(RoundedImageView)findViewById(R.id.imageprofile);
        sign_up_stu=(Button)findViewById(R.id.buttonSignUp);
        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();


        img_view_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage.setVisibility(View.INVISIBLE);
                openFileChooser();

            }
        });


       

        sign_up_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_stu=name.getText().toString().trim();
                email_stu = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(TextUtils.isEmpty(email_stu)){
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
                    Toast.makeText(SignUpActivity.this, "PLEASE UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
                }

                fAuth.createUserWithEmailAndPassword(email_stu,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            userid=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("student_accounts").document(userid);
                            Map<String,Object> user= new HashMap<>();
                            user.put("Email",email_stu);
                            user.put("Password",password);
                            user.put("First Name",name_stu);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startstorageprocessimage();
                            startActivity(new Intent(getApplicationContext(),Student_Dashboard.class));
                        }
                    }
                });
            }

        });



//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
//            }
//        });
    }

    private void startstorageprocessimage() {
        if(mImageUri==null){
            Toast.makeText(SignUpActivity.this, "PLEASE UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
        }

        final StorageReference reference=storageReference.child("image_files/"+System.currentTimeMillis()+".jpeg");
        reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("student_accounts").document(fAuth.getUid());
                        Map<String,Object> user= new HashMap<>();
                        user.put("imageUrl",uri.toString());
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpActivity.this, "Photo Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "Failed to update the photo", Toast.LENGTH_SHORT).show();
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
            builder.build().load(mImageUri).into(img_view_student);
        }
    }
}