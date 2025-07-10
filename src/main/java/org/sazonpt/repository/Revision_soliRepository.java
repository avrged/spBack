package org.sazonpt.repository;

import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.model.Solicitud_registro;

public interface Revision_soliRepository {
    void AddSoli(Solicitud_registro soli);
    boolean FindSoli(int id);
    boolean DeleteSoli(int id);
    Revision_solicitud UpdateRevision(Revision_solicitud soli);
}
