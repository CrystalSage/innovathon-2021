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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Counsellor_Dashboard extends AppCompatActivity {

    ImageView logout;
    ArrayList<studentsmodel> arrayList;
    RecyclerView recview;
    myadapter adapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_dashboard);

        logout=(ImageView) findViewById(R.id.logout_couns);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recview=(RecyclerView)findViewById(R.id.students_displayed);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        arrayList= new ArrayList<studentsmodel>();

        adapter= new myadapter(Counsellor_Dashboard.this,arrayList);

        recview.setAdapter(adapter);

        EventChangeListener();

        Toast.makeText(this, "Fetching STUDENT DATA", Toast.LENGTH_SHORT).show();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
            }
        });
    }

    private void EventChangeListener() {
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
    }
}