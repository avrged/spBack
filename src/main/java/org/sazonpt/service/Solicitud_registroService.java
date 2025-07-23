package org.sazonpt.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.repository.Solicitud_registroRepository;

public class Solicitud_registroService {
    // Actualizar solo el estado de una solicitud
    public void updateEstado(int idSolicitud, String nuevoEstado) throws SQLException {
        this.solicitudRepo.updateEstado(idSolicitud, nuevoEstado);
    }
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
        if (solicitud.getFacebook() == null) solicitud.setFacebook("");
        if (solicitud.getInstagram() == null) solicitud.setInstagram("");
        if (solicitud.getMenu() == null) solicitud.setMenu("");
        if (solicitud.getEtiqueta() == null) solicitud.setEtiqueta("");
        if (solicitud.getFecha() == null) {
            solicitud.setFecha(LocalDate.now());
        }
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
        if (solicitud.getFacebook() == null) solicitud.setFacebook("");
        if (solicitud.getInstagram() == null) solicitud.setInstagram("");
        if (solicitud.getMenu() == null) solicitud.setMenu("");
        if (solicitud.getEtiqueta() == null) solicitud.setEtiqueta("");
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
        // Cambia el estado de la solicitud a 'aprobado'
        solicitudRepo.aprobarSolicitud(idSolicitud);
    }

    public void rechazarSolicitud(int idSolicitud) throws SQLException {
        if (solicitudRepo.FindSolicitudR(idSolicitud) == null) {
            throw new IllegalArgumentException("No existe una solicitud con este ID");
        }

        solicitudRepo.rechazarSolicitud(idSolicitud);
    }
}
