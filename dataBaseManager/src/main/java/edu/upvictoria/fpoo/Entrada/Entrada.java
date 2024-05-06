package edu.upvictoria.fpoo.Entrada;

import java.io.BufferedReader;
import java.io.IOException;

import edu.upvictoria.fpoo.Input;

public class Entrada {

    private Input input = new Input();

    public void evaluar(){

        String contenido = consulta();

                

    }

    private String consulta() {
        String a, contenido = "";
        boolean bandera = true;
        while (bandera) {
            BufferedReader obj = input.entradaBuffered();
            try {
                a = obj.readLine();
                if(a != null){
                    contenido += a;
                }else{
                    bandera = false;
                }
                if(contenido.endsWith(";")){
                    bandera = false;
                }
            } catch (IOException e) {
                System.out.println("Algo salio mal");
            }
        }
        return contenido;
    }

}
