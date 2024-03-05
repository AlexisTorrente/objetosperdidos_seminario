package com.example.tp_grupo6_seminario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tp_grupo6_seminario.Dao.GeneroDao;
import com.example.tp_grupo6_seminario.Dao.ProvinciaDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.adapter.UsuarioAdapter;
import com.example.tp_grupo6_seminario.entidades.Genero;
import com.example.tp_grupo6_seminario.entidades.Localidad;
import com.example.tp_grupo6_seminario.entidades.Provincia;
import com.example.tp_grupo6_seminario.entidades.TipoUsuario;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class mensajeriaInternaFragment extends Fragment {
    private ListView lvChats;
    private UsuarioAdapter usuarioAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public mensajeriaInternaFragment() {
        // Required empty public constructor
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensajeria_interna, container, false);

        lvChats = view.findViewById(R.id.lvChats);

        // Simplemente usa un ArrayAdapter para mostrar la lista de usuarios en el ListView
        // Aquí asumo que tienes una lista de usuarios llamada "usuarioList"



        UsuarioDao gdao = new UsuarioDao(requireContext(), lvChats);
        gdao.execute("LISTADO");

        // List<Usuario> listaDeUsuarios = obtenerUsuariosDesdeBaseDeDatos();


        // lvChats.setAdapter(usuarioAdapter);
        lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Manejar la acción cuando se hace clic en un usuario de la lista
                Usuario selectedUser = (Usuario) parent.getItemAtPosition(position);
                // Implementa la lógica según tus necesidades
            }
        });

        return view;
    }

    // Implementa esta función según tu lógica para obtener la lista de usuarios desde la base de datos
    private List<Usuario> obtenerUsuariosDesdeBaseDeDatos() {
        try {
            // Aquí deberías llamar a tu DAO para obtener la lista de usuarios desde la base de datos
            UsuarioDao usuariodao = new UsuarioDao(this.getContext());
            return usuariodao.obtenerListadoDeUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar el error de alguna manera, por ejemplo, mostrando un mensaje de error.
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }
        /*
        List<Usuario> usuarios = new ArrayList<>();
        TipoUsuario tipoUsuario = new TipoUsuario();

        Provincia tipoProv = new Provincia();
        Localidad tipoLoc = new Localidad();
        Genero tipoGene = new Genero();
        // Generar usuarios de prueba
        Usuario usuario1 = new Usuario(
                1,
                tipoUsuario,  // Debes tener una clase TipoUsuario con un constructor adecuado
                tipoProv,  // Debes tener una clase Provincia con un constructor adecuado
                tipoLoc,  // Debes tener una clase Localidad con un constructor adecuado
                tipoGene,  // Debes tener una clase Genero con un constructor adecuado
                "user1",
                "contrasena1",
                "Nombre1",
                "Apellido1",
                "12345678",
                "correo1@example.com",
                "2000-01-01",
                "Descripción de perfil 1",
                true
        );

        Usuario usuario2 = new Usuario(
                2,
                tipoUsuario,  // Debes tener una clase TipoUsuario con un constructor adecuado
                tipoProv,  // Debes tener una clase Provincia con un constructor adecuado
                tipoLoc,  // Debes tener una clase Localidad con un constructor adecuado
                tipoGene,  // Debes tener una clase Genero con un constructor adecuado
                "user2",
                "contrasena2",
                "Nombre2",
                "Apellido2",
                "87654321",
                "correo2@example.com",
                "1990-05-15",
                "Descripción de perfil 2",
                true
        );

        // Agregar usuarios a la lista
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        return usuarios;
        }
 */

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mensajeriaInternaFragment() {
        // Required empty public constructor
    }
*/


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mensajeriaInternaFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static mensajeriaInternaFragment newInstance(String param1, String param2) {
        mensajeriaInternaFragment fragment = new mensajeriaInternaFragment();
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
        View view = inflater.inflate(R.layout.fragment_mensajeria_interna, container, false);
        Log.d("Mensajeri_Interna","Entro al create view.");
        lvChats = view.findViewById(R.id.lvChats);

        // Simplemente usa un ArrayAdapter para mostrar la lista de usuarios en el ListView
        // Aquí asumo que tienes una lista de usuarios llamada "usuarioList"



        UsuarioDao gdao = new UsuarioDao(requireContext(), lvChats);
        gdao.execute("LISTADO_CHATS");

        // List<Usuario> listaDeUsuarios = obtenerUsuariosDesdeBaseDeDatos();


        // lvChats.setAdapter(usuarioAdapter);
        lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Manejar la acción cuando se hace clic en un usuario de la lista
                Usuario selectedUser = (Usuario) parent.getItemAtPosition(position);
                abrirChat(selectedUser);
            }
        });

        return view;
    }

    private void abrirChat(Usuario selectedUser) {
        /*(new Intent(requireContext(), ChatActivity.class)
                .putExtra("selectedUser", selectedUser));


         */
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedUser", selectedUser);

        NavController navController = Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment_content_activity_navigation_drawer);
        navController.navigate(R.id.chatFragment,bundle);

        /*
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra("selectedUser", selectedUser);
        startActivity(intent);
        */
    }
}