package org.sazonpt.service;

import org.sazonpt.model.Comprobante;
import org.sazonpt.repository.ComprobanteRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ComprobanteService {
    
    private final ComprobanteRepository comprobanteRepository;

    public ComprobanteService(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    public List<Comprobante> obtenerTodosLosComprobantes() {
        try {
            return comprobanteRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los comprobantes: " + e.getMessage(), e);
        }
    }

    public Optional<Comprobante> obtenerComprobantePorId(int idComprobante, int idRestaurante, 
                                                        int idSolicitud, int idRestaurantero, int idZona) {
        try {
            return comprobanteRepository.findById(idComprobante, idRestaurante, idSolicitud, 
                                                 idRestaurantero, idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el comprobante: " + e.getMessage(), e);
        }
    }

    public List<Comprobante> obtenerComprobantesPorRestaurante(int idRestaurante, int idSolicitud, 
                                                              int idRestaurantero, int idZona) {
        try {
            return comprobanteRepository.findByRestaurante(idRestaurante, idSolicitud, 
                                                          idRestaurantero, idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los comprobantes del restaurante: " + e.getMessage(), e);
        }
    }

    public List<Comprobante> obtenerComprobantesPorRestaurantero(int idRestaurantero) {
        try {
            return comprobanteRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los comprobantes del restaurantero: " + e.getMessage(), e);
        }
    }

    public List<Comprobante> obtenerComprobantesPorTipo(String tipo) {
        try {
            // Validar que el tipo sea válido
            Comprobante.TipoComprobante.fromString(tipo);
            
            return comprobanteRepository.findByTipo(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de comprobante inválido: " + tipo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los comprobantes por tipo: " + e.getMessage(), e);
        }
    }

    public List<Comprobante> obtenerComprobantesPorZona(int idZona) {
        try {
            if (idZona <= 0) {
                throw new IllegalArgumentException("ID de zona inválido");
            }
            
            return comprobanteRepository.findByZona(idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los comprobantes por zona: " + e.getMessage(), e);
        }
    }

    public Comprobante crearComprobante(Comprobante comprobante) {
        // Validaciones
        if (comprobante.getTipo() == null || comprobante.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de comprobante es obligatorio");
        }
        
        // Validar que el tipo sea válido
        try {
            Comprobante.TipoComprobante.fromString(comprobante.getTipo());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de comprobante inválido: " + comprobante.getTipo());
        }

        if (comprobante.getRuta_archivo() == null || comprobante.getRuta_archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        if (comprobante.getId_restaurante() <= 0) {
            throw new IllegalArgumentException("ID de restaurante inválido");
        }
        if (comprobante.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido"); 
        }
        if (comprobante.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }
        if (comprobante.getId_zona() <= 0) {
            throw new IllegalArgumentException("ID de zona inválido");
        }

        // Verificar que el restaurante existe
        try {
            if (!comprobanteRepository.restauranteExists(comprobante.getId_restaurante(), 
                    comprobante.getId_solicitud(), comprobante.getId_restaurantero(), 
                    comprobante.getId_zona())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurante: " + e.getMessage(), e);
        }

        // Establecer fecha actual si no se proporciona
        if (comprobante.getFecha_subida() == null) {
            comprobante.setFecha_subida(LocalDateTime.now());
        }

        try {
            return comprobanteRepository.save(comprobante);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el comprobante: " + e.getMessage(), e);
        }
    }

    public boolean actualizarComprobante(int idComprobante, int idRestaurante, int idSolicitud, 
                                        int idRestaurantero, int idZona, Comprobante comprobanteActualizado) {
        try {
            // Verificar que el comprobante existe
            if (!comprobanteRepository.existsById(idComprobante, idRestaurante, idSolicitud, 
                                                 idRestaurantero, idZona)) {
                throw new IllegalArgumentException("El comprobante no existe");
            }

            // Validaciones
            if (comprobanteActualizado.getTipo() == null || comprobanteActualizado.getTipo().trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de comprobante es obligatorio");
            }
            
            // Validar que el tipo sea válido
            try {
                Comprobante.TipoComprobante.fromString(comprobanteActualizado.getTipo());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de comprobante inválido: " + comprobanteActualizado.getTipo());
            }

            if (comprobanteActualizado.getRuta_archivo() == null || 
                comprobanteActualizado.getRuta_archivo().trim().isEmpty()) {
                throw new IllegalArgumentException("La ruta del archivo es obligatoria");
            }

            if (comprobanteActualizado.getId_restaurante() <= 0) {
                throw new IllegalArgumentException("ID de restaurante inválido");
            }
            if (comprobanteActualizado.getId_solicitud() <= 0) {
                throw new IllegalArgumentException("ID de solicitud inválido");
            }
            if (comprobanteActualizado.getId_restaurantero() <= 0) {
                throw new IllegalArgumentException("ID de restaurantero inválido");
            }
            if (comprobanteActualizado.getId_zona() <= 0) {
                throw new IllegalArgumentException("ID de zona inválido");
            }

            // Verificar que el restaurante existe
            if (!comprobanteRepository.restauranteExists(comprobanteActualizado.getId_restaurante(), 
                    comprobanteActualizado.getId_solicitud(), comprobanteActualizado.getId_restaurantero(), 
                    comprobanteActualizado.getId_zona())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }

            // Establecer los IDs de la clave primaria
            comprobanteActualizado.setId_comprobante(idComprobante);
            comprobanteActualizado.setId_restaurante(idRestaurante);
            comprobanteActualizado.setId_solicitud(idSolicitud);
            comprobanteActualizado.setId_restaurantero(idRestaurantero);
            comprobanteActualizado.setId_zona(idZona);

            return comprobanteRepository.update(comprobanteActualizado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el comprobante: " + e.getMessage(), e);
        }
    }

    public boolean eliminarComprobante(int idComprobante, int idRestaurante, int idSolicitud, 
                                      int idRestaurantero, int idZona) {
        try {
            if (!comprobanteRepository.existsById(idComprobante, idRestaurante, idSolicitud, 
                                                 idRestaurantero, idZona)) {
                throw new IllegalArgumentException("El comprobante no existe");
            }

            return comprobanteRepository.delete(idComprobante, idRestaurante, idSolicitud, 
                                               idRestaurantero, idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el comprobante: " + e.getMessage(), e);
        }
    }

    public boolean existeComprobante(int idComprobante, int idRestaurante, int idSolicitud, 
                                    int idRestaurantero, int idZona) {
        try {
            return comprobanteRepository.existsById(idComprobante, idRestaurante, idSolicitud, 
                                                   idRestaurantero, idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia del comprobante: " + e.getMessage(), e);
        }
    }

    /**
     * Método utilitario para crear un comprobante asociado a un restaurante específico
     */
    public Comprobante crearComprobanteParaRestaurante(String tipo, String rutaArchivo, 
                                                      int idRestaurante, int idSolicitud, 
                                                      int idRestaurantero, int idZona) {
        Comprobante nuevoComprobante = new Comprobante();
        nuevoComprobante.setTipo(tipo);
        nuevoComprobante.setRuta_archivo(rutaArchivo);
        nuevoComprobante.setId_restaurante(idRestaurante);
        nuevoComprobante.setId_solicitud(idSolicitud);
        nuevoComprobante.setId_restaurantero(idRestaurantero);
        nuevoComprobante.setId_zona(idZona);
        nuevoComprobante.setFecha_subida(LocalDateTime.now());
        
        return crearComprobante(nuevoComprobante);
    }

    /**
     * Obtener todos los tipos de comprobante disponibles
     */
    public List<String> obtenerTiposComprobante() {
        return java.util.Arrays.stream(Comprobante.TipoComprobante.values())
                .map(Comprobante.TipoComprobante::getValor)
                .toList();
    }
}
