package com.example.tp_grupo6_seminario.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Dao.GeneroDao;
import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Localidad;

import java.util.ArrayList;

public class DataGeneros extends AsyncTask<String, Void, String> {

    private Spinner spinnerGenero;
    private Context context;
    private ArrayList<Genero> listaGeneros = new ArrayList<Genero>();

    public DataGeneros(Spinner spinner, Context ct) {
        spinnerGenero = spinner;
        context = ct;
    }
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            listaGeneros.clear(); // Limpiar la lista
            GeneroDao generoDao = new GeneroDao(context);
            listaGeneros = generoDao.obtenerListadoDeGeneros();
            response = "Generos cargadas exitosamente";
        } catch (Exception e) {
            e.printStackTrace();
            response = "no se han podido cargar los Generos";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaGeneros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);
    }
}
