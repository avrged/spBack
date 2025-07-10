package org.sazonpt.model;

public class Zona {
    private int id_zona;
    private final String nombre;
    private String descripcion;

    public Zona(int id_zona, String nombre, String descripcion){
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public  int getId_zona(){return id_zona;}
    public String getNombre(){return nombre;}
    public String getDescripcion(){return descripcion;}
}
