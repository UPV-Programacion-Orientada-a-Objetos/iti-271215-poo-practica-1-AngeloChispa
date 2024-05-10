package edu.upvictoria.fpoo.Entrada;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.upvictoria.fpoo.Input;
import edu.upvictoria.fpoo.ManejoArchivo;
import edu.upvictoria.fpoo.Atributos.Tabla;
import edu.upvictoria.fpoo.Exceptions.InvalidSentenceFormatException;
import edu.upvictoria.fpoo.Exceptions.InvalidTableNameException;
import edu.upvictoria.fpoo.Exceptions.PathIsNullException;

public class Entrada {

    private Input input = new Input();
    private Tabla tabla = new Tabla();
    //Quitar la inicializacón
    private String ruta = "/home/yisuscena/Escritorio/baseDatos";
    private ManejoArchivo archivo = new ManejoArchivo();
    private String[] dataTypes = { " INT", "\tINT", "\tVARCHAR", " VARCHAR" };
    private String[] keywords = {" INT", "\tINT", "\tVARCHAR", " VARCHAR", " NOT", "\tNOT", " NULL", "\tNULL",
            " PRIMARY", "\tPRIMARY", " KEY", "\tKEY" };

    public void evaluar() throws InvalidSentenceFormatException, InvalidTableNameException, PathIsNullException {
        String contenido = consulta();
        if (contenido.contains("USE ") || contenido.contains("USE\t")) {
            if (!(validar(contenido, "USE"))) {
                throw new InvalidSentenceFormatException();
            }
            useFuncion(contenido);
        } else if (contenido.contains("CREATE ") || contenido.contains("CREATE\t")) {
            if(ruta.isBlank()){
                throw new PathIsNullException();
            }
            if (!(validar(contenido, "CREATE"))
                    || (!(contenido.contains("TABLE\t")) && !(contenido.contains("TABLE ")))) {
                throw new InvalidSentenceFormatException();
            }
            if (!(validarTWS(contenido, "CREATE", "TABLE"))) {
                throw new InvalidSentenceFormatException();
            }
            createTable(contenido);
        }else if(contenido.contains("SHOW ") || contenido.contains("SHOW\t")){
            if(ruta.isBlank()){
                throw new PathIsNullException();
            }
            if(!(validar(contenido, "SHOW"))){
                throw new InvalidSentenceFormatException();
            }
            if(!(validarTWS(contenido, "SHOW", "TABLES"))){
                throw new InvalidSentenceFormatException();
            }
            showTables();
        }else if(contenido.contains("DROP ") || contenido.contains("DROP\t")){
            if(ruta.isBlank()){
                throw new PathIsNullException();
            }
            if(!(validar(contenido, "DROP"))){
                throw new InvalidSentenceFormatException();
            }
            if(!(validarTWS(contenido, "DROP", "TABLE"))){
                throw new InvalidSentenceFormatException();
            }
            dropTable(contenido.toUpperCase());
        }
        else {
            throw new InvalidSentenceFormatException();
        }
    }

    private boolean validarTWS(String contenido, String keyWordOne, String keyWordTwo) {
        contenido = contenido.trim();
        if ((contenido.indexOf(keyWordOne) + keyWordOne.length()) == (contenido.indexOf(keyWordTwo))) {
            return false;
        }
        return validar(contenido.substring(keyWordOne.length()), keyWordTwo);
    }

    private boolean validar(String contenido, String keyword) {
        contenido = contenido.trim();
        if (contenido.indexOf(keyword) != 0) {
            return false;
        }
        return true;
    }

    private void dropTable(String contenido){
        String ubicacion, ubicacionOculta;
        ubicacion = ruta + File.separator +  contenido.substring(contenido.indexOf("TABLE") + 5, contenido.length()-1).trim() + ".csv";
        ubicacionOculta = ruta + File.separator + "." + contenido.substring(contenido.indexOf("TABLE") + 5, contenido.length()-1).trim() + ".csv";
        System.out.println(ubicacion);
        try {
            archivo.existeDirectorio(ubicacion);
        } catch (Exception e) {
            System.out.println("El archivo no existe");
        }
        System.out.println("La tabla se borrara permanentemente desea continuar S/n  ");
        if(input.entradaString().toUpperCase().equals("S")){
            File file = new File(ubicacion);
            file.delete();
            file = new File(ubicacionOculta);
            file.delete();
        }
    }

