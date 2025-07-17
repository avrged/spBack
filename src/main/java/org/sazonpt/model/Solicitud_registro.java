package org.sazonpt.model;

import java.time.LocalDate;

public class Solicitud_registro {
    private int id_solicitud;
    private int id_restaurantero;
    private LocalDate fecha;
    private boolean estado;
    private String nombre_propuesto_restaurante;
    private String correo;
    private String direccion_propuesta;

    // Constructor por defecto (necesario para Jackson)
    public Solicitud_registro() {
    }

    // Constructor con parámetros
    public Solicitud_registro(int id_solicitud, int id_restaurantero, LocalDate fecha, boolean estado, String nombre_propuesto_restaurante, String correo, String direccion_propuesta){
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.fecha = fecha;
        this.estado = estado;
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
        this.correo = correo;
        this.direccion_propuesta = direccion_propuesta;
    }

    // Getters
    public int getSolicitudR(){return id_solicitud;}
    public int getId_restaurantero(){return id_restaurantero;}
    public LocalDate getFecha(){return fecha;}
    public boolean getEstado(){return estado;}
    public String getNombrePropuesto(){return nombre_propuesto_restaurante;}
    public String getCorreo(){return correo;}
    public String getDireccionPropuesta(){return direccion_propuesta;}

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

    public void setEstado(boolean estado) {
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

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getCodigoRestaurantero(){return id_restaurantero;}
    public void setCodigo_restaurantero(int codigo_restaurantero) {
        this.id_restaurantero = codigo_restaurantero;
    }
    public void setEstadoSR(boolean estado){
        this.estado = estado;
    }
}
