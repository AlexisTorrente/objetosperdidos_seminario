package com.example.tp_grupo6_seminario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.TipoDeObjetoDao;
import com.example.tp_grupo6_seminario.Data.ViewModelDatos;
import com.example.tp_grupo6_seminario.adapter.ViewPagerAdapter;
import com.example.tp_grupo6_seminario.adapter.gridviewAdapter;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.TipoObjeto;

import java.text.SimpleDateFormat;
import java.util.List;

public class objetosPerdidosFragment extends Fragment{

    private GridView gridView;
    private boolean dataLoaded = false;
    private ViewModelDatos viewModel;
    ProgressBar pb;
    private LinearLayout llyProvincia, llyTipoObjeto, llyBusqueda, llyLocalidad, llyFechaInicio, llyFechaFin;
    private TextView tvProvincia, tvTipoObjeto, tvBusqueda, tvLocalidad, tvFechaInicio, tvFechaFin;
    private Button btnFiltroProvincia, btnFiltroTipoObjeto, btnFiltroTexto, btnFiltroLocalidad, btnFiltroFechas, btnLimpiarFiltros, btnActualizarGV;
    private String filtro = "";
    private Provincia provinciaFiltro = null;
    private TipoObjeto tipoObjetoFiltro = null;
    private String textoFiltro = "";
    private Localidad localidadFiltro = null;
    private String fechaInicioFiltro = "", fechaFinFiltro = "";

/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objetos_perdidos, container, false);

        gridView = (GridView) view.findViewById(R.id.gvObjetosPerdidos);

        PublicacionDao publicacionDao = new PublicacionDao(requireContext(), gridView);
        publicacionDao.execute("LISTADO");


        return view;
    }

 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objetos_perdidos, container, false);


        gridView = view.findViewById(R.id.gvObjetosPerdidos);
        pb = view.findViewById(R.id.progressBarFragmentoPerdido);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelDatos.class);
        btnFiltroProvincia = (Button) view.findViewById(R.id.btnFiltroProvincia);
        btnFiltroTipoObjeto = (Button) view.findViewById(R.id.btnFiltroTipoObjeto);
        btnFiltroTexto = (Button) view.findViewById(R.id.btnFiltroTexto);
        btnFiltroLocalidad = (Button) view.findViewById(R.id.btnFiltroLocalidad);
        btnFiltroFechas = (Button) view.findViewById(R.id.btnFiltroFechas);
        llyProvincia = (LinearLayout) view.findViewById(R.id.llyProvincia);
        llyTipoObjeto = (LinearLayout) view.findViewById(R.id.llyTipoObjeto);
        llyBusqueda = (LinearLayout) view.findViewById(R.id.llyBusqueda);
        llyLocalidad = (LinearLayout) view.findViewById(R.id.llyLocalidad);
        llyFechaInicio = (LinearLayout) view.findViewById(R.id.llyFechaInicio);
        llyFechaFin = (LinearLayout) view.findViewById(R.id.llyFechaFin);
        tvProvincia = (TextView) view.findViewById(R.id.tvProvincia);
        tvTipoObjeto = (TextView) view.findViewById(R.id.tvTipoObjeto);
        tvBusqueda = (TextView) view.findViewById(R.id.tvBusqueda);
        tvLocalidad = (TextView) view.findViewById(R.id.tvLocalidad);
        tvFechaInicio = (TextView) view.findViewById(R.id.tvFechaInicio);
        tvFechaFin = (TextView) view.findViewById(R.id.tvFechaFin);
        btnLimpiarFiltros = (Button) view.findViewById(R.id.btnLimpiarFiltros);
        btnActualizarGV = (Button) view.findViewById(R.id.btnActualizarGVOP);

        mostrarFiltros();

        // Verificar si los datos ya se cargaron previamente en el ViewModel
        List<Publicacion> publicaciones = viewModel.getPublicacionesObjPerdido();
        if (publicaciones != null) {
            gridView.setAdapter(null);
            gridviewAdapter adapter = new gridviewAdapter(requireContext(), publicaciones);
            gridView.setAdapter(adapter);
            pb.setVisibility(View.INVISIBLE);
        } else {
            // Si los datos aún no se han cargado, carga los datos y guárdalos en el ViewModel
            cargarGridView();
        }

        btnFiltroProvincia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPopUpFiltroProvincia();
            }
        });

        btnFiltroTipoObjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroTipoObjeto();
            }
        });

        btnFiltroTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroTexto();
            }
        });

        btnFiltroLocalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroLocalidad();
            }
        });

        btnFiltroFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroFechas();
            }
        });

        btnLimpiarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarFiltros();
                mostrarFiltros();
                cargarGridView();
            }
        });

        btnActualizarGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarGridView();
            }
        });

        return view;
    }

    public void mostrarPopUpFiltroProvincia(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_spinner_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione una provincia");
        final Spinner spPopUpProvincia = popupView.findViewById(R.id.spPopUp);
        if(provinciaFiltro != null){
            ProvinciaDao provinciaDao = new ProvinciaDao(requireContext(), spPopUpProvincia, provinciaFiltro);
            provinciaDao.execute("LISTADO", true);
        }
        else{
            ProvinciaDao provinciaDao = new ProvinciaDao(requireContext(), spPopUpProvincia);
            provinciaDao.execute("LISTADO", true);
        }
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinciaFiltro = (Provincia) spPopUpProvincia.getSelectedItem();
                if(provinciaFiltro.getId() == 0){
                    provinciaFiltro = null;
                }
                if(provinciaFiltro != null){
                    localidadFiltro = null;
                }
                mostrarFiltros();
                cargarGridView();
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        btnpopupCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el popup sin realizar cambios
                dialog.dismiss();
            }
        });
    }

    public void mostrarPopUpFiltroTipoObjeto(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_spinner_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione un tipo de objeto");
        final Spinner spPopUpTipoObjeto = popupView.findViewById(R.id.spPopUp);
        if(tipoObjetoFiltro != null){
            TipoDeObjetoDao TipoObjetoDao = new TipoDeObjetoDao(requireContext(), spPopUpTipoObjeto, tipoObjetoFiltro);
            TipoObjetoDao.execute("LISTADO", true);
        }
        else{
            TipoDeObjetoDao TipoObjetoDao = new TipoDeObjetoDao(requireContext(), spPopUpTipoObjeto);
            TipoObjetoDao.execute("LISTADO", true);
        }
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipoObjetoFiltro = (TipoObjeto) spPopUpTipoObjeto.getSelectedItem();
                if(tipoObjetoFiltro.getId() == 0){
                    tipoObjetoFiltro = null;
                }
                mostrarFiltros();
                cargarGridView();
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        btnpopupCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el popup sin realizar cambios
                dialog.dismiss();
            }
        });
    }

    public void mostrarPopUpFiltroLocalidad(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_spinner_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione una Localidad");
        final Spinner spPopUpLocalidad = popupView.findViewById(R.id.spPopUp);
        if(localidadFiltro != null){
            LocalidadDao localidadDao = new LocalidadDao(requireContext(), spPopUpLocalidad, localidadFiltro);
            if(provinciaFiltro != null) {
                localidadDao.execute("LISTADO", provinciaFiltro, true);
            }
            else{
                localidadDao.execute("LISTADO", null, true);
            }
        }
        else{
            LocalidadDao localidadDao = new LocalidadDao(requireContext(), spPopUpLocalidad);
            if(provinciaFiltro != null) {
                localidadDao.execute("LISTADO", provinciaFiltro, true);
            }
            else{
                localidadDao.execute("LISTADO", null, true);
            }
        }

        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localidadFiltro = (Localidad) spPopUpLocalidad.getSelectedItem();
                if(localidadFiltro.getId() == 0){
                    localidadFiltro = null;
                }
                mostrarFiltros();
                cargarGridView();
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        btnpopupCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el popup sin realizar cambios
                dialog.dismiss();
            }
        });
    }

    public void mostrarPopUpFiltroTexto(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_edittext_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Busqueda");
        final EditText etPopUpTexto = popupView.findViewById(R.id.etPopUp);
        etPopUpTexto.setHint("Buscar");
        if(!textoFiltro.isEmpty()) {
            etPopUpTexto.setText(textoFiltro);
        }
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textoFiltro = etPopUpTexto.getText().toString().trim();

                mostrarFiltros();
                cargarGridView();
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        btnpopupCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el popup sin realizar cambios
                dialog.dismiss();
            }
        });
    }

    public void mostrarPopUpFiltroFechas(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_2_fecha_edittext_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione las fechas");
        final EditText etPopUpFechaInicio = popupView.findViewById(R.id.etFechaInicioPopUp);
        final EditText etPopUpFechaFin = popupView.findViewById(R.id.etFechaFinPopUp);
        if(!fechaInicioFiltro.isEmpty()) {
            etPopUpFechaInicio.setText(formatearFecha(fechaInicioFiltro));
        }
        if(!fechaFinFiltro.isEmpty()) {
            etPopUpFechaFin.setText(formatearFecha(fechaFinFiltro));
        }
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaInicioFiltro = etPopUpFechaInicio.getText().toString().trim();
                fechaFinFiltro = etPopUpFechaFin.getText().toString().trim();

                if(!fechaInicioFiltro.isEmpty()){
                    fechaInicioFiltro = validarYformatearFecha(fechaInicioFiltro);
                    if(fechaInicioFiltro.isEmpty()){
                        mostrarMensaje("Fecha invalida");
                        dialog.dismiss();
                    }
                }
                if(!fechaFinFiltro.isEmpty()){
                    fechaFinFiltro = validarYformatearFecha(fechaFinFiltro);
                    if(fechaFinFiltro.isEmpty()){
                        mostrarMensaje("Fecha invalida");
                        dialog.dismiss();
                    }
                }

                mostrarFiltros();
                cargarGridView();
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        btnpopupCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el popup sin realizar cambios
                dialog.dismiss();
            }
        });
    }

    public void cargarGridView(){

        filtro = " Estado = 1 AND Recuperado = 0 AND ID_TipoPublicacion = 1";
        if (pb.getVisibility() == View.INVISIBLE) {
            pb.setVisibility(View.VISIBLE);
        }
        gridView.setAdapter(null);
        PublicacionDao publicacionDao = new PublicacionDao(requireContext(), gridView,1);
        publicacionDao.setPb(pb);

        if(provinciaFiltro != null){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " ID_Provincia = " + provinciaFiltro.getId();
        }
        if(tipoObjetoFiltro != null){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " ID_TipoObjeto = " + tipoObjetoFiltro.getId();
        }
        if(!textoFiltro.isEmpty()){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " Titulo LIKE '%" + textoFiltro + "%'";
        }
        if(localidadFiltro != null){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " ID_Localidad = " + localidadFiltro.getId();
        }
        if(!fechaInicioFiltro.isEmpty()){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " FechaPublicacion >= '" + fechaInicioFiltro + "'";
        }
        if(!fechaFinFiltro.isEmpty()){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND";
            }
            filtro = filtro + " FechaPublicacion <= '" + fechaFinFiltro + "'";
        }

        publicacionDao.execute("LISTADO", filtro);
    }

    public void mostrarFiltros(){
        btnLimpiarFiltros.setVisibility(View.GONE);
        if(provinciaFiltro == null){
            llyProvincia.setVisibility(View.GONE);
        }
        else{
            llyProvincia.setVisibility(View.VISIBLE);
            tvProvincia.setText(provinciaFiltro.getNombre());
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }

        if(tipoObjetoFiltro == null){
            llyTipoObjeto.setVisibility(View.GONE);
        }
        else{
            llyTipoObjeto.setVisibility(View.VISIBLE);
            tvTipoObjeto.setText(tipoObjetoFiltro.getDescripcion());
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }

        if(textoFiltro.isEmpty()){
            llyBusqueda.setVisibility(View.GONE);
        }
        else{
            llyBusqueda.setVisibility(View.VISIBLE);
            tvBusqueda.setText(textoFiltro);
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }

        if(localidadFiltro == null){
            llyLocalidad.setVisibility(View.GONE);
        }
        else{
            llyLocalidad.setVisibility(View.VISIBLE);
            tvLocalidad.setText(localidadFiltro.getNombre());
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }

        if(fechaInicioFiltro.isEmpty()){
            llyFechaInicio.setVisibility(View.GONE);
        }
        else{
            llyFechaInicio.setVisibility(View.VISIBLE);
            tvFechaInicio.setText(formatearFecha(fechaInicioFiltro));
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }

        if(fechaFinFiltro.isEmpty()){
            llyFechaFin.setVisibility(View.GONE);
        }
        else{
            llyFechaFin.setVisibility(View.VISIBLE);
            tvFechaFin.setText(formatearFecha(fechaFinFiltro));
            btnLimpiarFiltros.setVisibility(View.VISIBLE);
        }
    }

    private String validarYformatearFecha (String inputDate){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDateFormat.setLenient(false);
        outputDateFormat.setLenient(false);

        try {
            java.util.Date utilDate = inputDateFormat.parse(inputDate);
            String outputDate = outputDateFormat.format(utilDate);

            return outputDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String formatearFecha(String fecha){
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDateFormat.setLenient(false);
        outputDateFormat.setLenient(false);

        try {
            java.util.Date utilDate = inputDateFormat.parse(fecha);
            String outputDate = outputDateFormat.format(utilDate);

            return outputDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void limpiarFiltros(){
        provinciaFiltro = null;
        tipoObjetoFiltro = null;
        textoFiltro = "";
        localidadFiltro = null;
        fechaInicioFiltro = "";
        fechaFinFiltro = "";
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
}
