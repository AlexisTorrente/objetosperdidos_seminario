package com.example.tp_grupo6_seminario.Dao;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.adapter.MensajeAdapter;
import com.example.tp_grupo6_seminario.adapter.notificacionesAdapter;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Mensaje;
import com.example.tp_grupo6_seminario.entidades.Notificacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MensajeDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private ListView lvMensajes;
    private String operacion;

    private Mensaje mensaje;

    public MensajeDao(Context ct) {
        this.context = ct;
    }
    public MensajeDao(Context ct, ListView listView) {
        this.context = ct;
        this.lvMensajes = listView;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "INSERTAR_MENSAJE": //Inserta MSJ
                return realizarInsercionMensaje((Mensaje) params[1]);
            case "LISTAR_MENSAJES": //Lista mensajes
                return obtenerListadoDeMensajesEntre2usuarios((String) params[1], (String) params[2]);
            default:
                return null; // Operación no reconocida
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result != null) {
            if(result instanceof List<?>){
                // adaptador del list view con la lista de msjs
                MensajeAdapter mensajeAdapter = new MensajeAdapter(context,(List<Mensaje>)result);
                lvMensajes.setAdapter(mensajeAdapter);

            }else if(result instanceof Localidad){

            }
        }
    }

    private ArrayList<Mensaje> obtenerListadoDeMensajesEntre2usuarios(String desde, String hasta)
    {
        ArrayList<Mensaje> listaMensajes = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            //Traigo las notificaciones del dia, activas, correspondientes a la localidad del usuario que las solicita.
            ResultSet rs = st.executeQuery("SELECT * FROM Mensajes WHERE desde = '" + desde + "' AND hacia = '" + hasta + "'" + " OR ");
            while(rs.next()){
                Mensaje msj = new Mensaje(rs.getInt("id")
                        ,rs.getString("Mensaje")
                        ,rs.getString("desde")
                        ,rs.getString("hacia")
                        ,rs.getDate("horaEnvio")
                );
                listaMensajes.add(msj);
            }

            rs = st.executeQuery("SELECT * FROM Mensajes WHERE desde = '" + hasta + "' AND hacia = '" + desde + "'");
            while(rs.next()){
                Mensaje msj = new Mensaje(rs.getInt("id")
                        ,rs.getString("Mensaje")
                        ,rs.getString("desde")
                        ,rs.getString("hacia")
                        ,rs.getDate("horaEnvio")
                );
                listaMensajes.add(msj);
            }
            Collections.sort(listaMensajes);
            con.close();
            return listaMensajes;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listaMensajes;
    }

    private boolean realizarInsercionMensaje(Mensaje msj) {
        // Lógica de inserción en la base de datos
        boolean response = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);


            String sql = "INSERT INTO Mensajes (mensaje,desde,hacia,horaEnvio) VALUES (?, ?, ?, ?)";
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            // Itera sobre los objetos Articulo proporcionados y realiza las inserciones

            stmt.setString(1, msj.getMsj());
            stmt.setString(2, msj.getDesde());
            stmt.setString(3, msj.getHacia());
            java.util.Date utilDate = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
            stmt.setTimestamp(4, timestamp);

            // Ejecuta la sentencia SQL de inserción
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = true;
                //response = "Se agregó el usuario con éxito.";
            } else {
                response = false;
                // response = "Error al intentar agregar el usuario.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = false;
            //response = "Error con conexion a la BD";
        } finally {
            try {
                // Cierra los recursos
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

}
