package org.sazonpt.model;
import java.time.LocalDate;

public class Solicitud_registro {
    private int id_solicitud;
    private int id_restaurantero;
    private LocalDate fecha;
    private String estado;
    private String restaurante;
    private String correo;
    private String direccion;
    private String imagen1;
    private String imagen2;
    private String imagen3;
    private String comprobante;
    private String propietario;
    private String numero;
    private String horario;

    public Solicitud_registro() {
        this.estado = "pendiente";
        this.propietario = "";
        this.numero = "";
        this.horario = "";
    }

    public Solicitud_registro(int id_restaurantero, LocalDate fecha, String restaurante, String correo, String direccion, String imagen1, String comprobante) {
        this.id_restaurantero = id_restaurantero;
        this.fecha = fecha;
        this.estado = "pendiente";
        this.restaurante = restaurante;
        this.correo = correo;
        this.direccion = direccion;
        this.imagen1 = imagen1;
        this.comprobante = comprobante;
        this.numero = "";
        this.horario = "";
    }

    // Constructor con parámetros completo
    public Solicitud_registro(int id_solicitud, int id_restaurantero, LocalDate fecha, String estado, String restaurante, String correo, String direccion, String imagen1, String imagen2, String imagen3, String comprobante, String propietario, String numero, String horario){
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.fecha = fecha;
        this.estado = estado;
        this.restaurante = restaurante;
        this.correo = correo;
        this.direccion = direccion;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.imagen3 = imagen3;
        this.comprobante = comprobante;
        this.propietario = propietario;
        this.numero = numero;
        this.horario = horario;
    }

    // Getters
    public int getId_solicitud(){return id_solicitud;}
    public int getId_restaurantero(){return id_restaurantero;}
    public LocalDate getFecha(){return fecha;}
    public String getEstado(){return estado;}
    public String getRestaurante(){return restaurante;}
    public String getCorreo(){return correo;}
    public String getDireccion(){return direccion;}
    public String getImagen1(){return imagen1;}
    public String getImagen2(){return imagen2;}
    public String getImagen3(){return imagen3;}
    public String getComprobante(){return comprobante;}
    public String getPropietario(){return propietario;}
    public String getNumero(){return numero;}
    public String getHorario(){return horario;}

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

    public void setRestaurante(String restaurante) {
        this.restaurante = restaurante;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public void setImagen2(String imagen2) {
        this.imagen2 = imagen2;
    }

    public void setImagen3(String imagen3) {
        this.imagen3 = imagen3;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
