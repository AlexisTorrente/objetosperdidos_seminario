package com.example.tp_grupo6_seminario;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.GeneroDao;
import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarUsuario extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etNombre, etApellido, etCorreo, etFechaNacimiento, etUsuario;
    Spinner spGeneros,spProvincias,spLocalidades;
    Button btnActualizarUsuario;
    private boolean primeraSeleccion = true;
    private datosUsuarioViewModel viewModel;
    public EditarUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarUsuario newInstance(String param1, String param2) {
        EditarUsuario fragment = new EditarUsuario();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // PARA QUE FUNCIONE SE DEBE QUITAR EL getArguments()
        viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_usuario, container, false);

        etNombre = (EditText) view.findViewById(R.id.etNombreEditarUsr);
        etApellido = (EditText) view.findViewById(R.id.etApellidoEditarUsr);
        etCorreo = (EditText) view.findViewById(R.id.etCorreoEditarUsr);
        etFechaNacimiento = (EditText) view.findViewById(R.id.etFechaNacUsr);
        etUsuario = (EditText) view.findViewById(R.id.etUsuarioEditarUsr);
        spGeneros = (Spinner) view.findViewById(R.id.spGeneroEditarUsr);
        spProvincias = (Spinner) view.findViewById(R.id.spProvinciaEditarUsr);
        spLocalidades = (Spinner) view.findViewById(R.id.spLocalidadEditarUsr);
        btnActualizarUsuario = view.findViewById(R.id.btnActualizarEditarUsr);
        spProvincias.setEnabled(false);
        spLocalidades.setEnabled(false);

        Usuario usuarioLogeado = viewModel.getUsuario().getValue();

        spProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (primeraSeleccion) {
                    primeraSeleccion = false;
                }else{
                    spLocalidades.setAdapter(null);
                    LocalidadDao ldao = new LocalidadDao(requireContext(), spLocalidades);

                    Provincia provinciaSeleccionada = (Provincia) spProvincias.getSelectedItem();
                    int idProv = provinciaSeleccionada.getId();
                    String nombreProv = provinciaSeleccionada.getNombre();

                    Provincia prov = new Provincia();
                    prov.setId(idProv);
                    prov.setNombre(nombreProv);

                    ldao.execute("LISTADO", prov);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        if (usuarioLogeado != null) {
                // Obtiene el nombre de usuario y lo muestra en el EditText
                etUsuario.setText(usuarioLogeado.getNombreUsuario());
                etNombre.setText(usuarioLogeado.getNombre());
                etApellido.setText(usuarioLogeado.getApellido());
                etCorreo.setText(usuarioLogeado.getEmail());
                etFechaNacimiento.setText(usuarioLogeado.getFechaNac());
        }

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

        btnActualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llamarValidaciones()) {
                    if (usuarioLogeado != null) {
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
                        // Obtiene el nombre de usuario y lo muestra en el EditText
                        usuarioLogeado.setNombreUsuario(etUsuario.getText().toString());
                        usuarioLogeado.setGenero(gen);
                        usuarioLogeado.setProvincia(prov);
                        usuarioLogeado.setLocalidad(loc);
                        usuarioLogeado.setNombre(etNombre.getText().toString());
                        usuarioLogeado.setApellido(etApellido.getText().toString());
                        usuarioLogeado.setEmail(etCorreo.getText().toString());
                        usuarioLogeado.setFechaNac(etFechaNacimiento.getText().toString());

                        UsuarioDao udao = new UsuarioDao(requireContext(),usuarioLogeado);
                        udao.execute("ACTUALIZAR",usuarioLogeado);

                        NavController navController = Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                        navController.navigate(R.id.perfilUsuarioFragment);
                    }


                }
            }
        });

        GeneroDao gdao = new GeneroDao(requireContext(),spGeneros, usuarioLogeado);
        gdao.execute("LISTADO");

        ProvinciaDao pdao = new ProvinciaDao(getContext(),spProvincias, spLocalidades,usuarioLogeado);
        pdao.execute("LISTADO");


        return view;
    }

    private boolean llamarValidaciones() {
        return validarNombre(etNombre.getText().toString()) &&
                validarApellido(etApellido.getText().toString()) &&
                validarFechaFormato(etFechaNacimiento.getText().toString()) &&
                validarFechaFuturaoActual(etFechaNacimiento.getText().toString()) &&
                validarEmail(etCorreo.getText().toString()) &&
                validarUsuario(etUsuario.getText().toString());
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
            Date fechaMin = dateFormat.parse("01/01/1900");

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

    private void mostrarMensaje(String mensaje){
        Toast.makeText(requireContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
}