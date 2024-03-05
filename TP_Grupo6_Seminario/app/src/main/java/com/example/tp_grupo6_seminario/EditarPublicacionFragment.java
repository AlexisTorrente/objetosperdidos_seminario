package com.example.tp_grupo6_seminario;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
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
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.TipoDeObjetoDao;
import com.example.tp_grupo6_seminario.Dao.TipoPublicacionDao;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;
import com.example.tp_grupo6_seminario.entidades.TipoPublicacion;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPublicacionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Publicacion publicacion;
    private EditText etTitulo, etFecha, etRangoHorario, etPosUbicaciones, etDescripcion;
    private Spinner spTipoPublicacion, spTipoObjeto, spProvincia, spLocalidad;
    private ImageView imagen;
    private Button btnSelecImagen, btnAplicarCambios;
    private java.sql.Date fecha;
    private int idImagen;
    private String RutaImagen;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    public EditarPublicacionFragment() {
        // Required empty public constructor
    }

    public static EditarPublicacionFragment newInstance(String param1, String param2) {
        EditarPublicacionFragment fragment = new EditarPublicacionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_publicacion, container, false);

        try{
            etTitulo = (EditText) view.findViewById(R.id.etTitulo);
            etDescripcion = (EditText) view.findViewById(R.id.etDescripcion);
            etFecha = (EditText) view.findViewById(R.id.etFecha);
            etRangoHorario = (EditText) view.findViewById(R.id.etRangoHorario);
            etPosUbicaciones = (EditText) view.findViewById(R.id.etPosUbicaciones);
            spTipoPublicacion = (Spinner) view.findViewById(R.id.spTipoPublicacion);
            spProvincia = (Spinner) view.findViewById(R.id.spProvincia);
            spLocalidad = (Spinner) view.findViewById(R.id.spLocalidad);
            spTipoObjeto = (Spinner) view.findViewById(R.id.spTipoObjeto);
            imagen = (ImageView) view.findViewById(R.id.imagen);
            btnAplicarCambios = (Button) view.findViewById(R.id.btnAplicarCambiosEdit);
            btnSelecImagen = (Button) view.findViewById(R.id.btnSelecImagen);

            Bundle bundle = getArguments();
            if (bundle != null) {
                publicacion = (Publicacion) bundle.getSerializable("Publicacion");
                if (publicacion != null) {
                    etTitulo.setText(publicacion.getTitulo());
                    etDescripcion.setText(publicacion.getDescripcion());
                    etFecha.setText(formatearFecha(publicacion.getFechaPublicacion()));
                    etRangoHorario.setText(publicacion.getHoraPublicacion());
                    etPosUbicaciones.setText(publicacion.getUbicacion());
                    //spTipoPublicacion;
                    //spLocalidad;
                    //spTipoObjeto;

                    PublicacionDao PDAOImagen = new PublicacionDao(getContext(), imagen);
                    PDAOImagen.execute("CargarImagen", publicacion.getId(), publicacion.getImagen().toString());

//                    idImagen = getContext().getResources().getIdentifier(publicacion.getImagen(), "drawable", getContext().getPackageName());
//                    imagen.setImageResource(idImagen);
                }
            }

            btnAplicarCambios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validarCamposCompletos()) {
                        if (validarYformatearFecha(etFecha.getText().toString()) && validarFechaFutura()) {
                            try {
                                PublicacionDao _PublicacionDao = new PublicacionDao(requireContext());

                                publicacion.setTipoPublicacion((TipoPublicacion) spTipoPublicacion.getSelectedItem());
                                publicacion.setTipoObjeto((TipoObjeto) spTipoObjeto.getSelectedItem());
                                //publicacion.setUsuario(usuarioLogueado);
                                publicacion.setProvincia((Provincia) spProvincia.getSelectedItem());
                                publicacion.setLocalidad((Localidad) spLocalidad.getSelectedItem());
                                publicacion.setTitulo(etTitulo.getText().toString().trim());
                                publicacion.setUbicacion(etPosUbicaciones.getText().toString().trim());

                                if (RutaImagen != null && !RutaImagen.isEmpty()) {
                                    publicacion.setImagen(RutaImagen);
                                }else{
                                    publicacion.setImagen("prueba");
                                }
                                publicacion.setFechaPublicacion(fecha);
                                publicacion.setHoraPublicacion(etRangoHorario.getText().toString().trim());
                                publicacion.setDescripcion(etDescripcion.getText().toString().trim());
                                //publicacion.setRecuperado(false);
                                //publicacion.setEstado(true);

                                _PublicacionDao.execute("ACTUALIZAR", publicacion);
                                limpiarCampos();
                                mostrarMensaje("Publicacion actualizada");

                                NavController navController = Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                                navController.navigate(R.id.inicioFragment);

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

            btnSelecImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Si no tienes el permiso, solicítalo al usuario
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        Log.i("INFO_MISPUBLI"," No tenía permiso");
                    }else{
                        cargarImagen();
                        Log.i("INFO_MISPUBLI"," Ya tengo permiso");

                    }
                }
            });

            spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spLocalidad.setAdapter(null);
                    Provincia provinciaSelecionada = (Provincia) spProvincia.getSelectedItem();

                    if(provinciaSelecionada.getId() == publicacion.getProvincia().getId()){
                        LocalidadDao localidadDao = new LocalidadDao(requireContext(), spLocalidad, publicacion.getLocalidad());
                        localidadDao.execute("LISTADO", provinciaSelecionada);
                    }
                    else {
                        LocalidadDao localidadDao = new LocalidadDao(requireContext(), spLocalidad);
                        localidadDao.execute("LISTADO", provinciaSelecionada);
                    }

                /*int idProv = provinciaSelecionada.getId();
                String nombreProv = provinciaSelecionada.getNombre();
                mostrarMensaje(idProv + " - " + nombreProv);*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            try {
                TipoPublicacionDao tipoPublicacionDao = new TipoPublicacionDao(requireContext(), spTipoPublicacion, publicacion.getTipoPublicacion());
                tipoPublicacionDao.execute("LISTADO");

                TipoDeObjetoDao tipoDeObjetoDao = new TipoDeObjetoDao(requireContext(), spTipoObjeto, publicacion.getTipoObjeto());
                tipoDeObjetoDao.execute("LISTADO");

                ProvinciaDao provinciaDao = new ProvinciaDao(requireContext(), spProvincia, publicacion.getProvincia());
                provinciaDao.execute("LISTADO");

            }
            catch (Exception e){
                Log.i("ERROR_MISPUBLI"," Excepcion: "+e.getMessage());
                e.printStackTrace();
            }

        }catch (Exception ex){
            Log.i("ERROR_MISPUBLI"," Excepcion: "+ex.getMessage());
        }

        return view;
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

    public String formatearFecha(Date fecha){
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDateFormat.setLenient(false);
        outputDateFormat.setLenient(false);

        try {
            String outputDate = outputDateFormat.format(fecha);

            return outputDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean validarCamposCompletos(){
        if(!etTitulo.getText().toString().trim().isEmpty() && !etFecha.getText().toString().trim().isEmpty() && !etPosUbicaciones.getText().toString().trim().isEmpty() && !etDescripcion.getText().toString().trim().isEmpty()){
            return true;
        }
        else{
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
      try{
          Log.i("INFO_MISPUBLI"," Llegue a cargarImagen()");

          Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          //iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          iGallery.setType("image/");
          startActivityForResult(iGallery.createChooser(iGallery, "Seleccione una aplicación"), 10);

      }catch (Exception ex){
          Log.i("ERROR_MISPUBLI"," Excepcion: "+ex.getMessage());
      }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);
            Log.i("INFO_MISPUBLI","Uri path: "+path);

            RutaImagen = obtenerRutaDeImagenDesdeURI(path);
            Log.i("INFO_MISPUBLI","RutaImagen: "+RutaImagen);

        }
    }

    private String obtenerRutaDeImagenDesdeURI(Uri uri) {
        String path = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }

}