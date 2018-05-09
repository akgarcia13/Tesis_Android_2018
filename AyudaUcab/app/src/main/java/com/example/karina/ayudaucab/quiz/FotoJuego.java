package com.example.karina.ayudaucab.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.example.karina.ayudaucab.objeto.Imagen;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FotoJuego.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FotoJuego#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class FotoJuego extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FotoJuego() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment FotoJuego.
     */
    // TODO: Rename and change types and number of parameters
    /*public static FotoJuego newInstance(String param1, String param2) {
        FotoJuego fragment = new FotoJuego();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/


    // FOTO JUEGO peticion de imagen para inicializar la foto
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /* new Runnable() {
            @Override
            public void run() {
                //SE HACE LA PETICION DE LA IMAGEN
                TareaWSObtenerImagen tarea = new TareaWSObtenerImagen();
                tarea.execute();
            }
        }.run();*/

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*Bundle bundle = this.getArguments();
        if(bundle != null){
            perfil_usuario = bundle.getParcelable("perfil_usuario_fotoJuego");

        }*/
    }

    // SON LAS PREGUNTAS INCVORRECTAS PARA EL QUIZ
    private TextView texto1,texto2;
    private Button respuesta1,respuesta2,respuesta3;
    private ImageView fotoPregunta;
    private String respuestaCorrecta,categoria;
    private  int intento=2;
    Context context;

    private String preguntasAnimal [] = {
            "Gato", "Perro", "Estrella","Caballo",
            "Tigre","Tortuga"

    };
    private String preguntasDeporte [] = {
            "Basket", "Beisbol", "Futbol",
            "Natacion"

    };
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foto_juego, container, false);
        respuesta1 = (Button) view.findViewById(R.id.pregunta1FotoJuego);
        respuesta2 = (Button) view.findViewById(R.id.pregunta2FotoJuego);
        respuesta3 = (Button) view.findViewById(R.id.pregunta3FotoJuego);
        texto1 = (TextView) view.findViewById(R.id.preguntaFotoJuego); //QUE CATEGORIA ES DEPORTE O ANIMAL
        texto2 = (TextView) view.findViewById(R.id.intentoFotoJuego); // CUANTOS INTENTOS HAY 2 o 1 o 0
        context = view.getContext();
        fotoPregunta =(ImageView)view.findViewById(R.id.fotoPregunta); //la foto

        request = Volley.newRequestQueue(getContext());
        cargarImagen();
        /*if(!imagenPregunta.getIma_categoria().equalsIgnoreCase("null")){


            quizJuego();
        }else{

            Intent intent = new Intent(context, MenuPrincipal.class);
            startActivity(intent);
        }*/
        //quizJuego();
        return view;

    }

    private Imagen imagenPregunta = null;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public void cargarImagen(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando imagen ...");
        progreso.setCancelable(false);
        showpDialog();
    numeroAleatorio = numeroAleatorio(49);
        //Toast.makeText(getActivity(), "Numero Aleatorio: "+numeroAleatorio, Toast.LENGTH_LONG).show();
        String url ="https://ayudaucab.herokuapp.com/api/imagenes/"+numeroAleatorio;
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
       // Long.i("Error",error.toString());
    }


    //PETICION AL WEB SERVICE DE LA IMAGEN
    @Override
    public void onResponse(JSONObject response) {
        hidepDialog();
        //Toast.makeText(getContext(),"Mensaje: "+response,Toast.LENGTH_SHORT).show();
        imagenPregunta = new Imagen();
        try {
          ;




            imagenPregunta.setIma_id(Integer.parseInt(response.optString("ima_id")));

            imagenPregunta.setIma_nombre(response.optString("ima_nombre"));
            imagenPregunta.setDato(response.optString("bytes"));
            imagenPregunta.setIma_categoria(response.optString("ima_categoria"));
            String respuesta_completa = response.optString("ima_respuesta_correcta");
            String[] parts = respuesta_completa.split("\\d+");
            respuestaCorrecta = parts[0];
            imagenPregunta.setIma_respuesta_correcta(respuestaCorrecta);

            imagenPregunta.addImagenRequest(imagenPregunta);

            fotoPregunta.setImageBitmap(imagenPregunta.getImagen());
            texto1.setText("¿Que " + imagenPregunta.getIma_categoria() + "  es ?");
            texto2.setText("Intentos: " + intento);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Verifica si la imagen selecionada son iguales
                    quizJuego(imagenPregunta);
                }
            },10);



        } catch (Exception e) {
            e.printStackTrace();
        }




        /*new Thread(new Runnable() {
            public void run() {
                //Aquí ejecutamos nuestras tareas costosas
                quizJuego(imagenPregunta);
            }
        }).start();*/

        /*if(!imagenPregunta.getIma_categoria().equalsIgnoreCase("null")){


            quizJuego();
        }else{

            Intent intent = new Intent(context, MenuPrincipal.class);
            startActivity(intent);
        }*/
    }



    public int numeroAleatorio(int maximo){
        int numeroAleatorio;
        numeroAleatorio = (int) (Math.random() * maximo) ;
        if (numeroAleatorio < 0) {
            numeroAleatorio = numeroAleatorio * -1;
        }
        if(numeroAleatorio == 0) numeroAleatorio =1;

        return numeroAleatorio;
    }


    //RESPUESTA INCORRECTA IMAGEN DE OPS
    private void respuestaIncorrecta(){
        intento--;

        texto2.setText("Intentos: " + intento);

        if(intento > 0) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            alertDialogBuilder
                    .setView(inflater.inflate(R.layout.incorrecto, null))
                    .setMessage(" !Ops!\n Tienes: " + intento + " Intento")
                    .setCancelable(false)

                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
            ;

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        salir();

    }


    //Respuesta Incorrecta Salir o volver a Intentarlo
    private void respuestaIncorrectaFinal(){
       // intento--;

        texto2.setText("Intentos: " + intento);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
                            }
                        }

                )
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, MenuPrincipal.class);
                                startActivity(intent);
                            }
                        }
                )
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }

    //RESPUESTA CORRECTA IMAGEN DE GREAT Y AUMENTA DE NIVEL
    private void respuestaCorrecta(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialogBuilder
                .setView(inflater.inflate(R.layout.correcto,null))
                .setMessage(" !!!Sigue así!!! ")
                .setCancelable(false)
                .setPositiveButton("Siguiente imagen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(InternetExcepcion.isOnline(context)){
                                    TareaWSAumentarNivel tarea1 = new TareaWSAumentarNivel();
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
                                    Intent intent = new Intent(view.getContext(), MenuPrincipal.class);
                                        /*Bundle bundle = new Bundle();

                                        bundle.putParcelable("usuario",perfil_usuario);
                                        intent.putExtras(bundle);

                                        System.out.println("Problemas Internos");
                                        Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();*/
                                    startActivity(intent);
                                }
                             // recargarFragment();
                              dialogInterface.dismiss();
                            }
                        }

                )
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(InternetExcepcion.isOnline(context)){
                                    TareaWSAumentarNivel tarea1 = new TareaWSAumentarNivel();
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
                                    Intent intent = new Intent(view.getContext(), MenuPrincipal.class);
                                        /*Bundle bundle = new Bundle();

                                        bundle.putParcelable("usuario",perfil_usuario);
                                        intent.putExtras(bundle);

                                        System.out.println("Problemas Internos");
                                        Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();*/
                                    startActivity(intent);
                                }
                                dialogInterface.dismiss();
                               /* Intent intent = new Intent(context, MenuPrincipal.class);
                                startActivity(intent);*/
                            }
                        }
                )
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    public void recargarFragment(){
        Fragment fragment = new FotoJuego();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
    }


    public void salir(){

        if(intento  <= 0){
            respuestaIncorrectaFinal();

        }
    }

    int numeroAleatorio;
    public void quizJuego(final Imagen imagenPregunta){
      try {

          //fotoPregunta.setImageBitmap(imagenPregunta.getImagen());
          //texto1.setText("¿Que " + imagenPregunta.getIma_categoria() + "  es ?");
          //texto2.setText("Intentos: " + intento);


          /*Toast.makeText(getActivity(), "Categoria: "+imagenPregunta.getIma_categoria().
               toString(), Toast.LENGTH_LONG).show();*/

          //PARA RELLENAR LOS BOTONES DE LAS RESPUESTAS INCORRECTAS deporte
          if (imagenPregunta.getIma_categoria().equalsIgnoreCase("Deporte")) {

              numeroAleatorio = numeroAleatorio(4);


              String tempString = imagenPregunta.getIma_respuesta_correcta();


              // respuesta1.setText(respuestaCorrecta);
              while (tempString.equalsIgnoreCase(imagenPregunta.getIma_respuesta_correcta())) {
                  numeroAleatorio = numeroAleatorio(4);
                  tempString = preguntasDeporte[numeroAleatorio];
                  // Toast.makeText(getActivity(), "Aqui el error", Toast.LENGTH_LONG).show();
                  //BUSCA ARRIBA

              }
              respuesta1.setText(tempString);
              int temp = numeroAleatorio;

              String tempString2 = imagenPregunta.getIma_respuesta_correcta();

              // respuesta2.setText(respuestaCorrecta);

              //mientras que arriba no agarre la respuesta correcta, entonces vas a agarrar la respuesta incorrecta
              while (tempString2.equalsIgnoreCase(imagenPregunta.getIma_respuesta_correcta())) {

                  while (numeroAleatorio == temp) {
                      numeroAleatorio = numeroAleatorio(4);

                  }
                  tempString2 = preguntasDeporte[numeroAleatorio];

              }
              respuesta2.setText(tempString2);


          } else {

              //PARA RELLENAR LOS BOTONES DE LAS RESPUESTAS INCORRECTAS Animal
              numeroAleatorio = numeroAleatorio(4);
              String tempString1 = imagenPregunta.getIma_respuesta_correcta();
              //respuesta1.setText(respuestaCorrecta);
              while (tempString1.equalsIgnoreCase(imagenPregunta.getIma_respuesta_correcta())) {
                  numeroAleatorio = numeroAleatorio(4);
                  tempString1 = preguntasAnimal[numeroAleatorio];
                  //Toast.makeText(getActivity(), "Aqui el error", Toast.LENGTH_LONG).show();

              }
              respuesta1.setText(tempString1);
              String tempString2 = imagenPregunta.getIma_respuesta_correcta();
              int temp = numeroAleatorio;

              //respuesta2.setText(respuestaCorrecta);
              while (tempString2.equalsIgnoreCase(respuestaCorrecta)) {

                  while (numeroAleatorio == temp) {
                      numeroAleatorio = numeroAleatorio(4);

                  }
                  tempString2 = preguntasAnimal[numeroAleatorio];

              }

              respuesta2.setText(tempString2);

          }

          respuesta3.setText(imagenPregunta.getIma_respuesta_correcta());

          final String respuestaCorrecta2 = imagenPregunta.getIma_respuesta_correcta();

          // LISTENER DEL BOTON DE LA RESPUESTA INCORRECTA 1
          respuesta1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //My logic for Button goes in here

                  if (respuesta1.getText() == respuestaCorrecta2) {
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

          // LISTENER DEL BOTON DE LA RESPUESTA INCORRECTA 2 XQ SON 2 INTENTOS
          respuesta2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //My logic for Button goes in here

                  if (respuesta2.getText() == respuestaCorrecta2) {
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

                  if (respuesta3.getText() == respuestaCorrecta2) {
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

      }catch (NullPointerException e2){
            e2.printStackTrace();
      }catch (RuntimeException e1){
              e1.printStackTrace();

      }catch (Exception e){
          e.printStackTrace();
      }



    }



    /*private class TareaWSObtenerImagen extends AsyncTask<String,Integer,Boolean> {
        int numeroAleatorio;
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            //numeroAleatorio = (int)(Math.random()*50)+1;
            //if(numeroAleatorio < 0){ numeroAleatorio = numeroAleatorio *-1;}
            HttpGet get = new
                    HttpGet("https://ayudaucab.herokuapp.com/api/imagenes/"+numeroAleatorio(50) );

            get.setHeader("content-type", "application/json");

            try
            {

                //respuesta de la BASE DE DATOS
                HttpResponse resp = httpClient.execute(get);

                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                imagenPregunta = new Imagen();
                imagenPregunta.setIma_id(Integer.parseInt(respJSON.getString("ima_id")));
                imagenPregunta.setIma_nombre(respJSON.getString("ima_nombre"));
                imagenPregunta.setDato(respJSON.getString("bytes"));
                imagenPregunta.setIma_categoria(respJSON.getString("ima_categoria"));
                String respuesta_completa = respJSON.getString("ima_respuesta_correcta");
                String[] parts = respuesta_completa.split("\\d+");
                respuestaCorrecta = parts[0];
                imagenPregunta.setIma_respuesta_correcta(respuestaCorrecta);



                if(imagenPregunta.getIma_respuesta_correcta() == "null")
                    resul = false;
            }
            catch(Exception ex)
            { //SI NO HAY INTERNET
                Log.e("ServicioRest IMAGEN","Error!", ex);
                System.out.println("Error: "+ex.getMessage().toString());
                resul = false;
            }

            return resul;
        }


        //SI ya esta lista la imagen de la BASE DE DATOS
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                fotoPregunta.setImageBitmap(imagenPregunta.getImagen());
                texto1.setText("¿Que " + imagenPregunta.getIma_categoria() + "  es ?");
                texto2.setText("Intentos: " + intento);


                Toast.makeText(getActivity(), "Categoria: "+imagenPregunta.getIma_categoria().
                        toString(), Toast.LENGTH_LONG).show();

                //PARA RELLENAR LOS BOTONES DE LAS RESPUESTAS INCORRECTAS deporte
                if (imagenPregunta.getIma_categoria().equalsIgnoreCase("Deporte")) {

                    numeroAleatorio =numeroAleatorio(4);



                    respuesta1.setText(respuestaCorrecta);
                    while(respuesta1.getText().toString().equalsIgnoreCase(respuestaCorrecta)){
                        numeroAleatorio = numeroAleatorio(4);

                        //BUSCA ARRIBA
                        respuesta1.setText(preguntasDeporte[numeroAleatorio]);
                    }
                    int temp = numeroAleatorio;

                    respuesta2.setText(respuestaCorrecta);

                    //mientras que arriba no agarre la respuesta correcta, entonces vas a agarrar la respuesta incorrecta
                    while(respuesta2.getText().toString().equalsIgnoreCase(respuestaCorrecta)){

                        while (numeroAleatorio == temp)
                        {
                            numeroAleatorio = numeroAleatorio(4);

                        }
                        respuesta2.setText(preguntasDeporte[numeroAleatorio]);
                    }




                } else {

                    //PARA RELLENAR LOS BOTONES DE LAS RESPUESTAS INCORRECTAS Animal
                    numeroAleatorio =numeroAleatorio(4);
                    respuesta1.setText(respuestaCorrecta);
                    while(respuesta1.getText().toString().equalsIgnoreCase(respuestaCorrecta)){
                        numeroAleatorio = numeroAleatorio(4);


                        respuesta1.setText(preguntasAnimal[numeroAleatorio]);
                    }
                    int temp = numeroAleatorio;

                    respuesta2.setText(respuestaCorrecta);
                    while(respuesta2.getText().toString().equalsIgnoreCase(respuestaCorrecta)){

                        while (numeroAleatorio == temp)
                        {
                            numeroAleatorio = numeroAleatorio(4);

                        }
                        respuesta2.setText(preguntasAnimal[numeroAleatorio]);
                    }



                }


                // LISTENER DEL BOTON DE LA RESPUESTA INCORRECTA 1
                respuesta1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //My logic for Button goes in here

                        if (respuesta1.getText() == respuestaCorrecta) {
                            /*mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                            //respuestaCorrecta();
                        //} else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/

                            //respuestaIncorrecta();

                          //  respuesta1.setBackgroundColor(Color.RED);
                        /*}
                    }
                });

                // LISTENER DEL BOTON DE LA RESPUESTA INCORRECTA 2 XQ SON 2 INTENTOS
                respuesta2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //My logic for Button goes in here

                        if (respuesta2.getText() == respuestaCorrecta) {
                            /*mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                            //respuestaCorrecta();
                        /*} else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/

                            //respuestaIncorrecta();

                          /*  respuesta2.setBackgroundColor(Color.RED);
                        }
                    }
                });
                respuesta3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //My logic for Button goes in here

                        if (respuesta3.getText() == respuestaCorrecta) {
                           /* mScore = mScore + 1;
                            updateScore(mScore);
                            updateQuestion();
                            //This line of code is optiona
                            Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();*/
                            //respuestaCorrecta();
                        /*} else {
                           /* Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            updateQuestion();*/

                            //respuestaIncorrecta();

                          /*  respuesta3.setBackgroundColor(Color.RED);
                        }
                    }
                });



                respuesta3.setText(respuestaCorrecta);

            }else{
                Intent intent = new Intent(context, MenuPrincipal.class);
                Bundle bundle = new Bundle();

                bundle.putParcelable("usuario",perfil_usuario);
                intent.putExtras(bundle);
                startActivity(intent);
                System.out.println("Usuario no registrado o mala combianacion de contraseña ");
                Toast.makeText(getActivity(), "Problemas internos, por favor vuelva a intentarlo", Toast.LENGTH_LONG).show();
            }
        }
    }*/





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
    private class TareaWSAumentarNivel extends AsyncTask<String,Integer,Boolean> {
        Usuario user = new Usuario();
        String salir;
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
                    salir = params[6];
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


            if (result)
            {
                Toast.makeText(getActivity(), "Aumento de Nivel ", Toast.LENGTH_LONG).show();

                guardarPreferencias(user);
                System.out.println("Se ha registrado");
                if(salir.equalsIgnoreCase("si")){
                    recargarFragment();
                }else{
                    Intent intent = new Intent(context, MenuPrincipal.class);
                                startActivity(intent);

                }


            }else{

                System.out.println("Problemas Internos");
                Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();
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
