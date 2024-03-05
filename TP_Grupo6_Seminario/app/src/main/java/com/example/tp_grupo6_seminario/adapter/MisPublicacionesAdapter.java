package com.example.tp_grupo6_seminario.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tp_grupo6_seminario.Dao.CajaObjetoDao;
import com.example.tp_grupo6_seminario.Dao.PublicacionDao;
import com.example.tp_grupo6_seminario.Dao.UsuarioDao;
import com.example.tp_grupo6_seminario.MisPublicacionesFragment;
import com.example.tp_grupo6_seminario.activity_navigation_drawer;
import com.example.tp_grupo6_seminario.entidades.Publicacion;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.Usuario;

import java.util.List;

public class MisPublicacionesAdapter extends ArrayAdapter<Publicacion> {
    private  Context context;
    private List<Publicacion> ListaMisPublicaciones;
    private NavController NavController;
    public MisPublicacionesAdapter(Context contexto, List<Publicacion> listaMisPublicaciones,NavController navController) {
        super(contexto, R.layout.list_item_mispublicaciones, listaMisPublicaciones);
        context = contexto;
        ListaMisPublicaciones = listaMisPublicaciones;
        NavController = navController;
        Log.i("INFO_MISPUBLI","Llegue a MisPublicacionesAdapter");

    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        try{
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item_mispublicaciones,null);
            
            Log.i("INFO_MISPUBLI","llegue al getView");

            ImageView imageViewImagenP = (ImageView) item.findViewById(R.id.imageViewImagenP);
            TextView txtTituloPublicacion = (TextView) item.findViewById(R.id.txtTituloPublicacion);
            TextView txtInfoExtra = (TextView) item.findViewById(R.id.txtInfoExtra);
            Button btnEditarPublicacion = (Button) item.findViewById(R.id.btnEditarMiPublicacion);
            Button btnEliminarPublicacion = (Button) item.findViewById(R.id.btnEliminarMiPublicacion);
            TextView tvRecuperado = (TextView) item.findViewById(R.id.txtRecuperado);

            txtTituloPublicacion.setText(getItem(position).getTitulo());
            txtInfoExtra.setText(getItem(position).getTipoPublicacion().getDescripcion());
            //imageViewImagenP.setImageResource();

            Log.i("INFO","Titulo: " +txtTituloPublicacion.toString()+ "/ TipoEvento: " +txtInfoExtra.toString());

            Publicacion ItemPrincipal = getItem(position);

            if(ItemPrincipal.isRecuperado() == true){
                btnEditarPublicacion.setVisibility(View.GONE);
                btnEliminarPublicacion.setVisibility(View.GONE);
                tvRecuperado.setVisibility(View.VISIBLE);
            }
            btnEditarPublicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Log.i("INFO_MISPUBLI","Llegue al OnClick()");

                        Publicacion publicacion = getItem(position);
                        if (publicacion != null) {

                           /* datosPublicacionViewModel DatosPublicacion = new datosPublicacionViewModel();
                            DatosPublicacion.setPublicacion(publicacion);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("NASHE", 5);

                            MisPublicacionesFragment fragment = new MisPublicacionesFragment();
                            fragment.setArguments(bundle);

                            NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_activity_navigation_drawer);
                            navController.navigate(R.id.editarPublicacionFragment);*/
                            Log.i("INFO_MISPUBLI","Publicacion no nula, voy a EditarPublicacion");

                            PublicacionDao publicacionDao = new PublicacionDao(context);
                            //publicacionDao.setPb(pb);
                            //pb.setVisibility(View.VISIBLE);
                            publicacionDao.execute("OBTENER_PUBLICACION_POR_ID", Integer.parseInt(String.valueOf(ItemPrincipal.getId())), "EDITAR");



                        }else{
                            Log.e("ERROR_MisPublicacionesAdapter", "La Publicacion es nula");

                        }

                    }catch (Exception e){
                        Log.i("ERROR_MisPublicacionesAdapter",e.getMessage());
                    }
                }
            });

            btnEliminarPublicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idMiPublicacion = ItemPrincipal.getId();

                    mostrarPopupConfirmacion(idMiPublicacion);
                }
            });
            return item;

        }catch (Exception ex){
            Log.i("ERROR_MisPublicacionesAdapter","excepción: "+ex.getMessage());
            return null;
        }
    }



    private void mostrarPopupConfirmacion(int idMiPublicacion) {

        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_confirmar_recuperado, null);

        Button btnPopUpRecuperado = popupView.findViewById(R.id.btnPopUpRecuperado);
        Button btnPopUpCancelar = popupView.findViewById(R.id.btnPopUpCancelar);


        // Crear un AlertDialog para mostrar el popup
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogStyle);
        builder.setView(popupView);

        // Crear el AlertDialog
        final AlertDialog dialog = builder.create();

        // Mostrar el popup
        dialog.show();

        btnPopUpRecuperado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("INFO_MISPUBLI","Llegue al OnClick() de btnPopUpRecuperado");

                if(idMiPublicacion > -1){
                    Log.i("INFO_MISPUBLI","ID_Püblicacion: "+idMiPublicacion);
                }else{
                    Log.i("INFO_MISPUBLI","ID_Püblicacion devolvío cualquier cosa");

                }

                PublicacionDao publicacionDao = new PublicacionDao(context);
                publicacionDao.execute("RECUPERADO",idMiPublicacion);

                dialog.dismiss();
            }
        });

        btnPopUpCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
