package com.example.manzreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {
    TextView register_btn;
    Intent i;
    Button inisesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register_btn = (TextView) findViewById(R.id.txtv_register_and_btn);
        inisesion = (Button) findViewById(R.id.button_sesion);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(login.this,Sing_up.class);
                startActivity(i);
            }
        });

        inisesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(login.this,MainActivity.class);
                startActivity(i);

            }
        });


    }
}
