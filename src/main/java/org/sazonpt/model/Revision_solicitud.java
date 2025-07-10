package org.sazonpt.model;

import java.time.LocalDate;

public class Revision_solicitud {
    private int id_revision;
    private int codigo_solicitud;
    private int codigo_admin;
    private LocalDate fecha;

    public Revision_solicitud(int id_revision, int codigo_solicitud, int codigo_admin, LocalDate fecha) {
        this.id_revision = id_revision;
        this.codigo_solicitud = codigo_solicitud;
        this.codigo_admin = codigo_admin;
        this.fecha = fecha;
    }

    public int getId_revision() {return id_revision;}
    public int  getCodigo_solicitud() {return codigo_solicitud;}
    public int  getCodigo_admin() {return codigo_admin;}
    public LocalDate getFecha() {return fecha;}
}
