package org.sazonpt.model;

import java.time.LocalDate;

public class Imagen {
    private int id_imagen;
    private int id_restaurante;
    private LocalDate fecha_subida;
    private String ruta_archivoI;
    private boolean estado;

    // Constructor por defecto
    public Imagen() {
    }

    public Imagen(int id_imagen, int id_restaurante, LocalDate fecha_subida, String ruta_archivoI, boolean estado){
        this.id_imagen = id_imagen;
        this.id_restaurante = id_restaurante;
        this.fecha_subida = fecha_subida;
        this.ruta_archivoI = ruta_archivoI;
        this.estado = estado;
    }

    // Getters nuevos
    public int getId_imagen(){return id_imagen;}
    public int getId_restaurante(){return id_restaurante;}
    public LocalDate getFecha_subida(){return fecha_subida;}
    public String getRuta_archivoI(){return ruta_archivoI;}
    public boolean getEstado(){return estado;}

    // Setters nuevos
    public void setId_imagen(int id_imagen) {this.id_imagen = id_imagen;}
    public void setId_restaurante(int id_restaurante) {this.id_restaurante = id_restaurante;}
    public void setFecha_subida(LocalDate fecha_subida) {this.fecha_subida = fecha_subida;}
    public void setRuta_archivoI(String ruta_archivoI) {this.ruta_archivoI = ruta_archivoI;}
    public void setEstado(boolean estado) {this.estado = estado;}

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getCodigo_restarante(){return id_restaurante;}  // Mantener typo para compatibilidad
    public void setEstadoI(boolean estado){this.estado = estado;}
}
