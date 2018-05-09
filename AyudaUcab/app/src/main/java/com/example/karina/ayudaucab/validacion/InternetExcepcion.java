package com.example.karina.ayudaucab.validacion;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.karina.ayudaucab.R;
import com.example.karina.ayudaucab.quiz.AudioJuego;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.PendingIntent.getActivity;

public class InternetExcepcion {
    private static ConnectivityManager manager;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean isOnline(Context context) {
        //boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else
            return false;
    }
    /*private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }*/

    public static void validarInternet(LayoutInflater inflater1,Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = inflater1;
        alertDialogBuilder
                .setView(inflater.inflate(R.layout.internet_exepcion,null))
                .setMessage(" !Ops! se necesita estar conectado ")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                            }
                        }

                )
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
