package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.repository.Revision_soliRepository;

public class Revision_soliService {
    private final Revision_soliRepository revisionRepo;

    public Revision_soliService(Revision_soliRepository revisionRepo) {
        this.revisionRepo = revisionRepo;
    }

    public List<Revision_solicitud> getAllRevisiones() throws SQLException {
        return revisionRepo.findAllRevisiones();
    }

    public List<Solicitud_registro> getAllSolicitudes() throws SQLException {
        return revisionRepo.findAllSolicitudes();
    }

    public Revision_solicitud getRevisionById(int idRevision) throws SQLException {
        return revisionRepo.findRevisionById(idRevision);
    }

    public Solicitud_registro getSolicitudById(int idSolicitud) throws SQLException {
        return revisionRepo.FindSoli(idSolicitud);
    }

    public void createSolicitud(Solicitud_registro solicitud) throws SQLException {
        if (solicitud.getNombrePropuesto() == null || solicitud.getNombrePropuesto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre propuesto del restaurante es obligatorio");
        }

        if (solicitud.getCorreo() == null || solicitud.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (!solicitud.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }

        if (solicitud.getDireccionPropuesta() == null || solicitud.getDireccionPropuesta().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección propuesta es obligatoria");
        }

        if (solicitud.getCodigoRestaurantero() <= 0) {
            throw new IllegalArgumentException("Código de restaurantero inválido");
        }

        revisionRepo.AddSoli(solicitud);
    }

    public void createRevision(Revision_solicitud revision) throws SQLException {
        if (revision.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido");
        }

        if (revision.getId_admin() <= 0) {
            throw new IllegalArgumentException("ID de administrador inválido");
        }

        // Si no se especifica fecha, usar la fecha actual
        if (revision.getFecha() == null) {
            revision.setFecha(java.time.LocalDate.now());
        }

        revisionRepo.AddRevision(revision);
    }

    public void updateRevision(Revision_solicitud revision) throws SQLException {
        if (revisionRepo.findRevisionById(revision.getId_revision()) == null) {
            throw new IllegalArgumentException("No existe una revisión con este ID");
        }

        if (revision.getCodigo_solicitud() <= 0) {
            throw new IllegalArgumentException("Código de solicitud inválido");
        }

        if (revision.getCodigo_admin() <= 0) {
            throw new IllegalArgumentException("Código de administrador inválido");
        }

        revisionRepo.UpdateRevision(revision);
    }

    public void deleteSolicitud(int idSolicitud) throws SQLException {
        if (revisionRepo.FindSoli(idSolicitud) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }
        
        boolean deleted = revisionRepo.DeleteSoli(idSolicitud);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la solicitud");
        }
    }
}
