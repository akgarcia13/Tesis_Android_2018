package com.example.karina.ayudaucab.menu.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.codificacion.Codificacion;
import com.example.karina.ayudaucab.menu.principal.MenuPrincipal;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;
import com.example.karina.ayudaucab.validacion.InternetExcepcion;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText usuario,email,password;
    private Button inicio_sesion, ir_registrar, ir_recuperar;

    private AwesomeValidation awesomeValidation;

    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usuario = (EditText) view.findViewById(R.id.etUsernameLogin);
        email = (EditText) view.findViewById(R.id.etUsernameLogin);
        password = (EditText) view.findViewById(R.id.etPasswordLogin);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(usuario, "[a-z0-9ü][a-z0-9ü.]{3,16}$+|"+ Patterns.EMAIL_ADDRESS,"Entre 3 a 16 carateres entre letras y numeros" +
                " o ingrese email" );

        awesomeValidation.addValidation(password,
                "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{6,}",
                "De 6 a 8 caracteres entre mayùsculas, minùsculas, nùmeros y especiales como *.");
        inicio_sesion = (Button) view.findViewById(R.id.bLogin);

        ir_registrar= (Button) view.findViewById(R.id.bLoginRegistrar);
        ir_recuperar= (Button) view.findViewById(R.id.bLoginRecuperarPassword);
        //request = Volley.newRequestQueue(getContext());

        context = view.getContext();

        ir_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).selectTab(1);
            }
        });

        ir_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).selectTab(2);
            }
        });


        inicio_sesion.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
             if(InternetExcepcion.isOnline(context) ) {
                 // do something
                 if (awesomeValidation.validate()) {
                     progreso = new ProgressDialog(getContext());
                     progreso.setMessage("Iniciando por favor espere...");
                     progreso.setCancelable(false);
                     showpDialog();
                     //aqui pasamos los datos para iniciar sesion
                     TareaWSIniciarSesion tarea = new TareaWSIniciarSesion();
                     tarea.execute(
                             usuario.getText().toString(),
                             password.getText().toString(),
                             email.getText().toString()

                     );
                     awesomeValidation.clear();
                     // Here, we are sure that form is successfully validated. So, do your stuffs now...
                     // Toast.makeText(getActivity(), "Se ha registrado con exito: sexo: "+sexo, Toast.LENGTH_LONG).show();
                 }

             }else {

                InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
            }
//Para pruebas

             /*   usuario_perfil = new Usuario();
                usuario_perfil.setUs_id(7);
                usuario_perfil.setUs_nombre_usuario("us_nombre_usuario");
                usuario_perfil.setUs_nombre("us_nombre");
                usuario_perfil.setUs_apellido("us_apellido");
                usuario_perfil.setUs_fecha_nacimiento("us_fecha_nacimiento");
                usuario_perfil.setUs_genero('M'); //cambiar a char
                usuario_perfil.setUs_email("us_email");
                usuario_perfil.setUs_password("us_password");
                usuario_perfil.setUs_nivel_juego(0);;*/
                //Intent intent = new Intent(context, MenuPrincipal.class);

                /*Bundle bundle = new Bundle();

                bundle.putParcelable("usuario",usuario_perfil);
                intent.putExtras(bundle);*/
                //UsuarioLogeado usuarioLogeado = new UsuarioLogeado(usuario_perfil);
                //guardarPreferencias();

                //startActivity(intent);
                //getActivity().finish();

            }
        });

        return view;
    }

    public void guardarPreferencias(){

        SharedPreferences preferences = context.getSharedPreferences("credenciales",
                context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("us_id", usuario_perfil.getUs_id());
        editor.putString("us_nombre_usuario", usuario_perfil.getUs_nombre_usuario());
        editor.putString("us_nombre", usuario_perfil.getUs_nombre());
        editor.putString("us_apellido", usuario_perfil.getUs_apellido());
        editor.putString("us_fecha_nacimiento", usuario_perfil.getUs_fecha_nacimiento());
        editor.putString("us_genero",String.valueOf( usuario_perfil.getUs_genero()));
        editor.putString("us_email", usuario_perfil.getUs_email());
        editor.putString("us_password", usuario_perfil.getUs_password());
        editor.putInt("us_nivel_juego", usuario_perfil.getUs_nivel_juego());

        editor.commit();
    }

    public Usuario usuario_perfil;
    ProgressDialog progreso;
    //Barra de progresos
    private void showpDialog() {
        if (!progreso.isShowing())
            progreso.show();
    }

    private void hidepDialog() {
        if (progreso.isShowing())
            progreso.dismiss();
    }

    private class TareaWSIniciarSesion extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("https://ayudaucab.herokuapp.com/api/usuarios/login");

            post.setHeader("content-type", "application/json");


            try
            {


                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("us_nombre_usuario", Codificacion.Encriptar(params[0]));

                dato.put("us_password", Codificacion.Encriptar(params[1]));
                dato.put("us_email", Codificacion.Encriptar(params[2]));

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity); // lo que retorna el webservice

                HttpResponse resp = httpClient.execute(post);

                String respStr = EntityUtils.toString(resp.getEntity());
                //Objeto de la base de datos
                JSONObject respJSON = new JSONObject(respStr);
                usuario_perfil = new Usuario();
                usuario_perfil.setUs_id(Integer.parseInt(respJSON.getString("us_id")));
                usuario_perfil.setUs_nombre_usuario(Codificacion.Desencriptar(respJSON.getString("us_nombre_usuario")));
                usuario_perfil.setUs_nombre(Codificacion.Desencriptar(respJSON.getString("us_nombre")));
                usuario_perfil.setUs_apellido(Codificacion.Desencriptar(respJSON.getString("us_apellido")));
                usuario_perfil.setUs_fecha_nacimiento(respJSON.getString("us_fecha_nacimiento"));
                usuario_perfil.setUs_genero(respJSON.getString("us_genero").charAt(0)); //cambiar a char
                usuario_perfil.setUs_email(Codificacion.Desencriptar(respJSON.getString("us_email")));
                usuario_perfil.setUs_password(Codificacion.Desencriptar(respJSON.getString("us_password")));
                usuario_perfil.setUs_nivel_juego(Integer.parseInt(respJSON.getString("us_nivel_juego")));


                if(usuario_perfil.getUs_id() == -1)
                     resul= false; // contrasena invalida o correo, nombre de uusuario no registrado
            }
            catch(Exception ex)
            { //Si no hay internet
                Log.e("ServicioRest","Error!", ex);
                System.out.println("Error: "+ex.getMessage().toString());
                resul = false;
                hidepDialog();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                System.out.println("Correcto");
                Toast.makeText(getActivity(), "Bienvenido "+usuario_perfil.getUs_nombre_usuario(),
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, MenuPrincipal.class); //Entra al menu principal
                UsuarioLogeado usuarioLogeado = new UsuarioLogeado(usuario_perfil);
                guardarPreferencias();
                /*Bundle bundle = new Bundle();

                bundle.putParcelable("usuario",usuario_perfil);
                intent.putExtras(bundle);*/
                startActivity(intent);
                hidepDialog();
                getActivity().finish();
            }else{
                hidepDialog();
                System.out.println("Usuario no registrado o mala combinacion de contraseña ");
                Toast.makeText(getActivity(), "Usuario no registrado o mala combinacion de contraseña", Toast.LENGTH_LONG).show();
            }
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
