package edu.upvictoria.fpoo.Atributos;

import java.util.ArrayList;

public class Atributo {
    private String nombre;
    private Object tipo;
    private boolean isNull;
    private String primaryKey; 
    protected ArrayList<Object> instancias = new ArrayList<>();
    protected ArrayList<String> datos;

    public Atributo(String name, String tipo, boolean isNull, boolean key, ArrayList<String> datos){
        this.datos = datos;
        this.nombre = name;
        this.isNull = isNull;
        if(key){
            this.primaryKey = this.nombre;
        }

    }
    
    public String getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Object getTipo() {
        return tipo;
    }
    public void setTipo(Object tipo) {
        this.tipo = tipo;
    }
    public boolean isNull() {
        return isNull;
    }
    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }
}
