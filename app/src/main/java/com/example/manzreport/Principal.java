package com.example.manzreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class Principal extends AppCompatActivity {
    Button ini;
    Intent i, j;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ini = (Button) findViewById(R.id.sesioniniciar);

        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = new Intent(Principal.this, login.class);
                startActivity(j);
            }
        });
    }

}