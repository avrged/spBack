package org.sazonpt.service;

import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.repository.Revision_solicitudRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Revision_solicitudService {
    
    private final Revision_solicitudRepository revisionRepository;

    public Revision_solicitudService(Revision_solicitudRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    public List<Revision_solicitud> obtenerTodasLasRevisiones() {
        try {
            return revisionRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las revisiones de solicitud: " + e.getMessage(), e);
        }
    }

    public Optional<Revision_solicitud> obtenerRevisionPorId(int id) {
        try {
            return revisionRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la revisión de solicitud: " + e.getMessage(), e);
        }
    }

    public List<Revision_solicitud> obtenerRevisionesPorSolicitud(int idSolicitud) {
        try {
            return revisionRepository.findBySolicitud(idSolicitud);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las revisiones de la solicitud: " + e.getMessage(), e);
        }
    }

    public List<Revision_solicitud> obtenerRevisionesPorAdministrador(int idAdministrador) {
        try {
            return revisionRepository.findByAdministrador(idAdministrador);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las revisiones del administrador: " + e.getMessage(), e);
        }
    }

    public Revision_solicitud crearRevision(Revision_solicitud revision) {
        // Validaciones
        if (revision.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido");
        }
        if (revision.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }
        if (revision.getId_administrador() <= 0) {
            throw new IllegalArgumentException("ID de administrador inválido");
        }

        // Verificar que la solicitud existe
        try {
            if (!revisionRepository.solicitudExists(revision.getId_solicitud())) {
                throw new IllegalArgumentException("La solicitud especificada no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la solicitud: " + e.getMessage(), e);
        }

        // Verificar que el administrador existe
        try {
            if (!revisionRepository.administradorExists(revision.getId_administrador())) {
                throw new IllegalArgumentException("El administrador especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el administrador: " + e.getMessage(), e);
        }

        // Establecer fecha actual si no se proporciona
        if (revision.getFecha() == null) {
            revision.setFecha(LocalDateTime.now());
        }

        try {
            return revisionRepository.save(revision);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la revisión de solicitud: " + e.getMessage(), e);
        }
    }

    public boolean actualizarRevision(int id, Revision_solicitud revisionActualizada) {
        try {
            // Verificar que la revisión existe
            if (!revisionRepository.existsById(id)) {
                throw new IllegalArgumentException("La revisión de solicitud no existe");
            }

            // Establecer fecha actual si no se proporciona
            if (revisionActualizada.getFecha() == null) {
                revisionActualizada.setFecha(LocalDateTime.now());
            }

            revisionActualizada.setId_revision(id);
            return revisionRepository.update(revisionActualizada);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la revisión de solicitud: " + e.getMessage(), e);
        }
    }

    public boolean eliminarRevision(int idRevision, int idSolicitud, int idAdministrador) {
        try {
            if (!revisionRepository.existsById(idRevision)) {
                throw new IllegalArgumentException("La revisión de solicitud no existe");
            }

            return revisionRepository.delete(idRevision, idSolicitud, idAdministrador);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la revisión de solicitud: " + e.getMessage(), e);
        }
    }

    public boolean existeRevision(int id) {
        try {
            return revisionRepository.existsById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia de la revisión: " + e.getMessage(), e);
        }
    }

    public Revision_solicitud crearRevisionPorAdministrador(int idSolicitud, int idRestaurantero, int idAdministrador) {
        Revision_solicitud revision = new Revision_solicitud();
        revision.setId_solicitud(idSolicitud);
        revision.setId_restaurantero(idRestaurantero);
        revision.setId_administrador(idAdministrador);
        
        return crearRevision(revision);
    }

    public Revision_solicitud aprobarSolicitud(int idSolicitud, int idRestaurantero, int idAdministrador) {
        try {
            // Crear la revisión que registra la aprobación
            Revision_solicitud revision = crearRevisionPorAdministrador(idSolicitud, idRestaurantero, idAdministrador);
            
            // Actualizar el estado de la solicitud a "aprobada"
            boolean estadoActualizado = revisionRepository.actualizarEstadoSolicitud(idSolicitud, "aprobada");
            
            if (!estadoActualizado) {
                throw new RuntimeException("Error al actualizar el estado de la solicitud");
            }
            
            return revision;
        } catch (SQLException e) {
            throw new RuntimeException("Error al aprobar la solicitud: " + e.getMessage(), e);
        }
    }
}
