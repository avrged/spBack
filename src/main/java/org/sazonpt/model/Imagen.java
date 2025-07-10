package org.sazonpt.model;

import java.time.LocalDate;

public class Imagen {
    private int id_imagen;
    private int codigo_restarante;
    private LocalDate fecha_subida;
    private String ruta_archivoI;
    private  boolean estado;

    public Imagen(int id_imagen, int codigo_restarante, LocalDate fecha_subida, String ruta_archivoI, boolean estado){
        this.id_imagen=id_imagen;
        this.codigo_restarante=codigo_restarante;
        this.fecha_subida=fecha_subida;
        this.ruta_archivoI=ruta_archivoI;
        this.estado=estado;
    }

    public int getId_imagen(){return id_imagen;}
    public int getCodigo_restarante(){return codigo_restarante;}
    public LocalDate getFecha_subida(){return fecha_subida;}
    public String getRuta_archivoI(){return ruta_archivoI;}

    public  void setEstadoI(boolean estado){this.estado=estado;}
    public boolean getEstado(){return estado;}
}
