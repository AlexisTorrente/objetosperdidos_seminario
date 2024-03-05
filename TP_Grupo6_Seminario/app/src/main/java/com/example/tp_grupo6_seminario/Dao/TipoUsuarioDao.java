package com.example.tp_grupo6_seminario.Dao;

import android.content.Context;
import android.os.AsyncTask;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoUsuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoUsuarioDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ArrayList<TipoUsuario> listaTipoUsuario = new ArrayList<TipoUsuario>();

    public TipoUsuarioDao(Context ct) {
        this.context = ct;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String response = "";
        try {
            response = "";
        } catch (Exception e) {
            e.printStackTrace();
            response = "";
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object result) {
    }

    public TipoUsuario obtenerTipoDeUsuario(int idTipoUsuario){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM `Tipo Usuarios` WHERE ID_TipoUsuario = "+idTipoUsuario);
            while(ct.next()) {
                TipoUsuario tipoUsuario = new TipoUsuario(ct.getInt("ID_TipoUsuario"),ct.getString("Descripcion"));
                con.close();
                return tipoUsuario;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TipoUsuario>obtenerListadoDeTipoDeUsuarios(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `TIPO USUARIOS`");
            TipoUsuario tipoUsuario;
            while(rs.next()){
                tipoUsuario = new TipoUsuario(rs.getInt("ID_TipoUsuario"),rs.getString("Descripcion"));
                listaTipoUsuario.add(tipoUsuario);
            }
            con.close();
            return  listaTipoUsuario;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
