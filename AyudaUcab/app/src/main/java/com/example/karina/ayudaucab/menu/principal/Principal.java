package com.example.karina.ayudaucab.menu.principal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.memoria.MemoriaJuego;
import com.example.karina.ayudaucab.objeto.Usuario;
import com.example.karina.ayudaucab.quiz.AudioJuego;
import com.example.karina.ayudaucab.quiz.FotoJuego;
import com.example.karina.ayudaucab.user.login.UsuarioLogeado;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Principal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Principal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Principal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Principal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Principal.
     */
    // TODO: Rename and change types and number of parameters
    public static Principal newInstance(String param1, String param2) {
        Principal fragment = new Principal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Usuario perfil_usuario = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       /* Bundle bundle = this.getArguments();
        if(bundle != null){
            perfil_usuario = bundle.getParcelable("perfil_usuario_principal");

        }*/
    }
    private Button goFoto,goAudio,goMemoria,salirJuego;
    private TextView bienvenido;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_principal, container, false);
        goFoto = (Button) view.findViewById(R.id.goFotoJuego);
        goAudio = (Button) view.findViewById(R.id.goAudioJuego);
        goMemoria = (Button) view.findViewById(R.id.goMemoriaJuego);
        bienvenido =(TextView)view.findViewById(R.id.textBienvenido);

        bienvenido.setText("Bienvenido "+ UsuarioLogeado.userLogin().getUs_nombre());
        //salirJuego = (Button) view.findViewById(R.id.salirJuego);
        Fragment fragment;
        goFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here
                Fragment fragment = new FotoJuego();
                /*Bundle bundle = new Bundle();
                bundle.putParcelable("perfil_usuario_fotoJuego",perfil_usuario);
                fragment.setArguments(bundle);*/
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.Contenedor, fragment).
                        commit();

            }
        });
        goAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here
                Fragment fragment = new AudioJuego();
               /* Bundle bundle = new Bundle();
                bundle.putParcelable("perfil_usuario_AudioJuego",perfil_usuario);
                fragment.setArguments(bundle);*/
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.Contenedor, fragment).
                        commit();

            }
        });
        goMemoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                Fragment fragment = new MemoriaJuego();
                /*Bundle bundle = new Bundle();
                bundle.putParcelable("perfil_usuario_Memoria",perfil_usuario);
                fragment.setArguments(bundle);*/
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.Contenedor, fragment).
                        commit();
            }
        });
        /*salirJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here
                Intent intent = new Intent(getActivity(), MenuPrincipal.class);

                Bundle bundle = new Bundle();

                bundle.putParcelable("usuario",null);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();

            }
        });*/
        return view;
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
