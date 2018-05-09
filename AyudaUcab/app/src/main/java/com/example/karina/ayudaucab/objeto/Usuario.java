package com.example.karina.ayudaucab.objeto;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private int us_id =-1;
    private String us_nombre_usuario;
    private String us_nombre;
    private String us_apellido;
    private String us_fecha_nacimiento;
    private char us_genero;
    private String us_email;
    private String us_password;
    private int us_nivel_juego = 0;



    //COnstructor Vacio
    public Usuario (){

    }

    // Constructor Para instanciar con todos los Atributos
    public Usuario(int us_id, String us_nombre_usuario, String us_nombre, String us_apellido, String us_fecha_nacimiento, char us_genero, String us_email, String us_password, int us_nivel_juego) {
        this.us_id = us_id;
        this.us_nombre_usuario = us_nombre_usuario;
        this.us_nombre = us_nombre;
        this.us_apellido = us_apellido;
        this.us_fecha_nacimiento = us_fecha_nacimiento;
        this.us_genero = us_genero;
        this.us_email = us_email;
        this.us_password = us_password;
        this.us_nivel_juego = us_nivel_juego;
    }

    //Constructor para instanciar con todos los atributos sin el US_ID
    public Usuario(String us_nombre_usuario, String us_nombre, String us_apellido, String us_fecha_nacimiento, char us_genero, String us_email, String us_password, int us_nivel_juego) {
        this.us_nombre_usuario = us_nombre_usuario;
        this.us_nombre = us_nombre;
        this.us_apellido = us_apellido;
        this.us_fecha_nacimiento = us_fecha_nacimiento;
        this.us_genero = us_genero;
        this.us_email = us_email;
        this.us_password = us_password;
        this.us_nivel_juego = us_nivel_juego;
    }

    public int getUs_id() {
        return us_id;
}

    public void setUs_id(int us_id) {
        this.us_id = us_id;
    }

    public String getUs_nombre_usuario() {
        return us_nombre_usuario;
    }

    public void setUs_nombre_usuario(String us_nombre_usuario) {
        this.us_nombre_usuario = us_nombre_usuario;
    }

    public String getUs_nombre() {
        return us_nombre;
    }

    public void setUs_nombre(String us_nombre) {
        this.us_nombre = us_nombre;
    }

    public String getUs_apellido() {
        return us_apellido;
    }

    public void setUs_apellido(String us_apellido) {

        this.us_apellido = us_apellido;
    }

    public String getUs_fecha_nacimiento() {
        return us_fecha_nacimiento;
    }

    public void setUs_fecha_nacimiento(String us_fecha_nacimiento) {

        this.us_fecha_nacimiento = us_fecha_nacimiento;

    }

    public char getUs_genero() {
        return us_genero;
    }

    public void setUs_genero(char us_genero) {
        this.us_genero = us_genero;
    }

    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String us_email) {
        this.us_email = us_email;
    }

    public String getUs_password() {
        return us_password;
    }

    public void setUs_password(String us_password) {
        this.us_password = us_password;
    }

    public int getUs_nivel_juego() {
        return us_nivel_juego;
    }

    public void setUs_nivel_juego(int us_nivel_juego) {
        this.us_nivel_juego = us_nivel_juego;
    }


    protected Usuario(Parcel in) {
        us_id = in.readInt();
        us_nombre_usuario = in.readString();
        us_nombre = in.readString();
        us_apellido = in.readString();
        us_fecha_nacimiento = in.readString();
        us_genero = (char) in.readValue(char.class.getClassLoader());
        us_email = in.readString();
        us_password = in.readString();
        us_nivel_juego = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(us_id);
        dest.writeString(us_nombre_usuario);
        dest.writeString(us_nombre);
        dest.writeString(us_apellido);
        dest.writeString(us_fecha_nacimiento);
        dest.writeValue(us_genero);
        dest.writeString(us_email);
        dest.writeString(us_password);
        dest.writeInt(us_nivel_juego);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };


}