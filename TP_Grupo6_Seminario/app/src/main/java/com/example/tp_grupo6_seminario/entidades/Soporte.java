package com.example.tp_grupo6_seminario.entidades;

public class Soporte {
    
    private int id;
    private Usuario usuario;
    private String resumen;
    private String detalle;
    private boolean estado;

    public Soporte(){}

    public Soporte(int id, Usuario usuario, String resumen, String detalle, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.resumen = resumen;
        this.detalle = detalle;
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

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Soporte{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", resumen='" + resumen + '\'' +
                ", detalle='" + detalle + '\'' +
                ", estado=" + estado +
                '}';
    }
}
