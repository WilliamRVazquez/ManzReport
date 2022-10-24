package com.example.manzreport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.collection.LLRBNode;

public class ver_reportes extends AppCompatActivity {
ImageButton atras;

    ListView l;
    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies","Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes);
        atras = (ImageButton) findViewById(R.id.atras_de_ver_reporte);

        l = findViewById(R.id.list);
        ArrayAdapter<String> arr;
        //arr = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,tutorials);
        arr = new ArrayAdapter<String>(getApplicationContext(),R.layout.listviewresours,tutorials);
        l.setAdapter(arr);


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                return;
            }
        });

    }
}