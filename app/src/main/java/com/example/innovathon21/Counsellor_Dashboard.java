package com.example.innovathon21;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Counsellor_Dashboard extends AppCompatActivity {

    ImageView logout,fetch_image;
   /* ArrayList<studentsmodel> arrayList;
    RecyclerView recview;
    myadapter adapter;*/
    FirebaseFirestore db;
   /* ProgressDialog progressDialog;*/
    TextView fetch_name;
    FirebaseAuth firebaseAuth;
    String userid;
    Button std_data,recent_conv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_dashboard);

        firebaseAuth=FirebaseAuth.getInstance();

        userid=firebaseAuth.getCurrentUser().getUid();

        fetch_image=(ImageView)findViewById(R.id.fetch_image_couns);
        recent_conv=(Button)findViewById(R.id.recent_messages_coun);

        recent_conv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),recent_conv_counc.class));
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),New_Conversation_couns.class));
            }
        });

        std_data=(Button)findViewById(R.id.stdents_data_couns);
        logout=(ImageView) findViewById(R.id.logout_couns);
        fetch_name=(TextView)findViewById(R.id.fetch_textview_couns);

        std_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Students_data_view.class));
            }
        });
     /*   progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
       // recview=(RecyclerView)findViewById(R.id.students_displayed);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));*/

        db=FirebaseFirestore.getInstance();
        /*arrayList= new ArrayList<studentsmodel>();

        adapter= new myadapter(Counsellor_Dashboard.this,arrayList);

        recview.setAdapter(adapter);*/

        //EventChangeListener();

        //Toast.makeText(this, "Fetching STUDENT DATA", Toast.LENGTH_SHORT).show();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
            }
        });

        fetch_data();


    }

    private void fetch_data() {
        DocumentReference documentReference=db.collection("couns_accounts").document(userid);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                fetch_name.setText(value.getString("name"));
               // String img_url=value.getString("imageUrl");
                Glide.with(getApplicationContext()).load(value.getString("imageUrl")).into(fetch_image);
            }
        });

    }

    /*private void EventChangeListener() {
        db.collection("student_accounts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Toast.makeText(Counsellor_Dashboard.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType()==DocumentChange.Type.ADDED){

                                arrayList.add(dc.getDocument().toObject(studentsmodel.class));
                            }

                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });
    }*/
}