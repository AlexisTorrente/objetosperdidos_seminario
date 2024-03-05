package com.example.tp_grupo6_seminario;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_grupo6_seminario.adapter.MensajeAdapter;
import com.example.tp_grupo6_seminario.entidades.Mensaje;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Usuario selectedUser;
    private List<Mensaje> mensajesList;
    private MensajeAdapter mensajeAdapter;

    private EditText editTextMensaje;
    private ImageButton btnEnviarMensaje;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Obtiene el usuario seleccionado de los extras
        //selectedUser = getIntent().getParcelableExtra("selectedUser");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedUser = (Usuario) bundle.getSerializable("selectedUser");
            if (selectedUser != null) {
                // Haz lo que necesites con el objeto recibido
                Log.d("ChatActivity", "Nombre: " + selectedUser.getNombre() + ", ID: " + selectedUser.getId());
            }
        }

        // Inicializa la lista de mensajes
        mensajesList = new ArrayList<>();

        // Configura el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mensajeAdapter = new MensajeAdapter(mensajesList);
        recyclerView.setAdapter(mensajeAdapter);

        // Configura el EditText y el botón para enviar mensajes
        editTextMensaje = findViewById(R.id.editTextMensaje);
        btnEnviarMensaje = findViewById(R.id.btnEnviarMensaje);

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(selectedUser);
            }
        });

        // Puedes personalizar la interfaz según tus necesidades, por ejemplo, mostrando el nombre del usuario en la barra de título
        //getSupportActionBar().setTitle(selectedUser.getNombre());
    }

    private void enviarMensaje(Usuario selectedUser) {
        String contenidoMensaje = editTextMensaje.getText().toString().trim();
        if (!contenidoMensaje.isEmpty()) {
            // Crea un nuevo mensaje y añádelo a la lista
            Date fechaActual = new Date(System.currentTimeMillis());
            Mensaje mensaje = new Mensaje(contenidoMensaje, "Remitente actual", selectedUser.getNombre(), fechaActual);
            mensajesList.add(mensaje);
            mensajeAdapter.notifyDataSetChanged();

            // Limpia el EditText después de enviar el mensaje
            editTextMensaje.getText().clear();
        }
    }
    */
}