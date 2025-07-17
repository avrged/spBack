package org.sazonpt.model;

import java.time.LocalDate;

public class Revision_solicitud {
    private int id_revision;
    private int id_solicitud;
    private int id_admin;
    private LocalDate fecha;

    // Constructor por defecto
    public Revision_solicitud() {
    }

    public Revision_solicitud(int id_revision, int id_solicitud, int id_admin, LocalDate fecha) {
        this.id_revision = id_revision;
        this.id_solicitud = id_solicitud;
        this.id_admin = id_admin;
        this.fecha = fecha;
    }

    // Getters
    public int getId_revision() {return id_revision;}
    public int getId_solicitud() {return id_solicitud;}
    public int getId_admin() {return id_admin;}
    public LocalDate getFecha() {return fecha;}

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

    // Métodos de compatibilidad (mantener nombres antiguos si es necesario)
    public int getCodigo_solicitud() {return id_solicitud;}
    public int getCodigo_admin() {return id_admin;}
}
