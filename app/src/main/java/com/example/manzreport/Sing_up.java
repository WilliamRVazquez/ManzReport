package com.example.manzreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Sing_up extends AppCompatActivity {
    TextView btn_inisesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        btn_inisesion = (TextView) findViewById(R.id.txtv_inisesion_btn);

        btn_inisesion.setOnClickListener(new View.OnClickListener() {
            //con este codigo hago la accion para regresar a la pagina anterior
            @Override
            public void onClick(View view) {
                onBackPressed();
                return;
            }
        });
    }
}