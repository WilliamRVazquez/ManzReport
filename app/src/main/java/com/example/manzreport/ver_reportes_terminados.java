package com.example.manzreport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.manzreport.adapter.TerminadoAdapter;
import com.example.manzreport.model.terminado;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ver_reportes_terminados extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView mRecycler;
    TerminadoAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;
    String correo_e;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes_terminados);

        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes Terminados");
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences prefs = this.getSharedPreferences("Preferences", 0);
        correo_e = prefs.getString("users", "");
        mRecycler = findViewById(R.id.recyclerViewSingle);

        String id = mAuth.getCurrentUser().getUid();

        query = mFirestore.collection("Reportes").whereEqualTo("id_user",id).whereEqualTo("Aceptado", "Terminado");
        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        mRecycler.setLayoutManager(new ver_reportes.WrapContentLinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        //query = mFirestore.collection("Reportes").whereEqualTo("Aceptado", "Terminado");

        FirestoreRecyclerOptions<terminado> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<terminado>().setQuery(query, terminado.class).build();

        mAdapter = new TerminadoAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
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