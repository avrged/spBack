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

    // Constructor por defecto
    public Restaurante() {
    }

    public Restaurante(int id_restaurante, int id_solicitud_aprobada, int id_zona, String nombre, String direccion, String horario, String telefono, String etiquetas) {
        this.id_restaurante = id_restaurante;
        this.id_solicitud_aprobada = id_solicitud_aprobada;
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.direccion = direccion;
        this.horario = horario;
        this.telefono = telefono;
        this.etiquetas = etiquetas;
    }

    // Getters nuevos
    public int getIdRestaurante(){return id_restaurante;}
    public int getId_solicitud_aprobada(){return id_solicitud_aprobada;}
    public int getId_zona(){return id_zona;}

    // Setters nuevos
    public void setId_restaurante(int id_restaurante) {this.id_restaurante = id_restaurante;}
    public void setId_solicitud_aprobada(int id_solicitud_aprobada) {this.id_solicitud_aprobada = id_solicitud_aprobada;}
    public void setId_zona(int id_zona) {this.id_zona = id_zona;}

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

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getSolicitud_aprobada(){return id_solicitud_aprobada;}
    public int getCodigo_zona(){return id_zona;}
}
