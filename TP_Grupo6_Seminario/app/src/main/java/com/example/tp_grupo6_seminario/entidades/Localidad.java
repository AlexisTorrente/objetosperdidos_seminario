package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;

public class Localidad implements Serializable {
    private int id;
    private Provincia provincia;
    private String nombre;



    public Localidad() {
    }
    public Localidad(int id, String nombre, Provincia provincia) {
        this.id = id;
        this.nombre = nombre;
        this.provincia = provincia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return nombre;
    }
}