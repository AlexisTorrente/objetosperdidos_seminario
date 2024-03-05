package com.example.tp_grupo6_seminario.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;

import java.util.ArrayList;

public class DataLocalidades extends AsyncTask<String, Void, String> {

    private Spinner spinnerLocalidad;
    private Context context;
    private ArrayList<Localidad> listaLocalidades = new ArrayList<Localidad>();

    public DataLocalidades(Spinner spinner, Context ct) {
        spinnerLocalidad = spinner;
        context = ct;
    }
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            listaLocalidades.clear(); // Limpiar la lista
            LocalidadDao localidadDao = new LocalidadDao(context);
            listaLocalidades = localidadDao.obtenerListadoDeLocalidades(null);
            response = "Localidades cargadas exitosamente";
        } catch (Exception e) {
            e.printStackTrace();
            response = "no se ha podido cargar las Localidades";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaLocalidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocalidad.setAdapter(adapter);
    }
}
