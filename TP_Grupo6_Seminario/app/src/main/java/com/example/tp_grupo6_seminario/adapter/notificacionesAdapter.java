package com.example.tp_grupo6_seminario.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tp_grupo6_seminario.Dao.CajaObjetoDao;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.CajaObjeto;
import com.example.tp_grupo6_seminario.entidades.Notificacion;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class notificacionesAdapter extends ArrayAdapter<Notificacion> {
    public List<Notificacion> listaNotificaciones;
    private Context context;
    public notificacionesAdapter(Context contexto, List<Notificacion>listaNotificaciones) {
        super(contexto, R.layout.fragment_ver_mapa,listaNotificaciones);
        this.listaNotificaciones = listaNotificaciones;
        context = contexto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.list_item_notificaciones,null);

        TextView txtTitulo = (TextView) item.findViewById(R.id.txtTitulo);
        TextView txtDescripcion = (TextView) item.findViewById(R.id.txtDescripcion);

        txtTitulo.setText(getItem(position).getDescripcion());
        txtDescripcion.setText(getItem(position).getLocalidad().getNombre());

        Log.i("NOTIFICACION","Titulo: " +txtTitulo.toString()+ "/ Descripcion: " +txtDescripcion.toString());

        return item;
    }

}
