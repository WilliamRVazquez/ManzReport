package com.example.manzreport;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button btn_ver_reportes, btn_crear_reporte, btnPerfil;
    public static final String TAG = "TAG";
    Intent i;
    String borrar;
    int op;
    Query query;
    GoogleMap gmap;
    FirebaseAuth fAuth;
    double latfire;
    double longfire;
    String markerlisto = "";
    String lat, longi;
    TextView direccion1;
    TextView rol;
    TextView passwd;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    String Rol = "";
    private String password;
    String ban;
    String userdata;
    String deletes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_crear_reporte = (Button) findViewById(R.id.btn_crear_reporte);
        btn_ver_reportes = (Button) findViewById(R.id.btn_ver_reportes);
        mAuth = FirebaseAuth.getInstance();
        btnPerfil = (Button) findViewById(R.id.btnPerfil);
        mFirestore = FirebaseFirestore.getInstance();
        VereportDialogprogres vereportDialogprogres = new VereportDialogprogres(MainActivity.this);
        Cerrarapp cerrarapp = new Cerrarapp(MainActivity.this);
        fAuth = FirebaseAuth.getInstance();
        direccion1 = (TextView) findViewById(R.id.txtdireccion);
        rol = (TextView) findViewById(R.id.rol);
        passwd = (TextView) findViewById(R.id.pass);
        String idus = mAuth.getCurrentUser().getUid();

        //Bundle data = this.getIntent().getExtras();
        //password = data.getString("password");
        mFirestore.collection("users").whereEqualTo("Id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 ban = document.getId();

                                Log.d(TAG, document.getId()+ " => " + document.getString("fName"));



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        mFirestore.collection("Reportes").whereEqualTo("id_user", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userdata = document.getId();
                                Log.d(TAG, document.getId());



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFirestore.collection("users").whereEqualTo("Id", mAuth.getCurrentUser().getUid())
                .whereEqualTo("borrado", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                AlertDialog.Builder exit = new AlertDialog.Builder(MainActivity.this);

                                exit.setMessage("El usuario se eliminara por incumplir las normas")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteuser();

                                            }
                                        });
                                AlertDialog titulo = exit.create();
                                titulo.setTitle("Usuario eliminado");
                                titulo.show();



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        mFirestore.collection("users").whereEqualTo("Id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getString("Rol"));
                                //esto es para obtener en un querySnapshot algo especifico de un documento en string y asi no obtener el data
                                String roles = document.getString("Rol");
                                rol.setText(roles);



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btn_crear_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markerlisto.equals("ready")) {
                    Bundle extras = new Bundle();
                    String ubicaciones = direccion1.getText().toString();

                    extras.putDouble("latitud", latfire);
                    extras.putDouble("longitud", longfire);
                    extras.putString("ubicacion", ubicaciones);

                    Intent intent = new Intent(MainActivity.this, crear_reportes.class);
                    intent.putExtras(extras);

                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Necesita escoger una ubicacion", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_ver_reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vereportDialogprogres.StartAlertDialog();
                Handler handler =  new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vereportDialogprogres.dismissDialog();
                        Bundle extras = new Bundle();
                        String rolesitos = rol.getText().toString();


                        extras.putString("rol", rolesitos);

                        Intent intent = new Intent(MainActivity.this, ver_reportes.class);
                        intent.putExtras(extras);

                        startActivity(intent);
                    }
                },1000);

            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, UserPerfil.class);
                startActivity(i);
            }
        });
    }

    private void deleteuser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore.collection("users").document(ban).delete();
        mFirestore.collection("Reportes").document().delete();
        removeAllItemsFromShoppingCart();



        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    //mAuth.signOut();
                    //eliminar clave
                    FirebaseAuth.getInstance().signOut();


                    Intent intent = new Intent(MainActivity.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);



                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo eliminar: "+task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void removeAllItemsFromShoppingCart() {
        mFirestore.collection("Reportes")
                .whereEqualTo("id_user", ban)
                .get()
                .addOnSuccessListener((querySnapshot) -> {
                    WriteBatch batch = mFirestore.batch();
                    for (DocumentSnapshot doc : querySnapshot) {
                        batch.delete(doc.getReference());
                    }

                    batch.commit()
                            .addOnSuccessListener((result) -> {
                                Log.i(TAG, "All items have been removed.");
                            })
                            .addOnFailureListener((error) -> {
                                Log.e(TAG, "Failed to remove all items.", error);
                            });
                })
                .addOnFailureListener((error) -> {
                    Log.e(TAG, "Failed to get your cart items.", error);
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setTrafficEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gmap.setMyLocationEnabled(true);
        getLastLocation();
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                latfire = latLng.latitude;

                longfire = latLng.longitude;
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> direccion = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    direccion1.setText(direccion.get(0).getAddressLine(0));
                    String mostradireccion = direccion1.getText().toString();
                    markerOptions.title(mostradireccion);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                gmap.clear();
                //gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
                gmap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markerlisto = "ready";

            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //boton mi ubicacion, pero se desabilito con el codigo de abajo

        //googleMap.getUiSettings().setMyLocationButtonEnabled(false); //para apagar el boton de localizacion

        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }

    private void getLastLocation() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                if (gmap != null) {

                                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),16));
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) { e.printStackTrace(); }
    }

    public void onBackPressed() {
        AlertDialog.Builder exit = new AlertDialog.Builder(this);
        exit.setMessage("Esta seguro de salir de la Aplicacion?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true); finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = exit.create();
        titulo.setTitle("Salir de la app");
        titulo.show();

    }



    @Override
    protected void onStart() {
        super.onStart();
        direccion1.setVisibility(GONE);
        rol.setVisibility(GONE);
        passwd.setVisibility(GONE);

    }
}