package org.sazonpt.model;

import java.time.LocalDateTime;

public class Revision_solicitud {
    private int id_revision;
    private LocalDateTime fecha;
    private int id_solicitud;
    private int id_restaurantero;
    private int id_administrador;

    // Constructor vacío
    public Revision_solicitud() {}

    // Constructor completo
    public Revision_solicitud(int id_revision, LocalDateTime fecha, 
                             int id_solicitud, int id_restaurantero, int id_administrador) {
        this.id_revision = id_revision;
        this.fecha = fecha;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.id_administrador = id_administrador;
    }

    // Constructor sin ID (para nuevos registros)
    public Revision_solicitud(LocalDateTime fecha, int id_solicitud, 
                             int id_restaurantero, int id_administrador) {
        this.fecha = fecha;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.id_administrador = id_administrador;
    }

    // Getters
    public int getId_revision() { return id_revision; }
    public LocalDateTime getFecha() { return fecha; }
    public int getId_solicitud() { return id_solicitud; }
    public int getId_restaurantero() { return id_restaurantero; }
    public int getId_administrador() { return id_administrador; }

    // Setters
    public void setId_revision(int id_revision) { this.id_revision = id_revision; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setId_solicitud(int id_solicitud) { this.id_solicitud = id_solicitud; }
    public void setId_restaurantero(int id_restaurantero) { this.id_restaurantero = id_restaurantero; }
    public void setId_administrador(int id_administrador) { this.id_administrador = id_administrador; }

    @Override
    public String toString() {
        return "Revision_solicitud{" +
                "id_revision=" + id_revision +
                ", fecha=" + fecha +
                ", id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                ", id_administrador=" + id_administrador +
                '}';
    }
}
