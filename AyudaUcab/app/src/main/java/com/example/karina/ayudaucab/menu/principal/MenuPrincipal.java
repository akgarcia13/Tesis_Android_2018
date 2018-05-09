package com.example.karina.ayudaucab.menu.principal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.memoria.MemoriaJuego;
import com.example.karina.ayudaucab.menu.login.LoginActivity;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.quiz.AudioJuego;
import com.example.karina.ayudaucab.quiz.FotoJuego;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActualizarPerfil.OnFragmentInteractionListener,
        FotoJuego.OnFragmentInteractionListener,
        AudioJuego.OnFragmentInteractionListener,
        MemoriaJuego.OnFragmentInteractionListener,
        Principal.OnFragmentInteractionListener {
    private Usuario usuario_perfil = UsuarioLogeado.userLogin();

    private TextView menuPrincipalNivelJuego,textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =  navigationView.getHeaderView(0);

        menuPrincipalNivelJuego = (TextView) headerView.findViewById(R.id.menuPrincipalNivelJuego);
        if(usuario_perfil.getUs_nivel_juego() <2) {
            menuPrincipalNivelJuego.setText("Nivel " + 1);
        }else if(usuario_perfil.getUs_nivel_juego() >= 2 && usuario_perfil.getUs_nivel_juego() < 4  ) {
            menuPrincipalNivelJuego.setText("Nivel " + 2);
        }else if(usuario_perfil.getUs_nivel_juego() >= 4 && usuario_perfil.getUs_nivel_juego() < 6) {
            menuPrincipalNivelJuego.setText("Nivel " + 3);
        }else if(usuario_perfil.getUs_nivel_juego() >= 6 && usuario_perfil.getUs_nivel_juego() < 8) {
            menuPrincipalNivelJuego.setText("Nivel " + 4);
        }else if(usuario_perfil.getUs_nivel_juego() >= 8 && usuario_perfil.getUs_nivel_juego() < 10) {
            menuPrincipalNivelJuego.setText("Nivel " + 5);
        }else if(usuario_perfil.getUs_nivel_juego() >= 10 && usuario_perfil.getUs_nivel_juego() < 12) {
            menuPrincipalNivelJuego.setText("Nivel " + 6);
        }else if(usuario_perfil.getUs_nivel_juego() >= 12 && usuario_perfil.getUs_nivel_juego() < 14) {
            menuPrincipalNivelJuego.setText("Nivel " + 7);
        }else if(usuario_perfil.getUs_nivel_juego() >= 14 && usuario_perfil.getUs_nivel_juego() < 16) {
            menuPrincipalNivelJuego.setText("Nivel " + 8);
        }else if(usuario_perfil.getUs_nivel_juego() >= 16 && usuario_perfil.getUs_nivel_juego() < 17) {
            menuPrincipalNivelJuego.setText("Nivel " + 9);
        }else if(usuario_perfil.getUs_nivel_juego() >= 17) {
            menuPrincipalNivelJuego.setText("Nivel " + 10);
        }

        textView =(TextView)headerView.findViewById(R.id.textView);
        textView.setText("Hola! "+usuario_perfil.getUs_nombre());

        Fragment fragment = new Principal();
        getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
        //Bundle objetoEnviado = getIntent().getExtras();

        //objeto que viene desde el LOGIN

       /* if(objetoEnviado  != null)
        {
            usuario_perfil = objetoEnviado.getParcelable("usuario");
            if(usuario_perfil != null) {
                //Toast.makeText(getApplicationContext(), "Usuario : "+usuario_perfil.getUs_nombre_usuario(), Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("perfil_usuario_principal",usuario_perfil);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
            }else{
                finish();
            }


        }*/

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = new Principal();
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //BOTON CERRAR SESION
        if (id == R.id.action_settings) {

            SharedPreferences preferences =getSharedPreferences("credenciales",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            UsuarioLogeado.userLogOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    //MENU PRINCIPAL
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        Boolean fragmentoSeleccionado = false;
        if (id == R.id.nav_foto_juego) {
            // Handle the camera action
            fragment = new FotoJuego();
            fragmentoSeleccionado= true;
        } else if (id == R.id.nav_audio_juego) {
            fragment = new AudioJuego();
            fragmentoSeleccionado= true;
        } else if (id == R.id.nav_memoria) {
            fragment = new MemoriaJuego();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_perfil) {
            fragment = new ActualizarPerfil();
            fragmentoSeleccionado= true;
        } else if(id == R.id.cerrar_sesion_nav){
            SharedPreferences preferences =getSharedPreferences("credenciales",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            UsuarioLogeado.userLogOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            this.finish();
        }

        if(fragmentoSeleccionado)
        {

                /*if (usuario_perfil != null) {
                    TareaWSIniciarSesion tarea = new TareaWSIniciarSesion();
                   tarea.execute(
                            usuario_perfil.getUs_nombre_usuario().toString(),
                            usuario_perfil.getUs_password().toString(),
                            usuario_perfil.getUs_email().toString()

                    );
                }*/




                //SE MANDA EL USUARIO A ACTUALIZAR
            /*Bundle objetoEnviado = getIntent().getExtras();
            if (fragment instanceof  ActualizarPerfil)
            {
                if(objetoEnviado  != null)
                {
                    usuario_perfil = objetoEnviado.getParcelable("usuario");
                    if(usuario_perfil != null) {
                        Toast.makeText(getApplicationContext(), "Usuario : "+usuario_perfil.getUs_nombre_usuario(), Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("perfil_usuario",usuario_perfil);
                        fragment.setArguments(bundle);
                    }


                }




            }


            //SE MANDA EL USUARIO A FOTO JUEGO
            if (fragment instanceof  FotoJuego)
            {
                if(objetoEnviado  != null)
                {
                    usuario_perfil = objetoEnviado.getParcelable("usuario");
                    if(usuario_perfil != null) {
                        //Toast.makeText(getApplicationContext(), "Usuario : "+usuario_perfil.getUs_nombre_usuario(), Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("perfil_usuario_fotoJuego",usuario_perfil);
                        fragment.setArguments(bundle);
                    }
                }
            }


            //SE MANDA EL USUARIO A FOTO JUEGO
            if (fragment instanceof  AudioJuego)
            {
                if(objetoEnviado  != null)
                {
                    usuario_perfil = objetoEnviado.getParcelable("usuario");
                    if(usuario_perfil != null) {
                        //Toast.makeText(getApplicationContext(), "Usuario : "+usuario_perfil.getUs_nombre_usuario(), Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("perfil_usuario_AudioJuego",usuario_perfil);
                        fragment.setArguments(bundle);
                    }
                }
            }
            if (fragment instanceof  MemoriaJuego)
            {
                if(objetoEnviado  != null)
                {
                    usuario_perfil = objetoEnviado.getParcelable("usuario");
                    if(usuario_perfil != null) {
                        //Toast.makeText(getApplicationContext(), "Usuario : "+usuario_perfil.getUs_nombre_usuario(), Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("perfil_usuario_Memoria",usuario_perfil);
                        fragment.setArguments(bundle);
                    }
                }

            }*/

            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
