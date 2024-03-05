package com.example.tp_grupo6_seminario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp_grupo6_seminario.Dao.MensajeDao;
import com.example.tp_grupo6_seminario.Dao.NotificacionesDao;
import com.example.tp_grupo6_seminario.adapter.MensajeAdapter;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Mensaje;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.sql.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario usr;
    private TextView tvNombreUsuarioChat;
    private ListView rvMensajes;
    private EditText etMensajeChat;
    private Button btnEnviar;

    private Context context;
    private datosUsuarioViewModel datosUsuarioLoguado;

    public List<Mensaje> mensajesList;
    public MensajeAdapter mensajeAdapter;

    public chatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chatFragment newInstance(String param1, String param2) {
        chatFragment fragment = new chatFragment();
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

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tvNombreUsuarioChat = (TextView) view.findViewById(R.id.tvNombreUsuarioChat);

        etMensajeChat = (EditText) view.findViewById(R.id.etMensajeChat);
        btnEnviar = (Button) view.findViewById(R.id.btnEnviar);
        rvMensajes = (ListView) view.findViewById(R.id.rvMensajes);



        Bundle args = getArguments();
        if (args != null) {
            usr = (Usuario) args.getSerializable("selectedUser");
            // Ahora, tuObjeto contiene el objeto pasado desde el fragmento de origen
        }



        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(usr);
                cargarMensajes(usr.getNombre());
                // rvMensajes //  mensajesList.toArray();
            }
        });

        Log.d("ChatFragment","Nombre: " + usr.getNombre() + ", ID: " + usr.getId());
        tvNombreUsuarioChat.setText(usr.getNombreUsuario());
        context = getContext();// requireContext();
        cargarMensajes(usr.getNombre());
        return view;
    }

    private void enviarMensaje(Usuario selectedUser) {
        String contenidoMensaje = etMensajeChat.getText().toString();//.trim();
        if (!contenidoMensaje.isEmpty()) {
            // Crea un nuevo mensaje y añádelo a la lista
            Date fechaActual = new Date(System.currentTimeMillis());
            Mensaje mensaje = new Mensaje(contenidoMensaje, traigoUsuarioLogueado(), selectedUser.getNombre(), fechaActual);
            MensajeDao mensajedao = new MensajeDao(requireContext());
            mensajedao.execute("INSERTAR_MENSAJE",mensaje);
            //mensajesList.add(mensaje);
            //mensajeAdapter.notifyDataSetChanged();

            // Limpia el EditText después de enviar el mensaje
            etMensajeChat.getText().clear();
        }
    }

    private String traigoUsuarioLogueado(){
        datosUsuarioLoguado = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
        Usuario usuarioLogueado = datosUsuarioLoguado.getUsuario().getValue();

        Log.d("ChatFragment","NombreUsuario: " + usuarioLogueado.getNombreUsuario());
        return usuarioLogueado.getNombreUsuario();
    }

    public void cargarMensajes(String haciaUsuario){
        MensajeDao usuarioDao = new MensajeDao(requireContext(), rvMensajes);
        usuarioDao.execute("LISTAR_MENSAJES",traigoUsuarioLogueado(), haciaUsuario);
    }
/*
    private List<Mensaje> traigoMensajesEntreUsuarios(String usuarioDestino)
    {

        return;
    }
    */
}