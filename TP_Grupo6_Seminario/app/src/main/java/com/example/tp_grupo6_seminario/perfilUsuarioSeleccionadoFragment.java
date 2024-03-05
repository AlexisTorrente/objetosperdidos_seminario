package com.example.tp_grupo6_seminario;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.ComentarioPerfilDao;
import com.example.tp_grupo6_seminario.adapter.ComentarioPerfilAdapter;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.ComentarioPerfil;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perfilUsuarioSeleccionadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfilUsuarioSeleccionadoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvUsuario, tvNombre, tvCorreo, tvZona,tvDescripcion;
    private Button btnPublicarComentario;
    private ListView lvComentarios;
    private datosUsuarioViewModel viewModel;
    private ProgressBar pb;

    ArrayList<ComentarioPerfil> list = new ArrayList<>();

    public perfilUsuarioSeleccionadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfilUsuarioSeleccionadoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static perfilUsuarioSeleccionadoFragment newInstance(String param1, String param2) {
        perfilUsuarioSeleccionadoFragment fragment = new perfilUsuarioSeleccionadoFragment();
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
        if (getArguments() != null) {
            viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_usuario_seleccionado, container, false);

        lvComentarios = (ListView) view.findViewById(R.id.lvComentariosUsuarioSeleccionado);
        tvUsuario = (TextView) view.findViewById(R.id.tvUsuarioSeleccionado);
        tvNombre = (TextView) view.findViewById(R.id.tvNombreUsuarioSeleccionado);
        tvCorreo = (TextView) view.findViewById(R.id.tvCorreoUsuarioSeleccionado);
        tvZona = (TextView) view.findViewById(R.id.tvZonaUsuarioSeleccionado);
        tvDescripcion = (TextView) view.findViewById(R.id.tvDescripcionUsuarioSeleccionado);
        btnPublicarComentario = (Button) view.findViewById(R.id.btnPublicarComentarioUsuarioSeleccionado);
        pb = (ProgressBar) view.findViewById(R.id.progressBarEsperaComentarios);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Usuario usrPublicacion = (Usuario) bundle.getSerializable("usuario_perfilSeleccionado");
            if (usrPublicacion != null) {
                // Obtiene el nombre de usuario y lo muestra en el EditText
                tvUsuario.setText(usrPublicacion.getNombreUsuario());
                tvNombre.setText(usrPublicacion.getNombre());
                tvCorreo.setText(usrPublicacion.getEmail());
                tvZona.setText(usrPublicacion.getProvincia().getNombre() +" - "+ usrPublicacion.getLocalidad().getNombre());
                tvDescripcion.setText(usrPublicacion.getDescripcionPerfil());
                ComentarioPerfilDao comperdao = new ComentarioPerfilDao(requireContext(), lvComentarios,pb);
                pb.setVisibility(View.VISIBLE);
                comperdao.execute("LISTADO_COMENTARIOS",usrPublicacion);
            }
        }

        btnPublicarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupPublicarComentario();
            }
        });


        return view;
    }

    private void mostrarPopupPublicarComentario(){
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_txt_y_2_botones, null);

        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Ingresa lo que quieras comentar.");
        final EditText etPopUpComentario = popupView.findViewById(R.id.etPopUpTexto);
        etPopUpComentario.setHint("Ingrese aquí el comentario que desea publicar. Máximo 400 carácteres.");
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpActualizar);
        btnpopupAct.setText("Publicar");
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        // Configurar el botón "Actualizar"
        btnpopupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el texto del EditText del popup
                String updatedText = etPopUpComentario.getText().toString();
                if (!updatedText.isEmpty()) {
                    ComentarioPerfil coment = new ComentarioPerfil();
                    Log.d("PerfilUsrSeleccionado","ENTRO EN EL POPUP");
                    Usuario usr = viewModel.getUsuario().getValue();
                    if(usr != null) {
                        Log.d("PerfilUsrSeleccionado","EL USR NO ES NULO");
                        coment.setUsuarioComenterio(usr);
                        coment.setComenterio(updatedText);
                        list.clear();
                        Bundle bundle = getArguments();

                        if (bundle != null) {
                            Usuario usrPublicacion = (Usuario) bundle.getSerializable("usuario_perfilSeleccionado");
                            if (usrPublicacion != null) {
                                list.add(coment);
                                ComentarioPerfilDao comdao = new ComentarioPerfilDao(getContext());
                                ComentarioPerfil cperfil = new ComentarioPerfil();
                                cperfil.setUsuarioPerfil(usrPublicacion);
                                cperfil.setUsuarioComenterio(usr);
                                cperfil.setComenterio(updatedText);

                                ComentarioPerfilAdapter adapter = (ComentarioPerfilAdapter) lvComentarios.getAdapter();
                                adapter.agregarComentarios(list);

                                comdao.execute("AGREGAR",cperfil);
                            }else{
                                mostrarMensaje("Ha ocurrido un error con respecto a la publicacion. Si persiste contacte a soporte.");
                            }
                        }else {
                            mostrarMensaje("Ha ocurrido un error. Si persiste contacte a soporte.");
                        }



                    }else{
                        mostrarMensaje("Ha ocurrido un error con respecto a tu usuario. Si persiste contacte a soporte.");
                    }

                    // Cerrar el popup
                    dialog.dismiss();
                }else{
                   mostrarMensaje("No puedes publicar un comentario vacio.");
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

    private void mostrarMensaje(String mensaje){
        Toast.makeText(requireContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
}