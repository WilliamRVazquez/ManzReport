package com.example.manzreport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    String Rol;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes);

        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes");
        mToolbar.inflateMenu(R.menu.menu_reportes);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.rpTerminados){
                    startActivity(new Intent(ver_reportes.this, ver_reportes_terminados.class));
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mAuth = FirebaseAuth.getInstance();
        //atras = (ImageButton) findViewById(R.id.atras_de_ver_reporte);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerViewSingle);
        Rol = getIntent().getExtras().getString("rol");
        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference id = mFirestore.collection("users").document();
        mFirestore.collection("users")
                .whereEqualTo("Rol", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        if(Rol.equals("1")){
            query = mFirestore.collection("Reportes").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());
            setUpRecyclerView();

        }else if (Rol.equals("0")){
            query = mFirestore.collection("Reportes").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());
            setUpRecyclerView();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {


        mRecycler.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


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
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
}