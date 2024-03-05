package com.example.tp_grupo6_seminario;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.NotificacionesDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.TipoDeObjetoDao;
import com.example.tp_grupo6_seminario.Dao.TipoPublicacionDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Notificacion;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;
import com.example.tp_grupo6_seminario.entidades.TipoPublicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link altaObjetoEncontradoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class altaObjetoEncontradoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etTitulo, etFecha, etRangoHorario, etPosUbicaciones, etDescripcion;
    private Spinner spTipoObjeto, spProvincia, spLocalidad;
    private ImageView imagen;
    private Button btnSelecImagen, btnRegistrarOE;
    private Publicacion publicacion = new Publicacion();
    private java.sql.Date fecha;
    private Usuario usuarioLogueado;
    private datosUsuarioViewModel viewModel;
    private NotificacionesDao notificacionesDao;
    private Notificacion notificacion;
    private TipoPublicacion tipoPublicacion = new TipoPublicacion(2,"Objeto Encontrado");

    public altaObjetoEncontradoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment altaObjetoEncontradoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static altaObjetoEncontradoFragment newInstance(String param1, String param2) {
        altaObjetoEncontradoFragment fragment = new altaObjetoEncontradoFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alta_objeto_encontrado, container, false);

        etTitulo = (EditText) view.findViewById(R.id.etTitulo);
        etDescripcion = (EditText) view.findViewById(R.id.etDescripcion);
        etFecha = (EditText) view.findViewById(R.id.etFecha);
        etRangoHorario = (EditText) view.findViewById(R.id.etRangoHorario);
        etPosUbicaciones = (EditText) view.findViewById(R.id.etUbicacion);
        spProvincia = (Spinner) view.findViewById(R.id.spProvincia);
        spLocalidad = (Spinner) view.findViewById(R.id.spLocalidad);
        spTipoObjeto = (Spinner) view.findViewById(R.id.spTipoObjeto);
        imagen = (ImageView) view.findViewById(R.id.imagen);
        usuarioLogueado = viewModel.getUsuario().getValue();

        btnRegistrarOE = (Button) view.findViewById(R.id.btnRegistrarOE);
        btnRegistrarOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCamposCompletos()) {
                    if (validarYformatearFecha(etFecha.getText().toString()) && validarFechaFutura()) {
                        try {
                            PublicacionDao _PublicacionDao = new PublicacionDao(requireContext());

                            publicacion.setTipoPublicacion(tipoPublicacion);
                            publicacion.setTipoObjeto((TipoObjeto) spTipoObjeto.getSelectedItem());
                            publicacion.setUsuario(usuarioLogueado);
                            publicacion.setProvincia((Provincia) spProvincia.getSelectedItem());
                            publicacion.setLocalidad((Localidad) spLocalidad.getSelectedItem());
                            publicacion.setTitulo(etTitulo.getText().toString());
                            publicacion.setUbicacion(etPosUbicaciones.getText().toString());
                            publicacion.setImagen("prueba");
                            publicacion.setFechaPublicacion(fecha);
                            publicacion.setHoraPublicacion(etRangoHorario.getText().toString());
                            publicacion.setDescripcion(etDescripcion.getText().toString());
                            publicacion.setRecuperado(false);
                            publicacion.setEstado(true);

                            _PublicacionDao.execute("INSERTAR", publicacion);


                            limpiarCampos();
                            mostrarMensaje("Publicacion cargada");

                            //Notificamos a todos los usuarios
                            //Notificaciones notificaciones = new Notificaciones("","");

                            NavController navController = Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                            navController.navigate(R.id.inicioFragment);

                            //mostrarMensaje(tipoPublicacion.getId() + " - " + tipoPublicacion.getDescripcion());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        mostrarMensaje("Fecha invalida");
                    }
                }
                else{
                    mostrarMensaje("Los campos obligatorios deben estar completos");
                }

            }
        });

        btnSelecImagen = (Button) view.findViewById(R.id.btnSelecImagen);
        btnSelecImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spLocalidad.setAdapter(null);
                LocalidadDao localidadDao = new LocalidadDao(requireContext(), spLocalidad);
                Provincia provinciaSelecionada = (Provincia) spProvincia.getSelectedItem();
                localidadDao.execute("LISTADO", provinciaSelecionada);

                /*int idProv = provinciaSelecionada.getId();
                String nombreProv = provinciaSelecionada.getNombre();
                mostrarMensaje(idProv + " - " + nombreProv);*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            TipoPublicacionDao tipoPublicacionDao = new TipoPublicacionDao(requireContext(), tipoPublicacion);
            tipoPublicacionDao.execute("OBTENER_TIPO_PUBLICACION", 2);

            TipoDeObjetoDao tipoDeObjetoDao = new TipoDeObjetoDao(requireContext(), spTipoObjeto);
            tipoDeObjetoDao.execute("LISTADO");

            ProvinciaDao provinciaDao = new ProvinciaDao(requireContext(), spProvincia);
            provinciaDao.execute("LISTADO");

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private boolean validarCamposCompletos(){
        if(!etTitulo.getText().toString().trim().isEmpty() && !etFecha.getText().toString().trim().isEmpty() && !etPosUbicaciones.getText().toString().trim().isEmpty() && !etDescripcion.getText().toString().trim().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validarYformatearFecha (String inputDate){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDateFormat.setLenient(false);
        outputDateFormat.setLenient(false);

        try {
            java.util.Date utilDate = inputDateFormat.parse(inputDate);
            String outputDate = outputDateFormat.format(utilDate);
            java.util.Date utilDateOutput = outputDateFormat.parse(outputDate);
            fecha = new java.sql.Date(utilDateOutput.getTime());
            //fecha = outputDateFormat.parse(outputDate);


            //mostrarMensaje("Fecha ingresada: " + utilDate.toString());
            //mostrarMensaje("Fecha formateada: " + fecha.toString());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validarFechaFutura() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        java.util.Date fechaActual = new java.util.Date(); // Fecha actual

        try {
            return !fecha.after(fechaActual);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void limpiarCampos(){
        etTitulo.setText("");
        etDescripcion.setText("");
        etFecha.setText("");
        etRangoHorario.setText("");
        etPosUbicaciones.setText("");
        // spProvincia
        // spLocalidad
        // spTipoObjeto
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    private void cargarImagen(){
        Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        iGallery.setType("image/");
        startActivityForResult(iGallery.createChooser(iGallery, "Seleciona una aplicacion"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);

        }
    }
}