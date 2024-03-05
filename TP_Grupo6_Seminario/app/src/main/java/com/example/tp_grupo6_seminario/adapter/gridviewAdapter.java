package com.example.tp_grupo6_seminario.adapter;


import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.example.tp_grupo6_seminario.adapter.FTPUploader.obtenerPrimeraImagen;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.ObjetoDetalleFragment;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.ComentarioPerfil;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.entidades.Usuario;
import com.example.tp_grupo6_seminario.inicioFragment;

import java.io.File;
import java.util.List;

public class gridviewAdapter extends ArrayAdapter<Publicacion> {

    private Context context;
    int idItem;
    public String RutaImagen;
    public gridviewAdapter(Context context, List<Publicacion> objetos) {
        super(context, R.layout.fragment_inicio, objetos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.gridview_item,null);

        ImageView imageView = (ImageView) item.findViewById(R.id.imagenGridview);
        TextView tvTitulo = (TextView) item.findViewById(R.id.tvTituloGridview);
        LinearLayout linearLayout = (LinearLayout) item.findViewById(R.id.linearLayoutItem);

        String ImagenEnBD = getItem(position).getImagen().toString();
        int IdPublicacion = getItem(position).getId();

        Log.i("INFO_MISPUBLI"," Voy a ir a CargarImagen.");

        PublicacionDao PDAOImagen = new PublicacionDao(context, imageView);
        PDAOImagen.execute("CargarImagen", IdPublicacion, ImagenEnBD);

        tvTitulo.setText(getItem(position).getTitulo());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Publicacion publi = (Publicacion) getItem(position);
                Bundle args = new Bundle();
                args.putSerializable("Publicacion", publi);

                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_activity_navigation_drawer);

                navController.navigate(R.id.ObjetoDetalleFragment, args);

            }
        });


        return item;
    }


}


