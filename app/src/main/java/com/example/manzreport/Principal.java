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

import java.util.Timer;
import java.util.TimerTask;

public class Principal extends AppCompatActivity {
    Button ini;
    Intent i, j;
    FirebaseAuth fAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //ini = (Button) findViewById(R.id.sesioniniciar);


        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(Principal.this, login.class);
                    startActivity(i);
                    finish();
                }
            };
            Timer time = new Timer();
            time.schedule(task, 2500);
        }

        /*
        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = new Intent(Principal.this, login.class);
                startActivity(j);
            }
        });*/

    }





    //codigo Para los permisos





    public void onBackPressed() { moveTaskToBack(true); finish(); } //codigo para cerrar la app all momento de darle para atras




}