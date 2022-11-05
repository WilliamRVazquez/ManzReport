package com.example.manzreport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Detalle_Reportes extends AppCompatActivity {
    ImageView report_photo;
    TextView tipreport, ubi, descripcion;
    StorageReference storageReference;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    Toolbar mToolbar;


    String idd;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reportes);
        String id = getIntent().getStringExtra("id_Reportes");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        tipreport = findViewById(R.id.tipreport);
        ubi = findViewById(R.id.ubi);
        descripcion = findViewById(R.id.descripcion);
        report_photo = findViewById(R.id.report_photo);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Detalles del Reporte");
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        if (id != null){
            idd = id;
            getReport(id);
        }
    }


    private void getReport(String id) {
        mfirestore.collection("Reportes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String tipoReport = documentSnapshot.getString("tiporeporte");
                String ubiReport = documentSnapshot.getString("ubicacion");
                String descripcionReport = documentSnapshot.getString("descripcion");
                String photoReport = documentSnapshot.getString("photo");

                tipreport.setText(tipoReport);
                ubi.setText(ubiReport);
                descripcion.setText(descripcionReport);

                try {
                    if(!photoReport.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.with(Detalle_Reportes.this)
                                .load(photoReport)
                                .into(report_photo);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Detalle_Reportes.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Detalle_Reportes.this, ver_reportes.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}