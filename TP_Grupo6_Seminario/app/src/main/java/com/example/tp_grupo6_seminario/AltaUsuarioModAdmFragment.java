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
 * Use the {@link AltaUsuarioModAdmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltaUsuarioModAdmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario user = new Usuario();
    private TipoUsuario tipoUsuario = new TipoUsuario();
    private Button btnBuscarUsuarioAltaModADM, btnAlta_ADM, btnAlta_MOD;
    private EditText etIdUsuarioONombreAltaModADM;
    private TextView tvIDAltaModADM, tvNombreUsuarioAltaModADM, tvNombreyApellidoAltaModADM, tvCorreoElectronicoAltaModADM, tvProvinciaLocalidadAltaModADM;
    private ProgressBar pbAltaModADMUsr;

    public AltaUsuarioModAdmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AltaModADMUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AltaUsuarioModAdmFragment newInstance(String param1, String param2) {
        AltaUsuarioModAdmFragment fragment = new AltaUsuarioModAdmFragment();
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
        View view = inflater.inflate(R.layout.fragment_alta_usuario_mod_adm, container, false);

        etIdUsuarioONombreAltaModADM = (EditText) view.findViewById(R.id.etIdUsuarioONombreAltaModADM);
        btnBuscarUsuarioAltaModADM = (Button) view.findViewById(R.id.btnBuscarUsuarioAltaModADM);
        btnAlta_ADM = (Button) view.findViewById(R.id.btnAlta_ADM);
        btnAlta_MOD = (Button) view.findViewById(R.id.btnAlta_MOD);
        tvIDAltaModADM = (TextView) view.findViewById(R.id.tvIDAltaModADM);
        tvNombreUsuarioAltaModADM = (TextView) view.findViewById(R.id.tvNombreUsuarioAltaModADM);
        tvNombreyApellidoAltaModADM = (TextView) view.findViewById(R.id.tvNombreyApellidoAltaModADM);
        tvCorreoElectronicoAltaModADM = (TextView) view.findViewById(R.id.tvCorreoElectronicoAltaModADM);
        tvProvinciaLocalidadAltaModADM = (TextView) view.findViewById(R.id.tvProvinciaLocalidadAltaModADM);
        pbAltaModADMUsr = (ProgressBar) view.findViewById(R.id.pbUsuarioAltaModADM);

        HabilitarBotones(false);

        btnBuscarUsuarioAltaModADM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCamposCompletos()) {
                    try {
                        boolean digitsOnly = TextUtils.isDigitsOnly(etIdUsuarioONombreAltaModADM.getText().toString());
                        UsuarioDao _UsuarioDao = new UsuarioDao(requireContext(), tvIDAltaModADM, tvNombreUsuarioAltaModADM, tvNombreyApellidoAltaModADM, tvCorreoElectronicoAltaModADM, tvProvinciaLocalidadAltaModADM);
                        //Corroborar funcionalidad de los botones. Tengo duda como hacer esto con ambos.
                        // A mi entender, como lo acabo de hacer sólo estaría estableciendo la barra de progreso al ultimo boton seteado (MOD)
                        // Quizas debería usar dos barras de progreso?

                        _UsuarioDao.setBtn(btnAlta_ADM);
                        _UsuarioDao.setBtn(btnAlta_MOD);
                        pbAltaModADMUsr.setVisibility(View.VISIBLE);
                        _UsuarioDao.setPb(pbAltaModADMUsr);
                        if (digitsOnly) {
                            _UsuarioDao.execute("OBTENER", Integer.parseInt(etIdUsuarioONombreAltaModADM.getText().toString()));
                            _UsuarioDao.setNombreUsr(etIdUsuarioONombreAltaModADM.getText().toString());
                            limpiarCampos();
                            HabilitarBotones(true);

                        } else {
                            _UsuarioDao.execute("OBTENER_PORNOMBRE",etIdUsuarioONombreAltaModADM.getText().toString());
                            limpiarCampos();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnAlta_ADM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvIDAltaModADM.getText().toString().isEmpty()){
                    pbAltaModADMUsr.setVisibility(View.VISIBLE);
                    tipoUsuario.setId(3);
                    user.setTipoUsuario(tipoUsuario);
                    user.setId(Integer.parseInt(tvIDAltaModADM.getText().toString()));
                    UsuarioDao udao = new UsuarioDao(requireContext(), user);
                    udao.setPb(pbAltaModADMUsr);
                    udao.execute("ACTUALIZAR_TIPOUSUARIO");
                    limpiarCampos();
                    btnAlta_ADM.setEnabled(false);
                    pbAltaModADMUsr.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getContext(), "No se puede realizar la accion, al parecer no se ha encontrado un usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAlta_MOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvIDAltaModADM.getText().toString().isEmpty()){
                    pbAltaModADMUsr.setVisibility(View.VISIBLE);
                    tipoUsuario.setId(2);
                    user.setTipoUsuario(tipoUsuario);
                    user.setId(Integer.parseInt(tvIDAltaModADM.getText().toString()));
                    UsuarioDao udao = new UsuarioDao(requireContext(), user);
                    udao.setPb(pbAltaModADMUsr);
                    udao.execute("ACTUALIZAR_TIPOUSUARIO");
                    limpiarCampos();
                    btnAlta_MOD.setEnabled(false);
                }else{
                    Toast.makeText(getContext(), "No se puede realizar la accion, al parecer no se ha encontrado un usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void HabilitarBotones(boolean enabled) {
        btnAlta_ADM.setEnabled(enabled);
        btnAlta_MOD.setEnabled(enabled);
    }


    public void limpiarCampos(){

        etIdUsuarioONombreAltaModADM.setText("");
        tvIDAltaModADM.setText("");
        tvNombreUsuarioAltaModADM.setText("");
        tvCorreoElectronicoAltaModADM.setText("");
        tvProvinciaLocalidadAltaModADM.setText("");
        tvNombreyApellidoAltaModADM.setText("");
    }

    private boolean validarCamposCompletos(){
        if(!etIdUsuarioONombreAltaModADM.getText().toString().trim().isEmpty()){
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