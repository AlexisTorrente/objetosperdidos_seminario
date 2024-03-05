package com.example.tp_grupo6_seminario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.GeneroDao;
import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoUsuario;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class CrearUsuario extends AppCompatActivity {

    private Spinner spProvincias,spGeneros,spLocalidades;
    private EditText etNombre,etApellido,etDNI,etFechaNacimiento,etCorreo,etUsuario,etContrasenia,etRepetircontrasenia;
    private Button btnRegistrarse;
    private Usuario user = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        spProvincias = (Spinner) findViewById(R.id.spinnerProvincia);
        spGeneros = (Spinner) findViewById(R.id.spinnerGenero);
        spLocalidades = (Spinner) findViewById(R.id.spinnerLocalidad);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etCorreo = findViewById(R.id.etCorreoElectronico);
        etUsuario = findViewById(R.id.etUsuarioRegistro);
        etContrasenia = findViewById(R.id.etContraseniaRegistro);
        etRepetircontrasenia = findViewById(R.id.etRepetirContrasenia);


        // Esto es para que cada 2 caracteres se agregue un "/" en el edittext de fecha nacimiento, para más
        // comodidad
        etFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();

                if (text.length() == 2 && before == 0) {
                    text += "/"; // se agrega "/" después de los dos primeros caracteres
                } else if (text.length() == 5 && before == 0) {
                    text += "/"; // se agrega "/" después de los siguientes dos caracteres
                }

                if (!text.equals(s.toString())) {
                    etFechaNacimiento.setText(text);
                    etFechaNacimiento.setSelection(text.length());
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

       GeneroDao gdao = new GeneroDao(getApplicationContext(),spGeneros);
       gdao.execute("LISTADO");

        ProvinciaDao pdao = new ProvinciaDao(getApplicationContext(),spProvincias, spLocalidades);
        pdao.execute("LISTADO");


        spProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spLocalidades.setAdapter(null);
                LocalidadDao ldao = new LocalidadDao(getApplicationContext(), spLocalidades);

                Provincia provinciaSeleccionada = (Provincia) spProvincias.getSelectedItem();
                int idProv = provinciaSeleccionada.getId();
                String nombreProv = provinciaSeleccionada.getNombre();

                Provincia prov = new Provincia();
                prov.setId(idProv);
                prov.setNombre(nombreProv);

                ldao.execute("LISTADO",prov);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    public void redireccionarIniciarSesion(View view){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void registrarUsuario(View view) {
        if(llamarValidaciones()) {
            UsuarioDao udao = new UsuarioDao(getApplicationContext());

            Provincia provinciaSeleccionada = (Provincia) spProvincias.getSelectedItem();
            int idProv = provinciaSeleccionada.getId();
            String nombreProv = provinciaSeleccionada.getNombre();

            Provincia prov = new Provincia();
            prov.setId(idProv);
            prov.setNombre(nombreProv);

            Localidad localidadSeleccionada = (Localidad) spLocalidades.getSelectedItem();
            int idLoc = localidadSeleccionada.getId();
            String nombreLoc = localidadSeleccionada.getNombre();

            Localidad loc = new Localidad();
            loc.setId(idLoc);
            loc.setNombre(nombreLoc);
            loc.setProvincia(prov);

            Genero generoSeleccionado = (Genero) spGeneros.getSelectedItem();
            int idGen = generoSeleccionado.getId();
            String descripcionGen = generoSeleccionado.getDescripcion();

            Genero gen = new Genero();
            gen.setId(idGen);
            gen.setDescripcion(descripcionGen);

            TipoUsuario tuser = new TipoUsuario();
            tuser.setId(1);
            tuser.setDescripcion("Usuario");

            user.setNombre(etNombre.getText().toString());
            user.setApellido(etApellido.getText().toString());
            user.setDni(etDNI.getText().toString());
            user.setFechaNac(etFechaNacimiento.getText().toString());
            user.setEmail(etCorreo.getText().toString());
            user.setGenero(gen);
            user.setProvincia(prov);
            user.setLocalidad(loc);
            user.setNombreUsuario(etUsuario.getText().toString());
            user.setTipoUsuario(tuser);
            user.setContrasena(etContrasenia.getText().toString());
            user.setEstado(true);

            udao.execute("INSERTAR", user);
            limpiarCampos();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }

    public void limpiarCampos(){
        etNombre.setText("");
        etApellido.setText("");
        etContrasenia.setText("");
        etRepetircontrasenia.setText("");
        etCorreo.setText("");
        etFechaNacimiento.setText("");
        etDNI.setText("");
        etUsuario.setText("");

    }

    // validaciones

    private boolean llamarValidaciones() {
        return validarNombre(etNombre.getText().toString()) &&
                validarApellido(etApellido.getText().toString()) &&
                validarDNI() &&
                validarFechaFormato(etFechaNacimiento.getText().toString()) &&
                validarFechaFuturaoActual(etFechaNacimiento.getText().toString()) &&
                validarEmail(etCorreo.getText().toString()) &&
                validarUsuario(etUsuario.getText().toString()) &&
                validarContrasenia(etContrasenia.getText().toString(), etRepetircontrasenia.getText().toString());
    }



    private boolean validarNombre(String nombre){
        if (nombre.isEmpty()){
            mostrarMensaje("Debe ingresar su nombre.");
            return false;
        }

        if (!nombre.matches("[a-zA-Z]+")){
            mostrarMensaje("El nombre de una persona solo puede tener letras");
            return false;
        }
        return true;
    }

    private boolean validarApellido(String apellido){
        if (apellido.isEmpty()){
            mostrarMensaje("Debe ingresar su apellido.");
            return false;
        }

        if (!apellido.matches("[a-zA-Z]+")){
            mostrarMensaje("El apellido de una persona solo puede tener letras");
            return false;
        }
        return true;
    }

    private boolean validarDNI(){
        if (etDNI.getText().toString().isEmpty()){
            mostrarMensaje("Debe ingresar su DNI.");
            return false;
        }

        if(Integer.parseInt(etDNI.getText().toString()) <= 10000000){
            mostrarMensaje("Debe ingresar un DNI válido.");
            return false;
        }
        return true;
    }

    private boolean validarFechaFormato(String fechaIngresada) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        if(fechaIngresada.isEmpty()){
            mostrarMensaje("Debe ingresar su fecha de nacimiento.");
            return false;
        }
        return true;
    }

    private boolean validarFechaFuturaoActual(String fechaIngresada) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        Date fechaActual = new Date(); // Fecha actual
        try {
            Date fechaNac = dateFormat.parse(fechaIngresada);
            Date fechaMin = dateFormat.parse("01/01/1924");

            if(fechaNac.after(fechaActual) || fechaNac.before(fechaMin)){
                mostrarMensaje("La fecha de nacimiento ingresada es inválida.");
                return false;
            }
            return true;
        } catch (ParseException e) {
            mostrarMensaje("La fecha de nacimiento ingresada es inválida.");
            return false;
        }
    }

    private boolean validarEmail(String email) {
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)+";
        if(email.isEmpty()){
            mostrarMensaje("debe ingresar un correo electronico.");
            return false;
        }
        if(Pattern.matches(emailPattern, email)){
            return true;
        }
        mostrarMensaje("debe ingresar un correo electronico válido.");
        return false;
    }

    private boolean validarUsuario(String usuario){
        if (usuario.isEmpty()){
            mostrarMensaje("debe ingresar el nombre de usuario.");
            return false;
        }

        return true;
    }

    private boolean validarContrasenia(String pass1, String pass2){
        if (pass1.isEmpty() || pass2.isEmpty()){
            mostrarMensaje("debe ingresar y repetir la contraseña.");
            return false;
        }

        if (!pass1.equals(pass2)){
            mostrarMensaje("Las contraseñas ingresadas deben coincidir.");
            return false;
        }

        if (pass1.length() < 8 || pass2.length() < 8) {
            mostrarMensaje("La contraseña debe tener al menos 8 caracteres.");
            return false;
        }

        boolean contieneLetras = false;
        boolean contieneNumeros = false;

        for (char c : pass1.toCharArray()) {
            if (Character.isLetter(c)) {
                contieneLetras = true;
            } else if (Character.isDigit(c)) {
                contieneNumeros = true;
            }

            if (contieneLetras && contieneNumeros) {
                return true; // La contraseña cumple con los requisitos
            }
        }

        mostrarMensaje("La contraseña debe contener letras y números.");
        return false;
    }




    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

}