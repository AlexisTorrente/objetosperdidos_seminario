package com.example.tp_grupo6_seminario.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private TipoUsuario tipoUsuario;
    private Provincia provincia;
    private Localidad localidad;
    private Genero genero;
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String fechaNac;
    private String descripcionPerfil;
    private boolean estado;

    public Usuario() {
    }

    public Usuario(int id, TipoUsuario tipoUsuario, Provincia provincia, Localidad localidad, Genero genero, String nombreUsuario, String contrasena, String nombre, String apellido, String dni, String email, String fechaNac, boolean estado) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.provincia = provincia;
        this.localidad = localidad;
        this.genero = genero;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaNac = fechaNac;
        this.estado = estado;
    }

    public Usuario(int id, TipoUsuario tipoUsuario, Provincia provincia, Localidad localidad, Genero genero, String nombreUsuario, String contrasena, String nombre, String apellido, String dni, String email, String fechaNac,String descripcionPerfil, boolean estado) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.provincia = provincia;
        this.localidad = localidad;
        this.genero = genero;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaNac = fechaNac;
        this.descripcionPerfil = descripcionPerfil;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public  void setDescripcionPerfil(String descripcionPerfil){this.descripcionPerfil = descripcionPerfil;}
    public String getDescripcionPerfil(){return descripcionPerfil;}

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", tipoUsuario=" + tipoUsuario +
                ", provincia=" + provincia +
                ", localidad=" + localidad +
                ", genero=" + genero +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", fechaNac='" + fechaNac + '\'' +
                ", estado=" + estado +
                '}';
    }
}