    private void useFuncion(String contenido) throws InvalidSentenceFormatException {
        contenido = contenido.substring(3, contenido.length() - 1).trim();
        try {
            archivo.existeDirectorio(contenido);
            ruta = contenido;
            System.out.println(ruta);
        } catch (IOException e) {
            throw new InvalidSentenceFormatException();
        }
    }

    private void showTables(){
        System.out.println();
        File file = new File(ruta);
        File[] archivos = file.listFiles();
        for(File a : archivos){
            if(!(a.getName().indexOf(".") == 0)){
                System.out.println(a.getName().substring(0,a.getName().indexOf(".csv")));
            }
            
        }
    }

    private void createTable(String contenido) throws InvalidTableNameException {
        ArrayList<ArrayList<String>> listaPadre = new ArrayList<>();
        boolean key = false , isNull = false;
        if (contenido.contains("(") && contenido.contains(")")) {
            String nombre = validateName(contenido.substring(contenido.indexOf("TABLE") + 5, contenido.indexOf("(")).trim().toUpperCase());
            validarAtributo(contenido.substring(contenido.indexOf("(") + 1, contenido.indexOf(")")), listaPadre);
            for(ArrayList<String> a : listaPadre){
                if(a.contains("NULL")){
                    isNull = true;
                }
                if(a.contains("PRIMARY") && a.contains("KEY")){
                    key = true;
                }
                tabla.crearAtributo(nombre, a.get(0), a.get(1), isNull, key, a);
            }
            tabla.crearTabla(ruta);
        } else {
            throw new InvalidSentenceFormatException();
        }
    }
    

    private void validarAtributo(String contenido, ArrayList<ArrayList<String>> listaPadre) throws InvalidSentenceFormatException {
        boolean bandera = false;
        String temp = "";
        partirAtributo(contenido, listaPadre);
        for (ArrayList<String> a : listaPadre) {
            a.set(0, validateName(a.get(0).toUpperCase()));
            for (String c : dataTypes) {
                if (a.get(1).equals(c.trim())) {
                    bandera = true;
                }
            }
            if (!bandera) {
                throw new InvalidSentenceFormatException();
            }
            bandera = false;
            for (int i = 2; i < a.size(); i++) {
                if(i+1 < a.size()){
                    for(int j = i+1; j<a.size(); j++){
                        if(a.get(i).equals(a.get(j))){
                            throw new InvalidSentenceFormatException();
                        }
                    }
                }
                for (int j = 4; j<keywords.length; j++ ) {
                    if (a.get(i).equals(keywords[j].trim())) {
                        bandera = true;
                        break;
                    }
                }
                if (!bandera) {
                    throw new InvalidSentenceFormatException();
                }
                bandera = false;
            }
            System.out.println(a);
        }
    }

    private void partirAtributo(String contenido, ArrayList<ArrayList<String>> listaPadre)
            throws InvalidSentenceFormatException {
        boolean bandera = false;
        if(contenido.isBlank()){
            throw new InvalidSentenceFormatException();
        }
        contenido = contenido + ",";
        String[] raw = contenido.split(",");
        for (String a : raw) {
            ArrayList<String> lista = new ArrayList<>();
            int temp = 0;
            for (String b : keywords) {
                if (a.contains(b)) {
                    try {
                        lista.add(a.substring(temp, a.indexOf(b)).trim());
                    } catch (StringIndexOutOfBoundsException e) {
                        throw new InvalidSentenceFormatException();
                    }
                    temp += a.substring(temp, a.indexOf(b)).length();
                    bandera = true;
                }
            }
            if (bandera) {
                lista.add(a.substring(temp).trim());
                listaPadre.add(lista);
            }else{
                throw new InvalidSentenceFormatException();
            }
        }
    }

    private String validateName(String contenido) {
        boolean bandera = false;
        String temp = archivo.fileToString("LegalCharacters.csv");
        String[] caracter = temp.split(",");
        temp = archivo.fileToString("IllegalWords.csv");
        String[] palabras = temp.split(",");
        for (String a : palabras) {
            if (contenido.equals(a)) {
                throw new InvalidTableNameException();
            }
        }
        for (int i = 0; i < contenido.length(); i++) {
            for (int j = 0; j < caracter.length; j++) {
                if (new String(new char[]{contenido.charAt(i)}).equals(caracter[j])) {
                    bandera = true;
                    break;
                }
            }
            if (!bandera) {
                throw new InvalidTableNameException();
            }
            bandera = false;
        }
        return contenido;
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
