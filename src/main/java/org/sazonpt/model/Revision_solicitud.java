package org.sazonpt.model;

import java.time.LocalDate;

public class Revision_solicitud {
    private int id_revision;
    private int id_solicitud;
    private int id_admin;
    private LocalDate fecha;
    private String status;

    // Constructor por defecto
    public Revision_solicitud() {
        this.status = "pendiente"; // Status por defecto
    }

    // Constructor sin status (usa "pendiente" por defecto)
    public Revision_solicitud(int id_solicitud, int id_admin, LocalDate fecha) {
        this.id_solicitud = id_solicitud;
        this.id_admin = id_admin;
        this.fecha = fecha;
        this.status = "pendiente"; // Status por defecto
    }

    public Revision_solicitud(int id_revision, int id_solicitud, int id_admin, LocalDate fecha, String status) {
        this.id_revision = id_revision;
        this.id_solicitud = id_solicitud;
        this.id_admin = id_admin;
        this.fecha = fecha;
        this.status = status;
    }

    // Getters
    public int getId_revision() {return id_revision;}
    public int getId_solicitud() {return id_solicitud;}
    public int getId_admin() {return id_admin;}
    public LocalDate getFecha() {return fecha;}
    public String getStatus() {return status;}

    // Setters
    public void setId_revision(int id_revision) {
        this.id_revision = id_revision;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
