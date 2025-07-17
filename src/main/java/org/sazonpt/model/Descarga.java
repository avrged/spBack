package org.sazonpt.model;

public class Descarga {
    private int id_descarga;
    private int cantidad_descargas;
    private int id_adquisicion;
    private String lugar_origen;
    private String opiniones;

    // Constructor por defecto
    public Descarga() {
    }

    public Descarga(int id_descarga, int cantidad_descargas, int id_adquisicion, String lugar_origen, String opiniones){
        this.id_descarga = id_descarga;
        this.cantidad_descargas = cantidad_descargas;
        this.id_adquisicion = id_adquisicion;
        this.lugar_origen = lugar_origen;
        this.opiniones = opiniones;
    }

    // Getters nuevos
    public int getId_descarga() {return id_descarga;}
    public int getCantidad_descargas() {return cantidad_descargas;}
    public int getId_adquisicion() {return id_adquisicion;}
    public String getLugar_origen() {return lugar_origen;}
    public String getOpiniones() {return opiniones;}

    // Setters nuevos
    public void setId_descarga(int id_descarga) {this.id_descarga = id_descarga;}
    public void setCantidad_descargas(int cantidad_descargas) {this.cantidad_descargas = cantidad_descargas;}
    public void setId_adquisicion(int id_adquisicion) {this.id_adquisicion = id_adquisicion;}
    public void setLugar_origen(String lugar_origen) {this.lugar_origen = lugar_origen;}
    public void setOpiniones(String opiniones) {this.opiniones = opiniones;}

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getCodigo_adquisicion() {return id_adquisicion;}
}
