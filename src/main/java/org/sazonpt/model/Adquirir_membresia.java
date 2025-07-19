package org.sazonpt.model;

import java.time.LocalDate;

public class Adquirir_membresia {
    private int id_adquisicion;
    private int id_restaurantero;
    private LocalDate fecha_adquisicion;
    private double costo;
    private String estado;

    public Adquirir_membresia() {
    }

    public Adquirir_membresia(int id_adquisicion, int id_restaurantero, LocalDate fecha_adquisicion, double costo, String estado){
        this.id_adquisicion = id_adquisicion;
        this.id_restaurantero = id_restaurantero;
        this.fecha_adquisicion = fecha_adquisicion;
        this.costo = costo;
        this.estado = estado;
    }

    public int getId_adquisicion(){return id_adquisicion;}
    public int getId_restaurantero(){return id_restaurantero;}
    public LocalDate getFecha_adquisicion(){return fecha_adquisicion;}
    public double getCosto(){return costo;}
    public String getEstado(){return estado;}

    public void setId_adquisicion(int id_adquisicion) {this.id_adquisicion = id_adquisicion;}
    public void setId_restaurantero(int id_restaurantero) {this.id_restaurantero = id_restaurantero;}
    public void setFecha_adquisicion(LocalDate fecha_adquisicion){this.fecha_adquisicion = fecha_adquisicion;}
    public void setCosto(double costo){this.costo = costo;}
    public void setEstado(String estado) {this.estado = estado;}
}
