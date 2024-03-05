package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;

public class ComentarioPublicacion implements Serializable {

    private int id;
    private Usuario usuario;
    private Publicacion publicacion;
    private String comentario;
    private boolean estado;
    
    public ComentarioPublicacion(){}

    public ComentarioPublicacion(int id, Usuario usuario, Publicacion publicacion, String comentario, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.publicacion = publicacion;
        this.comentario = comentario;
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

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ComentarioPublicacion{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", publicacion=" + publicacion +
                ", comentario='" + comentario + '\'' +
                ", estado=" + estado +
                '}';
    }
}
