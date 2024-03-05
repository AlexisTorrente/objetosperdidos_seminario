package com.example.tp_grupo6_seminario.Dao;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReporteDao extends AsyncTask<Object,Void,Object> {
    Context context;
    private String operacion;
    TextView tvProvinciaMOR,tvLocalidadMOP,tvLocalidadMOE,tvLocalidadMOR,tvTipoObjeto,tvReporte;
    ProgressBar pbReporte;
    public ReporteDao(Context contexto){
        context = contexto;
        Log.i("INFO","Llegue al constructor ");

    }

    public void setTextView(TextView tvReporte){this.tvReporte = tvReporte;}
    public void setPb(ProgressBar pbReporte){this.pbReporte = pbReporte;}

    @Override
    protected void onPostExecute(Object result) {
        try {
            if (result instanceof String) {

                String ReporteResultado = (String) result;
                Log.i("INFO","Resultado: "+ ReporteResultado);

                if(operacion.equals("OBTENER_LOCALIDAD_MAX_PERDIDOS_TP")){

                    tvReporte.setText(ReporteResultado);

                }else if(operacion.equals("OBTENER_LOCALIDAD_MAX_RECUPERADOS")){

                    tvLocalidadMOR = ((Activity) context).findViewById(R.id.tvLocalidadMOR);
                    tvLocalidadMOR.setText(ReporteResultado);

                } else if(operacion.equals("OBTENER_TIPOOBJETO_MAX_PERDIDOS")){
                    tvTipoObjeto = ((Activity) context).findViewById(R.id.tvTipoObjeto);
                    if(pbReporte!=null){
                        pbReporte.setVisibility(View.INVISIBLE);
                    }
                    tvTipoObjeto.setText(ReporteResultado);

                } else if(operacion.equals("OBTENER_PROVINCIA_MAX_RECUPERADOS")){
                    tvProvinciaMOR = ((Activity) context).findViewById(R.id.tvProvinciaMOR);
                    tvProvinciaMOR.setText(ReporteResultado);
                }else if (operacion.equals("OBTENER_PROVINCIA_MAX_PORTIPO")){
                    tvReporte.setText(ReporteResultado);
                }else if(operacion.equals("OBTENER_MAX_PUBLICACION_PORTIPO")){
                    tvReporte.setText(ReporteResultado);
                }else if(operacion.equals("OBTENER_MAX_OBJETOS_RECUPERADOS")){
                    tvReporte.setText(ReporteResultado);
                }else if(operacion.equals("FILTRADOS")){
                    tvReporte.setText(ReporteResultado);
                    if(pbReporte !=null){
                        pbReporte.setVisibility(View.INVISIBLE);
                    }
                }

            }
        } catch (Exception e) {
            Log.i("ERROR_REPORTE", e.getMessage());
        }    }

    @Override
    protected Object doInBackground(Object... params) {
        Log.i("INFO","Llegue al doInBackground() ");

        if (params.length < 1 || params[0] == null) {
            return null;
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "OBTENER_LOCALIDAD_MAX_PERDIDOS_TP":
                return  ObtenerLocalidadConMasObjetosPerdidos((int) params[1]);
            case "OBTENER_LOCALIDAD_MAX_RECUPERADOS":
                return ObtenerLocalidadMaxRecuperados();
            case "OBTENER_TIPOOBJETO_MAX_PERDIDOS":
                return ObteneTipoObjetoMasPerdidos();
            case "OBTENER_PROVINCIA_MAX_RECUPERADOS":
                return ObtenerProvinciaMaxRecuperados();
            case "OBTENER_PROVINCIA_MAX_PORTIPO":
                return ObtenerProvinciaMasObjetosPorTipo((int) params[1]);
            case "OBTENER_MAX_PUBLICACION_PORTIPO":
                return ObtenerMaxPublicacionesPorTipo((int) params[1]);
            case "OBTENER_MAX_OBJETOS_RECUPERADOS":
                return ObtenerPublicacionesRecuperados();
            case "FILTRADOS":
                return ObtenerPublicacionesFiltrado((String) params[1]);
            default:
                return null;
        }
    }

    public String ObtenerLocalidadConMasObjetosPerdidos(int tipoPublicacion){
        try{
            String Localidad = "Error";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("CALL ObtenerLocalidadConMasPublicacionesPorTipo("+tipoPublicacion+")");

            while(rs.next()){

                Localidad = rs.getString("Nombre");
                break;
            }
            con.close();

            return Localidad;

        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_REPORTE",e.getMessage());
            return null;
        }
    }
    public String ObtenerLocalidadMaxRecuperados(){
        try{
            String Localidad = "Error";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("CALL ObtenerLocalidadConMasRecuperados()");

            while(rs.next()){
                Localidad = rs.getString("Nombre");
                break;
            }
            con.close();
            return Localidad;

        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_REPORTE",e.getMessage());
            return null;
        }
    }
    public String ObteneTipoObjetoMasPerdidos(){
        try{

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("CALL ObtenerTipoObjetoConMasPublicaciones()");

            String TipoObjeto = "Error";
            while(rs.next()){
                TipoObjeto = rs.getString("Descripcion");
                break;
            }
            con.close();
            return TipoObjeto;

        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_REPORTE",e.getMessage());
            return null;
        }
    }
    public String ObtenerProvinciaMaxRecuperados(){
        try{
            String Localidad = "Error";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("CALL ObtenerProvinciaConMasRecuperados()");
            while(rs.next()){
                Localidad = (rs.getString("Nombre"));
                break;
            }
            con.close();
            return Localidad;

        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_REPORTE",e.getMessage());
            return null;
        }
    }

    public String ObtenerMaxPublicacionesPorTipo(int tipoPublicacion) {
        int cantidad = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT COUNT(*) AS total FROM Publicaciones WHERE Estado = 1 AND Recuperado = 0 AND ID_TipoPublicacion = "+tipoPublicacion);
            if (ct.next()) {
                cantidad = ct.getInt("total");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ReporteDao","ERROR, LINEA 212");
        }
        return String.valueOf(cantidad);
    }

    public String ObtenerPublicacionesRecuperados() {
        int cantidad = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT COUNT(*) AS total FROM Publicaciones WHERE Estado = 1 AND Recuperado = 1");
            if (ct.next()) {
                cantidad = ct.getInt("total");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ReporteDao","ERROR, LINEA 229");
        }
        return String.valueOf(cantidad);
    }

    public String ObtenerProvinciaMasObjetosPorTipo(int tipoPublicacion) {
        String provincia = "Error";
        Provincia provinciaConMasPublicaciones = new Provincia();
        ProvinciaDao pdao = new ProvinciaDao(context);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT p.ID_Provincia, p.Nombre, COUNT(*) AS cantidad " +
                    "FROM Publicaciones AS pub " +
                    "INNER JOIN Provincias AS p ON pub.ID_Provincia = p.ID_Provincia " +
                    "WHERE pub.ID_TipoPublicacion = "+tipoPublicacion+" AND pub.Estado = 1 AND pub.Recuperado = 0 " +
                    "GROUP BY p.ID_Provincia " +
                    "ORDER BY cantidad DESC " +
                    "LIMIT 1");

            if (ct.next()) {
                provincia = ct.getString("Nombre");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ReporteDao","ERROR, LINEA 255");
        }
        return provincia;
    }

    public String ObtenerPublicacionesFiltrado(String filtrado) {
        int cantidadRegistros = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT COUNT(*) AS cantidad " +
                    "FROM Publicaciones" +
                    " WHERE " + filtrado);

            if (ct.next()) {
                cantidadRegistros = ct.getInt("cantidad");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ReporteDao", "ERROR, LINEA 255");
        }
        return String.valueOf(cantidadRegistros);
    }


}
