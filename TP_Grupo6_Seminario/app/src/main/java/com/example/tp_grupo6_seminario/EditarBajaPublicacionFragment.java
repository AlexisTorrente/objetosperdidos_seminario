package com.example.tp_grupo6_seminario;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.entidades.Publicacion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarBajaPublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarBajaPublicacionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etIdPublicacion;
    private ImageView ivPublicacion;
    private TextView tvIdPub, tvTituloPub;
    private Button btnBuscar, btnEditar, btnBaja;
    private ProgressBar pb;
    private Publicacion publicacion;

    public EditarBajaPublicacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarBajaPublicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarBajaPublicacionFragment newInstance(String param1, String param2) {
        EditarBajaPublicacionFragment fragment = new EditarBajaPublicacionFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_baja_publicacion, container, false);

        etIdPublicacion = (EditText) view.findViewById(R.id.etIdPublicacion);
        btnBuscar = (Button) view.findViewById(R.id.btnBuscarIdPub);
        ivPublicacion = (ImageView) view.findViewById(R.id.ivPublicacion);
        tvIdPub = (TextView) view.findViewById(R.id.tvIdPub);
        tvTituloPub = (TextView) view.findViewById(R.id.tvTituloPub);
        btnEditar = (Button) view.findViewById(R.id.btnEditarPub);
        btnBaja = (Button) view.findViewById(R.id.btnBajaPub);
        pb = (ProgressBar) view.findViewById(R.id.pbBaja);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCamposCompletos()){
                    limpiarCampos();
                    try{
                        boolean digitsOnly = TextUtils.isDigitsOnly(etIdPublicacion.getText().toString().trim());
                        PublicacionDao publicacionDao = new PublicacionDao(requireContext(), ivPublicacion, tvTituloPub, tvIdPub);
                        publicacionDao.setPb(pb);
                        if(digitsOnly){
                            pb.setVisibility(View.VISIBLE);
                            publicacionDao.execute("OBTENER_PUBLICACION_POR_ID", Integer.parseInt(etIdPublicacion.getText().toString().trim()));
                        }
                        else{
                            Toast.makeText(getContext(), "Debes ingresar solo numeros", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicacionDao publicacionDao = new PublicacionDao(requireContext(), ivPublicacion, tvTituloPub, tvIdPub);
                publicacionDao.setPb(pb);
                pb.setVisibility(View.VISIBLE);
                publicacionDao.execute("OBTENER_PUBLICACION_POR_ID", Integer.parseInt(tvIdPub.getText().toString()), "EDITAR");


            }
        });

        btnBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvIdPub.getText().toString().isEmpty()){
                    try {
                        PublicacionDao publicacionDao = new PublicacionDao(requireContext());
                        publicacionDao.setPb(pb);
                        publicacionDao.execute("BAJA", Integer.parseInt(tvIdPub.getText().toString()));
                        limpiarCampos();
                        etIdPublicacion.setText("");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });




        return view;
    }

    private boolean validarCamposCompletos(){
        if(!etIdPublicacion.getText().toString().trim().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    public void limpiarCampos(){
        ivPublicacion.setImageResource(R.drawable.icono_usuario_generico);
        tvTituloPub.setText("");
        tvIdPub.setText("");
    }
}