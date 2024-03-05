package com.example.tp_grupo6_seminario.Dao;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.adapter.notificacionesAdapter;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Notificacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class NotificacionesDao extends AsyncTask<Object, Void, Object> {
    private Context context;
    private String operacion;
    private Usuario usuario;
    private Notificacion notificacion;
    private ArrayList<Notificacion> listaNotificaciojnes = new ArrayList<>();
    private ListView lvNotificaciones;

    public NotificacionesDao(Context ct) {
        this.context = ct;
    }
    public NotificacionesDao(Context ct, ListView listView) {
        this.context = ct;
        this.lvNotificaciones = listView;
    }

    public NotificacionesDao(Context ct, Notificacion notificacion) {
        this.context = ct;
        this.notificacion = notificacion;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "INSERTAR_NOTIFICACION": //Inserta notificacion
                return agregarNotificacion((Notificacion) params[1]);
            case "LISTAR_NOTIFICACIONES": //Lista Notificacion del dia, activa filtrada por localidad
                return obtenerListadoDeNotificaciones((int) params[1]);
            default:
                return null; // Operación no reconocida
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result != null) {
            if(result instanceof List<?>){
                // Configurar el adaptador del list view con la lista de notificaciones
                notificacionesAdapter notiAdapter = new notificacionesAdapter(context,(List<Notificacion>)result);
                lvNotificaciones.setAdapter(notiAdapter);

            }else if(result instanceof Localidad){

            }
        }
    }

    public String agregarNotificacion(Notificacion notificacion) {
        String response ="";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String sql = "INSERT INTO Notificaciones (ID_Notificaciones,ID_Usuario,ID_Publicacion,ID_Localidad,Descripcion,Fecha_Hora,Estado) VALUES (?,?,?,?,?,?,?)";

            stmt = (PreparedStatement) conn.prepareStatement(sql);

            stmt.setInt(1, notificacion.getId());
            stmt.setInt(2, notificacion.getUsuario().getId());
            stmt.setInt(3,notificacion.getPublicacion().getId() );
            stmt.setInt(4,notificacion.getLocalidad().getId() );
            stmt.setString(5,notificacion.getDescripcion() );
            stmt.setDate(6,notificacion.getFecha_hora() );
            stmt.setBoolean(7, true);

            // Ejecuta la sentencia SQL de inserción
            int filasAfectadas = stmt.executeUpdate();

       //EL RESPONSE LO VOY A SACAR
            if (filasAfectadas > 0) {
                response = "Se agregó la notificacion exitosamente.";     }
            else {    response = "Error al intentar agregar la notificacion.";   }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Error con conexion a la BD";
        } finally {
            try {
                // Cierra los recursos
                if (stmt != null) {  stmt.close();   }
                if (conn != null) {  conn.close();   }
            } catch (SQLException e) {  e.printStackTrace();   }
        }
        return response;
    }


    public Notificacion obtenerNotificacion(int idNotificacion){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Notificaciones WHERE ID_Notificaciones = "+idNotificacion);
            UsuarioDao usuarioDao = new UsuarioDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            PublicacionDao publicacionDao = new PublicacionDao(context);
            while(ct.next()) {
                Notificacion notificacion = new Notificacion(ct.getInt("ID_Notificaciones")
                                                            ,usuarioDao.obtenerUsuario(ct.getInt("ID_Usuario"))
                                                            ,publicacionDao.obtenerPublicacion(ct.getInt("ID_Publicacion"))
                                                            ,localidadDao.obtenerLocalidad(ct.getInt("ID_Localidad"))
                                                            ,ct.getString("Descripcion")
                                                            ,ct.getDate("Fecha_hora")
                                                            ,ct.getBoolean("Estado")
                );
                con.close();
                return notificacion;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Notificacion>obtenerListadoDeNotificaciones(int idLocalidad){
        //retorna el listado de notificaciones
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            //Traigo las notificaciones del dia, activas, correspondientes a la localidad del usuario que las solicita.
            ResultSet rs = st.executeQuery("SELECT * FROM Notificaciones WHERE Estado = 1 AND fecha_hora = CURDATE() AND ID_Localidad = "+ idLocalidad);
            //  ResultSet rs = st.executeQuery("SELECT * FROM Notificaciones ");
            UsuarioDao usuarioDao = new UsuarioDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            PublicacionDao publicacionDao = new PublicacionDao(context);
            while(rs.next()){
                Notificacion notificacion = new Notificacion(rs.getInt("ID_Notificaciones")
                        ,usuarioDao.obtenerUsuario(rs.getInt("ID_Usuario"))
                        ,publicacionDao.obtenerPublicacion(rs.getInt("ID_Publicacion"))
                        ,localidadDao.obtenerLocalidad(rs.getInt("ID_Localidad"))
                        ,rs.getString("Descripcion")
                        ,rs.getDate("Fecha_hora")
                        ,rs.getBoolean("Estado")
                );
                listaNotificaciojnes.add(notificacion);
            }
            con.close();
            return  listaNotificaciojnes;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
