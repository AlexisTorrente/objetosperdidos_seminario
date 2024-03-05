package com.example.tp_grupo6_seminario.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tp_grupo6_seminario.Dao.CajaObjetoDao;
import com.example.tp_grupo6_seminario.R;
import com.example.tp_grupo6_seminario.entidades.CajaObjeto;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class ubicacionCajasAdapter extends ArrayAdapter<CajaObjeto> {

    public List<CajaObjeto> ListaCajas;
    public TextView txtMunicipio;
    private Context context;
    GoogleMap gMap;
    public ubicacionCajasAdapter(Context contexto,TextView txtDescripcionCaja,List<CajaObjeto> UbicacionCajas,GoogleMap gMAP) {
        super(contexto, R.layout.fragment_ver_mapa,UbicacionCajas);
        this.ListaCajas = UbicacionCajas;
        this.txtMunicipio = txtDescripcionCaja;
        this.gMap = gMAP;
        context = contexto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.list_item_ubicaciones_cajas,null);

        LinearLayout linearLayout = (LinearLayout) item.findViewById(R.id.linearLayoutCaja);

        TextView txtNombreMunicipio = (TextView) item.findViewById(R.id.txtNombreMunicipio);
        TextView txtNombreLocalidad = (TextView) item.findViewById(R.id.txtLocalidad);

        txtNombreMunicipio.setText(getItem(position).getDescripcion());
        txtNombreLocalidad.setText(getItem(position).getLocalidad().getNombre());
        Log.i("INFO","Municipio: " +txtNombreMunicipio.toString()+ "/ Localidad: " +txtNombreLocalidad.toString());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    CajaObjetoDao cajaObjetoDao = new CajaObjetoDao(context,txtMunicipio,gMap);
                    cajaObjetoDao.execute("OBTENER_CAJA_BYLOCALIDAD",getItem(position).getLocalidad().getId());

                }catch (Exception e){
                    Log.i("ERROR_OBJ",e.getMessage());
                }
            }
        });

        return item;
    }

}
