package com.example.tp_grupo6_seminario.entidades;

import java.sql.Date;

public class Notificacion {
    private int id;
    private Usuario usuario;
    private Publicacion publicacion;
    private Localidad localidad;
    private String descripcion;
    private Date fecha_hora; //Date?
    private boolean estado;

    public Notificacion(int id, Usuario usuario, Publicacion publicacion, Localidad localidad, String descripcion, Date fecha_hora, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.publicacion = publicacion;
        this.localidad = localidad;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPublicacion(Publicacion publicacion){this.publicacion = publicacion;}

    public Localidad getLocalidad() {
        return localidad;
    }
    public Publicacion getPublicacion(){return publicacion;}

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Date getFecha_hora() {
        return fecha_hora;
    }
    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", publicacion=" + publicacion +
                ", localidad=" + localidad +
                ", descripcion='" + descripcion + '\'' +
                ", fecha_hora=" + fecha_hora +
                ", estado=" + estado +
                '}';
    }
}
