package com.example.tp_grupo6_seminario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactarSoporteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactarSoporteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String MailSoporte = "gaston.sosa@alumnos.frgp.utn.edu.ar"; //Mail provisional, podemos crear uno
    EditText etAsunto, etMensaje;
    public ContactarSoporteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactarSoporteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactarSoporteFragment newInstance(String param1, String param2) {
        ContactarSoporteFragment fragment = new ContactarSoporteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_contactar_soporte, container, false);

        etAsunto = myView.findViewById(R.id.editTextAsunto);
        etMensaje = myView.findViewById(R.id.editTextMensaje);

        Button btnEnviarMail = myView.findViewById(R.id.btnEnviarMail);
        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMail(v);
            }
        });

        return myView;
    }


    public void EnviarMail(View view){

        try{
            String mensaje = ValidarDatosCorreo();
            if(mensaje.contains("ok!")){

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{MailSoporte.toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT,etAsunto.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT,etMensaje.getText().toString());

                startActivity(intent);

            }else{

                Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){

        }
    }

    public String ValidarDatosCorreo(){
        String msg = "";
        try{
            String Asunto = etAsunto.getText().toString();
            String Mensaje = etMensaje.getText().toString();
            if(!(TextUtils.isEmpty(Asunto)) && (Asunto != "")){
                if(!(TextUtils.isEmpty(Mensaje)) && (Mensaje != "")){
                    msg = "ok!";
                }else{
                    msg = "Debe ingresar un texto en el mensaje";
                }
            }else{
                msg = "Debe completar todos los campos!";
            }
        }catch (Exception ex){
            msg = "Error: " + ex.getMessage();
        }
        return  msg;
    }

}