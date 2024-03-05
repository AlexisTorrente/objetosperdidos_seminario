package com.example.tp_grupo6_seminario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.net.URI;

import kotlin.text.UStringsKt;


public class ContactarSoporte extends AppCompatActivity{

    String MailSoporte = "gaston.sosa@alumnos.frgp.utn.edu.ar"; //Mail provisional, podemos crear uno
    EditText etAsunto, etMensaje;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactar_soporte);
        etAsunto = findViewById(R.id.editTextAsunto);
        etMensaje = findViewById(R.id.editTextMensaje);

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

                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
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
