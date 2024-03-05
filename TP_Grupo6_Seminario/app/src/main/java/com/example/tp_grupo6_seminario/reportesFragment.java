package com.example.tp_grupo6_seminario;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.LocalidadDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.ReporteDao;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reportesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reportesFragment extends Fragment {
    View view;
    Context context;

    private String mParam1;
    private String mParam2;
    private TextView tvProvinciaMOE,tvProvinciaMOP,tvORTotal,tvOETotal,tvOPTotal,tvLocalidadMOP,tvLocalidadMOE,tvOPFiltrado,tvOEFiltrado,tvORFiltrado;
    private Button btnFiltrarProvinciaReporte, btnFiltrarLocalidadReporte, btnFiltrarRangoFechaReporte;
    private ProgressBar pbReportes;
    private Provincia provinciaFiltro = null;
    private Localidad localidadFiltro = null;
    private String filtro = "", fechaInicioFiltro = "", fechaFinFiltro = "";
    public reportesFragment() {
        // Required empty public constructor
    }

    public static reportesFragment newInstance(String param1, String param2) {
        reportesFragment fragment = new reportesFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reportes, container, false);
        context = getContext();
        tvProvinciaMOE = (TextView) view.findViewById(R.id.tvProvinciaMOE);
        tvProvinciaMOP = (TextView) view.findViewById(R.id.tvProvinciaMOP);
        tvORTotal = (TextView) view.findViewById(R.id.tvORTotal);
        tvOETotal = (TextView) view.findViewById(R.id.tvOETotal);
        tvOPTotal = (TextView) view.findViewById(R.id.tvOPTotal);
        tvLocalidadMOP = (TextView) view.findViewById(R.id.tvLocalidadMOP);
        tvLocalidadMOE = (TextView) view.findViewById(R.id.tvLocalidadMOE);
        pbReportes = (ProgressBar) view.findViewById(R.id.pbReportes);
        btnFiltrarProvinciaReporte = (Button) view.findViewById(R.id.btnFiltrarPorProvinciaReportes);
        btnFiltrarLocalidadReporte = (Button) view.findViewById(R.id.btnFiltrarPorLocalidadReportes);
        btnFiltrarRangoFechaReporte = (Button) view.findViewById(R.id.btnFiltrarPorFechasReportes);
        tvOPFiltrado = (TextView) view.findViewById(R.id.tvOPFiltrado);
        tvOEFiltrado = (TextView) view.findViewById(R.id.tvOEFiltrado);
        tvORFiltrado = (TextView) view.findViewById(R.id.tvORFiltrado);

        btnFiltrarProvinciaReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPopUpFiltroProvincia();
            }
        });

        btnFiltrarLocalidadReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroLocalidad();
            }
        });

        btnFiltrarRangoFechaReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpFiltroFechas();
            }
        });



        this.ObtenerReportesIniciales();

        return view;
    }

    public void ObtenerReportesIniciales(){
        try{
            ReporteDao ReporteDao= new ReporteDao(context);
            ReporteDao.setTextView(tvOPTotal);
            ReporteDao.execute("OBTENER_MAX_PUBLICACION_PORTIPO",1);
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvOETotal);
            ReporteDao.execute("OBTENER_MAX_PUBLICACION_PORTIPO",2);
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvORTotal);
            ReporteDao.execute("OBTENER_MAX_OBJETOS_RECUPERADOS");
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvProvinciaMOP);
            ReporteDao.execute("OBTENER_PROVINCIA_MAX_PORTIPO",1);
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvProvinciaMOE);
            ReporteDao.execute("OBTENER_PROVINCIA_MAX_PORTIPO",2);
            ReporteDao = new ReporteDao(context);
            ReporteDao.execute("OBTENER_PROVINCIA_MAX_RECUPERADOS");
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvLocalidadMOP);
            ReporteDao.execute("OBTENER_LOCALIDAD_MAX_PERDIDOS_TP",1);
            ReporteDao = new ReporteDao(context);
            ReporteDao.setTextView(tvLocalidadMOE);
            ReporteDao.execute("OBTENER_LOCALIDAD_MAX_PERDIDOS_TP",2);
            ReporteDao = new ReporteDao(context);
            ReporteDao.execute("OBTENER_LOCALIDAD_MAX_RECUPERADOS");
            ReporteDao = new ReporteDao(context);
            ReporteDao.setPb(pbReportes);
            ReporteDao.execute("OBTENER_TIPOOBJETO_MAX_PERDIDOS");

        }catch (Exception ex){
            Log.i("ERROR_REPORTE", ex.getMessage());
        }
    }

    public void mostrarPopUpFiltroProvincia(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_spinner_y_2_botones, null);
        localidadFiltro = null;
        fechaFinFiltro = "";
        fechaInicioFiltro = "";
        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione una provincia");
        final Spinner spPopUpProvincia = popupView.findViewById(R.id.spPopUp);
        ProvinciaDao provinciaDao = new ProvinciaDao(requireContext(), spPopUpProvincia);
        provinciaDao.execute("LISTADO");
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
                cargarDatosFiltrados();
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
        provinciaFiltro = null;
        fechaFinFiltro ="";
        fechaInicioFiltro = "";
        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione una Localidad");
        final Spinner spPopUpLocalidad = popupView.findViewById(R.id.spPopUp);
        LocalidadDao localidadDao = new LocalidadDao(requireContext(), spPopUpLocalidad);
        localidadDao.execute("LISTADO_LOC");

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
                cargarDatosFiltrados();
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

        provinciaFiltro = null;
        localidadFiltro = null;
        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Seleccione las fechas");
        final EditText etPopUpFechaInicio = popupView.findViewById(R.id.etFechaInicioPopUp);
        final EditText etPopUpFechaFin = popupView.findViewById(R.id.etFechaFinPopUp);
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpAceptar);
        btnpopupAct.setText("Aceptar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        etPopUpFechaInicio.addTextChangedListener(new TextWatcher() {
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
                    etPopUpFechaInicio.setText(text);
                    etPopUpFechaInicio.setSelection(text.length());
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etPopUpFechaFin.addTextChangedListener(new TextWatcher() {
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
                    etPopUpFechaFin.setText(text);
                    etPopUpFechaFin.setSelection(text.length());
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

                if(!fechaInicioFiltro.isEmpty() && !fechaFinFiltro.isEmpty()){
                    fechaInicioFiltro = validarYformatearFecha(fechaInicioFiltro);
                    fechaFinFiltro = validarYformatearFecha(fechaFinFiltro);
                    cargarDatosFiltrados();
                    dialog.dismiss();
                }else{
                        mostrarMensaje("Fecha invalida, revise los datos.");
                }

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

    public void cargarDatosFiltrados(){
        tvOPFiltrado.setText("");
        tvOEFiltrado.setText("");
        tvORFiltrado.setText("");
        filtro = " Estado = 1";
        if (pbReportes.getVisibility() == View.INVISIBLE) {
            pbReportes.setVisibility(View.VISIBLE);
        }

        if(provinciaFiltro != null){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND ID_TipoPublicacion = 1 AND Recuperado = 0";
            }
            filtro = filtro + " AND ID_Provincia = " + provinciaFiltro.getId();
            ReporteDao reporteDao = new ReporteDao(requireContext());
            reporteDao.setTextView(tvOPFiltrado);
            reporteDao.execute("FILTRADOS", filtro);

            filtro = " Estado = 1 AND ID_TipoPublicacion = 2 AND Recuperado = 0 AND ID_Provincia ="+provinciaFiltro.getId();
            reporteDao = new ReporteDao(context);
            reporteDao.setTextView(tvOEFiltrado);
            reporteDao.execute("FILTRADOS",filtro);

            filtro = " Estado = 1 AND Recuperado = 1 AND ID_Provincia ="+provinciaFiltro.getId();
            reporteDao = new ReporteDao(context);
            reporteDao.setTextView(tvORFiltrado);
            reporteDao.setPb(pbReportes);
            reporteDao.execute("FILTRADOS",filtro);
        }

        if(localidadFiltro != null){
            if(!filtro.isEmpty()){
                filtro = filtro + " AND ID_TipoPublicacion = 1 AND Recuperado = 0";
            }
            filtro = filtro + " AND ID_Localidad = " + localidadFiltro.getId();
            ReporteDao reporteDao = new ReporteDao(requireContext());
            reporteDao.setTextView(tvOPFiltrado);
            reporteDao.execute("FILTRADOS", filtro);

            filtro = " Estado = 1 AND ID_TipoPublicacion = 2 AND Recuperado = 0 AND ID_Localidad ="+localidadFiltro.getId();
            reporteDao = new ReporteDao(context);
            reporteDao.setTextView(tvOEFiltrado);
            reporteDao.execute("FILTRADOS",filtro);

            filtro = " Estado = 1 AND Recuperado = 1 AND ID_Localidad ="+localidadFiltro.getId();
            reporteDao = new ReporteDao(context);
            reporteDao.setTextView(tvORFiltrado);
            reporteDao.setPb(pbReportes);
            reporteDao.execute("FILTRADOS",filtro);
        }

        if(!fechaInicioFiltro.isEmpty() && !fechaFinFiltro.isEmpty()){
            if(!filtro.isEmpty()){
                    filtro = filtro + " AND";
            }
                filtro = filtro + " FechaPublicacion >= '" + fechaInicioFiltro + "'" + " AND"+" FechaPublicacion <= '" + fechaFinFiltro + "'"+ " AND ID_TipoPublicacion = 1 AND Recuperado = 0";
                ReporteDao reporteDao = new ReporteDao(requireContext());
                reporteDao.setTextView(tvOPFiltrado);
                reporteDao.execute("FILTRADOS", filtro);

                filtro = " Estado = 1 AND"+ " FechaPublicacion >= '" + fechaInicioFiltro + "'" + " AND"+" FechaPublicacion <= '" + fechaFinFiltro + "'"+ " AND ID_TipoPublicacion = 2 AND Recuperado = 0";
                reporteDao = new ReporteDao(context);
                reporteDao.setTextView(tvOEFiltrado);
                reporteDao.execute("FILTRADOS",filtro);

                filtro = " Estado = 1 AND Recuperado = 1 AND"+" FechaPublicacion >= '" + fechaInicioFiltro + "'" + " AND"+" FechaPublicacion <= '" + fechaFinFiltro + "'";
                reporteDao = new ReporteDao(context);
                reporteDao.setTextView(tvORFiltrado);
                reporteDao.setPb(pbReportes);
                reporteDao.execute("FILTRADOS",filtro);
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

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

}