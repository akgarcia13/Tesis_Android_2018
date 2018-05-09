package com.example.karina.ayudaucab.menu.login;

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
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.codificacion.Codificacion;
import com.example.karina.ayudaucab.mail.MailJob;
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
 * {@link FargotPasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FargotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FargotPasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FargotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FargotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FargotPasswordFragment newInstance(String param1, String param2) {
        FargotPasswordFragment fragment = new FargotPasswordFragment();
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


    private AwesomeValidation awesomeValidation;
    private EditText email;
    private Button  registrar,ir_inicio;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fargot_password, container, false);

        context = view.getContext();
        email = (EditText) view.findViewById(R.id.etEmail2);

        awesomeValidation.addValidation(email, Patterns.EMAIL_ADDRESS,"Email invàlido: Por ejemplo xxx@xxx.com");
        registrar = (Button) view.findViewById(R.id.bRegister2);
        registrar.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // do something


                if(InternetExcepcion.isOnline(context) ){
                    if (awesomeValidation.validate()) {

                        progreso = new ProgressDialog(getContext());
                        progreso.setMessage("Enviando Contraseña por favor espere...");
                        progreso.setCancelable(false);
                        showpDialog();

                        TareaWSRecuperar tarea = new TareaWSRecuperar();
                        tarea.execute(

                                email.getText().toString()


                        );
                        awesomeValidation.clear();
                        // Here, we are sure that form is successfully validated. So, do your stuffs now...
                        // Toast.makeText(getActivity(), "Se ha registrado con exito: sexo: "+sexo, Toast.LENGTH_LONG).show();
                    }

                }else {

                    InternetExcepcion.validarInternet( getActivity().getLayoutInflater(), context);
                }


            }
        });

        ir_inicio = (Button) view.findViewById(R.id.bRegisterInicio);

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

    private class TareaWSRecuperar extends AsyncTask<String,Integer,Boolean> {
        String email,password;
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("https://ayudaucab.herokuapp.com/api/usuarios/recuperar-contrasena");

            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();


                dato.put("us_email", Codificacion.Encriptar(params[0]));

                email = params[0];
                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);

                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                password = Codificacion.Desencriptar(respJSON.getString("us_password"));
                if(password == "null")
                    resul = false;
            }
            catch(Exception ex)
            {
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
                hidepDialog();
                new MailJob("proyecto.ayudaucab@gmail.com", "ayudaucab2018").execute(
                        new MailJob.Mail("proyecto.ayudaucab@gmail.com",
                                email, "Recuperar Contraseña", "Su contraseña es:"+password)
                );
                System.out.println("Se ha enviado la contraseña a su correo");
                Toast.makeText(getActivity(), "Se ha enviado la contraseña a su correo ", Toast.LENGTH_LONG).show();
            }else{
                hidepDialog();
                Toast.makeText(getActivity(), "Correo no registrado: ", Toast.LENGTH_LONG).show();
                System.out.println("Correo no resgistrado");
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
