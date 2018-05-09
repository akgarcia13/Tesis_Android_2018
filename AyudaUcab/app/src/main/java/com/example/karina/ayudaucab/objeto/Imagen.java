package com.example.karina.ayudaucab.objeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;

public class Imagen {
    private int ima_id;
    private String ima_nombre;
    private byte[] bytes;
    private String ima_categoria;
    private String ima_respuesta_correcta;

    private static ArrayList<Imagen> imagenRequest = new ArrayList<Imagen>();


    public Imagen(){

    }

    public void addImagenRequest (Imagen imagen){
        imagenRequest.add(imagen);
    }

    public static  Imagen imagenServicio(){
        return imagenRequest.get(0);
    }

    public static void eliminarImagenServicio(){
        imagenRequest.remove(0);
    }

    private String dato;

    private Bitmap imagen;

    public String getDato() {
        return dato;
    }


    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            int alto = 600;
            int acho = 600;
            Bitmap foto = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen = Bitmap.createScaledBitmap(foto,alto,acho,true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }



    public int getIma_id() {
        return ima_id;
    }

    public void setIma_id(int ima_id) {
        this.ima_id = ima_id;
    }

    public String getIma_nombre() {
        return ima_nombre;
    }

    public void setIma_nombre(String ima_nombre) {
        this.ima_nombre = ima_nombre;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getIma_categoria() {
        return ima_categoria;
    }

    public void setIma_categoria(String ima_categoria) {
        this.ima_categoria = ima_categoria;
    }

    public String getIma_respuesta_correcta() {
        return ima_respuesta_correcta;
    }

    public void setIma_respuesta_correcta(String ima_respuesta_correcta) {
        this.ima_respuesta_correcta = ima_respuesta_correcta;
    }

}
