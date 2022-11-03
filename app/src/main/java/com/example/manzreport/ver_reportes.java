package com.example.manzreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.manzreport.adapter.ReportAdapter;
import com.example.manzreport.model.Report;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ver_reportes extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView mRecycler;
    ReportAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    ImageButton atras;
    Query query;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes);
        mAuth = FirebaseAuth.getInstance();
        atras = (ImageButton) findViewById(R.id.atras_de_ver_reporte);
        setUpRecyclerView();






        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                return;
            }
        });
    }

    private void setUpRecyclerView() {
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewSingle);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));




        query = mFirestore.collection("Reportes").whereEqualTo("id_user", mAuth.getCurrentUser().getUid()).whereEqualTo("Aceptado", "Si");


        FirestoreRecyclerOptions<Report> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Report>().setQuery(query, Report.class).build();

        mAdapter = new ReportAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();




        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}