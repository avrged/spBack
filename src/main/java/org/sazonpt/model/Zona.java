package org.sazonpt.model;

public class Zona {
    
    private int id_zona;
    private String nombre;
    private int id_restaurantero;
    
    public Zona() {
    }
    
    public Zona(int id_zona, String nombre, int id_restaurantero) {
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.id_restaurantero = id_restaurantero;
    }
    
    public Zona(String nombre, int id_restaurantero) {
        this.nombre = nombre;
        this.id_restaurantero = id_restaurantero;
    }
    
    public int getId_zona() {
        return id_zona;
    }
    
    public void setId_zona(int id_zona) {
        this.id_zona = id_zona;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getId_restaurantero() {
        return id_restaurantero;
    }
    
    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }
    
    @Override
    public String toString() {
        return "Zona{" +
                "id_zona=" + id_zona +
                ", nombre='" + nombre + '\'' +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Zona zona = (Zona) o;
        
        return id_zona == zona.id_zona;
    }
    
    @Override
    public int hashCode() {
        return id_zona;
    }
}
