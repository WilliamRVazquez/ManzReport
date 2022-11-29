package com.example.manzreport.model;

import com.google.firebase.Timestamp;

public class Report {
    String titulo;
    String ubicacion;
    Timestamp date;
    String Aceptado;
    public Report(){

    }

    public Report(String titulo, String ubicacion,Timestamp date, String Aceptado) {
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.date = date;
        this.Aceptado = Aceptado;


    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getAceptado() {
        return Aceptado;
    }

    public void setAceptado(String Aceptado) {
        this.Aceptado = Aceptado;
    }

}
