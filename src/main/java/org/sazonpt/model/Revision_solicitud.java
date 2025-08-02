package org.sazonpt.model;

public class Revision_solicitud {
    private int id_revision;
    private String fecha;
    private String revision_solicitudcol;
    private int id_solicitud;
    private int id_restaurantero;
    private int id_administrador;

    // Constructor vacío
    public Revision_solicitud() {}

    // Constructor completo
    public Revision_solicitud(int id_revision, String fecha, String revision_solicitudcol, 
                             int id_solicitud, int id_restaurantero, int id_administrador) {
        this.id_revision = id_revision;
        this.fecha = fecha;
        this.revision_solicitudcol = revision_solicitudcol;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.id_administrador = id_administrador;
    }

    // Getters
    public int getId_revision() { return id_revision; }
    public String getFecha() { return fecha; }
    public String getRevision_solicitudcol() { return revision_solicitudcol; }
    public int getId_solicitud() { return id_solicitud; }
    public int getId_restaurantero() { return id_restaurantero; }
    public int getId_administrador() { return id_administrador; }

    // Setters
    public void setId_revision(int id_revision) { this.id_revision = id_revision; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setRevision_solicitudcol(String revision_solicitudcol) { 
        this.revision_solicitudcol = revision_solicitudcol; 
    }
    public void setId_solicitud(int id_solicitud) { this.id_solicitud = id_solicitud; }
    public void setId_restaurantero(int id_restaurantero) { this.id_restaurantero = id_restaurantero; }
    public void setId_administrador(int id_administrador) { this.id_administrador = id_administrador; }
}
