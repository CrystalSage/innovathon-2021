package com.example.innovathon21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Students_data_view extends AppCompatActivity {
    RecyclerView recview;
    FirebaseFirestore db;
   ProgressDialog progressDialog;
   private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_data_view);

        db=FirebaseFirestore.getInstance();
        recview=(RecyclerView)findViewById(R.id.students_displayed);

        Query query = db.collection("student_accounts");

        FirestoreRecyclerOptions<studentsmodel> options = new FirestoreRecyclerOptions.Builder<studentsmodel>()
                .setQuery(query, studentsmodel.class)
                .build();

        adapter= new FirestoreRecyclerAdapter<studentsmodel, newviewholder>(options) {
            @NonNull
            @Override
            public newviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
                return new newviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull newviewholder holder, int position, @NonNull studentsmodel model) {
                holder.name.setText(model.getName());
                holder.email.setText(model.getEmail());
                Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);
            }
        };
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);

        Toast.makeText(this, "Fetching STUDENT DATA", Toast.LENGTH_SHORT).show();


    }

    private class newviewholder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,email;

        public newviewholder(@NonNull View itemView) {
            super(itemView);

            img=(CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}