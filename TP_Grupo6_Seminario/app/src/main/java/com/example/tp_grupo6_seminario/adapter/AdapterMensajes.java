package com.example.tp_grupo6_seminario.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    List<Mensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m)
    {
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getMensaje().setText(listMensaje.get(position).getMsj());
        //holder.getMensaje().setText(listMensaje.get(position).getMsj());
        // holder.getHora().setText(listMensaje.get(position).getHora());
    }

    @Override
    public int getItemCount() {
       return listMensaje.size();
    }
}
