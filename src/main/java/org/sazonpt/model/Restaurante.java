package org.sazonpt.model;

public class Restaurante {
    private int id_restaurante;
    private int codigo_solicitud_aprobada;
    private int codigo_zona;
    private String nombre;
    private String direccion;
    private String horario;
    private String telefono;
    private String etiquetas;

    public Restaurante(int id_restaurante, int codigo_solicitud_aprobada, int codigo_zona, String nombre, String direccion, String horario, String telefono, String etiquetas) {
        this.id_restaurante = id_restaurante;
        this.codigo_solicitud_aprobada = codigo_solicitud_aprobada;
        this.codigo_zona = codigo_zona;
        this.nombre = nombre;
        this.direccion = direccion;
        this.horario = horario;
        this.telefono = telefono;
        this.etiquetas = etiquetas;
    }

    public int getIdRestaurante(){return  id_restaurante;}
    public  int getSolicitud_aprobada(){return codigo_solicitud_aprobada;}
    public  int getCodigo_zona(){return codigo_zona;}

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
