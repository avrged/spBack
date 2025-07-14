package org.sazonpt.model;

import java.time.LocalDate;

public class Adquirir_membresia {
    private  int id_adquisicion;
    private int codigo_restaurantero;
    private LocalDate fecha_adquisicion;
    private double costo;
    private boolean estado;

    public Adquirir_membresia(int id_adquisicion, int codigo_restaurantero, LocalDate fecha_adquisicion, double costo, boolean estado){
        this.id_adquisicion=id_adquisicion;
        this.codigo_restaurantero=codigo_restaurantero;
        this.fecha_adquisicion=fecha_adquisicion;
        this.costo=costo;
        this.estado=estado;
    }

    public int getIdAdquisicion(){return id_adquisicion;}
    public int getCodigoRestaurantero(){return codigo_restaurantero;}
    public void setFechaAdquisicion(LocalDate fecha_adquisicion){this.fecha_adquisicion = fecha_adquisicion;}
    public LocalDate getFechaAdquisicion(){return fecha_adquisicion;}

    public void setCosto(double costo){this.costo=costo;}
    public double getCosto(){return costo;}
    public boolean getEstado(){return estado;}
}
