package com.example.manzreport.model;

public class Report {
    String tiporeporte;
    String ubicacion;
    public Report(){

    }

    public Report(String tiporeporte, String ubicacion) {
        this.tiporeporte = tiporeporte;
        this.ubicacion = ubicacion;

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


}
