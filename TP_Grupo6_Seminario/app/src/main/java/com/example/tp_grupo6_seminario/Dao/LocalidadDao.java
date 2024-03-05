package com.example.tp_grupo6_seminario.Dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;
import com.example.tp_grupo6_seminario.entidades.Usuario;

public class LocalidadDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ArrayList<Localidad> listaLocalidades = new ArrayList<Localidad>();
    private String operacion;
    private Usuario usuario;
    private Localidad localidadSeleccionada = null;
    private Spinner spLocalidad;
    private boolean filtro = false;
    private Provincia provincia;
    public LocalidadDao(Context ct) {
        this.context = ct;
    }

    public LocalidadDao(Context ct, Spinner spinner) {
        this.context = ct;
        this.spLocalidad = spinner;
    }
    public LocalidadDao(Context ct, Spinner spinner, Localidad localidad) {
        this.context = ct;
        this.spLocalidad = spinner;
        this.localidadSeleccionada = localidad;
    }

    public LocalidadDao(Context ct, Spinner spinner, Usuario usr) {
        this.context = ct;
        this.spLocalidad = spinner;
        this.usuario = usr;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "OBTENER_PROVINCIA_POR_ID": //obtiene un usuario por id
                return  obtenerLocalidad((int) params[1]);
            case "LISTADO": //lista todos los usuarios
                if(params.length > 2){
                    this.filtro = (boolean) params[2];
                }
                return obtenerListadoDeLocalidades((Provincia) params[1]);
            case "LISTADO_LOC": //lista TODAS las localidades
                return obtenerListadoLocalidades();
            default:
                return null; // Operación no reconocida
        }

        /*String response = "";
        try {
            response = "Localidades cargadas exitosamente";
        } catch (Exception e) {
            e.printStackTrace();
            response = "no se ha podido cargar las Localidades";
        }
        return response;*/
    }


    public Localidad obtenerLocalidad(int idLocalidad){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Localidades WHERE ID_localidad = "+idLocalidad);
            Localidad localidad = new Localidad();
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            while(rs.next()){
                localidad.setId(rs.getInt("ID_Localidad"));
                localidad.setNombre(rs.getString("Nombre"));
                localidad.setProvincia(provinciaDao.obtenerProvincia(rs.getInt("ID_Provincia")));
                break;
            }
            con.close();
            return localidad;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Localidad>obtenerListadoDeLocalidades(Provincia provincia){
        //si se pasa la provincia como parametro se filtra por las de esa provincia
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = provincia == null ||provincia.getId() == 0 ? st.executeQuery("SELECT * FROM Localidades") : st.executeQuery("SELECT * FROM Localidades WHERE ID_Provincia = "+provincia.getId()) ;
            Localidad localidad;
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            while(rs.next()){
                localidad = new Localidad(rs.getInt("ID_Localidad"),rs.getString("Nombre"),provinciaDao.obtenerProvincia(rs.getInt("ID_Provincia")));
                listaLocalidades.add(localidad);
            }
            con.close();
            return  listaLocalidades;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Localidad>obtenerListadoLocalidades(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Localidades");
            Localidad localidad;
            while(rs.next()){
                localidad = new Localidad(rs.getInt("ID_Localidad"),rs.getString("Nombre"),null);
                listaLocalidades.add(localidad);
            }
            con.close();
            return  listaLocalidades;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(result != null){
            if(result instanceof List<?>){
                if(filtro) {
                    Localidad localidadAux = new Localidad();
                    localidadAux.setId(0);
                    localidadAux.setNombre("Todas");
                    listaLocalidades.add(0, localidadAux);
                }
                // Configurar el adaptador del Spinner con la lista de localidades
                ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaLocalidades);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLocalidad.setAdapter(adapter);

                if(localidadSeleccionada != null){
                    spLocalidad.setSelection(getIndex(spLocalidad, localidadSeleccionada.getNombre()));
                }

                if (usuario != null) {
                    for (int i = 0; i < listaLocalidades.size(); i++) {
                        if (listaLocalidades.get(i).getId() == usuario.getLocalidad().getId()) {
                            spLocalidad.setSelection(i);
                            spLocalidad.setEnabled(true);
                            break; // Una vez que se ha encontrado la coincidencia, puedes salir del bucle
                        }else{
                            spLocalidad.setEnabled(true);
                        }
                    }
                }
                spLocalidad.setEnabled(true);
            }else if(result instanceof Localidad){

            }
        }
    }

    public int getIndex(Spinner spinner, String nombre){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equals(nombre)){
                return i;
            }
        }

        return 0;
    }
}
