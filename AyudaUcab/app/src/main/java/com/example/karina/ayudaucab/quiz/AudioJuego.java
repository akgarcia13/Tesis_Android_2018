package com.example.karina.ayudaucab.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.menu.principal.MenuPrincipal;
import com.example.karina.ayudaucab.objeto.Audio;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;
import com.example.karina.ayudaucab.validacion.InternetExcepcion;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioJuego.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioJuego#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioJuego extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AudioJuego() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment AudioJuego.
     */
    // TODO: Rename and change types and number of parameters
    /*public static AudioJuego newInstance(String param1, String param2) {
        AudioJuego fragment = new AudioJuego();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/


// audio JUEGO peticion de imagen para inicializar el audio
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /*Bundle bundle = this.getArguments();
        if(bundle != null){
            perfil_usuario = bundle.getParcelable("perfil_usuario_AudioJuego");

        }*/

        //se hace la peticion

        //TareaWSObtenerAudio  tarea = new TareaWSObtenerAudio ();
        //tarea.execute();

    }

    private TextView texto1,texto2,respuesta1TEXT,respuesta2TEXT,respuesta3TEXT;
    private Button audioPrincipal;
    private ImageView respuesta1,respuesta2,respuesta3;

    private String respuestaCorrecta,categoria;
    private  int intento=2;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_audio_juego, container, false);
        audioPrincipal = (Button) view.findViewById(R.id.principalAudioJuego);
        respuesta1 = (ImageView) view.findViewById(R.id.pregunta1AudioJuego);
        respuesta2 = (ImageView) view.findViewById(R.id.pregunta2AudioJuego);
        respuesta3 = (ImageView) view.findViewById(R.id.pregunta3AudioJuego);
        texto1 = (TextView) view.findViewById(R.id.preguntaAudioJuego);
        texto2 = (TextView) view.findViewById(R.id.intentoAudioJuego);
        context = view.getContext();
        request = Volley.newRequestQueue(getContext());
        cargarAudio();
        return view;
    }


    public int numeroAleatorio(int maximo){
        int numeroAleatorio;
        numeroAleatorio = (int) (Math.random() * maximo) ;
        if (numeroAleatorio < 0) {
            numeroAleatorio = numeroAleatorio * -1;
        }
        if (numeroAleatorio == 0) {
            numeroAleatorio = 1;
        }
        return numeroAleatorio;
    }
    //Reproduce Audio
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private void playMp3 (byte[] mp3SoundByteArray){
        try {
            File tempMp3 = File.createTempFile("temp","mp3",context.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer.reset();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException ex){
            String s = ex.toString();
            ex.printStackTrace();
        }
    }
    private String preguntasAnimal [] = {
            "Buho", "Burro", "Caballo","Cabra",
            "Elefante","Gato","Pajaros","Pato",
            "Perro","Vaca"

    };

    private void cargarImagenPregunta(ImageView carta){

        if(String.valueOf(carta.getTag()).equalsIgnoreCase("Buho")){
            carta.setImageResource(R.drawable.ic_buho);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Burro")){
            carta.setImageResource(R.drawable.ic_burro);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Caballo")){
            carta.setImageResource(R.drawable.ic_caballo);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Cabra")){
            carta.setImageResource(R.drawable.ic_oveja);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Elefante")){
            carta.setImageResource(R.drawable.ic_elefante);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Gato")){
            carta.setImageResource(R.drawable.ic_gato);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Pajaros")){
            carta.setImageResource(R.drawable.ic_aves);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Pato")){
            carta.setImageResource(R.drawable.ic_pato);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Perro")){
            carta.setImageResource(R.drawable.ic_perro);
        }else if(String.valueOf(carta.getTag()).equalsIgnoreCase("Vaca")){
            carta.setImageResource(R.drawable.ic_vaca);
        }

    }
    //RESPUESTA INCORRECTA IMAGEN DE OPS
    private void respuestaIncorrecta(){
        intento--;

        texto2.setText("Intentos: " + intento);

        if(intento > 0) {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            alertDialogBuilder
                    .setView(inflater.inflate(R.layout.incorrecto, null))
                    .setMessage(" !Ops!\n Tienes: " + intento + " Intento")
                    .setCancelable(false)

                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            mediaPlayer.stop();
                        }
                    })
            ;

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        salir();

    }


    //Respuesta Incorrecta Salir o volver a Intentarlo
    private void respuestaIncorrectaFinal(){
        // intento--;

        texto2.setText("Intentos: " + intento);


        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialogBuilder
                .setView(inflater.inflate(R.layout.incorrecto,null))
                .setMessage(" !Ops!\n Tienes: "+intento+" Intento")
                .setCancelable(false)
                .setPositiveButton("Vuelve a intentarlo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                recargarFragment();
                                dialogInterface.dismiss();
                                mediaPlayer.stop();
                            }
                        }

                )
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, MenuPrincipal.class);
                                startActivity(intent);
                                mediaPlayer.stop();
                            }
                        }
                )
        ;
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }

    //RESPUESTA CORRECTA IMAGEN DE GREAT Y AUMENTA DE NIVEL
    private void respuestaCorrecta(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialogBuilder
                .setView(inflater.inflate(R.layout.correcto,null))
                .setMessage(" !!!Sigue así!!! ")
                .setCancelable(false)
                .setPositiveButton("Siguiente Reto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(InternetExcepcion.isOnline(context)){
                                    progreso = new ProgressDialog(getContext());
                                    progreso.setMessage("Subiste de nivel!!! ...");
                                    progreso.setCancelable(false);
                                    showpDialog();

                                    TareaWSAumentarNivelAudioJuego tarea1 = new TareaWSAumentarNivelAudioJuego();
                                    tarea1.execute(
                                            perfil_usuario.getUs_nombre_usuario().toString(),
                                            perfil_usuario.getUs_nombre().toString(),
                                            perfil_usuario.getUs_apellido().toString(),
                                            perfil_usuario.getUs_fecha_nacimiento().toString(),
                                            //String.valueOf(perfil_usuario.getUs_genero()) ,
                                            perfil_usuario.getUs_email().toString(),
                                            perfil_usuario.getUs_password().toString(),
                                            "si"
                                    );

                                }else {

                                    InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
                                    Intent intent = new Intent(getContext(), MenuPrincipal.class);
                                        /*Bundle bundle = new Bundle();

                                        bundle.putParcelable("usuario",perfil_usuario);
                                        intent.putExtras(bundle);

                                        System.out.println("Problemas Internos");
                                        Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();*/
                                    startActivity(intent);
                                }
                                //recargarFragment();
                                dialogInterface.dismiss();
                            }
                        }

                )
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(InternetExcepcion.isOnline(context)){
                                    progreso = new ProgressDialog(getContext());
                                    progreso.setMessage("Subiste de nivel!!! ...");
                                    progreso.setCancelable(false);
                                    showpDialog();
                                    TareaWSAumentarNivelAudioJuego tarea1 = new TareaWSAumentarNivelAudioJuego();
                                    tarea1.execute(
                                            perfil_usuario.getUs_nombre_usuario().toString(),
                                            perfil_usuario.getUs_nombre().toString(),
                                            perfil_usuario.getUs_apellido().toString(),
                                            perfil_usuario.getUs_fecha_nacimiento().toString(),
                                            //String.valueOf(perfil_usuario.getUs_genero()) ,
                                            perfil_usuario.getUs_email().toString(),
                                            perfil_usuario.getUs_password().toString(),
                                            "no"
                                    );

                                }else {

                                    InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
                                    Intent intent = new Intent(getContext(), MenuPrincipal.class);
                                        /*Bundle bundle = new Bundle();

                                        bundle.putParcelable("usuario",perfil_usuario);
                                        intent.putExtras(bundle);

                                        System.out.println("Problemas Internos");
                                        Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();*/
                                    startActivity(intent);
                                }
                                dialogInterface.dismiss();
                               /* Intent intent = new Intent(context, MenuPrincipal.class);
                                startActivity(intent);
                                mediaPlayer.stop();*/
                            }
                        }
                )
        ;
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    public void recargarFragment(){
        Fragment fragment = new AudioJuego();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
        mediaPlayer.stop();
    }


    public void salir(){

        if(intento  <= 0){
            respuestaIncorrectaFinal();

        }
    }

    private Audio audioJuego;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    public void cargarAudio(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando Audio ...");
        progreso.setCancelable(false);
        showpDialog();

        String url ="https://ayudaucab.herokuapp.com/api/audios/"+numeroAleatorio(10);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(jsonObjectRequest);
    }

    //Barra de progresos
    private void showpDialog() {
        if (!progreso.isShowing())
            progreso.show();
    }

    private void hidepDialog() {
        if (progreso.isShowing())
            progreso.dismiss();
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        hidepDialog();
        InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //Toast.makeText(getContext(),"Mensaje: "+response,Toast.LENGTH_SHORT).show();
        audioJuego = new Audio();
        try {
            ;



            audioJuego.setAu_id(Integer.parseInt(response.optString("au_id")));
            audioJuego.setAu_nombre(response.optString("au_nombre"));
            audioJuego.setAu_bytes(response.optString("au_bytes"));
            audioJuego.setAu_categoria(response.optString("au_categoria"));
            String respuesta_completa = response.optString("au_respuesta_correcta");
            String[] parts = respuesta_completa.split("\\.");
            respuestaCorrecta = parts[0];
            audioJuego.setAu_respuesta_correcta(respuestaCorrecta);



        } catch (Exception e) {
            e.printStackTrace();
        }

        /*audioPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                playMp3(audioJuego.getAu_bytes());
            }
        });*/


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verifica si la imagen selecionada son iguales
                audioQuiz();
            }
        },10);

    }


    public void audioQuiz(){
        int numeroAleatorio;
        texto1.setText("¿Que " + audioJuego.getAu_categoria() + "  es ?");
        texto2.setText("Intentos: " + intento);




        numeroAleatorio =numeroAleatorio(4);


        respuesta1.setTag(respuestaCorrecta);
        while(respuesta1.getTag().toString().equalsIgnoreCase(respuestaCorrecta)){
            numeroAleatorio = numeroAleatorio(4);


            respuesta1.setTag(preguntasAnimal[numeroAleatorio]);

        }
        cargarImagenPregunta(respuesta1);
        int temp = numeroAleatorio;

        respuesta2.setTag(respuestaCorrecta);
        while(respuesta2.getTag().toString().equalsIgnoreCase(respuestaCorrecta)){
            numeroAleatorio = numeroAleatorio(4);

           if (numeroAleatorio != temp)

            respuesta2.setTag(preguntasAnimal[numeroAleatorio]);

            System.out.println("error 2 - AudioJuego");

        }
        cargarImagenPregunta(respuesta2);

        respuesta3.setTag(respuestaCorrecta);
        cargarImagenPregunta(respuesta3);


        audioPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                playMp3(audioJuego.getAu_bytes());
            }
        });


        respuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (respuesta1.getTag() == respuestaCorrecta) {
                            /*mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                    respuestaCorrecta();
                } else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/

                    respuestaIncorrecta();
                    respuesta1.setBackgroundColor(Color.RED);

                }
            }
        });
        respuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (respuesta2.getTag() == respuestaCorrecta) {
                            /*mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                    respuestaCorrecta();
                } else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/

                    respuestaIncorrecta();
                    respuesta2.setBackgroundColor(Color.RED);
                }
            }
        });
        respuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (respuesta3.getTag() == respuestaCorrecta) {
                           /* mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                    respuestaCorrecta();
                } else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/
                    respuestaIncorrecta();
                    respuesta3.setBackgroundColor(Color.RED);
                }
            }
        });




    }

    private Usuario perfil_usuario = UsuarioLogeado.userLogin();
    public void guardarPreferencias(Usuario perfil_usuario){

        SharedPreferences preferences = context.getSharedPreferences("credenciales",
                context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("us_id", perfil_usuario.getUs_id());
        editor.putString("us_nombre_usuario", perfil_usuario.getUs_nombre_usuario());
        editor.putString("us_nombre", perfil_usuario.getUs_nombre());
        editor.putString("us_apellido", perfil_usuario.getUs_apellido());
        editor.putString("us_fecha_nacimiento", perfil_usuario.getUs_fecha_nacimiento());
        editor.putString("us_genero",String.valueOf( perfil_usuario.getUs_genero()));
        editor.putString("us_email", perfil_usuario.getUs_email());
        editor.putString("us_password", perfil_usuario.getUs_password());
        editor.putInt("us_nivel_juego", perfil_usuario.getUs_nivel_juego());

        editor.commit();
    }
    private class TareaWSAumentarNivelAudioJuego extends AsyncTask<String,Integer,Boolean> {
        Usuario user = new Usuario();
        String salir ;
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("https://ayudaucab.herokuapp.com/api/usuarios/aumentar-nivel");

            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("us_id", perfil_usuario.getUs_id());

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);

                String respStr = EntityUtils.toString(resp.getEntity());
                //System.out.println("Lo que envia: "+respStr);
                //Toast.makeText(getActivity(), "Lo que envia: "+respStr, Toast.LENGTH_LONG).show();
                if(!respStr.equals("true")) {
                    resul = false;
                }else{

                    user.setUs_id(perfil_usuario.getUs_id());
                    user.setUs_nombre_usuario(params[0]);
                    user.setUs_nombre(params[1]);
                    user.setUs_apellido(params[2]);
                    user.setUs_fecha_nacimiento(params[3]);
                    user.setUs_genero(perfil_usuario.getUs_genero());
                    user.setUs_email(params[4]);
                    user.setUs_password(params[5]);
                    user.setUs_nivel_juego(perfil_usuario.getUs_nivel_juego()+1);
                    UsuarioLogeado.setAumentoNivel(perfil_usuario.getUs_nivel_juego()+1);
                    salir=params[6];
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                System.out.println("Error: "+ex.getMessage().toString());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hidepDialog();

            if (result)
            {


                guardarPreferencias(user);
                System.out.println("Se ha registrado");
                if(salir.equalsIgnoreCase("si")){
                    recargarFragment();
                }else{
                    Intent intent = new Intent(context, MenuPrincipal.class);
                    startActivity(intent);

                }



                Toast.makeText(getActivity(), "Aumento de Nivel ", Toast.LENGTH_LONG).show();
            }else{

                System.out.println("Problemas Internos");
                Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();
            }
            mediaPlayer.stop();


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
