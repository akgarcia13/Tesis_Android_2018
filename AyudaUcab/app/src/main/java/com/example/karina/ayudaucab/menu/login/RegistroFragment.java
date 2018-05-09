package com.example.karina.ayudaucab.menu.login;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.karina.ayudaucab.validacion.InternetExcepcion;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
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
    private Button mPickDate, registrar, ir_inicio;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_registro, container, false);
        context = view.getContext();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        nombre = (EditText) view.findViewById(R.id.registroNombre);
        apellido = (EditText) view.findViewById(R.id.registroApellido);
        email = (EditText) view.findViewById(R.id.registroEmail);
        usuario = (EditText) view.findViewById(R.id.registroUsuario);
        password = (EditText) view.findViewById(R.id.registroPassword);
        masculino = (RadioButton) view.findViewById(R.id.radioButtonMasculinoRegistro);
        femenino= (RadioButton) view.findViewById(R.id.radioButtonFemeninoRegistro);

        mDateDisplay = (EditText)view.findViewById(R.id.registroDate);

        mDateDisplay.setKeyListener(null);
        mPickDate = (Button)view.findViewById(R.id.myDatePickerButtonRegistro);
        mPickDate.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // do something
                obtenerFecha();
            }
        });

        awesomeValidation.addValidation(nombre, "[a-zA-Z\\s]+","Ingresar un nombre vàlido");
        awesomeValidation.addValidation(apellido, "[a-zA-Z\\s]+","Ingresar un apelido vàlido");
        awesomeValidation.addValidation(email, Patterns.EMAIL_ADDRESS,"Email invàlido: Por ejemplo xxx@xxx.com");
        /*awesomeValidation.addValidation( mDateDisplay, Range.closed(1900, Calendar.getInstance().get(Calendar.YEAR)),
                "Ingresar una fecha de nacimiento vàlida");*/
        //awesomeValidation.addValidation(usuario, RegexTemplate.NOT_EMPTY,"El usuario no puede estar vacìo");
        awesomeValidation.addValidation(usuario, "[a-z0-9ü][a-z0-9ü.]{3,16}$+","Entre 3 a 16 carateres entre letras y numeros");
        awesomeValidation.addValidation(password,
                "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{6,}",
                "De 6 a 8 caracteres entre mayùsculas, minùsculas, nùmeros y especiales como *.");
        registrar = (Button) view.findViewById(R.id.btmRegister);
        registrar.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // do something
                 if(InternetExcepcion.isOnline(context) ){
                //Para pruebas
                     if (awesomeValidation.validate()) {
                         if (masculino.isChecked()==true) {
                             //código
                             sexo="M";
                         } else
                         if (femenino.isChecked()==true) {
                             // código
                             sexo="F";
                         }

                         progreso = new ProgressDialog(getContext());
                         progreso.setMessage("Registrando nuevo Usuario por favor espere...");
                         progreso.setCancelable(false);
                         showpDialog();

                         TareaWSInsertar tarea = new TareaWSInsertar();
                         tarea.execute(
                                 Codificacion.Encriptar(usuario.getText().toString()),
                                 Codificacion.Encriptar(nombre.getText().toString()),
                                 Codificacion.Encriptar(apellido.getText().toString()),
                                 mDateDisplay.getText().toString(),
                                 sexo,
                                 Codificacion.Encriptar(email.getText().toString()),
                                 Codificacion.Encriptar( password.getText().toString())

                         );
                         awesomeValidation.clear();
                         // Here, we are sure that form is successfully validated. So, do your stuffs now...
                         // Toast.makeText(getActivity(), "Se ha registrado con exito: sexo: "+sexo, Toast.LENGTH_LONG).show();
                     }
                 }else {

                     InternetExcepcion.validarInternet(getActivity().getLayoutInflater(), context);
                 }

            }
        });
        ir_inicio = (Button) view.findViewById(R.id.btmRegisterLogin);
        ir_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).selectTab(0);
            }
        });
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

    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("https://ayudaucab.herokuapp.com/api/usuarios");

            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("us_nombre_usuario", params[0]);
                dato.put("us_nombre", params[1]);
                dato.put("us_apellido", params[2]);
                dato.put("us_fecha_nacimiento", params[3]);
                dato.put("us_genero", params[4]);
                dato.put("us_email", params[5]);
                dato.put("us_password", params[6]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);

                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("Lo que envia: "+respStr);
                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            { // SI HAY UN ERROR DE CONEXION
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
                awesomeValidation.clear();
                System.out.println("Se ha registrado");
                Toast.makeText(getActivity(), "Se ha registrado con exito ", Toast.LENGTH_LONG).show();
                hidepDialog();
                ((LoginActivity)getActivity()).selectTab(0);

            }else{
                awesomeValidation.clear();
                System.out.println("No se ha registrado");
                hidepDialog();
                Toast.makeText(getActivity(), "No se ha registrado, intente con otro Nombre de usuario o email", Toast.LENGTH_LONG).show();
            }
        }
    }
    /*public void onClick(View v) {
        switch (v.getId()){
            case R.id.myDatePickerButtonRegistro:
                obtenerFecha();
                break;
        }
    }*/
    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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
