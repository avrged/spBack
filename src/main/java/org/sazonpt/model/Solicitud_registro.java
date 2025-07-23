package org.sazonpt.model;
import java.time.LocalDate;

public class Solicitud_registro {
    private int id_solicitud;
    private LocalDate fecha;
    private String estado;
    private String restaurante;
    private String correo;
    private String direccion;
    private String imagen1;
    private String imagen2;
    private String imagen3;
    private String comprobante;
    private String menu;
    private String propietario;
    private String numero;
    private String horario;
    private String facebook;
    private String instagram;
    private String etiqueta1;
    private String etiqueta2;
    private String etiqueta3;

    public Solicitud_registro() {
        this.estado = "pendiente";
        this.propietario = "";
        this.numero = "";
        this.horario = "";
        this.facebook = "";
        this.instagram = "";
        this.menu = "";
        this.etiqueta1 = "";
        this.etiqueta2 = "";
        this.etiqueta3 = "";
    }

    // Constructor reducido sin id_restaurantero
    public Solicitud_registro(LocalDate fecha, String restaurante, String correo, String direccion, String imagen1, String comprobante) {
        this.fecha = fecha;
        this.estado = "pendiente";
        this.restaurante = restaurante;
        this.correo = correo;
        this.direccion = direccion;
        this.imagen1 = imagen1;
        this.comprobante = comprobante;
        this.menu = "";
        this.facebook = "";
        this.instagram = "";
        this.etiqueta1 = "";
        this.etiqueta2 = "";
        this.etiqueta3 = "";
    }

    // Constructor con parámetros completo
    public Solicitud_registro(int id_solicitud, LocalDate fecha, String estado, String restaurante, String correo, String direccion, String imagen1, String imagen2, String imagen3, String menu, String comprobante, String propietario, String numero, String horario, String facebook, String instagram, String etiqueta1, String etiqueta2, String etiqueta3){
        this.id_solicitud = id_solicitud;
        this.fecha = fecha;
        this.estado = estado;
        this.restaurante = restaurante;
        this.correo = correo;
        this.direccion = direccion;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.imagen3 = imagen3;
        this.menu = menu;
        this.comprobante = comprobante;
        this.propietario = propietario;
        this.numero = numero;
        this.horario = horario;
        this.facebook = facebook;
        this.instagram = instagram;
        this.etiqueta1 = etiqueta1;
        this.etiqueta2 = etiqueta2;
        this.etiqueta3 = etiqueta3;
    }

    // Getters
    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getMenu() { return menu; }
    public int getId_solicitud(){return id_solicitud;}
    public String getEtiqueta1() { return etiqueta1; }
    public String getEtiqueta2() { return etiqueta2; }
    public String getEtiqueta3() { return etiqueta3; }
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
    public void setFacebook(String facebook) { this.facebook = facebook; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    public void setMenu(String menu) { this.menu = menu; }
    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public void setEtiqueta1(String etiqueta1) {
        this.etiqueta1 = etiqueta1;
    }
    public void setEtiqueta2(String etiqueta2) {
        this.etiqueta2 = etiqueta2;
    }
    public void setEtiqueta3(String etiqueta3) {
        this.etiqueta3 = etiqueta3;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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
