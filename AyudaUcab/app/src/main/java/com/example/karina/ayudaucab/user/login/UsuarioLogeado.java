package com.example.karina.ayudaucab.user.login;

import com.example.karina.ayudaucab.objeto.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;

public  class UsuarioLogeado {
    private static ArrayList<Usuario> usuarioLogeado = new ArrayList<Usuario>();

    public  UsuarioLogeado (Usuario user){
        usuarioLogeado.add(user);
    }

    public static Usuario userLogin (){
        return  usuarioLogeado.get(0);
    }

    public  static void userLogOut(){
        usuarioLogeado.remove(0);
    }

    public  static boolean isLogin (){
        if(usuarioLogeado.isEmpty()){
           return  true;
        }
        return  false;
    }
    public static void setAumentoNivel(){
        usuarioLogeado.get(0).setUs_nivel_juego(usuarioLogeado.get(0).getUs_nivel_juego()+1);
    }
}
