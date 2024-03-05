package com.example.tp_grupo6_seminario.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp_grupo6_seminario.entidades.Usuario;

public class datosUsuarioViewModel extends ViewModel {
    private MutableLiveData<Usuario> usuarioDatos = new MutableLiveData<>();

    public void setUsuario(Usuario usuario) {
        usuarioDatos.setValue(usuario);
    }

    public LiveData<Usuario> getUsuario() {
        return usuarioDatos;
    }
}
