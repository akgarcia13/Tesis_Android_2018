package com.example.karina.ayudaucab.memoria;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.menu.principal.MenuPrincipal;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.quiz.FotoJuego;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;
import com.example.karina.ayudaucab.validacion.InternetExcepcion;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemoriaJuego.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemoriaJuego#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoriaJuego extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MemoriaJuego() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemoriaJuego.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoriaJuego newInstance(String param1, String param2) {
        MemoriaJuego fragment = new MemoriaJuego();
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
       /* Bundle bundle = this.getArguments();
        if(bundle != null){
            perfil_usuario = bundle.getParcelable("perfil_usuario_Memoria");

        }*/
    }
    Context context;
    TextView tv_p1, tv_p2,memoriaAnimal1,memoriaAnimal2;
    ImageView iv_11,iv_12,iv_13,iv_14,
            iv_21,iv_22,iv_23,iv_24,
            iv_31,iv_32,iv_33,iv_34,bienmal;
    //Imagenes
    Integer[] cardsArray ={101,102,103,104,105,106,201,202,203,204,205,206};
    //Actual Imagen
    int image101,image102,image103,image104,image105,image106,image201,image202,image203,
            image204,image205,image206;

    int firstCard,secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber=1;
    int turn =1;
    int playerPoints =0, cpuPoints =0;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_memoria_juego, container, false);
        context = view.getContext();

        memoriaAnimal1 = (TextView) view.findViewById(R.id.memoriaAnimal1);
        memoriaAnimal2 = (TextView) view.findViewById(R.id.memoriaAnimal2);


        iv_11 = (ImageView)view.findViewById(R.id.iv_11);
        iv_12 = (ImageView)view.findViewById(R.id.iv_12);
        iv_13 = (ImageView)view.findViewById(R.id.iv_13);
        iv_14 = (ImageView)view.findViewById(R.id.iv_14);

        iv_21 = (ImageView)view.findViewById(R.id.iv_21);
        iv_22 = (ImageView)view.findViewById(R.id.iv_22);
        iv_23 = (ImageView)view.findViewById(R.id.iv_23);
        iv_24 = (ImageView)view.findViewById(R.id.iv_24);

        iv_31 = (ImageView)view.findViewById(R.id.iv_31);
        iv_32 = (ImageView)view.findViewById(R.id.iv_32);
        iv_33 = (ImageView)view.findViewById(R.id.iv_33);
        iv_34 = (ImageView)view.findViewById(R.id.iv_34);

        bienmal = (ImageView)view.findViewById(R.id.imagenBienMAl);


        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");

        iv_21.setTag("4");
        iv_22.setTag("5");
        iv_23.setTag("6");
        iv_24.setTag("7");

        iv_31.setTag("8");
        iv_32.setTag("9");
        iv_33.setTag("10");
        iv_34.setTag("11");

        //load the card Images
        frontOfCardsResources();

        //Shufle the images
        Collections.shuffle(Arrays.asList(cardsArray));

        //Cambiado el color del segundo jugador (Inactivo)
        //tv_p2.setTextColor(Color.GRAY);

        iv_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_11,theCard);
            }
        });

        iv_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_12,theCard);
            }
        });

        iv_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_13,theCard);
            }
        });

        iv_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_14,theCard);
            }
        });

        iv_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_21,theCard);
            }
        });

        iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_22,theCard);
            }
        });

        iv_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_23,theCard);
            }
        });

        iv_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_24,theCard);
            }
        });

        iv_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_31,theCard);
            }
        });

        iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_32,theCard);
            }
        });

        iv_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_33,theCard);
            }
        });

        iv_34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_34,theCard);
            }
        });

        return view;
    }

    private void doStuff(ImageView iv, int card){

        if(cardsArray[card] == 101){
            iv.setImageResource(image101);
        }else if(cardsArray[card] == 102){
            iv.setImageResource(image102);
        }else if(cardsArray[card] == 103){
            iv.setImageResource(image103);
        }else if(cardsArray[card] == 104){
            iv.setImageResource(image104);
        }else if(cardsArray[card] == 105){
            iv.setImageResource(image105);
        }else if(cardsArray[card] == 106){
            iv.setImageResource(image106);
        }else if(cardsArray[card] == 201){
            iv.setImageResource(image201);
        }else if(cardsArray[card] == 202){
            iv.setImageResource(image202);
        }else if(cardsArray[card] == 203){
            iv.setImageResource(image203);
        }else if(cardsArray[card] == 204){
            iv.setImageResource(image204);
        }else if(cardsArray[card] == 205){
            iv.setImageResource(image205);
        }else if(cardsArray[card] == 206){
            iv.setImageResource(image206);
        }
        // check cual imagen es seleccionadado y lo guarda en una variable temp

        if(cardNumber == 1){
            firstCard = cardsArray[card];

            if(firstCard > 200 ){
                firstCard = firstCard -100;
            }

            cardNumber = 2;
            clickedFirst = card;

            if((firstCard == 101) || (firstCard ==201)){
                memoriaAnimal1.setTextColor(Color.parseColor("#F50057"));
                memoriaAnimal1.setText("Cerdo");


            }else if((firstCard == 102) || (firstCard ==202)){
                memoriaAnimal1.setTextColor(Color.parseColor("#4E342E"));
                memoriaAnimal1.setText("Gato");

            }else if((firstCard == 103) || (firstCard ==203)){
                memoriaAnimal1.setTextColor(Color.parseColor("#795548"));
                memoriaAnimal1.setText("Mono");

            }else if((firstCard == 104) || (firstCard ==204)){
                memoriaAnimal1.setTextColor(Color.parseColor("#E91E63"));
                memoriaAnimal1.setText("Oveja");


            }else if((firstCard == 105) || (firstCard ==205)){

                memoriaAnimal1.setTextColor(Color.parseColor("#795548"));
                memoriaAnimal1.setText("Perro");
            }else if((firstCard == 106) || (firstCard ==206)){
                memoriaAnimal1.setTextColor(Color.parseColor("#F57F17"));
                memoriaAnimal1.setText("Tigre");

            }

            iv.setEnabled(false);
        }else if (cardNumber == 2){
            secondCard = cardsArray[card];
            if(secondCard > 200 ){
                secondCard = secondCard -100;
            }
            cardNumber = 1;
            clickedSecond = card;



            if((secondCard == 101) || (secondCard ==201)){

                memoriaAnimal2.setTextColor(Color.parseColor("#F50057"));
                memoriaAnimal2.setText("Cerdo");
            }else if((secondCard == 102) || (secondCard ==202)){

                memoriaAnimal2.setTextColor(Color.parseColor("#4E342E"));
                memoriaAnimal2.setText("Gato");
            }else if((secondCard == 103) || (secondCard ==203)){

                memoriaAnimal2.setTextColor(Color.parseColor("#795548"));
                memoriaAnimal2.setText("Mono");
            }else if((secondCard == 104) || (secondCard ==204)){

                memoriaAnimal2.setTextColor(Color.parseColor("#E91E63"));
                memoriaAnimal2.setText("Oveja");

            }else if((secondCard == 105) || (secondCard ==205)){

                memoriaAnimal2.setTextColor(Color.parseColor("#795548"));
                memoriaAnimal2.setText("Perro");

            }else if((secondCard == 106) || (secondCard ==206)){

                memoriaAnimal2.setTextColor(Color.parseColor("#F57F17"));
                memoriaAnimal2.setText("Tigre");
            }

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);

            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);

            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Verifica si la imagen selecionada son iguales
                    calculate();
                }
            },1000);


        }
    }
    private void calculate(){
        //Si las imagenes son iguales se van quittando
        if(firstCard == secondCard){
            if(clickedFirst == 0){
                iv_11.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 1){
                iv_12.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 2){
                iv_13.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 3){
                iv_14.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 4){
                iv_21.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 5){
                iv_22.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 6){
                iv_23.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 7){
                iv_24.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 8){
                iv_31.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 9){
                iv_32.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 10){
                iv_33.setVisibility(View.INVISIBLE);
            }else  if(clickedFirst == 11){
                iv_34.setVisibility(View.INVISIBLE);
            }

            if(clickedSecond == 0){
                iv_11.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 1){
                iv_12.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 2){
                iv_13.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 3){
                iv_14.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 4){
                iv_21.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 5){
                iv_22.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 6){
                iv_23.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 7){
                iv_24.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 8){
                iv_31.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 9){
                iv_32.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 10){
                iv_33.setVisibility(View.INVISIBLE);
            }else  if(clickedSecond  == 11){
                iv_34.setVisibility(View.INVISIBLE);
            }
            //Agregamos los puntos a los jugadores
            if(turn == 1){
                playerPoints++;
                //tv_p1.setText("Jugador 1: "+playerPoints);
            }else if(turn == 2){
                cpuPoints++;
                // tv_p2.setText("Jugador 2: "+cpuPoints);
            }

            /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            alertDialogBuilder
                    .setView(inflater.inflate(R.layout.correcto,null))
                    .setMessage(" Muy bien! ¡Sigue Así!")
                    .setCancelable(false)
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            }

                    )
            ;
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/
            bienmal.setImageResource(R.drawable.ic_bien);

        }else {
            iv_11.setImageResource(R.drawable.ic_back);
            iv_12.setImageResource(R.drawable.ic_back);
            iv_13.setImageResource(R.drawable.ic_back);
            iv_14.setImageResource(R.drawable.ic_back);

            iv_21.setImageResource(R.drawable.ic_back);
            iv_22.setImageResource(R.drawable.ic_back);
            iv_23.setImageResource(R.drawable.ic_back);
            iv_24.setImageResource(R.drawable.ic_back);

            iv_31.setImageResource(R.drawable.ic_back);
            iv_32.setImageResource(R.drawable.ic_back);
            iv_33.setImageResource(R.drawable.ic_back);
            iv_34.setImageResource(R.drawable.ic_back);

            //Cambia los turnos de los jugadores
            if(turn == 1){
                turn =2;
                //tv_p1.setTextColor(Color.GRAY);
                //tv_p2.setTextColor(Color.BLACK);

            }else  if(turn == 2){
                turn =1;
                // tv_p2.setTextColor(Color.GRAY);
                //tv_p1.setTextColor(Color.BLACK);

            }

          /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            alertDialogBuilder
                    .setView(inflater.inflate(R.layout.incorrecto,null))
                    .setMessage(" !Casi! Vuelve a intentarlo ")
                    .setCancelable(false)
                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
            ;
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/
          bienmal.setImageResource(R.drawable.ic_mal);
        }

        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);

        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);

        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);


        memoriaAnimal1.setText("");
        memoriaAnimal2.setText("");
        //PAra finalizar
        checkEnd();

    }

    private  void  checkEnd(){
        if(iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE ){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            alertDialogBuilder
                    .setView(inflater.inflate(R.layout.correcto,null))
                    .setMessage(" !!!Sigue así!!! ")
                    .setCancelable(false)
                    .setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if(InternetExcepcion.isOnline(context)){
                                        TareaWSAumentarNivelMemoria tarea1 = new TareaWSAumentarNivelMemoria();
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
                                        TareaWSAumentarNivelMemoria tarea1 = new TareaWSAumentarNivelMemoria();
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
    }

    private void frontOfCardsResources(){
        image101 = R.drawable.ic_iamge101;
        image102 = R.drawable.ic_iamge102;
        image103 = R.drawable.ic_iamge103;
        image104 = R.drawable.ic_iamge104;
        image105 = R.drawable.ic_iamge105;
        image106 = R.drawable.ic_iamge106;
        image201 = R.drawable.ic_iamge201;
        image202 = R.drawable.ic_iamge202;
        image203 = R.drawable.ic_iamge203;
        image204 = R.drawable.ic_iamge204;
        image205 = R.drawable.ic_iamge205;
        image206 = R.drawable.ic_iamge206;

    }

    private Usuario perfil_usuario = UsuarioLogeado.userLogin();
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
    public void recargarFragment(){
        Fragment fragment = new MemoriaJuego();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
    }
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
    private class TareaWSAumentarNivelMemoria extends AsyncTask<String,Integer,Boolean> {
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

                System.out.println("Se ha registrado");


                guardarPreferencias(user);
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
