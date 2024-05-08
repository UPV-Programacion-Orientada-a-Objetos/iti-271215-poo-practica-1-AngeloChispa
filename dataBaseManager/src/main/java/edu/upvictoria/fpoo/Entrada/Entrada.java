package edu.upvictoria.fpoo.Entrada;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import edu.upvictoria.fpoo.Input;
import edu.upvictoria.fpoo.ManejoArchivo;
import edu.upvictoria.fpoo.Exceptions.InvalidSentenceFormatException;

public class Entrada {

    private Input input = new Input();
    private String ruta;
    private ManejoArchivo archivo = new ManejoArchivo();

    public void evaluar() throws InvalidSentenceFormatException {
        String contenido = consulta();
        if (contenido.contains("USE")) {
            if (!(validar(contenido, "USE",0))) {
                throw new InvalidSentenceFormatException();
            }
            useFuncion(contenido);
        }else if(contenido.contains("CREATE")){
            if(!(contenido.contains("TABLE")) || !(validar(contenido, "CREATE",0))){
                throw new InvalidSentenceFormatException();
            }
            if(!(validarTWS(contenido, "CREATE", "TABLE", contenido.indexOf("CREATE") + 6))){
                throw new InvalidSentenceFormatException();
            }   
            
        }
        
    }

    private boolean validarTWS(String contenido, String keyWordOne, String keyWordTwo, int inicioBusqueda){

        if((contenido.indexOf(keyWordOne) + keyWordOne.length()) == (contenido.indexOf(keyWordTwo))){
            return false;
        }

        return validar(contenido, keyWordTwo, inicioBusqueda);

    }

    private boolean validar(String contenido, String keyword, int inicioBusqueda) {
        if (contenido.indexOf(keyword) != 0) {
            for (int i = contenido.indexOf(keyword); i > inicioBusqueda; i--) {
                if (!(contenido.substring(contenido.indexOf(keyword) - i, contenido.indexOf(keyword) - i + 1)
                        .equals(" "))
                        && !(contenido.substring(contenido.indexOf(keyword) - i, contenido.indexOf(keyword) - i + 1)
                                .equals("\t"))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void useFuncion(String contenido) {
        contenido = contenido.substring(contenido.indexOf(File.separator), contenido.length() - 1);
        try {
            archivo.existeDirectorio(contenido);
            ruta = contenido;
            System.out.println(ruta);
        } catch (IOException e) {
            System.out.println("El directorio no existe");
        }
    }

    private void createTable(String contenido){



    }

    private String consulta() {
        String a, contenido = "";
        boolean bandera = true;
        while (bandera) {
            BufferedReader obj = input.entradaBuffered();
            try {
                a = obj.readLine();
                if (a != null) {
                    contenido += a;
                } else {
                    bandera = false;
                }
                if (contenido.endsWith(";")) {
                    bandera = false;
                }
            } catch (IOException e) {
                System.out.println("Algo salio mal");
            }
        }
        return contenido;
    }

}
