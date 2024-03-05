package com.example.tp_grupo6_seminario.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp_grupo6_seminario.entidades.Publicacion;

public class datosPublicacionViewModel extends ViewModel {
    private MutableLiveData<Publicacion> PublicacionDatos = new MutableLiveData<>();

    public void setPublicacion(Publicacion publicacion) {
        PublicacionDatos.setValue(publicacion);
    }

    public LiveData<Publicacion> getPublicacion() {
        return PublicacionDatos;
    }

}
