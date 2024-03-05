package com.example.tp_grupo6_seminario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.entidades.Usuario;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BajaUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BajaUsuarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario user = new Usuario();
    private Button btnBuscarUsuarioBaja, btnBaja;
    private EditText etIdUsuarioONombreBaja;
    private TextView tvIDBaja, tvNombreUsuarioBaja, tvNombreyApellidoBaja, tvCorreoElectronicoBaja, tvProvinciaLocalidadBaja;
    private ProgressBar pbBajaUsr;

    public BajaUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BajaUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BajaUsuarioFragment newInstance(String param1, String param2) {
        BajaUsuarioFragment fragment = new BajaUsuarioFragment();
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
        View view = inflater.inflate(R.layout.fragment_baja_usuario, container, false);

        etIdUsuarioONombreBaja = (EditText) view.findViewById(R.id.etIdUsuarioONombreBaja);
        btnBuscarUsuarioBaja = (Button) view.findViewById(R.id.btnBuscarUsuarioBaja);
        btnBaja = (Button) view.findViewById(R.id.btnBaja);
        tvIDBaja = (TextView) view.findViewById(R.id.tvIDBaja);
        tvNombreUsuarioBaja = (TextView) view.findViewById(R.id.tvNombreUsuarioBaja);
        tvNombreyApellidoBaja = (TextView) view.findViewById(R.id.tvNombreyApellidoBaja);
        tvCorreoElectronicoBaja = (TextView) view.findViewById(R.id.tvCorreoElectronicoBaja);
        tvProvinciaLocalidadBaja = (TextView) view.findViewById(R.id.tvProvinciaLocalidadBaja);
        pbBajaUsr = (ProgressBar) view.findViewById(R.id.pbBajaUsuario);

        btnBaja.setEnabled(false);

        btnBuscarUsuarioBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCamposCompletos()) {
                    try {
                        boolean digitsOnly = TextUtils.isDigitsOnly(etIdUsuarioONombreBaja.getText().toString());
                        UsuarioDao _UsuarioDao = new UsuarioDao(requireContext(), tvIDBaja, tvNombreUsuarioBaja, tvNombreyApellidoBaja, tvCorreoElectronicoBaja, tvProvinciaLocalidadBaja);
                        _UsuarioDao.setBtn(btnBaja);
                        pbBajaUsr.setVisibility(View.VISIBLE);
                        _UsuarioDao.setPb(pbBajaUsr);
                        if (digitsOnly) {
                            _UsuarioDao.execute("OBTENER", Integer.parseInt(etIdUsuarioONombreBaja.getText().toString()));
                            _UsuarioDao.setNombreUsr(etIdUsuarioONombreBaja.getText().toString());
                            limpiarCampos();

                        } else {
                            _UsuarioDao.execute("OBTENER_PORNOMBRE",etIdUsuarioONombreBaja.getText().toString());
                            limpiarCampos();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvIDBaja.getText().toString().isEmpty()){
                    pbBajaUsr.setVisibility(View.VISIBLE);
                    UsuarioDao udao = new UsuarioDao(requireContext());
                    udao.setPb(pbBajaUsr);
                    udao.execute("BAJA",Integer.parseInt(tvIDBaja.getText().toString()));
                    limpiarCampos();
                    btnBaja.setEnabled(false);
                }else{
                    Toast.makeText(getContext(), "No se puede realizar la accion, al parecer no se ha encontrado un usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    public void limpiarCampos(){

        etIdUsuarioONombreBaja.setText("");
        tvIDBaja.setText("");
        tvNombreUsuarioBaja.setText("");
        tvCorreoElectronicoBaja.setText("");
        tvProvinciaLocalidadBaja.setText("");
        tvNombreyApellidoBaja.setText("");
    }

    private boolean validarCamposCompletos(){
        if(!etIdUsuarioONombreBaja.getText().toString().trim().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
}