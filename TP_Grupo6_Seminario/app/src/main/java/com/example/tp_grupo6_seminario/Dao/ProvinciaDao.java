package com.example.tp_grupo6_seminario.Dao;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;

public class ProvinciaDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ArrayList<Provincia> listaProvincias = new ArrayList<Provincia>();
    private String operacion;
    private Usuario usuario;
    private Spinner spProvincias, spLocalidades;
    private Provincia provinciaSeleccionada = null;
    private boolean filtro = false;
    public ProvinciaDao(Context ct) {
        this.context = ct;
    }
    public ProvinciaDao(Context ct, Spinner spinner) {
        this.context = ct;
        this.spProvincias = spinner;
    }

    public ProvinciaDao(Context ct, Spinner spinner, Provincia prov) {
        this.context = ct;
        this.spProvincias = spinner;
        this.provinciaSeleccionada = prov;
    }

    public ProvinciaDao(Context ct, Spinner spinnerProv, Spinner spinnerLoc) {
        this.context = ct;
        this.spProvincias = spinnerProv;
        this.spLocalidades = spinnerLoc;
    }

    public ProvinciaDao(Context ct, Spinner spinnerProv, Spinner spinnerLoc, Usuario usr) {
        this.context = ct;
        this.spProvincias = spinnerProv;
        this.spLocalidades = spinnerLoc;
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
                return  obtenerProvincia((int) params[1]);
            case "LISTADO": //lista todos los usuarios
                if(params.length > 1){
                    this.filtro = (boolean) params[1];
                }
                return obtenerListadoDeProvincias();
            default:
                return null; // Operación no reconocida
        }

        /*String response = "";
        try {
            response = "Provincias cargadas exitosamente";
        } catch (Exception e) {
            e.printStackTrace();
            response = "no se ha podido cargar las Provincias";
        }
        return response;*/
    }



    public Provincia obtenerProvincia(int idProvincia){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Provincias WHERE ID_Provincia = "+idProvincia);
            while(ct.next()) {
                Provincia provincia = new Provincia(ct.getInt("ID_Provincia"),ct.getString("Nombre"));
                con.close();
                return provincia;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Provincia>obtenerListadoDeProvincias(){
        //retorna un listado de provincias
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Provincias");
            Provincia provincia;
            while(rs.next()){
                provincia = new Provincia(rs.getInt("ID_Provincia"),rs.getString("Nombre"));
                listaProvincias.add(provincia);
            }
            con.close();
            return  listaProvincias;
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
                List<Provincia> provincia = (List<Provincia>) result;
                if(filtro) {
                    Provincia provAux = new Provincia();
                    provAux.setId(0);
                    provAux.setNombre("Todas");
                    provincia.add(0, provAux);
                }
                ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaProvincias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProvincias.setAdapter(adapter);

                if(provinciaSeleccionada != null){
                    spProvincias.setSelection(getIndex(spProvincias, provinciaSeleccionada.getNombre()));
                }

                if (usuario != null) {
                    for (int i = 0; i < provincia.size(); i++) {
                        if (provincia.get(i).getId() == usuario.getProvincia().getId()) {
                            spProvincias.setSelection(i);
                            spProvincias.setEnabled(true);
                            break; // Una vez que se ha encontrado la coincidencia, puedes salir del bucle
                        }else{
                            spProvincias.setEnabled(true);
                        }
                    }
                }
                spProvincias.setEnabled(true);
                if(spLocalidades!=null){
                    Provincia provinciaSeleccionada = (Provincia) spProvincias.getSelectedItem();
                    int idProv = provinciaSeleccionada.getId();
                    String nombreProv = provinciaSeleccionada.getNombre();

                    Provincia prov = new Provincia();
                    prov.setId(idProv);
                    prov.setNombre(nombreProv);

                    LocalidadDao ldao = new LocalidadDao(context,spLocalidades,usuario);
                    ldao.execute("LISTADO",prov);
                }
            }else if(result instanceof Provincia){

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
