package org.sazonpt.model;

import java.time.LocalDate;

public class Solicitud_registro {
    private int id_solicitud;
    private int codigo_restaurantero;
    private LocalDate fecha;
    private boolean estado;
    private String nombre_propuesto_restaurante;
    private String correo;
    private String direccion_propuesta;

    public Solicitud_registro(int id_solicitud, int codigo_restaurantero, LocalDate fecha, boolean estado, String nombre_propuesto_restaurante, String correo, String direccion_propuesta){
        this.id_solicitud = id_solicitud;
        this.codigo_restaurantero = codigo_restaurantero;
        this.fecha = fecha;
        this.estado = estado;
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
        this.correo = correo;
        this.direccion_propuesta = direccion_propuesta;
    }

    public int getSolicitudR(){return id_solicitud;}

    public int getCodigoRestaurantero(){return codigo_restaurantero;}
    public LocalDate getFecha(){return fecha;}

    public void setEstadoSR(boolean estado){this.estado=estado;}
    public boolean getEstado(){return estado;}
    public String getNombrePropuesto(){return nombre_propuesto_restaurante;}
    public String getCorreo(){return correo;}
    public String getDireccionPropuesta(){return direccion_propuesta;}
}
