
package org.sazonpt.model;
import java.time.LocalDate;

public class Solicitud_registro {
    private int id_solicitud;
    private int id_restaurantero;
    private LocalDate fecha;
    private String estado;
    private String nombre_propuesto_restaurante;
    private String correo;
    private String direccion_propuesta;
    private String ruta_imagen;
    private String ruta_comprobante;

    // Constructor por defecto (necesario para Jackson)
    public Solicitud_registro() {
        this.estado = "pendiente"; // Estado por defecto
    }

    // Constructor sin estado (usa "pendiente" por defecto)
    public Solicitud_registro(int id_restaurantero, LocalDate fecha, String nombre_propuesto_restaurante, String correo, String direccion_propuesta, String ruta_imagen, String ruta_comprobante) {
        this.id_restaurantero = id_restaurantero;
        this.fecha = fecha;
        this.estado = "pendiente"; // Estado por defecto
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
        this.correo = correo;
        this.direccion_propuesta = direccion_propuesta;
        this.ruta_imagen = ruta_imagen;
        this.ruta_comprobante = ruta_comprobante;
    }

    // Constructor con parámetros
    public Solicitud_registro(int id_solicitud, int id_restaurantero, LocalDate fecha, String estado, String nombre_propuesto_restaurante, String correo, String direccion_propuesta, String ruta_imagen, String ruta_comprobante){
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.fecha = fecha;
        this.estado = estado;
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
        this.correo = correo;
        this.direccion_propuesta = direccion_propuesta;
        this.ruta_imagen = ruta_imagen;
        this.ruta_comprobante = ruta_comprobante;
    }

    // Getters
    public int getId_solicitud(){return id_solicitud;}
    public int getId_restaurantero(){return id_restaurantero;}
    public LocalDate getFecha(){return fecha;}
    public String getEstado(){return estado;}
    public String getNombrePropuesto(){return nombre_propuesto_restaurante;}
    public String getCorreo(){return correo;}
    public String getDireccionPropuesta(){return direccion_propuesta;}
    public String getRuta_imagen(){return ruta_imagen;}
    public String getRuta_comprobante(){return ruta_comprobante;}

    // Setters (necesarios para Jackson)
    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNombre_propuesto_restaurante(String nombre_propuesto_restaurante) {
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDireccion_propuesta(String direccion_propuesta) {
        this.direccion_propuesta = direccion_propuesta;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public void setRuta_comprobante(String ruta_comprobante) {
        this.ruta_comprobante = ruta_comprobante;
    }

    // Métodos de compatibilidad eliminados
}
