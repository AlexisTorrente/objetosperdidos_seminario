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
import com.example.tp_grupo6_seminario.entidades.TipoUsuario;
import com.example.tp_grupo6_seminario.entidades.Usuario;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BajaUsuarioModAdmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BajaUsuarioModAdmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario user = new Usuario();
    private TipoUsuario tipoUsuario = new TipoUsuario();
    private Button btnBuscarUsuarioBajaModADM, btnBaja_MOD_ADM;
    private EditText etIdUsuarioONombreBajaModADM;
    private TextView tvIDBajaModADM, tvNombreUsuarioBajaModADM, tvNombreyApellidoBajaModADM, tvCorreoElectronicoBajaModADM, tvProvinciaLocalidadBajaModADM;
    private ProgressBar pbBajaModADMUsr;

    public BajaUsuarioModAdmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BajaModADMUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BajaUsuarioModAdmFragment newInstance(String param1, String param2) {
        BajaUsuarioModAdmFragment fragment = new BajaUsuarioModAdmFragment();
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
        View view = inflater.inflate(R.layout.fragment_baja_usuario_mod_adm, container, false);

        etIdUsuarioONombreBajaModADM = (EditText) view.findViewById(R.id.etIdUsuarioONombreBajaModADM);
        btnBuscarUsuarioBajaModADM = (Button) view.findViewById(R.id.btnBuscarUsuarioBajaModADM);
        btnBaja_MOD_ADM = (Button) view.findViewById(R.id.btnBaja_MOD_ADM);
        tvIDBajaModADM = (TextView) view.findViewById(R.id.tvIDBajaModADM);
        tvNombreUsuarioBajaModADM = (TextView) view.findViewById(R.id.tvNombreUsuarioBajaModADM);
        tvNombreyApellidoBajaModADM = (TextView) view.findViewById(R.id.tvNombreyApellidoBajaModADM);
        tvCorreoElectronicoBajaModADM = (TextView) view.findViewById(R.id.tvCorreoElectronicoBajaModADM);
        tvProvinciaLocalidadBajaModADM = (TextView) view.findViewById(R.id.tvProvinciaLocalidadBajaModADM);
        pbBajaModADMUsr = (ProgressBar) view.findViewById(R.id.pbUsuarioBajaModADM);

        HabilitarBotones(false);

        btnBuscarUsuarioBajaModADM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCamposCompletos()) {
                    try {
                        boolean digitsOnly = TextUtils.isDigitsOnly(etIdUsuarioONombreBajaModADM.getText().toString());
                        UsuarioDao _UsuarioDao = new UsuarioDao(requireContext(), tvIDBajaModADM, tvNombreUsuarioBajaModADM, tvNombreyApellidoBajaModADM, tvCorreoElectronicoBajaModADM, tvProvinciaLocalidadBajaModADM);

                        _UsuarioDao.setBtn(btnBaja_MOD_ADM);
                        pbBajaModADMUsr.setVisibility(View.VISIBLE);
                        _UsuarioDao.setPb(pbBajaModADMUsr);
                        if (digitsOnly) {
                            _UsuarioDao.execute("OBTENER", Integer.parseInt(etIdUsuarioONombreBajaModADM.getText().toString()));
                            _UsuarioDao.setNombreUsr(etIdUsuarioONombreBajaModADM.getText().toString());
                            limpiarCampos();
                            HabilitarBotones(true);

                        } else {
                            _UsuarioDao.execute("OBTENER_PORNOMBRE",etIdUsuarioONombreBajaModADM.getText().toString());
                            limpiarCampos();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnBaja_MOD_ADM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvIDBajaModADM.getText().toString().isEmpty()){
                    pbBajaModADMUsr.setVisibility(View.VISIBLE);
                    tipoUsuario.setId(1);
                    user.setTipoUsuario(tipoUsuario);
                    user.setId(Integer.parseInt(tvIDBajaModADM.getText().toString()));
                    UsuarioDao udao = new UsuarioDao(requireContext(), user);
                    udao.setPb(pbBajaModADMUsr);
                    udao.execute("ACTUALIZAR_TIPOUSUARIO");
                    limpiarCampos();
                    btnBaja_MOD_ADM.setEnabled(false);
                }else{
                    Toast.makeText(getContext(), "No se puede realizar la accion, al parecer no se ha encontrado un usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void HabilitarBotones(boolean enabled) {
        btnBaja_MOD_ADM.setEnabled(enabled);
    }


    public void limpiarCampos(){

        etIdUsuarioONombreBajaModADM.setText("");
        tvIDBajaModADM.setText("");
        tvNombreUsuarioBajaModADM.setText("");
        tvCorreoElectronicoBajaModADM.setText("");
        tvProvinciaLocalidadBajaModADM.setText("");
        tvNombreyApellidoBajaModADM.setText("");
    }

    private boolean validarCamposCompletos(){
        if(!etIdUsuarioONombreBajaModADM.getText().toString().trim().isEmpty()){
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