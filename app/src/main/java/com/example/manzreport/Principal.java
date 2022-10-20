package com.example.manzreport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Principal extends AppCompatActivity {
    Button ini;
    Intent i, j;
    FirebaseAuth fAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int REQUEST_CODE = 200;
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ini = (Button) findViewById(R.id.sesioniniciar);
        verificarPermisos();

        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = new Intent(Principal.this, login.class);
                startActivity(j);
            }
        });
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }





    //codigo Para los permisos

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int  permiso_location_precisa = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if( permiso_location_precisa == PackageManager.PERMISSION_GRANTED){
            //metodo de mandar mensajes
            Toast.makeText(this, "Consedido", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

}