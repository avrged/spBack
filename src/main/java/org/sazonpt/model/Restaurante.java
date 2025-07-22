package org.sazonpt.model;

public class Restaurante {
    private int id_restaurante;
    private int id_solicitud_aprobada;
    private int id_zona;
    private String nombre;
    private String direccion;
    private String horario;
    private String telefono;
    private String etiquetas;
    private String imagen1;
    private String imagen2;
    private String imagen3;
    private String facebook;
    private String instagram;

    // Constructor por defecto
    public Restaurante() {
    }

    public Restaurante(int id_restaurante, int id_solicitud_aprobada, int id_zona, String nombre, String direccion, String horario, String telefono, String etiquetas,
                       String imagen1, String imagen2, String imagen3, String facebook, String instagram) {
        this.id_restaurante = id_restaurante;
        this.id_solicitud_aprobada = id_solicitud_aprobada;
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.direccion = direccion;
        this.horario = horario;
        this.telefono = telefono;
        this.etiquetas = etiquetas;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.imagen3 = imagen3;
        this.facebook = facebook;
        this.instagram = instagram;
    }

    // Getters nuevos
    public int getIdRestaurante(){return id_restaurante;}
    public int getId_solicitud_aprobada(){return id_solicitud_aprobada;}
    public int getId_zona(){return id_zona;}
    public String getImagen1() { return imagen1; }
    public String getImagen2() { return imagen2; }
    public String getImagen3() { return imagen3; }
    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }

    // Setters nuevos
    public void setId_restaurante(int id_restaurante) {this.id_restaurante = id_restaurante;}
    public void setId_solicitud_aprobada(int id_solicitud_aprobada) {this.id_solicitud_aprobada = id_solicitud_aprobada;}
    public void setId_zona(int id_zona) {this.id_zona = id_zona;}
    public void setImagen1(String imagen1) { this.imagen1 = imagen1; }
    public void setImagen2(String imagen2) { this.imagen2 = imagen2; }
    public void setImagen3(String imagen3) { this.imagen3 = imagen3; }
    public void setFacebook(String facebook) { this.facebook = facebook; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public void setNombre(String nombre){this.nombre = nombre;}
    public String getNombre(){return nombre;}

    public void setDireccion(String direccion){this.direccion = direccion;}
    public String getDireccion(){return direccion;}

    public void setHorario(String horario){this.horario = horario;}
    public String getHorario(){return horario;}

    public void setTelefono(String telefono){this.telefono = telefono;}
    public String getTelefono(){return telefono;}

    public void setEtiquetas(String etiquetas){this.etiquetas = etiquetas;}
    public String getEtiquetas(){return etiquetas;}

}
