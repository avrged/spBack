package org.sazonpt.service;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.repository.Solicitud_registroRepository;
import org.sazonpt.repository.RestauranteRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Solicitud_registroService {
    
    private final Solicitud_registroRepository solicitudRepository;
    private final RestauranteService restauranteService;

    public Solicitud_registroService(Solicitud_registroRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
        // Inicializar RestauranteService para la creación automática
        this.restauranteService = new RestauranteService(
            new RestauranteRepository(), 
            solicitudRepository
        );
    }

    public List<Solicitud_registro> obtenerTodasLasSolicitudes() {
        try {
            return solicitudRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las solicitudes de registro: " + e.getMessage(), e);
        }
    }

    public Optional<Solicitud_registro> obtenerSolicitudPorId(int id) {
        try {
            return solicitudRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la solicitud de registro: " + e.getMessage(), e);
        }
    }

    public List<Solicitud_registro> obtenerSolicitudesPorRestaurantero(int idRestaurantero) {
        try {
            return solicitudRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las solicitudes del restaurantero: " + e.getMessage(), e);
        }
    }

    public Solicitud_registro crearSolicitud(Solicitud_registro solicitud) {
        // Validaciones
        if (solicitud.getNombre_propuesto_restaurante() == null || solicitud.getNombre_propuesto_restaurante().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }
        if (solicitud.getCorreo() == null || solicitud.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (solicitud.getNombre_propietario() == null || solicitud.getNombre_propietario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del propietario es obligatorio");
        }
        if (solicitud.getHorario_atencion() == null || solicitud.getHorario_atencion().trim().isEmpty()) {
            throw new IllegalArgumentException("El horario de atención es obligatorio");
        }
        if (solicitud.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        // Validar formato de correo
        if (!esCorreoValido(solicitud.getCorreo())) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }

        // Verificar que el restaurantero existe
        try {
            if (!solicitudRepository.restauranteroExists(solicitud.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurantero especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurantero: " + e.getMessage(), e);
        }

        // Establecer valores por defecto
        if (solicitud.getFecha() == null) {
            solicitud.setFecha(LocalDateTime.now());
        }
        if (solicitud.getEstado() == null) {
            solicitud.setEstado(Solicitud_registro.EstadoSolicitud.PENDIENTE.getValor());
        }

        try {
            return solicitudRepository.save(solicitud);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la solicitud de registro: " + e.getMessage(), e);
        }
    }

    public boolean actualizarSolicitud(int id, Solicitud_registro solicitudActualizada) {
        try {
            // Verificar que la solicitud existe
            if (!solicitudRepository.existsById(id)) {
                throw new IllegalArgumentException("La solicitud de registro no existe");
            }

            // Validaciones
            if (solicitudActualizada.getNombre_propuesto_restaurante() == null || 
                solicitudActualizada.getNombre_propuesto_restaurante().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
            }
            if (solicitudActualizada.getCorreo() == null || solicitudActualizada.getCorreo().trim().isEmpty()) {
                throw new IllegalArgumentException("El correo es obligatorio");
            }
            if (solicitudActualizada.getNombre_propietario() == null || 
                solicitudActualizada.getNombre_propietario().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del propietario es obligatorio");
            }
            if (solicitudActualizada.getHorario_atencion() == null || 
                solicitudActualizada.getHorario_atencion().trim().isEmpty()) {
                throw new IllegalArgumentException("El horario de atención es obligatorio");
            }

            // Validar formato de correo
            if (!esCorreoValido(solicitudActualizada.getCorreo())) {
                throw new IllegalArgumentException("Formato de correo inválido");
            }

            // Validar estado si se proporciona
            if (solicitudActualizada.getEstado() != null) {
                try {
                    Solicitud_registro.EstadoSolicitud.fromString(solicitudActualizada.getEstado());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Estado de solicitud inválido: " + solicitudActualizada.getEstado());
                }
            }

            solicitudActualizada.setId_solicitud(id);
            return solicitudRepository.update(solicitudActualizada);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la solicitud de registro: " + e.getMessage(), e);
        }
    }

    public boolean cambiarEstadoSolicitud(int idSolicitud, String nuevoEstado) {
        try {
            // Verificar que la solicitud existe
            if (!solicitudRepository.existsById(idSolicitud)) {
                throw new IllegalArgumentException("La solicitud de registro no existe");
            }

            // Validar el estado
            try {
                Solicitud_registro.EstadoSolicitud.fromString(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de solicitud inválido: " + nuevoEstado);
            }

            return solicitudRepository.updateEstado(idSolicitud, nuevoEstado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar el estado de la solicitud: " + e.getMessage(), e);
        }
    }

    public boolean eliminarSolicitud(int idSolicitud, int idRestaurantero) {
        try {
            if (!solicitudRepository.existsById(idSolicitud)) {
                throw new IllegalArgumentException("La solicitud de registro no existe");
            }

            return solicitudRepository.delete(idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la solicitud de registro: " + e.getMessage(), e);
        }
    }

    public boolean existeSolicitud(int id) {
        try {
            return solicitudRepository.existsById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia de la solicitud: " + e.getMessage(), e);
        }
    }

    private boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
