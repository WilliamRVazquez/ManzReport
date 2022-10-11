package com.example.manzreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button ini;
    Intent i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ini = (Button) findViewById(R.id.sesioniniciar);

        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = new Intent(MainActivity.this, login.class);
                startActivity(j);
            }
        });
    }
}