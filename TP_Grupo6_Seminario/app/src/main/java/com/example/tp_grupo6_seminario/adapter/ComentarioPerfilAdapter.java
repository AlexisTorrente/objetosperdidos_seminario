package com.example.tp_grupo6_seminario.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.ComentarioPerfil;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ComentarioPerfilAdapter extends ArrayAdapter<ComentarioPerfil> {

    private List<ComentarioPerfil> comentarios;
    private Context context;
    private datosUsuarioViewModel viewModel;

    public ComentarioPerfilAdapter(Context context, List<ComentarioPerfil> objetos) {
        super(context, R.layout.fragment_perfil_usuario, objetos);
        this.comentarios = objetos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.list_item_comentarios_perfil,null);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(datosUsuarioViewModel.class);

        TextView tvUsuario = (TextView) item.findViewById(R.id.tvNombreUsuario);
        TextView tvComentario = (TextView) item.findViewById(R.id.tvComentario);

        tvUsuario.setText(getItem(position).getUsuarioComenterio().getNombreUsuario());
        tvComentario.setText(getItem(position).getComenterio());

        Usuario usr = (Usuario) getItem(position).getUsuarioComenterio();
        Usuario usuarioLogeado = viewModel.getUsuario().getValue();

            tvUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(usr.getId() != usuarioLogeado.getId()) {

                    Usuario usr = (Usuario) getItem(position).getUsuarioComenterio();
                    Bundle args = new Bundle();
                    args.putSerializable("usuario_perfilSeleccionado", usr);
                    NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_activity_navigation_drawer);
                    // Navegar al fragmento de destino
                    navController.navigate(R.id.perfilUsuarioSeleccionadoFragment, args);

                    }else {
                        Bundle args = new Bundle();
                        args.putSerializable("usuario_session", usuarioLogeado);
                        NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_activity_navigation_drawer);
                        navController.navigate(R.id.perfilUsuarioFragment,args);
                    }
                }
            });




        return item;
    }

    public void agregarComentarios(ArrayList<ComentarioPerfil> nuevosComentarios) {
        comentarios.addAll(nuevosComentarios);
        notifyDataSetChanged(); // Notificar al adaptador de los cambios
    }
}
