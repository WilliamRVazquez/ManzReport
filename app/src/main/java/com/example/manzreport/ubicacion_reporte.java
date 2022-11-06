package com.example.manzreport;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ubicacion_reporte extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gmap;
    String idd;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    double lati, longi;
    String puede;
    TextView tipreport;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_reporte);
        String id = getIntent().getStringExtra("id_Ubicacion");
        idd = id;
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tipreport = findViewById(R.id.textView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.tolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Ubicacion del reporte");
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());




    }

    private void getReport(String id) {
        mfirestore.collection("Reportes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String lat = documentSnapshot.getString("latitud");
                String lon = documentSnapshot.getString("longitud");
                tipreport.setText(lat);
                lati = Double.parseDouble(lat);
                longi = Double.parseDouble(lon);
                puede = lat;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap = googleMap;
        getReport(idd);
        mfirestore.collection("Reportes").document(idd).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String lat = documentSnapshot.getString("latitud");
                String lon = documentSnapshot.getString("longitud");
                tipreport.setText(lat);
                lati = Double.parseDouble(lat);
                longi = Double.parseDouble(lon);
                puede = lat;
                obtenerubi();

            }

            private void obtenerubi() {
                LatLng Reportubi = new LatLng(19.122128362793248, -104.33868795635321);
                gmap.moveCamera(CameraUpdateFactory.newLatLng(Reportubi));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(Reportubi)
                        .zoom(17) //para cambiar el zoom de hacercamiento
                        .bearing(90)
                        .tilt(40)
                        .build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                LatLng otro = new LatLng(lati,longi);
                gmap.addMarker(new MarkerOptions().position(otro).title("Ubicacion del reporte"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ubicacion_reporte.this, ver_reportes.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}