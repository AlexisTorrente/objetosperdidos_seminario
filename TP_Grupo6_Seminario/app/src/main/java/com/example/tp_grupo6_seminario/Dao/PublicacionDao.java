package com.example.tp_grupo6_seminario.Dao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.Data.ViewModelDatos;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.adapter.ComentarioPerfilAdapter;
import com.example.tp_grupo6_seminario.adapter.FTPUploader;
import com.example.tp_grupo6_seminario.adapter.MisPublicacionesAdapter;
import com.example.tp_grupo6_seminario.adapter.gridviewAdapter;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Notificacion;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;
import com.example.tp_grupo6_seminario.entidades.TipoPublicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PublicacionDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private boolean error;
    private ArrayList<Publicacion> listaDePublicaciones = new ArrayList<Publicacion>();
    private String operacion, operacionExtra = "";
    private GridView gridView;
    private ImageView ivImagenBaja;
    private TextView tvIdPub, tvTituloBaja;
    private ProgressBar pb;
    private ListView lvMisPublicaciones;
    private ViewModelDatos viewModelDatos;
    int MisPublicaciones, tipoPublicacion;
    TextView txtSinPublicacion;
    NavController navController;
    NotificacionesDao notificacionesDao;
    ProgressBar pbMisPublicaciones;
    public PublicacionDao(Context ct) {
        this.context = ct;
    }

    public PublicacionDao(Context ct, GridView gridView) {
        this.context = ct;
        this.gridView = gridView;
    }

    public PublicacionDao(Context ct, GridView gridView, int tipoPublicacion) {
        this.context = ct;
        this.gridView = gridView;
        this.tipoPublicacion = tipoPublicacion;
    }

    public PublicacionDao(Context ct, ImageView iv, TextView tv, TextView tvID) {
        this.context = ct;
        this.ivImagenBaja = iv;
        this.tvTituloBaja = tv;
        this.tvIdPub = tvID;
    }
    public PublicacionDao(Context ct, ListView lvMisPublicaciones,ProgressBar pbMisPublicaciones, int misPublicaciones,NavController navController) {
        this.context = ct;
        this.lvMisPublicaciones = lvMisPublicaciones;
        this.MisPublicaciones = misPublicaciones;
        this.navController = navController;
        this.pbMisPublicaciones = pbMisPublicaciones;
    }
    public PublicacionDao(Context ct, ImageView iv) {
        this.context = ct;
        this.ivImagenBaja = iv;
    }
    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "INSERTAR": //Inserta un nuevo usuario
                return insertarPublicacion((Publicacion) params[1]);
            case "ACTUALIZAR": //actualiza un usuario
                return actualizarPublicacion((Publicacion) params[1]);
            case "OBTENER_PUBLICACION_POR_ID": //obtiene un usuario por id
                if (params.length == 3) {
                    operacionExtra = (String) params[2];
                }
                return  obtenerPublicacion((int) params[1]);
            case "LISTADO": //lista todos los usuarios
                if(params.length == 1){
                    return obtenerListadoDePublicaciones();
                }
                else {
                    return obtenerListadoDePublicacionesConFiltro((String) params[1]);
                }
            case "BAJA":
                return bajaPublicacion((int) params[1]);
            case "RECUPERADO":
                Log.i("INFO_MISPUBLI","Llegue al OnClick() de btnPopUpRecuperado");

                int idMiPublicacion = (int) params[1];

                if(idMiPublicacion > -1){
                    Log.i("INFO_MISPUBLI","ID_Püblicacion: "+idMiPublicacion);
                }else{
                    Log.i("INFO_MISPUBLI","ID_Püblicacion devolvío cualquier cosa");

                }
                return EstablecerObjetoEncontrado((int) params[1]);
            case "CargarImagen":
                return ObtenerImagenPublicacion((int) params[1],(String) params[2]);

            default:
                return null; // Operación no reconocida
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if(result != null){
            if(result instanceof List<?>){

                if(MisPublicaciones != 1){
                    if(tipoPublicacion == 1) {

                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridviewAdapter adapter = new gridviewAdapter(context, (List<Publicacion>) result);
                                gridView.setAdapter(adapter);
                                pb.setVisibility(View.INVISIBLE);
                                viewModelDatos = new ViewModelProvider((ViewModelStoreOwner) context).get(ViewModelDatos.class);
                                viewModelDatos.setPublicacionesObjPerdido((List<Publicacion>) result);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }else if(tipoPublicacion == 2){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridviewAdapter adapter = new gridviewAdapter(context, (List<Publicacion>) result);
                                gridView.setAdapter(adapter);
                                pb.setVisibility(View.INVISIBLE);
                                viewModelDatos = new ViewModelProvider((ViewModelStoreOwner) context).get(ViewModelDatos.class);
                                viewModelDatos.setPublicacionesObjEncontrado((List<Publicacion>) result);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }else{
                    Log.i("INFO_MISPUBLI"," Llegue al onPostExecute");
                    List<Publicacion> misPublicaciones = (List<Publicacion>) result;

                    if (!misPublicaciones.isEmpty()) {
                        txtSinPublicacion = ((Activity) context).findViewById(R.id.txtSinPublicacion);

                        MisPublicacionesAdapter adptr = new MisPublicacionesAdapter(context, misPublicaciones,navController);
                        lvMisPublicaciones.setAdapter(adptr);
                        pbMisPublicaciones.setVisibility(View.GONE);
                        txtSinPublicacion.setVisibility(View.INVISIBLE);

                    }else{
                        txtSinPublicacion = ((Activity) context).findViewById(R.id.txtSinPublicacion);
                        pbMisPublicaciones.setVisibility(View.GONE);
                        txtSinPublicacion.setVisibility(View.VISIBLE);
                    }

                }

            }
            if(result instanceof Publicacion){
                Publicacion publicacion = (Publicacion) result;
                if(operacionExtra.equals("EDITAR")){
                    Bundle args = new Bundle();
                    args.putSerializable("Publicacion", publicacion);
                   // pb.setVisibility(View.INVISIBLE);

                    NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_activity_navigation_drawer);
                    navController.navigate(R.id.editarPublicacionFragment, args);
                }
                else {
                    int idItem = context.getResources().getIdentifier(publicacion.getImagen(), "drawable", context.getPackageName());
                    ivImagenBaja.setImageResource(idItem);
                    tvTituloBaja.setText(publicacion.getTitulo());
                    tvIdPub.setText(String.valueOf(publicacion.getId()));
                    pb.setVisibility(View.INVISIBLE);
                }
            }
            if(result instanceof String){
                if(operacion.equals("BAJA")){
                    String mensaje = (String) result;
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    if(pb != null){
                        pb.setVisibility(View.INVISIBLE);
                    }
                }else if(operacion.equals("RECUPERADO")){
                    Log.i("INFO_MISPUBLI","Llegue al OnPostExecute()");

                    String mensaje = (String) result;
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                }

            }
        }
        else{
            if(operacion.equals("OBTENER_PUBLICACION_POR_ID")) {
                Toast.makeText(context, "La publicacion no existe", Toast.LENGTH_SHORT).show();
            }
            if(pb != null){
                pb.setVisibility(View.INVISIBLE);
            }
            if(MisPublicaciones == 1){
                txtSinPublicacion = ((Activity) context).findViewById(R.id.txtSinPublicacion);
                txtSinPublicacion.setVisibility(View.VISIBLE);
            }

        }
    }

    private int obtenerUltimoIdDePublicacion(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM (SELECT MAX(ID_Publicacion) as ID FROM Publicaciones) A");
            int resultado = 0;
            while(ct.next()) {
                resultado = ct.getInt("ID");
                break;
            }
            con.close();
            return resultado;
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void insertarNotificacion(Publicacion publicacion){
        try {
            //Necesito buscar el id de la ultima publicacion que acaba de insertar
            //le seteo el Id
            publicacion.setId(obtenerUltimoIdDePublicacion());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.util.Date fechaActual = new java.util.Date(); // Fecha actual

            Notificacion notificacion = new Notificacion(0, publicacion.getUsuario(), publicacion, publicacion.getLocalidad(), publicacion.getDescripcion(),(Date) fechaActual, true);
            notificacionesDao = new NotificacionesDao(context);
            notificacionesDao.agregarNotificacion(notificacion);
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    private String insertarPublicacion(Publicacion publicacion){
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String sql = "INSERT INTO Publicaciones (ID_TipoPublicacion, ID_TipoObjeto, ID_Usuario, ID_Provincia, ID_Localidad, Titulo, Ubicacion, Imagen, FechaPublicacion, RangoHorario, Descripcion, Recuperado, Estado) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            stmt.setInt(1, publicacion.getTipoPublicacion().getId());
            stmt.setInt(2, publicacion.getTipoObjeto().getId());
            stmt.setInt(3, publicacion.getUsuario().getId());
            stmt.setInt(4, publicacion.getProvincia().getId());
            stmt.setInt(5, publicacion.getLocalidad().getId());
            stmt.setString(6, publicacion.getTitulo());
            stmt.setString(7, publicacion.getUbicacion());
            stmt.setString(8, publicacion.getImagen());
            stmt.setDate(9, publicacion.getFechaPublicacion());
            stmt.setString(10, publicacion.getHoraPublicacion());
            stmt.setString(11, publicacion.getDescripcion());
            stmt.setBoolean(12, publicacion.isRecuperado());
            stmt.setBoolean(13, publicacion.isEstado());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se agregó la publicacion con éxito.";
            } else {
                response = "No se pudo agregar la publicacion.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar agregar la publicacion.";
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();

                    //Insertamos una notificacion por cada publicacion - RR
                    insertarNotificacion(publicacion);
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

    private String actualizarPublicacion(Publicacion publicacion){
        //Actualiza o inserta una publicacion
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String sql = "UPDATE Publicaciones SET ID_TipoPublicacion = ?, ID_TipoObjeto = ?, ID_Usuario = ?, ID_Provincia = ?, ID_Localidad = ?, Titulo = ?, Ubicacion = ?, Imagen = ?, FechaPublicacion = ?, RangoHorario = ?, Descripcion = ?, Recuperado = ?, Estado = ? WHERE ID_Publicacion = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setInt(1, publicacion.getTipoPublicacion().getId());
            stmt.setInt(2, publicacion.getTipoObjeto().getId());
            stmt.setInt(3, publicacion.getUsuario().getId());
            stmt.setInt(4, publicacion.getProvincia().getId());
            stmt.setInt(5, publicacion.getLocalidad().getId());
            stmt.setString(6, publicacion.getTitulo());
            stmt.setString(7, publicacion.getUbicacion());
            stmt.setString(8, publicacion.getImagen());
            stmt.setDate(9, publicacion.getFechaPublicacion());
            stmt.setString(10, publicacion.getHoraPublicacion());
            stmt.setString(11, publicacion.getDescripcion());
            stmt.setBoolean(12, publicacion.isRecuperado());
            stmt.setBoolean(13, publicacion.isEstado());
            stmt.setInt(14, publicacion.getId());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se actualizó la publicacion con éxito.";

                String infoIMG = InsertarImagenPublicacion(publicacion.getId(),publicacion.getImagen());
                Log.i("INFO_MISPUBLI"," infoIMG: "+infoIMG.toString());

            } else {
                response = "No se pudo actualizar la publicacion con éxito.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar actualizar la publicacion";
        } finally {
            try {
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
    private String InsertarImagenPublicacion(int IdPublicacion, String rutaImagen){
        try{
            Log.i("INFO_MISPUBLI"," NombreRemoto: "+rutaImagen.toString());

            if (!rutaImagen.toString().isEmpty()) {
                if (FTPUploader.subirFoto(context,String.valueOf(IdPublicacion), rutaImagen)) {
                    Log.i("INFO_MISPUBLI","Ruta Local: "+rutaImagen.toString());
                } else {
                    Log.i("INFO_MISPUBLI"," No se puedo subir la foto.");
                }
            } else {
                Log.i("INFO_MISPUBLI"," No hay ninguna imagen cargada.");

            }

        }catch (Exception ex){
            Log.i("ERROR_MISPUBLI","Excepcion: "+ex.getMessage());
        }
        return "";
    }


    private String ObtenerImagenPublicacion(final int IdPublicacion, final String rutaImagen) {
        try {
            Log.i("INFO_MISPUBLI", " IdPublicacion: " + String.valueOf(IdPublicacion));
            Log.i("INFO_MISPUBLI", " RutaImagen: " + rutaImagen.toString());

            String rutaImagenLocal = FTPUploader.obtenerPrimeraImagen(context, ivImagenBaja, String.valueOf(IdPublicacion),rutaImagen);
            if (!(rutaImagenLocal.isEmpty())) {

                try{
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(rutaImagenLocal);
                            ivImagenBaja.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception ex){
                    Log.i("ERROR_MISPUBLI", "Linea 439 Excepcion: " + ex.getMessage());
                }
            }

        } catch (Exception ex) {
            Log.i("ERROR_MISPUBLI", "Linea 444 Excepcion: " + ex.getMessage());
        }
        return "";
    }

    private String EstablecerObjetoEncontrado(int idPublicacion) {

        String IdMiPublicacion = String.valueOf(idPublicacion);
        Log.i("INFO_MISPUBLI","Llegue a EstablecerObjetoEncontrado("+IdMiPublicacion+")");

        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Publicaciones SET Recuperado = 1 WHERE ID_Publicacion = ?";
            stmt = conn.prepareStatement(sql);

            // Establecer el valor del marcador de posición
            stmt.setInt(1, idPublicacion);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "¡OBJETO RECUPERADO!";
            } else {
                response = "No se pudo marcar recuperado el objeto.";
            }

        } catch (Exception ex) {
            Log.i("ERROR_MISPUBLI",ex.getMessage());

            ex.printStackTrace();
            response = "Error al intentar recuperar el objeto.";
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.i("ERROR_MISPUBLI",ex.getMessage());

            }
        }
        return response;
    }

    private String insertarActualizarPublicacion(Publicacion publicacion, boolean actualizar) {
        //Actualiza o inserta una publicacion
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String sql = actualizar ? "UPDATE Publicaciones SET ID_Publicacion = ?, ID_TipoPublicacion = ?, ID_TipoObjeto = ?, ID_Usuario = ?, ID_Provincia = ?, ID_Localidad = ?, Titulo = ?, Ubicacion = ?, Imagen = ?, FechaPublicacion = ?, RangoHorario = ?, Descripcion = ?, Recuperado = ?, Estado = ?" : "INSERT INTO Publicaciones (ID_Publicacion, ID_TipoPublicacion, ID_TipoObjeto, ID_Usuario, ID_Provincia, ID_Localidad, ID_Ubicacion, Imagen, FechaPublicacion, RangoHorario, Descripcion, Recuperado, Estado) VALUES (?,?,?,?,?,?,?,?,?.?,?,?,?)";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            if(actualizar){
                stmt.setInt(1, publicacion.getId());
            }
            else{
                stmt.setString(1, "NULL");
            }
            stmt.setInt(2, publicacion.getTipoPublicacion().getId());
            stmt.setInt(3, publicacion.getTipoObjeto().getId());
            stmt.setInt(4, publicacion.getUsuario().getId());
            stmt.setInt(5, publicacion.getProvincia().getId());
            stmt.setInt(6, publicacion.getLocalidad().getId());
            stmt.setString(7, publicacion.getTitulo());
            stmt.setString(8, publicacion.getUbicacion());
            stmt.setString(9, publicacion.getImagen());
            stmt.setDate(10, publicacion.getFechaPublicacion());
            stmt.setString(12, publicacion.getHoraPublicacion());
            stmt.setString(12, publicacion.getDescripcion());
            stmt.setBoolean(13, publicacion.isRecuperado());
            stmt.setBoolean(14, publicacion.isEstado());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = actualizar ? "Se actualizó la publicacion con éxito." : "Se agregó la publicacion con éxito.";
            } else {
                response = actualizar ?  "No se pudo actualizar la publicacion con éxito." : "No se pudo agregar la publicacion.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = actualizar ? "Error al intentar actualizar la publicacion" :  "Error al intentar agregar la publicacion.";
        } finally {
            try {
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

    public Publicacion obtenerPublicacion(int idPublicacion){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Publicaciones WHERE Estado = 1 AND ID_Publicacion = "+idPublicacion);
            TipoPublicacionDao tipoPublicacionDao = new TipoPublicacionDao(context);
            TipoDeObjetoDao tipoDeObjetoDatoDao = new TipoDeObjetoDao(context);
            UsuarioDao usuarioDao = new UsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            while(ct.next()) {
                Publicacion publicacion = new Publicacion(ct.getInt("ID_Publicacion")
                        ,tipoPublicacionDao.obtenerTipoPublicacion(ct.getInt("ID_TipoPublicacion"))
                        ,tipoDeObjetoDatoDao.obtenerTipoObjeto(ct.getInt("ID_TipoObjeto"))
                        ,usuarioDao.obtenerUsuario(ct.getInt("ID_Usuario"))
                        ,provinciaDao.obtenerProvincia(ct.getInt("ID_Provincia"))
                        ,localidadDao.obtenerLocalidad(ct.getInt("ID_Localidad"))
                        ,ct.getString("Titulo")
                        ,ct.getString("Ubicacion")
                        ,ct.getString("Imagen")
                        ,ct.getDate("FechaPublicacion")
                        ,ct.getString("RangoHorario")
                        ,ct.getString("Descripcion")
                        ,ct.getBoolean("Recuperado")
                        ,ct.getBoolean("Estado"));
                con.close();
                return publicacion;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String bajaPublicacion(int idPublicacion) {
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Publicaciones SET Estado = ? WHERE ID_Publicacion = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setInt(1, 0);
            stmt.setInt(2, idPublicacion);

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se dió de baja la publicacion con éxito";
            } else {
                response = "No se pudo dar de baja la publicacion";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar dar de baja la publicacion";
        } finally {
            try {
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

    public TipoPublicacion obtenerTipoPublicacion(int idTipoPublicacion) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM `Tipo Publicaciones` WHERE ID_TipoPublicacion = " + idTipoPublicacion);
            while (ct.next()) {
                TipoPublicacion tipoPubli = new TipoPublicacion(ct.getInt("ID_TipoPublicacion"), ct.getString("Descripcion"));
                con.close();
                return tipoPubli;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public Usuario obtenerUsuario(int idUsuario) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE ID_usuario = " + idUsuario);
            TipoUsuarioDao tipoUsuarioDao = new TipoUsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            GeneroDao generoDao = new GeneroDao(context);
            while (ct.next()) {
                Usuario usuario = new Usuario(ct.getInt("ID_usuario")
                        , tipoUsuarioDao.obtenerTipoDeUsuario(ct.getInt("ID_TipoUsuario"))
                        , provinciaDao.obtenerProvincia(ct.getInt("ID_Provincia"))
                        , localidadDao.obtenerLocalidad(ct.getInt("ID_Localidad"))
                        , generoDao.obtenerGenero(ct.getInt("ID_Genero"))
                        , ct.getString("NombreUsuario"), ct.getString("Contrasena")
                        , ct.getString("Nombre"), ct.getString("Apellido")
                        , ct.getString("DNI"), ct.getString("Email")
                        , ct.getString("FechaNacimiento"),
                        ct.getString("DescripcionPerfil"),ct.getBoolean("Estado"));
                con.close();
                return usuario;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public ArrayList<Publicacion>obtenerListadoDePublicaciones(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Publicaciones WHERE Estado = 1");
            TipoPublicacionDao tipoPublicacionDao = new TipoPublicacionDao(context);
            TipoDeObjetoDao tipoDeObjetoDatoDao = new TipoDeObjetoDao(context);
            UsuarioDao usuarioDao = new UsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            while(ct.next()) {
                Publicacion publicacion = new Publicacion(ct.getInt("ID_Publicacion")
                        ,obtenerTipoPublicacion(ct.getInt("ID_TipoPublicacion"))
                        ,obtenerTipoObjeto(ct.getInt("ID_TipoObjeto"))
                        ,obtenerUsuario(ct.getInt("ID_Usuario"))
                        ,obtenerProvincia(ct.getInt("ID_Provincia"))
                        ,obtenerLocalidad(ct.getInt("ID_Localidad"))
                        ,ct.getString("Titulo")
                        ,ct.getString("Ubicacion")
                        ,ct.getString("Imagen")
                        ,ct.getDate("FechaPublicacion")
                        ,ct.getString("RangoHorario")
                        ,ct.getString("Descripcion")
                        ,ct.getBoolean("Recuperado")
                        ,ct.getBoolean("Estado"));
                listaDePublicaciones.add(publicacion);
            }
            con.close();
            return  listaDePublicaciones;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Publicacion>obtenerListadoDePublicacionesConFiltro(String filtro){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Publicaciones WHERE" + filtro);
            TipoPublicacionDao tipoPublicacionDao = new TipoPublicacionDao(context);
            TipoDeObjetoDao tipoDeObjetoDatoDao = new TipoDeObjetoDao(context);
            UsuarioDao usuarioDao = new UsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            while(ct.next()) {
                Publicacion publicacion = new Publicacion(ct.getInt("ID_Publicacion")
                        ,obtenerTipoPublicacion(ct.getInt("ID_TipoPublicacion"))
                        ,obtenerTipoObjeto(ct.getInt("ID_TipoObjeto"))
                        ,obtenerUsuario(ct.getInt("ID_Usuario"))
                        ,obtenerProvincia(ct.getInt("ID_Provincia"))
                        ,obtenerLocalidad(ct.getInt("ID_Localidad"))
                        ,ct.getString("Titulo")
                        ,ct.getString("Ubicacion")
                        ,ct.getString("Imagen")
                        ,ct.getDate("FechaPublicacion")
                        ,ct.getString("RangoHorario")
                        ,ct.getString("Descripcion")
                        ,ct.getBoolean("Recuperado")
                        ,ct.getBoolean("Estado"));
                listaDePublicaciones.add(publicacion);
            }
            con.close();
            return  listaDePublicaciones;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
