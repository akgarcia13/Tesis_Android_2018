package com.example.karina.ayudaucab.menu.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.menu.principal.MenuPrincipal;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener
        ,RegistroFragment.OnFragmentInteractionListener
        ,FargotPasswordFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        usuarioLogeadoMain();
    }

    public void usuarioLogeadoMain (){
        SharedPreferences preferences = getSharedPreferences("credenciales",
                Context.MODE_PRIVATE);
        String nombre  = preferences.getString("us_nombre_usuario","null");
        if (!nombre.equalsIgnoreCase("null")){
            Usuario usuario = new Usuario();
            usuario.setUs_id(preferences.getInt("us_id",-1));
            usuario.setUs_nombre_usuario(preferences.getString("us_nombre_usuario","null"));
            usuario.setUs_nombre(preferences.getString("us_nombre","null"));
            usuario.setUs_apellido(preferences.getString("us_apellido","null"));
            usuario.setUs_fecha_nacimiento(preferences.getString("us_fecha_nacimiento","null"));
            usuario.setUs_genero(preferences.getString("us_genero","null").charAt(0));
            usuario.setUs_email(preferences.getString("us_email","null"));
            usuario.setUs_password(preferences.getString("us_password","null"));
            usuario.setUs_nivel_juego(preferences.getInt("us_nivel_juego",-1));

            UsuarioLogeado user = new UsuarioLogeado(usuario);


            Intent menuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(menuPrincipal);
        }
    }

    public void selectTab(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        int posicion = mViewPager.getCurrentItem();

       if (posicion == 0){
            super.onBackPressed();
       }else if (posicion ==1){
            selectTab(0);
       }else if (posicion == 2){
           selectTab(0);
       }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
       /* public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }*/
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = null;
            //PlaceholderFragment fragment = new PlaceholderFragment();
            switch (sectionNumber){
                case 1:
                    fragment = new LoginFragment();
                    break;
                case 2:
                    fragment = new RegistroFragment();
                    break;
                case 3:
                    fragment = new FargotPasswordFragment();
                    break;

            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
