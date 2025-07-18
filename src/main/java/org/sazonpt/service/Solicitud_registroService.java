package org.sazonpt.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.repository.Solicitud_registroRepository;

public class Solicitud_registroService {
    private final Solicitud_registroRepository solicitudRepo;

    public Solicitud_registroService(Solicitud_registroRepository solicitudRepo) {
        this.solicitudRepo = solicitudRepo;
    }

    public List<Solicitud_registro> getAllSolicitudes() throws SQLException {
        return solicitudRepo.ListAllSolicitudes();
    }

    public Solicitud_registro getById(int idSolicitud) throws SQLException {
        return solicitudRepo.FindSolicitudR(idSolicitud);
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

        if (solicitud.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        // Si no se especifica fecha, usar la fecha actual
        if (solicitud.getFecha() == null) {
            solicitud = new Solicitud_registro(
                solicitud.getId_solicitud(),
                solicitud.getId_restaurantero(),
                LocalDate.now(),
                solicitud.getEstado(),
                solicitud.getNombrePropuesto(),
                solicitud.getCorreo(),
                solicitud.getDireccionPropuesta(),
                solicitud.getRuta_imagen(),
                solicitud.getRuta_comprobante()
            );
        }

        solicitudRepo.AddSolicitudR(solicitud);
    }

    public void updateSolicitud(Solicitud_registro solicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(solicitud.getId_solicitud()) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }

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

        solicitudRepo.UpdateSolicitud(solicitud);
    }

    public void deleteSolicitud(int idSolicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(idSolicitud) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }
        
        boolean deleted = solicitudRepo.DeleteSolicitudR(idSolicitud);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la solicitud");
        }
    }
}
