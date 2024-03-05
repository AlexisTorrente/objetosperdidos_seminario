package com.example.tp_grupo6_seminario.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    public UsuarioAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> usuarios) {
        super(context, resource, usuarios);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.item_usuario, parent, false);
        }

        Usuario usuario = getItem(position);

        if (usuario != null) {
            TextView nombreTextView = itemView.findViewById(R.id.nombreTextView);
            //TextView apellidoTextView = itemView.findViewById(R.id.apellidoTextView);
            //TextView dniTextView = itemView.findViewById(R.id.dniTextView);
            // Otros TextView según tus necesidades

            // Establecer los valores en los TextView
            nombreTextView.setText(usuario.getNombreUsuario());
            //apellidoTextView.setText(usuario.getApellido());
            //dniTextView.setText(usuario.getDni());
            // Establecer otros valores según tus necesidades
        }

        return itemView;
    }
}