package com.example.tp_grupo6_seminario.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.Mensaje;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.List;

public class MensajeAdapter extends ArrayAdapter<Mensaje> {

    private Context context;
    private List<Mensaje> mensajesList;   // int resource, @NonNull List<Usuario> usuarios
    public MensajeAdapter(@NonNull Context context,@NonNull List<Mensaje> mensajesList)
    {
        super(context, 0, mensajesList);
        this.mensajesList = mensajesList;
        this.context = context;

    }

            /*
    public MensajeAdapter(Context context, List<Mensaje> mensajesList)
    {
        this.context = context;
        this.mensajesList = mensajesList;

    }
    */

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.card_view_mensajes, null);
            //itemView = inflater.inflate(R.layout.item_mensaje, parent, false);
        }

        Mensaje mensaje = getItem(position);

        if (mensaje != null) {
            TextView mensajeTextView = itemView.findViewById(R.id.mensajeMensaje);
            TextView usuario = itemView.findViewById(R.id.nombreMensaje);
            TextView hora = itemView.findViewById(R.id.horaMensaje);
            //TextView dniTextView = itemView.findViewById(R.id.dniTextView);
            // Otros TextView según tus necesidades

            // Establecer los valores en los TextView
            mensajeTextView.setText(mensaje.getMsj());
            usuario.setText(mensaje.getDesde());
            hora.setText(mensaje.getHora().toString());
            //apellidoTextView.setText(usuario.getApellido());
            //dniTextView.setText(usuario.getDni());
            // Establecer otros valores según tus necesidades
        }

        return itemView;
    }
}
