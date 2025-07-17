package org.sazonpt.model;

import java.time.LocalDate;

public class Comprobante {
    private int id_comprobante;
    private int id_restaurante;
    private String ruta_archivoC;
    private LocalDate fecha_subida;

    // Constructor por defecto
    public Comprobante() {
    }

    public Comprobante(int id_comprobante, int id_restaurante, String ruta_archivoC, LocalDate fecha_subida){
        this.id_comprobante = id_comprobante;
        this.id_restaurante = id_restaurante;
        this.ruta_archivoC = ruta_archivoC;
        this.fecha_subida = fecha_subida;
    }

    // Getters nuevos
    public int getIdComprobante(){return id_comprobante;}
    public int getId_restaurante(){return id_restaurante;}
    public String getRuta_Archivo(){return ruta_archivoC;}
    public LocalDate getFecha(){return fecha_subida;}

    // Setters nuevos
    public void setId_comprobante(int id_comprobante) {this.id_comprobante = id_comprobante;}
    public void setId_restaurante(int id_restaurante) {this.id_restaurante = id_restaurante;}
    public void setRuta_archivoC(String ruta_archivoC) {this.ruta_archivoC = ruta_archivoC;}
    public void setFecha_subida(LocalDate fecha_subida) {this.fecha_subida = fecha_subida;}

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getCodigo_Rest(){return id_restaurante;}
}
