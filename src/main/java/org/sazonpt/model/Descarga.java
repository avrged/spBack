package org.sazonpt.model;

public class Descarga {
    private int id_descarga;
    private int cantidad_descargas;
    private int codigo_adquisicion;
    private String lugar_origen;
    private String opiniones;

    public Descarga(int id_descarga, int cantidad_descargas, int codigo_adquisicion, String lugar_origen, String opiniones){
        this.id_descarga = id_descarga;
        this.cantidad_descargas = cantidad_descargas;
        this.codigo_adquisicion = codigo_adquisicion;
        this.lugar_origen = lugar_origen;
        this.opiniones = opiniones;
    }

    public int getId_descarga() {return id_descarga;}

    public void setCantidad_descargas(int cantidad_descargas){this.cantidad_descargas=cantidad_descargas;}
    public int getCantidad_descargas() {return cantidad_descargas;}
    public int getCodigo_adquisicion() {return codigo_adquisicion;}
    public String getLugar_origen() {return lugar_origen;}
    public String getOpiniones() {return opiniones;}
}
