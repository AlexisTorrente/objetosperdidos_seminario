package com.example.tp_grupo6_seminario.entidades;


import java.sql.Timestamp;
import java.util.Date;

public class Mensaje implements Comparable<Mensaje> {
    private int id;
    private String msj;
    private String desde;
    private String hacia;
    private Date hora;

    public Mensaje() {
        this.hora = new Date(System.currentTimeMillis()); // Set the current date and time
    }
    public Mensaje(String msj, String desde, String hacia, Date hora) {
        this.msj = msj;
        this.desde = desde;
        this.hacia = hacia;
        this.hora = hora;
    }
    public Mensaje(Integer id, String msj, String desde, String hacia, Date hora) {
        this.id = id;
        this.msj = msj;
        this.desde = desde;
        this.hacia = hacia;
        this.hora = hora;
    }

    @Override
    public int compareTo(Mensaje otroMensaje) {
        return Integer.compare(this.id, otroMensaje.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHacia() {
        return hacia;
    }

    public void setHacia(String hacia) {
        this.hacia = hacia;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }
}
