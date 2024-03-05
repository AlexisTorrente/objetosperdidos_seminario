package com.example.tp_grupo6_seminario.Dao;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoPublicacion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TipoPublicacionDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ArrayList<TipoPublicacion> listaTipoPublicacion = new ArrayList<TipoPublicacion>();
    private String operacion;
    private TipoPublicacion tipoPublicacion, tipoPublicacionSeleccionada;
    private Spinner spTipoPublicacion;

    public TipoPublicacionDao(Context ct) {
        this.context = ct;
    }
    public TipoPublicacionDao(Context ct, TipoPublicacion tipoPubli) {
        this.context = ct;
        this.tipoPublicacion = tipoPubli;
    }
    public TipoPublicacionDao(Context ct, Spinner sp) {
        this.context = ct;
        this.spTipoPublicacion = sp;
    }
    public TipoPublicacionDao(Context ct, Spinner sp, TipoPublicacion tipoPub) {
        this.context = ct;
        this.spTipoPublicacion = sp;
        this.tipoPublicacionSeleccionada = tipoPub;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "OBTENER_TIPO_PUBLICACION": //obtiene un usuario por id
                return  obtenerTipoPublicacion((int) params[1]);
            case "LISTADO": //lista todos los usuarios
                return obtenerListadoDeTipoPublicacion();
            default:
                return null; // Operación no reconocida
        }

        /*String response = "";
        try {
            response = "";
        } catch (Exception e) {
            e.printStackTrace();
            response = "";
        }
        return response;*/
    }

    public TipoPublicacion obtenerTipoPublicacion(int idTipoPublicacion){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM `Tipo Publicaciones` WHERE ID_TipoPublicacion = "+idTipoPublicacion);
            while(ct.next()) {
                TipoPublicacion tipoPubli = new TipoPublicacion(ct.getInt("ID_TipoPublicacion"),ct.getString("Descripcion"));
                con.close();
                return tipoPubli;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TipoPublicacion>obtenerListadoDeTipoPublicacion(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `Tipo Publicaciones`");
            TipoPublicacion tipoPublicacion;
            while(rs.next()){
                tipoPublicacion = new TipoPublicacion(rs.getInt("ID_TipoPublicacion"),rs.getString("Descripcion"));
                listaTipoPublicacion.add(tipoPublicacion);
            }
            con.close();
            return  listaTipoPublicacion;
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
                ArrayAdapter<TipoPublicacion> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaTipoPublicacion);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTipoPublicacion.setAdapter(adapter);

                if(tipoPublicacionSeleccionada != null){
                    spTipoPublicacion.setSelection(getIndex(spTipoPublicacion, tipoPublicacionSeleccionada.getDescripcion()));
                }

            }else if(result instanceof TipoPublicacion){
                tipoPublicacion = (TipoPublicacion) result;
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
