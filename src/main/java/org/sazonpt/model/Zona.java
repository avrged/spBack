package org.sazonpt.model;

public class Zona {
    private int id_zona;
    private String nombre;
    private String descripcion;

    // Constructor por defecto requerido por Jackson
    public Zona() {
    }

    public Zona(int id_zona, String nombre, String descripcion){
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Constructor sin id_zona para crear nuevas zonas
    public Zona(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public  int getId_zona(){return id_zona;}
    public String getNombre(){return nombre;}
    public String getDescripcion(){return descripcion;}

    // Setters requeridos por Jackson
    public void setId_zona(int id_zona) { this.id_zona = id_zona; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
