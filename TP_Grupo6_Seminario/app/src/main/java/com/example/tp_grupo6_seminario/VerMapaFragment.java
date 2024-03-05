package com.example.tp_grupo6_seminario;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_grupo6_seminario.Dao.CajaObjetoDao;
import com.example.tp_grupo6_seminario.Dao.ComentarioPerfilDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.CajaObjeto;
import com.example.tp_grupo6_seminario.entidades.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;


public class VerMapaFragment extends Fragment implements  OnMapReadyCallback {
    View view;
    Context context;
    TextView txtDescripcion;
    String Latitud;
    String Longitud;
    GoogleMap gMap;
    ListView lvMunicipios;
    ProgressBar pbListaMunicipios;
    private datosUsuarioViewModel viewModel;

    public VerMapaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ver_mapa, container, false);
        context = getContext();

        // Cargamos el mapa de Google en el fragmento
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
      try{
          gMap = googleMap;
          txtDescripcion = view.findViewById(R.id.txtDescripcion);
          viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);

          //Eventos:
          googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
              @Override
              public void onMapClick(@NonNull LatLng latLng) {
              }
          });

          googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
              @Override
              public void onMapLongClick(@NonNull LatLng latLng) {
              }
          });

          int localidadUsuario = 1;
          //Obtenemos el Usuario
          Usuario usuarioLogeado = viewModel.getUsuario().getValue();
          if (usuarioLogeado != null) {
              localidadUsuario = usuarioLogeado.getLocalidad().getId();
             Log.i("INFO_VERMAPA","Usuario no es null. Localidad: "+localidadUsuario);
          }

          CajaObjetoDao cajaObjetoDao = new CajaObjetoDao(context,txtDescripcion,Latitud,Longitud,gMap);
          cajaObjetoDao.execute("OBTENER_CAJA_BYLOCALIDAD",localidadUsuario);

          ObtenerMunicipios();

      }catch (Exception ex){
          String Mensaje = ex.getMessage().toString();
          Toast.makeText(getContext(),Mensaje,Toast.LENGTH_SHORT).show();
          Log.i("ERROR_OBJ",Mensaje);
      }

    }

    public void ObtenerMunicipios(){
        try{

            lvMunicipios = view.findViewById(R.id.lvMunicipios);
            pbListaMunicipios= view.findViewById(R.id.pbListaMunicipios);

            CajaObjetoDao cajaObjetoDao = new CajaObjetoDao(context,lvMunicipios,txtDescripcion,pbListaMunicipios,gMap);
            pbListaMunicipios.setVisibility(View.VISIBLE);
            cajaObjetoDao.execute("LISTADO_CAJAS");

        }catch (Exception ex){
            String Mensaje = "Error en ObtenerMunicipios():" + ex.getMessage().toString();
            Toast.makeText(getContext(),Mensaje,Toast.LENGTH_SHORT).show();
            Log.i("ERROR_OBJ",Mensaje);
        }
    }

}