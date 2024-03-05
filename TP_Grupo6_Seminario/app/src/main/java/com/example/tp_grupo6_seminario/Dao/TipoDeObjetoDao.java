package com.example.tp_grupo6_seminario.Dao;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoDeObjetoDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ArrayList<TipoObjeto> listaTipoObjeto = new ArrayList<TipoObjeto>();
    private String operacion;
    private Spinner spTipoObjeto;
    private TipoObjeto tipoObjetoSeleccionado = null;
    private boolean filtro = false;
    public TipoDeObjetoDao(Context ct) {
        this.context = ct;
    }
    public TipoDeObjetoDao(Context ct, Spinner spinner) {
        this.context = ct;
        this.spTipoObjeto = spinner;
    }
    public TipoDeObjetoDao(Context ct, Spinner spinner, TipoObjeto tipoObjeto) {
        this.context = ct;
        this.spTipoObjeto = spinner;
        this.tipoObjetoSeleccionado = tipoObjeto;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "OBTENER_TIPO_OBJETO_POR_ID": //obtiene un usuario por id
                return  obtenerTipoObjeto((int) params[1]);
            case "LISTADO": //lista todos los usuarios
                if(params.length > 1){
                    this.filtro = (boolean) params[1];
                }
                return obtenerListadoDeTipoDeObjetos();
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


    public TipoObjeto obtenerTipoObjeto(int idTipoObjeto){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `Tipo Objetos` WHERE ID_TipoObjeto = "+idTipoObjeto);
            TipoObjeto tipoObjeto;
            while(rs.next()){
                tipoObjeto = new TipoObjeto(rs.getInt("ID_TipoObjeto"),rs.getString("Descripcion"));
                con.close();
                return tipoObjeto;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TipoObjeto>obtenerListadoDeTipoDeObjetos(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs =  st.executeQuery("SELECT * FROM `Tipo Objetos` ");
            TipoObjeto tipoObjeto;
            while(rs.next()){
                tipoObjeto = new TipoObjeto(rs.getInt("ID_TipoObjeto"),rs.getString("Descripcion"));
                listaTipoObjeto.add(tipoObjeto);
            }
            con.close();
            return  listaTipoObjeto;
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
                    TipoObjeto tipoObjetoAux = new TipoObjeto();
                    tipoObjetoAux.setId(0);
                    tipoObjetoAux.setDescripcion("Todos");
                    listaTipoObjeto.add(0, tipoObjetoAux);
                }
                ArrayAdapter<TipoObjeto> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaTipoObjeto);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTipoObjeto.setAdapter(adapter);

                if(tipoObjetoSeleccionado != null){
                    spTipoObjeto.setSelection(getIndex(spTipoObjeto, tipoObjetoSeleccionado.getDescripcion()));
                }

            }else if(result instanceof TipoObjeto){

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