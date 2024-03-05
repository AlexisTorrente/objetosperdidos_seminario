package com.example.tp_grupo6_seminario.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import java.util.ArrayList;


public class DataProvincias extends AsyncTask<String, Void, String> {
    private Spinner spinnerProvincia;
    private Context context;
    private ArrayList<Provincia> listaProvincias = new ArrayList<Provincia>();

    public DataProvincias(Spinner spinner, Context ct) {
        spinnerProvincia = spinner;
        context = ct;
    }
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            listaProvincias.clear(); // Limpiar la lista
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            listaProvincias = provinciaDao.obtenerListadoDeProvincias();
            response = "Provincias cargadas exitosamente";
        } catch (Exception e) {
            e.printStackTrace();
            response = "no se ha podido cargar las Provincias";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaProvincias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincia.setAdapter(adapter);
    }
}
