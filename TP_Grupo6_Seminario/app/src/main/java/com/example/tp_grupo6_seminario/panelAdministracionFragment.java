package com.example.tp_grupo6_seminario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link panelAdministracionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class panelAdministracionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnEditBajaPub, btnBajaUsuario, btnAltaModAdm, btnBajaModAdm;
    private datosUsuarioViewModel viewModel;

    public panelAdministracionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment panelAdministracionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static panelAdministracionFragment newInstance(String param1, String param2) {
        panelAdministracionFragment fragment = new panelAdministracionFragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_panel_administracion, container, false);
        btnEditBajaPub = (Button) view.findViewById(R.id.btnEditBajaPublicacion);
        btnBajaUsuario = (Button) view.findViewById(R.id.btnBajaUsuario);
        btnAltaModAdm = (Button) view.findViewById(R.id.btnAltaModAdm);
        btnBajaModAdm = (Button) view.findViewById(R.id.btnBaja_MOD_ADM);


        btnEditBajaPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                navController.navigate(R.id.editarBajaPublicacionFragment);
            }
        });

        btnBajaUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                navController.navigate(R.id.bajaUsuarioFragment);
            }
        });

        btnAltaModAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario usr = viewModel.getUsuario().getValue();
                if(usr !=null && usr.getTipoUsuario().getId() == 3) {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                    navController.navigate(R.id.altaUsuarioModAdmFragment);
                }else{
                    Toast.makeText(requireContext(),"No tienes el permiso para realizar ésta acción.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBajaModAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                navController.navigate(R.id.bajaUsuarioModAdmFragment);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}