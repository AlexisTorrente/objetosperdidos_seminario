package com.example.tp_grupo6_seminario;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.example.tp_grupo6_seminario.adapter.datosUsuarioViewModel;
import com.example.tp_grupo6_seminario.entidades.Usuario;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_grupo6_seminario.databinding.ActivityNavigationDrawerBinding;

public class activity_navigation_drawer extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;

    private datosUsuarioViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(datosUsuarioViewModel.class);

        Usuario usr = viewModel.getUsuario().getValue();

        if(usr == null){
            Bundle bundle = new Bundle();
            usr = (Usuario) getIntent().getSerializableExtra("usuario_session");
            viewModel.setUsuario(usr);
        }

        viewModel.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                // Actualiza el Bundle con el usuario
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario_session", usuario);

                // Puedes almacenar el bundle actualizado en la actividad si es necesario
                getIntent().putExtras(bundle);
            }
        });

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = binding.navView;
        Menu menu = navigationView.getMenu();
        setSupportActionBar(binding.appBarActivityNavigationDrawer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.altaObjetoPerdidoFragment, R.id.inicioFragment,
                    R.id.altaObjetoEncontradoFragment, R.id.chatFragment, R.id.bajaUsuarioFragment, R.id.ContactarSoporteFragment, R.id.mensajeriaInternaFragment, R.id.perfilUsuarioFragment, R.id.reportesFragment, R.id.VerMapaFragment,R.id.MisPublicacionesFragment,R.id.panelAdministracionFragment)
                .setOpenableLayout(drawer)
                .build();

        usr = viewModel.getUsuario().getValue();

        if(usr.getTipoUsuario().getId() != 1){
            menu.removeItem(R.id.altaObjetoPerdidoFragment);
            menu.removeItem(R.id.altaObjetoEncontradoFragment);
            menu.removeItem(R.id.mensajeriaInternaFragment);
            menu.removeItem(R.id.chatFragment);
            menu.removeItem(R.id.ContactarSoporteFragment);
            menu.removeItem(R.id.MisPublicacionesFragment);
            menu.removeItem(R.id.notificacionesFragment);
        } else if (usr.getTipoUsuario().getId() == 1) {
            menu.removeItem(R.id.panelAdministracionFragment);
        }


        NavController navController = Navigation.findNavController(activity_navigation_drawer.this, R.id.nav_host_fragment_content_activity_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.cerrar_sesion) {
                    Intent i = new Intent(activity_navigation_drawer.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // para que no se pueda volver atrás
                    viewModel.setUsuario(null);
                    startActivity(i);
                    Log.d("Navigation_Drawer","PASO POR CERRAR SESION");
                    finish();
                    return true;
                }else {
                    NavController navController = Navigation.findNavController(activity_navigation_drawer.this, R.id.nav_host_fragment_content_activity_navigation_drawer);
                    navController.navigate(id);
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        MenuItem perfilUsr = menu.findItem(R.id.menu_item_perfilUsuario);
        perfilUsr.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Crear un Bundle y agregar el usuario

                // Navegar al fragmento de PerfilUsuarioFragment con el Bundle
                NavController navController = Navigation.findNavController(activity_navigation_drawer.this, R.id.nav_host_fragment_content_activity_navigation_drawer);
                navController.navigate(R.id.perfilUsuarioFragment);

                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
