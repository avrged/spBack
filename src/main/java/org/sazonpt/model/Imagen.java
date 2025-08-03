package org.sazonpt.model;

import java.time.LocalDateTime;

public class Imagen {
    
    private int id_imagen;
    private String ruta_imagen;  // Campo para almacenar la ruta de la imagen
    private LocalDateTime fecha_subida;
    private int id_restaurante;
    private int id_solicitud;
    private int id_restaurantero;
    
    // Constructor por defecto
    public Imagen() {
    }
    
    // Constructor completo
    public Imagen(int id_imagen, String ruta_imagen, LocalDateTime fecha_subida, 
                  int id_restaurante, int id_solicitud, int id_restaurantero) {
        this.id_imagen = id_imagen;
        this.ruta_imagen = ruta_imagen;
        this.fecha_subida = fecha_subida;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Constructor sin ID (para crear nuevos registros)
    public Imagen(String ruta_imagen, LocalDateTime fecha_subida, 
                  int id_restaurante, int id_solicitud, int id_restaurantero) {
        this.ruta_imagen = ruta_imagen;
        this.fecha_subida = fecha_subida;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Getters y Setters
    public int getId_imagen() {
        return id_imagen;
    }
    
    public void setId_imagen(int id_imagen) {
        this.id_imagen = id_imagen;
    }
    
    public String getRuta_imagen() {
        return ruta_imagen;
    }
    
    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }
    
    public LocalDateTime getFecha_subida() {
        return fecha_subida;
    }
    
    public void setFecha_subida(LocalDateTime fecha_subida) {
        this.fecha_subida = fecha_subida;
    }
    
    public int getId_restaurante() {
        return id_restaurante;
    }
    
    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }
    
    public int getId_solicitud() {
        return id_solicitud;
    }
    
    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }
    
    public int getId_restaurantero() {
        return id_restaurantero;
    }
    
    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }
    
    @Override
    public String toString() {
        return "Imagen{" +
                "id_imagen=" + id_imagen +
                ", ruta_imagen='" + ruta_imagen + '\'' +
                ", fecha_subida=" + fecha_subida +
                ", id_restaurante=" + id_restaurante +
                ", id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Imagen imagen = (Imagen) o;
        
        if (id_imagen != imagen.id_imagen) return false;
        if (id_restaurante != imagen.id_restaurante) return false;
        if (id_solicitud != imagen.id_solicitud) return false;
        return id_restaurantero == imagen.id_restaurantero;
    }
    
    @Override
    public int hashCode() {
        int result = id_imagen;
        result = 31 * result + id_restaurante;
        result = 31 * result + id_solicitud;
        result = 31 * result + id_restaurantero;
        return result;
    }
}
