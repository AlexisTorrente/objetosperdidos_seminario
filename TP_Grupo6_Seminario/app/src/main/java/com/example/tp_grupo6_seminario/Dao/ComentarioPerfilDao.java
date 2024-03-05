package com.example.tp_grupo6_seminario.Dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.adapter.ComentarioPerfilAdapter;
import com.example.tp_grupo6_seminario.adapter.gridviewAdapter;
import com.example.tp_grupo6_seminario.entidades.ComentarioPerfil;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.TipoUsuario;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ComentarioPerfilDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private String operacion;
    private ArrayList<ComentarioPerfil> listaComentariosPerfil = new ArrayList<ComentarioPerfil>();
    private ListView lv;
    private ProgressBar pb;
    public ComentarioPerfilDao(Context ct) {
        this.context = ct;
    }

    public ComentarioPerfilDao(Context ct, ListView lv, ProgressBar pb) {
        this.context = ct;
        this.lv = lv;
        this.pb = pb;
    }



    @Override
    protected Object doInBackground(Object... params) {
            if (params.length < 1 || params[0] == null) {
                return null; // No se proporcionó una operación válida
            }
            this.operacion = (String) params[0];
                switch (operacion) {
                    case "LISTADO_COMENTARIOS": //Inserta un nuevo usuario
                        return obtenerListadoDeComentariosPerfil((Usuario) params[1]);
                    case "AGREGAR":
                        return agregarComentario((ComentarioPerfil) params[1]);
                    default:
                        return null; // Operación no reconocida
                }
            }


    private String agregarComentario(ComentarioPerfil comentarioPerfil) {
        String response ="";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);


            String sql = "INSERT INTO `Comentarios perfil` (ID_UsuarioComentario,ID_UsuarioPerfil,Comentario,Estado) VALUES (?, ?, ?, ?)";
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            // Itera sobre los objetos Articulo proporcionados y realiza las inserciones

            stmt.setInt(1, comentarioPerfil.getUsuarioComenterio().getId());
            stmt.setInt(2, comentarioPerfil.getUsuarioPerfil().getId());
            stmt.setString(3, comentarioPerfil.getComenterio());
            stmt.setInt(4, 1);

            // Ejecuta la sentencia SQL de inserción
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se agregó el comentario con éxito.";
            } else {
                response = "Error al intentar agregar el comentario.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error con conexion a la BD";
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

    @Override
    protected void onPostExecute(Object result) {
        if(result != null){
            Log.d("ComentarioPerfilDao", "Todo correcto. Resultado no nulo.");
            if(result instanceof List<?>){
                Log.d("ComentarioPerfilDao", "Todo correcto. Restultado es una list.");
                ComentarioPerfilAdapter adapter = new ComentarioPerfilAdapter(context, (List<ComentarioPerfil>) result);
                pb.setVisibility(View.INVISIBLE);
                lv.setAdapter(adapter);

            }else if(result instanceof String){
                String mensaje = (String) result;
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<ComentarioPerfil>obtenerListadoDeComentariosPerfil(Usuario usuario){
        //si se pasa un usuario se listas los comentarios de el, sino todos
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            UsuarioDao usuarioDao = new UsuarioDao(context);
            ResultSet ct = st.executeQuery("SELECT * FROM `Comentarios perfil` WHERE ID_UsuarioPerfil = "+usuario.getId());
            while(ct.next()) {
                if(isCancelled()) return null;
                ComentarioPerfil comentarioPerfil = new ComentarioPerfil();
                if(isCancelled()) return null;
                comentarioPerfil.setId(ct.getInt("ID_ComentarioPerfil"));
                if(isCancelled()) return null;
                comentarioPerfil.setUsuarioComenterio(obtenerUsuario(ct.getInt("ID_UsuarioComentario")));
                if(isCancelled()) return null;
                comentarioPerfil.setUsuarioPerfil(obtenerUsuario(ct.getInt("ID_UsuarioPerfil")));
                if(isCancelled()) return null;
                comentarioPerfil.setComenterio(ct.getString("Comentario"));
                if(isCancelled()) return null;
                comentarioPerfil.setEstado(ct.getBoolean("Estado"));
                listaComentariosPerfil.add(comentarioPerfil);
                if(isCancelled())   return null;
            }
            con.close();
            Log.d("ComentarioPerfilDao", "Todo correcto. Linea 101.");
            return  listaComentariosPerfil;
        }
        catch (Exception e){
            Log.d("ComentarioPerfilDao", "Hubo excepcion. Linea 106.");
            e.printStackTrace();
        }
        return null;
    }

    public Usuario obtenerUsuario(int idUsuario) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE ID_Usuario = " + idUsuario);
            TipoUsuarioDao tipoUsuarioDao = new TipoUsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            GeneroDao generoDao = new GeneroDao(context);
            while (ct.next()) {
                Usuario usuario = new Usuario(ct.getInt("ID_Usuario")
                        , tipoUsuarioDao.obtenerTipoDeUsuario(ct.getInt("ID_TipoUsuario"))
                        , provinciaDao.obtenerProvincia(ct.getInt("ID_Provincia"))
                        , localidadDao.obtenerLocalidad(ct.getInt("ID_Localidad"))
                        , generoDao.obtenerGenero(ct.getInt("ID_Genero"))
                        , ct.getString("NombreUsuario"), ct.getString("Contrasena")
                        , ct.getString("Nombre"), ct.getString("Apellido")
                        , ct.getString("DNI"), ct.getString("Email")
                        , ct.getString("FechaNacimiento"), ct.getBoolean("Estado"));
                con.close();
                return usuario;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
