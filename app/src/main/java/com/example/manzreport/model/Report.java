package com.example.manzreport.model;

import com.google.firebase.Timestamp;

public class Report {
    String tiporeporte;
    String ubicacion;
    Timestamp date;
    public Report(){

    }

    public Report(String tiporeporte, String ubicacion, Timestamp date) {
        this.tiporeporte = tiporeporte;
        this.ubicacion = ubicacion;
        this.date = date;


    }

    public String getTiporeporte() {
        return tiporeporte;
    }

    public void setTiporeporte(String tiporeporte) {
        this.tiporeporte = tiporeporte;
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
