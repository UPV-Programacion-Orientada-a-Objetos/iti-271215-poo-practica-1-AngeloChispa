package edu.upvictoria.fpoo.Atributos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.upvictoria.fpoo.ManejoArchivo;

/**
 * Atributo
 */
public class Tabla {

    private String nombre;
    private ArrayList<Atributo> atributos = new ArrayList<>();
    private ManejoArchivo archivo = new ManejoArchivo();
 
    public void crearAtributo(String nombreTabla ,String name, String tipo, boolean isNull, boolean key, ArrayList<String> datos){

        this.nombre = nombreTabla;
        atributos.add(new Atributo(name, tipo, isNull, key, datos));

    }

    public void crearTabla(String path){
        String guardar = "", guardarSecreto = "";

        for(Atributo a : atributos){
            guardar += a.getNombre() + ",";
            for(String b : a.datos){
                guardarSecreto += b + ",";
            }
        }
        //guardar += "\n";   


        try {
            archivo.crearArchivo(path, this.nombre + ".csv");
            archivo.crearArchivo(path,  "." + this.nombre + ".csv");
        } catch (IOException e) {
            System.out.println("Algo salio mal");
        }

        archivo.addToFile(path + File.separator + this.nombre + ".csv", guardar);
        archivo.addToFile(path + File.separator + "." +  this.nombre + ".csv", guardarSecreto);
    }

}