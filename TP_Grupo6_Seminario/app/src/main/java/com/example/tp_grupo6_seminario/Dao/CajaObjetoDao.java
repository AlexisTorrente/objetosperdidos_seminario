package com.example.tp_grupo6_seminario.Dao;

import static com.example.tp_grupo6_seminario.R.*;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.VerMapaFragment;
import com.example.tp_grupo6_seminario.adapter.ComentarioPerfilAdapter;
import com.example.tp_grupo6_seminario.adapter.ubicacionCajasAdapter;
import com.example.tp_grupo6_seminario.entidades.CajaObjeto;
import com.example.tp_grupo6_seminario.entidades.ComentarioPerfil;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CajaObjetoDao extends AsyncTask<Object,Void,Object> {

    private Context context;
    private CajaObjeto cajaOBJ;
    private String operacion;
    private TextView txtDescripcionCaja;
    private String Latitud;
    private String Longitud;
    private GoogleMap gMap;
    private ArrayList<CajaObjeto> ListaCajasObjetos = new ArrayList<CajaObjeto>();
    private ListView lvUbicacionesCaja;

    private ProgressBar pb;


    public CajaObjetoDao(Context ct) {
        this.context = ct;
    }
    public CajaObjetoDao(Context ct, TextView txtDescripcionCaja, String Latitud, String Longitud, GoogleMap gMapa){

        this.context = ct;
        this.txtDescripcionCaja = txtDescripcionCaja;
        this.Latitud = Latitud;
        this.Longitud = Longitud;
        this.gMap = gMapa;
    }
    public CajaObjetoDao(Context ct, TextView txtDescripcionCaja, GoogleMap gMapa){

        this.context = ct;
        this.txtDescripcionCaja = txtDescripcionCaja;
        this.gMap = gMapa;
    }
    public CajaObjetoDao(Context ct,ListView lvMunicipios,TextView txtDescripcion,ProgressBar pb, GoogleMap gMapa) {
        this.context = ct;
        this.lvUbicacionesCaja = lvMunicipios;
        this.pb = pb;
        this.txtDescripcionCaja = txtDescripcion;
        this.gMap = gMapa;
    }

    public CajaObjetoDao(Context ct,CajaObjeto caja){

        this.context = ct;
        this.cajaOBJ = caja;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null;
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "OBTENER_CAJA_BYLOCALIDAD":
                return  ObtenerCaja((int) params[1]);
            case "LISTADO_CAJAS": //lista todos los usuarios
                return ObtenerListadoCajas();
            default:
                return null; // Operación no reconocida
        }

    }

    public CajaObjeto ObtenerCaja(int idLocalidad){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `Caja de objetos` WHERE Estado=1 AND ID_Localidad = "+idLocalidad);

            CajaObjeto cajaObjeto = new CajaObjeto();
            LocalidadDao localidadao = new LocalidadDao(context);

            while(rs.next()){

                cajaObjeto.setId(rs.getInt("ID_Caja"));
                cajaObjeto.setLocalidad(localidadao.obtenerLocalidad(rs.getInt("ID_Localidad")));
                cajaObjeto.setDescripcion(rs.getString("Descripcion"));
                cajaObjeto.setLatitud(rs.getString("Latitud"));
                cajaObjeto.setLongitud(rs.getString("Longitud"));
                cajaObjeto.setEstado(rs.getBoolean("Estado"));

                break;
            }
            con.close();

            return cajaObjeto;
        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_OBJ",e.getMessage());

        }
        return null;
    }

    public ArrayList<CajaObjeto> ObtenerListadoCajas(){
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `Caja de objetos` WHERE Estado=1");

            CajaObjeto cajaObj;
            LocalidadDao localidao = new LocalidadDao(context);

            while(rs.next()){

                cajaObj = new CajaObjeto(rs.getInt("ID_Caja"),localidao.obtenerLocalidad(rs.getInt("ID_Localidad")),
                        rs.getString("Descripcion"),rs.getString("Latitud"),rs.getString("Longitud"),rs.getBoolean("Estado"));

                ListaCajasObjetos.add(cajaObj);
            }
            con.close();

            return  ListaCajasObjetos;
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("ERROR_OBJ",e.getMessage());

        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {

        try{
            if(result != null){
                if(result instanceof List<?>){

                    ubicacionCajasAdapter adapter = new ubicacionCajasAdapter(context, txtDescripcionCaja, (List<CajaObjeto>) result,gMap);
                    pb.setVisibility(View.INVISIBLE);
                    lvUbicacionesCaja.setAdapter(adapter);

                }else if(result instanceof CajaObjeto){

                    this.cajaOBJ = (CajaObjeto) result;

                    if(cajaOBJ != null){

                        Log.i("INFO","OnPostExecute(). Municipio: "+ cajaOBJ.getDescripcion());
                        txtDescripcionCaja = ((Activity) context).findViewById(R.id.txtDescripcion);
                        txtDescripcionCaja.setText(cajaOBJ.getDescripcion());

                        setLatitud(cajaOBJ.getLatitud());
                        setLongitud(cajaOBJ.getLongitud());

                        String Localidad = cajaOBJ.getLocalidad().getNombre();

                        LatLng Municipio;

                        if (!TextUtils.isEmpty(Latitud)) {
                            Log.i("INFO","OnPostExecute(). Latitud no esta vacío: "+ Latitud + "Longitud: " + Longitud);
                            Municipio  = new LatLng(Double.parseDouble(Latitud), Double.parseDouble(Longitud));
                        }else{
                            Log.i("INFO","OnPostExecute(). Latitud esta vacío: "+ Latitud + "Latitud: "+ cajaOBJ.getLatitud() +"/ Longitud: " + cajaOBJ.getLongitud());
                            Municipio = new LatLng(Double.parseDouble(cajaOBJ.getLatitud()), Double.parseDouble(cajaOBJ.getLongitud()));
                        }

                        gMap.addMarker(new MarkerOptions().position(Municipio).title(Localidad));
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(Municipio));

                    }else{
                        Log.i("INFO","cajaOBJ fue nulo");
                    }

                }
            }
        }catch (Exception e){
            Log.i("ERROR_OBJ",e.getMessage());
        }
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }
}

