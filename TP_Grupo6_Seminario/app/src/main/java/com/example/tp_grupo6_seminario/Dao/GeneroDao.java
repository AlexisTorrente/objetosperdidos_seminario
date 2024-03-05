package com.example.tp_grupo6_seminario.Dao;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeneroDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private String operacion;
    private Usuario usuario;
    private ArrayList<Genero> listaGeneros = new ArrayList<Genero>();
    private Spinner spGeneros;

    public GeneroDao(Context ct) {
        this.context = ct;
    }

    public GeneroDao(Context ct, Spinner spGen) {
        this.context = ct;
        this.spGeneros = spGen;
    }

    public GeneroDao(Context ct, Spinner spGen, Usuario usr) {
        this.context = ct;
        this.spGeneros = spGen;
        this.usuario = usr;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "LISTADO": //lista todos los generos
                return obtenerListadoDeGeneros();
            default:
                return null; // Operación no reconocida
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result != null) {
            if (result instanceof List<?>) {
                List<Genero> generos = (List<Genero>) result;
                ArrayAdapter<Genero> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaGeneros);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spGeneros.setAdapter(adapter);

                // Luego, aquí puedes seleccionar el género correcto en el Spinner
                if (usuario != null) {
                    for (int i = 0; i < generos.size(); i++) {
                        if (generos.get(i).getId() == usuario.getGenero().getId()) {
                            spGeneros.setSelection(i);
                            break; // Una vez que se ha encontrado la coincidencia, puedes salir del bucle
                        }
                    }
                }
            }else if (result instanceof Genero) {

            }
        }
    }


    public Genero obtenerGenero(int idGenero){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Generos WHERE ID_Genero = "+idGenero);
            while(ct.next()) {
                Genero genero = new Genero(ct.getInt("ID_Genero"),ct.getString("Descripcion"));
                con.close();
                return genero;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Genero>obtenerListadoDeGeneros(){
        //retorna un listado de generos
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Generos");
            Genero genero;
            while(rs.next()){
                genero = new Genero(rs.getInt("ID_Genero"),rs.getString("Descripcion"));
                listaGeneros.add(genero);
            }
            con.close();
            return  listaGeneros;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
