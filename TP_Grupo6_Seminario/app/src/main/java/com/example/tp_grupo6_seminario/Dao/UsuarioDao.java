package com.example.tp_grupo6_seminario.Dao;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.tp_grupo6_seminario.Conexion.DataDB;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.activity_navigation_drawer;
import com.example.tp_grupo6_seminario.adapter.UsuarioAdapter;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Mensaje;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao extends AsyncTask<Object, Void, Object> {

    private Context context;
    private boolean error;

    private UsuarioAdapter usuarioAdapter;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
    private ListView listview;
    private String operacion, nombreUsr,contraseniaUsr;
    private ProgressBar pb;
    private Usuario usuario;
    private Mensaje mensaje;
    private datosUsuarioViewModel viewModel;
    private Button btnAccion;
    private TextView tvIDBaja, tvNombreUsuarioBaja, tvNombreyApellidoBaja, tvCorreoElectronicoBaja, tvProvinciaLocalidadBaja;
    private EditText etNombre,etApellido,etDNI,etFechaNacimiento,etCorreo,etUsuario,etContrasenia,etRepetircontrasenia;

    public UsuarioDao(Context ct) {
        this.context = ct;
    }

    public UsuarioDao(Context ct, Usuario usuario) {
        this.context = ct;
        this.usuario = usuario;
    }

    public UsuarioDao(Context ct, ListView lsUsers) {
        this.context = ct;
        this.listview = lsUsers;
    }

    public UsuarioDao(Context ct, Usuario usuario, String nombreUsuario, String contrasenia) {
        this.context = ct;
        this.usuario = usuario;
        this.nombreUsr = nombreUsuario;
        this.contraseniaUsr = contrasenia;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public void setBtn(Button btnIniciar){this.btnAccion = btnIniciar;}

    public void setNombreUsr(String nombreUsr){this.nombreUsr = nombreUsr;}

    public UsuarioDao(Context ct, TextView tvIDBaja, TextView tvNombreUsuarioBaja, TextView tvNombreyApellidoBaja, TextView tvCorreoElectronicoBaja, TextView tvProvinciaLocalidadBaja) {
        this.context = ct;
        this.tvIDBaja = tvIDBaja;
        this.tvNombreUsuarioBaja = tvNombreUsuarioBaja;
        this.tvNombreyApellidoBaja = tvNombreyApellidoBaja;
        this.tvCorreoElectronicoBaja = tvCorreoElectronicoBaja;
        this.tvProvinciaLocalidadBaja = tvProvinciaLocalidadBaja;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length < 1 || params[0] == null) {
            return null; // No se proporcionó una operación válida
        }
        this.operacion = (String) params[0];
        switch (operacion) {
            case "INSERTAR": //Inserta un nuevo usuario
                return realizarInsercionUsuario((Usuario) params[1]);
            case "ACTUALIZAR": //actualiza un usuario
                return ActualizarUsuario((Usuario) params[1]);
            case "ACTUALIZAR_DESCRIPCION": //actualiza un usuario
                return ActualizarDescripcionUsuario((Usuario) params[1]);
            case "ACTUALIZAR_TIPOUSUARIO": //actualiza el tipo de usuario
               return ActualizarTipoUsuario(this.usuario);
            case "OBTENER": //obtiene un usuario por id
                return obtenerUsuario((int) params[1]);
            case "LISTADO": //lista todos los usuarios

                return obtenerListadoDeUsuarios();
            case "LISTADO_CHATS":
                return obtenerListadoDeUsuariosChats();
            case "INICIAR SESION": // Aca se verifica si el usuario existe en la bd para el inicio de sesion
                return ObtenerUsuarioIniciarSesion(this.nombreUsr, this.contraseniaUsr);
            case "OBTENER_PORNOMBRE":
                return obtenerUsuarioPorNombreUsuario((String) params[1]);
            case "BAJA":
                return BajaUsuario((int) params[1]);
            default:
                return null; // Operación no reconocida
        }
    }


    private String ActualizarUsuario(Usuario usuario) {
        //Actualiza o inserta un usuario
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Usuarios SET ID_Provincia = ?, ID_Localidad = ?, ID_Genero = ?, NombreUsuario = ?, Nombre = ?, Apellido = ?, Email = ?, FechaNacimiento = ? WHERE ID_Usuario = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setInt(1, usuario.getProvincia().getId());
            stmt.setInt(2, usuario.getLocalidad().getId());
            stmt.setInt(3, usuario.getGenero().getId());
            stmt.setString(4, usuario.getNombreUsuario());
            stmt.setString(5, usuario.getNombre());
            stmt.setString(6, usuario.getApellido());
            stmt.setString(7, usuario.getEmail());
            stmt.setString(8, usuario.getFechaNac());
            stmt.setInt(9, usuario.getId());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se actualizó el usuario con éxito.";
            } else {
                response = "No se pudo actualizar el usuario con éxito.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar actualizar el usuario ";
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

    private String ActualizarTipoUsuario(Usuario usuario){
        //Actualiza el tipo de usuario
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Usuarios SET ID_TipoUsuario = ? WHERE ID_Usuario = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setInt(1, usuario.getTipoUsuario().getId());
            stmt.setInt(2, usuario.getId());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se actualizó el usuario con éxito.";
            } else {
                response = "No se pudo actualizar el usuario con éxito.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar actualizar el usuario ";
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
    private String BajaUsuario(int idUsuario) {
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Usuarios SET Estado = ? WHERE ID_Usuario = ? AND Estado = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setInt(1, 0);
            stmt.setInt(2, idUsuario);
            stmt.setInt(3, 1);

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se dió de baja el usuario con éxito.";
            } else {
                response = "No se pudo dar de baja el usuario.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar dar de baja el usuario ";
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

    private String ActualizarDescripcionUsuario(Usuario usuario){
        String response = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String sql = "UPDATE Usuarios SET DescripcionPerfil = ? WHERE ID_Usuario = ?";
            stmt = (PreparedStatement) conn.prepareStatement(sql);

            // Genera el objeto a insertar
            stmt.setString(1, usuario.getDescripcionPerfil());
            stmt.setInt(2, usuario.getId());

            // Ejecuta la sentencia SQL de inserción / actualizacion
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se actualizó la descripcion con éxito.";
            } else {
                response = "No se pudo actualizar la descripción.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error al intentar actualizar la descripcion.";
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

    private String realizarInsercionUsuario(Usuario usuario) {
        // Lógica de inserción en la base de datos
        String response = "";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);


            String sql = "INSERT INTO Usuarios (ID_TipoUsuario,ID_Provincia,ID_Localidad,ID_Genero,NombreUsuario,Contrasena,Nombre,Apellido,DNI,Email,FechaNacimiento,Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,?, ?)";
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            // Itera sobre los objetos Articulo proporcionados y realiza las inserciones

            stmt.setInt(1, usuario.getTipoUsuario().getId());
            stmt.setInt(2, usuario.getProvincia().getId());
            stmt.setInt(3, usuario.getLocalidad().getId());
            stmt.setInt(4, usuario.getGenero().getId());
            stmt.setString(5, usuario.getNombreUsuario());
            stmt.setString(6, usuario.getContrasena());
            stmt.setString(7, usuario.getNombre());
            stmt.setString(8, usuario.getApellido());
            stmt.setString(9, usuario.getDni());
            stmt.setString(10, usuario.getEmail());
            stmt.setString(11, usuario.getFechaNac());
            stmt.setBoolean(12, usuario.isEstado());

            // Ejecuta la sentencia SQL de inserción
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response = "Se agregó el usuario con éxito.";
            } else {
                response = "Error al intentar agregar el usuario.";
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

    public Usuario obtenerUsuario(int idUsuario) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE ID_Usuario = " + idUsuario+" AND Estado = 1");
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

    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE NombreUsuario = '" + nombreUsuario+"' AND Estado=1");
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
            Log.d("UsuarioDao","ERROR AL BUSCAR POR NOMBRE");
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Usuario> obtenerListadoDeUsuarios() {
        try {
            Log.d("UsuarioDao","Entro en obtenerListadoDeUsuarios()");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE Estado=1");
            while (ct.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(ct.getInt("ID_Usuario"));
                usuario.setNombreUsuario(ct.getString("NombreUsuario"));
                usuario.setApellido(ct.getString("Apellido"));
                usuario.setNombre(ct.getString("Nombre"));
                usuario.setDni(ct.getString("DNI"));
                listaUsuarios.add(usuario);
            }
            con.close();
            return listaUsuarios;
        } catch (Exception e) {
            Log.d("UsuarioDao","Error linea 442.");
            e.printStackTrace();
        }
        Log.d("UsuarioDao","VA A SER NULO");
        return null;
    }

    public ArrayList<Usuario> obtenerListadoDeUsuariosChats() {
        try {
            Log.d("UsuarioDao","Entro en obtenerListadoDeUsuariosChats()");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet ct = st.executeQuery("SELECT * FROM Usuarios WHERE Estado=1 AND ID_TipoUsuario = 1");
            while (ct.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(ct.getInt("ID_Usuario"));
                usuario.setNombreUsuario(ct.getString("NombreUsuario"));
                usuario.setApellido(ct.getString("Apellido"));
                usuario.setNombre(ct.getString("Nombre"));
                usuario.setDni(ct.getString("DNI"));
                listaUsuarios.add(usuario);
            }
            con.close();
            return listaUsuarios;
        } catch (Exception e) {
            Log.d("UsuarioDao","Error linea 442.");
            e.printStackTrace();
        }
        Log.d("UsuarioDao","VA A SER NULO");
        return null;
    }



    public Usuario ObtenerUsuarioIniciarSesion(String nombreUsuario, String contrasena) {
        Usuario usuario = new Usuario();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Usuarios WHERE NombreUsuario = ? AND Contrasena = ? AND Estado = ?");
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);
            stmt.setInt(3, 1);
            ResultSet rs = stmt.executeQuery();

            TipoUsuarioDao tipoUsuarioDao = new TipoUsuarioDao(context);
            ProvinciaDao provinciaDao = new ProvinciaDao(context);
            LocalidadDao localidadDao = new LocalidadDao(context);
            GeneroDao generoDao = new GeneroDao(context);

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("ID_Usuario"),
                        tipoUsuarioDao.obtenerTipoDeUsuario(rs.getInt("ID_TipoUsuario")),
                        provinciaDao.obtenerProvincia(rs.getInt("ID_Provincia")),
                        localidadDao.obtenerLocalidad(rs.getInt("ID_Localidad")),
                        generoDao.obtenerGenero(rs.getInt("ID_Genero")),
                        rs.getString("NombreUsuario"),
                        rs.getString("Contrasena"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("DNI"),
                        rs.getString("Email"),
                        rs.getString("FechaNacimiento"),
                        rs.getString("DescripcionPerfil"),
                        rs.getBoolean("Estado")
                );
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }




    @Override
    protected void onPostExecute(Object result) {
        if (result != null) {
            Log.d("UsuarioDao","Entro en onPostExecute, NO ES NULL");
            if (result instanceof String) {
                if(operacion.equals("ACTUALIZAR") || operacion.equals("ACTUALIZAR_TIPOUSUARIO")){
                    String mensaje = (String) result;
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(datosUsuarioViewModel.class);
                    viewModel.setUsuario(usuario);
                }else{
                    String mensaje = (String) result;
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    if(pb != null){
                        pb.setVisibility(View.INVISIBLE);
                    }
                }
            } else if (result instanceof Usuario) {
                if (operacion.equals("INICIAR SESION")) {

                    this.usuario = (Usuario) result;

                    if (usuario.getId() > 0) {
                        mostrarMensaje("Inicio de sesion éxitosa.");
                        pb.setVisibility(View.INVISIBLE); // Ocultar el ProgressBar
                        Intent intent = new Intent(context, activity_navigation_drawer.class);
                        intent.putExtra("usuario_session",usuario);
                        context.startActivity(intent);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Habilitar el botón después de 3 segundos
                                btnAccion.setEnabled(true);
                            }
                        }, 3000); // 3000 milisegundos = 3 segundos
                    } else {
                        mostrarMensaje("No se pudo iniciar sesion, por favor, revise los datos ingresados.");
                        pb.setVisibility(View.INVISIBLE); // Ocultar el ProgressBar
                        btnAccion.setEnabled(true);
                    }
                } else if(operacion.equals("OBTENER")){
                    this.usuario = (Usuario) result;
                    if (usuario.getId() > 0) {
                        mostrarMensaje("Se ha encontrado el usuario.");
                        tvIDBaja.setText(String.valueOf(usuario.getId()));
                        tvNombreUsuarioBaja.setText(usuario.getNombreUsuario());
                        tvNombreyApellidoBaja.setText(usuario.getNombre() + " " + usuario.getApellido());
                        tvCorreoElectronicoBaja.setText(usuario.getEmail());
                        tvProvinciaLocalidadBaja.setText(usuario.getProvincia() + " - " + usuario.getLocalidad());
                        pb.setVisibility(View.INVISIBLE);
                        this.btnAccion.setEnabled(true);
                    }
                } else if (operacion.equals("OBTENER_PORNOMBRE")) {
                    this.usuario = (Usuario) result;
                    if (usuario.getId() > 0) {
                        mostrarMensaje("Se ha encontrado el usuario.");
                        tvIDBaja.setText(String.valueOf(usuario.getId()));
                        tvNombreUsuarioBaja.setText(usuario.getNombreUsuario());
                        tvNombreyApellidoBaja.setText(usuario.getNombre() + " " + usuario.getApellido());
                        tvCorreoElectronicoBaja.setText(usuario.getEmail());
                        tvProvinciaLocalidadBaja.setText(usuario.getProvincia() + " - " + usuario.getLocalidad());
                        pb.setVisibility(View.INVISIBLE);
                        this.btnAccion.setEnabled(true);
                    }
                }
            }else if(result instanceof List<?>){
                if (operacion.equals("LISTADO")){
                    Log.d("UsuarioDao","Entro en onPostExecute, LISTADO");
                    usuarioAdapter = new UsuarioAdapter(context, R.layout.item_usuario, (List<Usuario>) result);
                    listview.setAdapter(usuarioAdapter);
                }else if(operacion.equals("LISTADO_CHATS")){
                    Log.d("UsuarioDao","Entro en onPostExecute, LISTADO_CHATS");
                    usuarioAdapter = new UsuarioAdapter(context, R.layout.item_usuario, (List<Usuario>) result);
                    listview.setAdapter(usuarioAdapter);
                }
            }
        }else if(operacion.equals("OBTENER")){
            if(pb != null){
                pb.setVisibility(View.INVISIBLE);
            }
            new UsuarioDao(context,this.tvIDBaja, this.tvNombreUsuarioBaja, this.tvNombreyApellidoBaja, this.tvCorreoElectronicoBaja, this.tvProvinciaLocalidadBaja).execute("OBTENER_PORNOMBRE",this.nombreUsr);
            }else{
            if(pb != null){
                pb.setVisibility(View.INVISIBLE);
            }
            mostrarMensaje("Ha ocurrido un error. El usuario que se busca no existe o ha sido baneado.");
        }
        Log.d("UsuarioDao","Entro en onPostExecute, NULL");
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show();
        }
    }


