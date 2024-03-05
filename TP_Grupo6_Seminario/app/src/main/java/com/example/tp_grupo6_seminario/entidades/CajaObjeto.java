package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;

public class CajaObjeto implements Serializable {

    private int id;
    private Localidad localidad;
    private String descripcion;
    private String Latitud;
    private String Longitud;
    boolean estado;

    public CajaObjeto(){}

    public CajaObjeto(int id, Localidad localidad, String descripcion, String latitud, String longitud, boolean estado) {
        this.id = id;
        this.localidad = localidad;
        this.descripcion = descripcion;
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CajaObjeto{" +
                "id=" + id +
                ", localidad=" + localidad +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                '}';
    }
}
