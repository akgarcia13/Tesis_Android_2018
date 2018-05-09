package com.example.karina.ayudaucab.menu.principal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.codificacion.Codificacion;
import com.example.karina.ayudaucab.menu.login.LoginActivity;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;
import com.example.karina.ayudaucab.validacion.InternetExcepcion;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualizarPerfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActualizarPerfil#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActualizarPerfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActualizarPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualizarPerfil.
     */
    // TODO: Rename and change types and number of parameters
   /* public static ActualizarPerfil newInstance(String param1, String param2) {
        ActualizarPerfil fragment = new ActualizarPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    private Usuario perfil_usuario = UsuarioLogeado.userLogin();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
            //perfil_usuario = (Usuario) getArguments().getSerializable("perfil_usuario");
        }
      /*  Bundle bundle = this.getArguments();
        if(bundle != null){
            perfil_usuario = bundle.getParcelable("perfil_usuario");

        }*/

    }

    private int mYear;
    private int mMonth;
    private int mDay;
    public void obtenerFecha(){
        final Calendar c = Calendar.getInstance();
        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);

        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                mYear = year;
                mDateDisplay.setText(diaFormateado + "/" + mesFormateado +"/"+ year);

            }
        },anio, mes, dia);
        recogerFecha .getDatePicker().setMaxDate(System.currentTimeMillis());

        recogerFecha.show();

    }

    private AwesomeValidation awesomeValidation;
    private String sexo;
    private RadioButton masculino,femenino;
    private EditText mDateDisplay;
    private EditText nombre,apellido,email,usuario,password;
    private Button mPickDate, registrar, eliminar;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actualizar_perfil, container, false);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        context = view.getContext();

        nombre = (EditText) view.findViewById(R.id.actulizarNombre);
        apellido = (EditText) view.findViewById(R.id.actulizarApellido);
        email = (EditText) view.findViewById(R.id.actulizarEmail);
        usuario = (EditText) view.findViewById(R.id.actulizarUsuario);
        password = (EditText) view.findViewById(R.id.actulizarPassword);
        //masculino = (RadioButton) view.findViewById(R.id.radioButtonMasculinoActulizar);
        //femenino= (RadioButton) view.findViewById(R.id.radioButtonFemeninoActulizar);

        mDateDisplay = (EditText)view.findViewById(R.id.actulizarDate);
        mDateDisplay.setKeyListener(null);
        mPickDate = (Button)view.findViewById(R.id.myDatePickerButtonActulizar);
        mPickDate.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // do something
                obtenerFecha();
            }
        });
        if(perfil_usuario != null) {
            nombre.setText(perfil_usuario.getUs_nombre());
            apellido.setText(perfil_usuario.getUs_apellido());
            email.setText(perfil_usuario.getUs_email());
            usuario.setText(perfil_usuario.getUs_nombre_usuario());
            password.setText(perfil_usuario.getUs_password());
            sexo = String.valueOf(perfil_usuario.getUs_genero());
            if (sexo == "M") {
                masculino.setChecked(true);
            } else if (sexo == "F") {
                femenino.setChecked(true);
            }


            mDateDisplay.setText(perfil_usuario.getUs_fecha_nacimiento());

            awesomeValidation.addValidation(nombre, "[a-zA-Z\\s]+","Ingresar un nombre vàlido");
            awesomeValidation.addValidation(apellido, "[a-zA-Z\\s]+","Ingresar un apelido vàlido");
            awesomeValidation.addValidation(email, Patterns.EMAIL_ADDRESS,"Email invàlido: Por ejemplo xxx@xxx.com");
        /*awesomeValidation.addValidation( mDateDisplay, Range.closed(1900, Calendar.getInstance().get(Calendar.YEAR)),
                "Ingresar una fecha de nacimiento vàlida");*/
            awesomeValidation.addValidation(usuario, "[a-z0-9ü][a-z0-9ü.]{3,16}$+","Entre 3 a 16 carateres entre letras y numeros");

            awesomeValidation.addValidation(password,
                    "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{6,}",
                    "De 6 a 8 caracteres entre mayùsculas, minùsculas, nùmeros y especiales como *.");
            registrar = (Button) view.findViewById(R.id.btmActulizar);
            registrar.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {
                    // do something
                    if(InternetExcepcion.isOnline(context) ){
                        if (awesomeValidation.validate()) {

                            progreso = new ProgressDialog(getContext());
                            progreso.setMessage("Actualizando "+usuario.getText()+" por favor espere...");
                            progreso.setCancelable(false);
                            showpDialog();

                            TareaWSActualizar tarea = new TareaWSActualizar();
                            tarea.execute(
                                    usuario.getText().toString(),
                                    nombre.getText().toString(),
                                    apellido.getText().toString(),
                                    mDateDisplay.getText().toString(),
                                    sexo,
                                    email.getText().toString(),
                                    password.getText().toString()

                            );

                            awesomeValidation.clear();
                            // Here, we are sure that form is successfully validated. So, do your stuffs now...
                            // Toast.makeText(getActivity(), "Se ha registrado con exito: sexo: "+sexo, Toast.LENGTH_LONG).show();
                        }

                    }else {

                        InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
                        Intent intent = new Intent(getContext(), MenuPrincipal.class);
                       /* Bundle bundle = new Bundle();

                        bundle.putParcelable("usuario",perfil_usuario);
                        intent.putExtras(bundle);

                        System.out.println("Problemas Internos");
                        Toast.makeText(getActivity(), "  Revise su coexion", Toast.LENGTH_LONG).show();*/
                        startActivity(intent);
                    }



                }
            });

            eliminar = (Button) view.findViewById(R.id.btmEliminar);
            eliminar .setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {


                    if(InternetExcepcion.isOnline(context)){

                        progreso = new ProgressDialog(getContext());
                        progreso.setMessage("Eliminando "+usuario.getText()+" por favor espere...");
                        progreso.setCancelable(false);
                        showpDialog();


                        TareaWSEliminar tarea = new TareaWSEliminar();
                        tarea.execute(


                        );
                        awesomeValidation.clear();

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


                    // Here, we are sure that form is successfully validated. So, do your stuffs now...
                    // Toast.makeText(getActivity(), "Se ha registrado con exito: sexo: "+sexo, Toast.LENGTH_LONG).show();

                }
            });

        }
        return view;
    }

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

    public void guardarPreferencias(){

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


    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {
        Usuario user = new Usuario();
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut put = new
                    HttpPut("https://ayudaucab.herokuapp.com/api/usuarios");

            put.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("us_id", perfil_usuario.getUs_id());
                dato.put("us_nombre_usuario", Codificacion.Encriptar(params[0]));
                dato.put("us_nombre", Codificacion.Encriptar(params[1]));
                dato.put("us_apellido", Codificacion.Encriptar(params[2]));
                dato.put("us_fecha_nacimiento", params[3]);
                //dato.put("us_genero", params[4]);
                dato.put("us_email", Codificacion.Encriptar(params[5]));
                dato.put("us_password", Codificacion.Encriptar(params[6]));

                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);

                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("Lo que envia: "+respStr);
                if(!respStr.equals("true")) {
                    resul = false;
                }else{

                    perfil_usuario.setUs_id(perfil_usuario.getUs_id());
                    perfil_usuario.setUs_nombre_usuario(params[0]);
                    perfil_usuario.setUs_nombre(params[1]);
                    perfil_usuario.setUs_apellido(params[2]);
                    perfil_usuario.setUs_fecha_nacimiento(params[3]);
                    perfil_usuario.setUs_genero(perfil_usuario.getUs_genero());
                    perfil_usuario.setUs_email(params[5]);
                    perfil_usuario.setUs_password(params[6]);
                    perfil_usuario.setUs_nivel_juego(perfil_usuario.getUs_nivel_juego());
                }
            }
            catch(Exception ex)
            {
                hidepDialog();
                Log.e("ServicioRest Actualizar","Error!", ex);
                System.out.println("Error: "+ex.getMessage().toString());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                hidepDialog();
                guardarPreferencias();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                alertDialogBuilder
                        .setView(inflater.inflate(R.layout.mensaje_layout,null))
                        .setMessage("  Actualizacion Exitosa")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent = new Intent(context, MenuPrincipal.class);


                                        startActivity(intent);

                                        dialogInterface.dismiss();
                                    }
                                }

                        )

                ;
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                System.out.println("Usuario Actualizado");




                Toast.makeText(getActivity(), "Usuario Actualizado ", Toast.LENGTH_LONG).show();
            }else{
                hidepDialog();
                System.out.println("No se ha registrado");
                Toast.makeText(getActivity(), "No se ha Actualizado, intento con otro usuario o Revise su coexion", Toast.LENGTH_LONG).show();
            }
        }
    }


    //Se elimina con el id del usuario que viene desde el LOGIN
    private class TareaWSEliminar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpDelete delete = new
                    HttpDelete("https://ayudaucab.herokuapp.com/api/usuarios/"+perfil_usuario.getUs_id());

            delete.setHeader("content-type", "application/json");

            try
            {




                HttpResponse resp = httpClient.execute(delete);

                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("Lo que envia: "+respStr);
                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest Eliminar","Error!", ex);
                System.out.println("Error: "+ex.getMessage().toString());
                resul = false;
                hidepDialog();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                hidepDialog();
                System.out.println("Usuario Eliminado");

                SharedPreferences preferences =getActivity().getSharedPreferences("credenciales",
                        Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                UsuarioLogeado.userLogOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();


                Toast.makeText(getActivity(), "Usuario Eliminado ", Toast.LENGTH_LONG).show();
            }else{
                hidepDialog();
                System.out.println("No se ha registrado");
                Toast.makeText(getActivity(), "No se ha Eliminado, intento con otro usuario o email", Toast.LENGTH_LONG).show();
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
