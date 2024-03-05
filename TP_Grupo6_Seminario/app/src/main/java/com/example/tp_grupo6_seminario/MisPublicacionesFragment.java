package com.example.tp_grupo6_seminario;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Usuario;


public class MisPublicacionesFragment extends Fragment {
    View view;
    Context context;
    ListView lvMisPublicaciones;
    ProgressBar pbMisPublicaciones;
    private datosUsuarioViewModel viewModel;
    public MisPublicacionesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);

        view = inflater.inflate(R.layout.fragment_mis_publicaciones, container, false);

        try{
             context = getContext();

            int idUsuario = 8;
            //Obtengo el usuario
            viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
            Usuario usuarioLogeado = viewModel.getUsuario().getValue();

            if (usuarioLogeado != null) {
                idUsuario = usuarioLogeado.getId();
                Log.i("INFO_MISPUBLI","Usuario: "+usuarioLogeado.getNombreUsuario().toString());
            }

            lvMisPublicaciones = view.findViewById(R.id.lvMisPublicaciones);
            pbMisPublicaciones = view.findViewById(R.id.pbMisPublicaciones);

            String ConsultaWhere = " ID_Usuario = "+ idUsuario +" and Estado=1";
            PublicacionDao publicacionDao = new PublicacionDao(context,lvMisPublicaciones,pbMisPublicaciones,1,navController);
            publicacionDao.execute("LISTADO",ConsultaWhere);

        }catch (Exception ex){
            Log.e("ERROR_MISPUBLI100", "FTP Connection Closed: " + ex.getMessage());
            ex.printStackTrace();
        } catch (OutOfMemoryError e) {
            Log.i("ERROR_MISPUBLI3", "Error de memoria: " + e.getMessage());
        }
        return view;

    }
}