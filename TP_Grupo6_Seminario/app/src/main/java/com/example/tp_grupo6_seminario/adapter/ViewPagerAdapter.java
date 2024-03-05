package com.example.tp_grupo6_seminario.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tp_grupo6_seminario.objetosEncontradosFragment;
import com.example.tp_grupo6_seminario.objetosPerdidosFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        // Agrega tus fragmentos a fragmentList aquí
        fragmentList.add(new objetosPerdidosFragment());
        fragmentList.add(new objetosEncontradosFragment());
        // ... agrega los demás fragmentos
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Devuelve el fragmento existente si ya ha sido creado
        if (fragmentList.size() > position) {
            return fragmentList.get(position);
        }

        // Crea y devuelve un nuevo fragmento si aún no existe
        switch (position) {
            case 0:
                return new objetosPerdidosFragment();
            case 1:
                return new objetosEncontradosFragment();
            // ... agrega los demás casos
            default:
                return new objetosPerdidosFragment(); // Ajusta según tus necesidades
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Reemplaza con el número real de fragmentos
    }




}
