package com.example.tp_grupo6_seminario;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tp_grupo6_seminario.Dao.NotificacionesDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Usuario;

public class NotificacionesFragment extends Fragment{
    Context context;
    ListView lvNotificaciones;
    View view;
    private datosUsuarioViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        context = getContext();
        //
        lvNotificaciones = view.findViewById(R.id.lvNotificaciones);
        cargarNotificaciones();
        return view;
    }

    public void cargarNotificaciones(){
        NotificacionesDao notificacionesDao = new NotificacionesDao(requireContext(),lvNotificaciones);
        viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
        int localidadUsuario = 1;
        //Obtenemos el Usuario
        Usuario usuarioLogeado = viewModel.getUsuario().getValue();
        if (usuarioLogeado != null) {
            localidadUsuario = usuarioLogeado.getLocalidad().getId();
            Log.i("INFO_NOTIF","Usuario no es null. Localidad: "+localidadUsuario);
        }
        notificacionesDao.execute("LISTAR_NOTIFICACIONES",localidadUsuario);
    }



}
