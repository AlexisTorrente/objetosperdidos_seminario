package com.example.tp_grupo6_seminario.Data;
import androidx.lifecycle.ViewModel;

import com.example.tp_grupo6_seminario.entidades.Publicacion;

import java.util.List;

public class ViewModelDatos extends ViewModel{
    private List<Publicacion> publicacionesObjPerdido;
    private List<Publicacion> publicacionesObjEncontrado;
    public List<Publicacion> getPublicacionesObjPerdido() {
        return publicacionesObjPerdido;
    }

    public List<Publicacion> getPublicacionesObjEncontrado() {
        return publicacionesObjEncontrado;
    }

    public void setPublicacionesObjPerdido(List<Publicacion> publicaciones) {
        this.publicacionesObjPerdido = publicaciones;
    }
    public void setPublicacionesObjEncontrado(List<Publicacion> publicaciones) {
        this.publicacionesObjEncontrado = publicaciones;
    }


}
