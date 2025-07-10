package org.sazonpt.repository;

import org.sazonpt.model.Solicitud_registro;

public interface Solicitud_registroRepository {
    void AddSolicitudR(Solicitud_registro soliR);
    boolean FindSolicitudR(int id);
    boolean DeleteSolicitudR(int id);
    Solicitud_registro UpdateSolicitud(Solicitud_registro soliR);
    void ListAllSolicitudes();
}
