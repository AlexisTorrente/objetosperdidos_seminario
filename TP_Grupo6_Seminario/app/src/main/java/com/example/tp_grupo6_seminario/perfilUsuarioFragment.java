package com.example.tp_grupo6_seminario;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tp_grupo6_seminario.Dao.ComentarioPerfilDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perfilUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfilUsuarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvUsuario, tvNombre, tvCorreo, tvZona,tvDescripcion;
    private ImageView  imgEditarUsr;
    private ListView lvComentarios;
    private datosUsuarioViewModel viewModel;
    private ProgressBar pb;

    public perfilUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfilUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static perfilUsuarioFragment newInstance(String param1, String param2) {
        perfilUsuarioFragment fragment = new perfilUsuarioFragment();
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
        // PARA QUE FUNCIONE SE DEBE QUITAR EL getArguments()
        viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);

       setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        lvComentarios = (ListView) view.findViewById(R.id.lvComentarios);
        tvUsuario = (TextView) view.findViewById(R.id.tvUsuario);
        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvCorreo = (TextView) view.findViewById(R.id.tvCorreo);
        tvZona = (TextView) view.findViewById(R.id.tvZona);
        tvDescripcion = (TextView) view.findViewById(R.id.tvDescripcion);
        imgEditarUsr = (ImageView) view.findViewById(R.id.imgEditarPerfilUsuario);
        pb = (ProgressBar) view.findViewById(R.id.progressBarPerfilUsr);

        ComentarioPerfilDao comperdao = new ComentarioPerfilDao(requireContext(), lvComentarios,pb);
        Usuario usuarioLogeado = viewModel.getUsuario().getValue();
        if (usuarioLogeado != null) {
                // Obtiene el nombre de usuario y lo muestra en el EditText
                tvUsuario.setText(usuarioLogeado.getNombreUsuario());
                tvNombre.setText(usuarioLogeado.getNombre());
                tvCorreo.setText(usuarioLogeado.getEmail());
                tvZona.setText(usuarioLogeado.getProvincia().getNombre() +" - "+ usuarioLogeado.getLocalidad().getNombre());
                tvDescripcion.setText(usuarioLogeado.getDescripcionPerfil());
                pb.setVisibility(View.VISIBLE);
                comperdao.execute("LISTADO_COMENTARIOS",usuarioLogeado);
        }

        tvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupEditarDescripcion(tvDescripcion);
            }
        });

        imgEditarUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el NavController desde la actividad que contiene los fragmentos
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);

                comperdao.cancel(true);
                // Navegar al fragmento de destino
                navController.navigate(R.id.editarUsuario);
            }
        });


        return view;
    }

    private void mostrarPopupEditarDescripcion(TextView textView) {
        // Inflar el diseño del popup
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_txt_y_2_botones, null);

        // Obtener las vistas dentro del popup
        TextView tvTitulo = popupView.findViewById(R.id.tvTituloPopUp);
        tvTitulo.setText("Ingresa la descripcion de tu perfil.");

        final EditText etPopUpDescripcion = popupView.findViewById(R.id.etPopUpTexto);
        etPopUpDescripcion.setHint("Ingrese aquí la descripción que quiere tener de usted en su perfil. Máximo 400 carácteres.");
        Button btnpopupAct = popupView.findViewById(R.id.btnPopUpActualizar);
        Button btnpopupCan = popupView.findViewById(R.id.btnPopUpCancelar);

        // Establecer el texto actual del TextView en el EditText
        etPopUpDescripcion.setText(textView.getText());

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
                String updatedText = etPopUpDescripcion.getText().toString();

                // Actualizar el TextView con el nuevo texto
                textView.setText(updatedText);

                // Guardo la descripcion en la BD
                Usuario usuarioLogeado = viewModel.getUsuario().getValue();
                if (usuarioLogeado != null) {
                        // Obtiene el nombre de usuario y lo muestra en el EditText
                        usuarioLogeado.setDescripcionPerfil(updatedText);
                        UsuarioDao udao = new UsuarioDao(requireContext());
                        udao.execute("ACTUALIZAR_DESCRIPCION",usuarioLogeado);
                        viewModel.setUsuario(usuarioLogeado);
                }
                // Cerrar el popup
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_navigation_drawer, menu);

        // Encuentra el ítem de menú que deseas ocultar por su ID
        MenuItem item = menu.findItem(R.id.menu_item_perfilUsuario);

        // Oculta el ítem estableciendo su visibilidad en false
        item.setVisible(false);
    }
}