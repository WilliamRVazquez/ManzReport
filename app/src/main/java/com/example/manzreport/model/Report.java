package com.example.manzreport.model;

import com.google.firebase.Timestamp;

public class Report {
    String tituloreporte;
    String ubicacion;
    Timestamp date;
    public Report(){

    }

    public Report(String tituloreporte, String ubicacion,Timestamp date) {
        this.tituloreporte = tituloreporte;
        this.ubicacion = ubicacion;
        this.date = date;


    }

    public String getTituloreporte() {
        return tituloreporte;
    }

    public void setTituloreporte(String tituloreporte) {
        this.tituloreporte = tituloreporte;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
