package com.example.tp_grupo6_seminario.entidades;

public class ComentarioPerfil {
    private int id;
    private Usuario usuarioComenterio;
    private Usuario usuarioPerfil;
    private String comenterio;
    private boolean estado;

    public ComentarioPerfil() {
    }

    public ComentarioPerfil(int id, Usuario usuarioComenterio, Usuario usuarioPerfil, String comenterio, boolean estado) {
        this.id = id;
        this.usuarioComenterio = usuarioComenterio;
        this.usuarioPerfil = usuarioPerfil;
        this.comenterio = comenterio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuarioComenterio() {
        return usuarioComenterio;
    }

    public void setUsuarioComenterio(Usuario usuarioComenterio) {
        this.usuarioComenterio = usuarioComenterio;
    }

    public Usuario getUsuarioPerfil() {
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(Usuario usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }

    public String getComenterio() {
        return comenterio;
    }

    public void setComenterio(String comenterio) {
        this.comenterio = comenterio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ComentarioPerfil{" +
                "id=" + id +
                ", usuarioComenterio=" + usuarioComenterio +
                ", usuarioPerfil=" + usuarioPerfil +
                ", comenterio='" + comenterio + '\'' +
                ", estado=" + estado +
                '}';
    }
}
