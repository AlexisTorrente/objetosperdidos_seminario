package com.example.tp_grupo6_seminario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.tp_grupo6_seminario.Dao.ComentarioPerfilDao;
import com.example.tp_grupo6_seminario.Dao.ComentarioPublicacionDao;
import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.ComentarioPublicacion;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;

public class ObjetoDetalleFragment extends Fragment {

    private ScrollView scrollView;
    private TextView tvTitulo, tvDescripcion, tvProvincia, tvLocalidad, tvUbicacion, tvUsuarioPublicacion, tvFechaPublicacion, tvRangoHorario, tvComentario;
    private ImageView ivPublicacion;
    private ListView lvComentarios;
    private Button btnPublicarComentario;
    private ProgressBar pb;
    private int idImagen;
    private Publicacion publicacion;
    private datosUsuarioViewModel viewModel;
    public ObjetoDetalleFragment() {
        // Required empty public constructor
    }


    public static ObjetoDetalleFragment newInstance(String param1, String param2) {
        ObjetoDetalleFragment fragment = new ObjetoDetalleFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_objeto_detalle, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        tvUsuarioPublicacion = (TextView) view.findViewById(R.id.tvUsuario);
        tvTitulo = (TextView) view.findViewById(R.id.tvTitulo);
        tvProvincia = (TextView) view.findViewById(R.id.tvProvincia);
        tvLocalidad = (TextView) view.findViewById(R.id.tvLocalidad);
        tvUbicacion = (TextView) view.findViewById(R.id.tvUbicacion);
        tvRangoHorario = (TextView) view.findViewById(R.id.tvRangoHorario);
        tvDescripcion = (TextView) view.findViewById(R.id.tvDescripcion);
        tvFechaPublicacion = (TextView) view.findViewById(R.id.tvFecha);
        ivPublicacion = (ImageView) view.findViewById(R.id.ivPublicacion);
        lvComentarios = (ListView) view.findViewById(R.id.lvComentarios);
        tvComentario = (TextView) view.findViewById(R.id.txtNuevoComentario) ;
        btnPublicarComentario = (Button) view.findViewById(R.id.btnPublicarComentario);
        pb = (ProgressBar) view.findViewById(R.id.progressBarComenPubli);

        viewModel = new ViewModelProvider(requireActivity()).get(datosUsuarioViewModel.class);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lvComentarios.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        lvComentarios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            publicacion = (Publicacion) bundle.getSerializable("Publicacion");
            if (publicacion != null) {
                tvUsuarioPublicacion.setText(publicacion.getUsuario().getNombreUsuario());
                tvTitulo.setText(publicacion.getTitulo());
                tvProvincia.setText(publicacion.getProvincia().getNombre());
                tvLocalidad.setText(publicacion.getLocalidad().getNombre());
                tvUbicacion.setText(publicacion.getUbicacion());
                tvRangoHorario.setText(publicacion.getHoraPublicacion());
                tvDescripcion.setText(publicacion.getDescripcion());
                tvFechaPublicacion.setText(formatearFecha(publicacion.getFechaPublicacion()));
                idImagen = getContext().getResources().getIdentifier(publicacion.getImagen(), "drawable", getContext().getPackageName());
                ivPublicacion.setImageResource(idImagen);

                tvUsuarioPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = getArguments();
                        Publicacion publicacion = (Publicacion) bundle.getSerializable("Publicacion");
                        Usuario usr = viewModel.getUsuario().getValue();
                        if(usr != null) {
                            if(publicacion.getUsuario().getId() != usr.getId()){
                                bundle.putSerializable("usuario_perfilSeleccionado", publicacion.getUsuario());

                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_activity_navigation_drawer);
                                // Navegar al fragmento de destino
                                navController.navigate(R.id.perfilUsuarioSeleccionadoFragment,bundle);
                            }
                        }
                    }
                });

                ComentarioPublicacionDao compudao = new ComentarioPublicacionDao(requireContext(), lvComentarios, pb);
                pb.setVisibility(View.VISIBLE);
                compudao.execute("LISTADO_COMENTARIOS", publicacion);
            }
        }

        btnPublicarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvComentario.getText().toString().trim().isEmpty()){
                    lvComentarios.setAdapter(null);
                    ComentarioPublicacionDao comentPuDao = new ComentarioPublicacionDao(requireContext());
                    ComentarioPublicacion comentarioPublicacion = new ComentarioPublicacion();
                    comentarioPublicacion.setUsuario(viewModel.getUsuario().getValue());
                    comentarioPublicacion.setPublicacion(publicacion);
                    comentarioPublicacion.setComentario(tvComentario.getText().toString().trim());
                    comentPuDao.execute("AGREGAR", comentarioPublicacion);

                    tvComentario.setText("");

                    ComentarioPublicacionDao compudao = new ComentarioPublicacionDao(requireContext(), lvComentarios, pb);
                    pb.setVisibility(View.VISIBLE);
                    compudao.execute("LISTADO_COMENTARIOS", publicacion);
                }
            }
        });

        return view;
    }

    public String formatearFecha(Date fecha){
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDateFormat.setLenient(false);
        outputDateFormat.setLenient(false);

        try {
            String outputDate = outputDateFormat.format(fecha);

            return outputDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}