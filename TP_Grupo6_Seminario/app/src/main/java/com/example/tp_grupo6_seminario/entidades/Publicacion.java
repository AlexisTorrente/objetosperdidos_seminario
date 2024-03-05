package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Publicacion implements Serializable {

    private int id;
    private String titulo;
    private TipoPublicacion tipoPublicacion;
    private TipoObjeto tipoObjeto;
    private Usuario usuario;
    private Provincia provincia;
    private Localidad localidad;
    private String ubicacion;
    private String imagen; //URL?
    private Date fechaPublicacion; //Date?
    private String horaPublicacion; //Time?
    private String descripcion;
    private boolean recuperado;
    private boolean estado;

    public Publicacion(){}

    public Publicacion(int id, TipoPublicacion tipoPublicacion, TipoObjeto tipoObjeto, Usuario usuario, Provincia provincia, Localidad localidad,String titulo, String ubicacion, String imagen, Date fechaPublicacion, String horaPublicacion, String descripcion, boolean recuperado, boolean estado) {
        this.id = id;
        this.tipoPublicacion = tipoPublicacion;
        this.tipoObjeto = tipoObjeto;
        this.usuario = usuario;
        this.provincia = provincia;
        this.localidad = localidad;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
        this.fechaPublicacion = fechaPublicacion;
        this.horaPublicacion = horaPublicacion;
        this.descripcion = descripcion;
        this.recuperado = recuperado;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(TipoPublicacion tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getHoraPublicacion() {
        return horaPublicacion;
    }

    public void setHoraPublicacion(String horaPublicacion) {
        this.horaPublicacion = horaPublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRecuperado() {
        return recuperado;
    }

    public void setRecuperado(boolean recuperado) {
        this.recuperado = recuperado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", tipoPublicacion=" + tipoPublicacion +
                ", tipoObjeto=" + tipoObjeto +
                ", usuario=" + usuario +
                ", provincia=" + provincia +
                ", localidad=" + localidad +
                ", titulo=" + titulo +
                ", ubicacion=" + ubicacion +
                ", imagen='" + imagen + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", horaPublicacion=" + horaPublicacion +
                ", descripcion='" + descripcion + '\'' +
                ", recuperado=" + recuperado +
                ", estado=" + estado +
                '}';
    }
}
