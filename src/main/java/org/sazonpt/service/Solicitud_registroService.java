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
        if (solicitud.getRestaurante() == null || solicitud.getRestaurante().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }

        if (solicitud.getCorreo() == null || solicitud.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (!solicitud.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }

        if (solicitud.getDireccion() == null || solicitud.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }

        // TEMPORALMENTE COMENTAMOS LA VALIDACIÓN DEL ID RESTAURANTERO
        // TODO: Descomentar cuando tengas restauranteros válidos en la BD
        /*
        if (solicitud.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }
        */

        // Si no se especifica fecha, establecerla directamente sin recrear el objeto
        if (solicitud.getFecha() == null) {
            solicitud.setFecha(LocalDate.now());
        }

        // Llamar directamente al repository sin recrear el objeto
        solicitudRepo.AddSolicitudR(solicitud);
    }

    public void updateSolicitud(Solicitud_registro solicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(solicitud.getId_solicitud()) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }

        if (solicitud.getRestaurante() == null || solicitud.getRestaurante().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }

        if (solicitud.getCorreo() == null || solicitud.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (!solicitud.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }

        if (solicitud.getDireccion() == null || solicitud.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
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

    public void aprobarSolicitud(int idSolicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(idSolicitud) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }

        solicitudRepo.aprobarSolicitud(idSolicitud);
    }

    public void rechazarSolicitud(int idSolicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(idSolicitud) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }

        solicitudRepo.rechazarSolicitud(idSolicitud);
    }
}
