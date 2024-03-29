package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;

public class Genero implements Serializable {
    private int id;
    private String descripcion;

    public Genero() {
    }

    public Genero(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
