package com.example.tp_grupo6_seminario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText nombreUsr, contraseniaUsr;
    ProgressBar pb;
    private Button btnIniciarSesion;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreUsr = (EditText) findViewById(R.id.etUsuario);
        contraseniaUsr = (EditText) findViewById(R.id.etContrasenia);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        pb = findViewById(R.id.barraEspera);
    }

    public void redireccionarRegistro(View view){
            Intent i = new Intent(getApplicationContext(),CrearUsuario.class);
            startActivity(i);
        }

/*
    public void iniciarSesion(View view) {
        if (validarCampos(nombreUsr.getText().toString(), contraseniaUsr.getText().toString())) {
            pb.setVisibility(View.VISIBLE);
            Usuario usr = new Usuario();
            UsuarioDao udao = new UsuarioDao(getApplicationContext(), usr, nombreUsr.getText().toString(), contraseniaUsr.getText().toString());
            udao.setProgressBar(pb);
            udao.execute("INICIAR SESION");
            if (usr != null && usr.getId() > 0) {
                    mostrarMensaje("ÉXITO");
            } else {
                    mostrarMensaje("ERROR AL INICIAR SESION");
            }

            //Intent i = new Intent(getApplicationContext(), activity_navigation_drawer.class);
            //startActivity(i);
        }
    }

 */

    public void iniciarSesion(View view) {
        if (validarCampos(nombreUsr.getText().toString(), contraseniaUsr.getText().toString())) {
            btnIniciarSesion.setEnabled(false);
            pb.setVisibility(View.VISIBLE); // Aca se pone visible el estado de carga, que aparece a la izquierda del boton iniciar sesion, luego en el onPostExecute se pone invisible.
            Usuario usr = new Usuario();
            UsuarioDao udao = new UsuarioDao(this, usr, nombreUsr.getText().toString(), contraseniaUsr.getText().toString());
            udao.setPb(pb);
            udao.setBtn(btnIniciarSesion);
            udao.execute("INICIAR SESION");
        }
    }


    private boolean validarCampos(String usuario, String contrasenia){
        if (usuario.isEmpty()){
            mostrarMensaje("debe ingresar el nombre de usuario.");
            return false;
        }

        if(contrasenia.isEmpty()){
            mostrarMensaje("debe ingresar la contraseña.");
            return false;
        }

        return true;
    }
    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

}