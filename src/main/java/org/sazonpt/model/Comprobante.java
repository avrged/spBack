package org.sazonpt.model;

import java.time.LocalDate;

public class Comprobante {
    private int id_comprobante;
    private int codigo_restaurante;
    private String ruta_archivoC;
    private LocalDate fecha_subida;

    public Comprobante(int id_comprobante, int codigo_restaurante, String ruta_archivoC, LocalDate fecha_subida){
        this.id_comprobante=id_comprobante;
        this.codigo_restaurante=codigo_restaurante;
        this.ruta_archivoC=ruta_archivoC;
        this.fecha_subida=fecha_subida;
    }

    public int getIdComprobante(){return id_comprobante;}
    public int getCodigo_Rest(){return codigo_restaurante;}
    public String getRuta_Archivo(){return ruta_archivoC;}
    public LocalDate getFecha(){return fecha_subida;}
}
