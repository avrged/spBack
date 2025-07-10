package org.sazonpt.model;

import java.time.LocalDate;

public class Adquirir_membresia {
    private  int id_adquisicion;
    private int codigo_restaurantero;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private double costo;
    private boolean estado;

    public Adquirir_membresia(int id_adquisicion, int codigo_restaurantero, LocalDate fecha_inicio, LocalDate fecha_fin, double costo, boolean estado){
        this.id_adquisicion=id_adquisicion;
        this.codigo_restaurantero=codigo_restaurantero;
        this.fecha_inicio=fecha_inicio;
        this.fecha_fin=fecha_fin;
        this.costo=costo;
        this.estado=estado;
    }

    public int getIdAdquisicion(){return id_adquisicion;}
    public int getCodigoRestaurantero(){return codigo_restaurantero;}
    public LocalDate getFechaInicio(){return fecha_inicio;}
    public LocalDate getFechaFin(){return fecha_fin;}

    public void setCosto(double costo){this.costo=costo;}
    public double getCosto(){return costo;}
    public boolean getEstado(){return estado;}
}
