package org.sazonpt.service;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.model.Zona;
import org.sazonpt.repository.RestauranteRepository;
import org.sazonpt.repository.Solicitud_registroRepository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    private final Solicitud_registroRepository solicitudRepository;
    private final ZonaService zonaService;

    public RestauranteService(RestauranteRepository restauranteRepository, 
                             Solicitud_registroRepository solicitudRepository,
                             ZonaService zonaService) {
        this.restauranteRepository = restauranteRepository;
        this.solicitudRepository = solicitudRepository;
        this.zonaService = zonaService;
    }

    public List<Restaurante> obtenerTodosLosRestaurantes() {
        try {
            return restauranteRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los restaurantes: " + e.getMessage(), e);
        }
    }

    public Optional<Restaurante> obtenerRestaurantePorId(int id) {
        try {
            return restauranteRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el restaurante: " + e.getMessage(), e);
        }
    }

    public List<Restaurante> obtenerRestaurantesPorRestaurantero(int idRestaurantero) {
        try {
            return restauranteRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los restaurantes del restaurantero: " + e.getMessage(), e);
        }
    }

    public List<Restaurante> obtenerRestaurantesPorZona(int idZona) {
        try {
            return restauranteRepository.findByZona(idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los restaurantes de la zona: " + e.getMessage(), e);
        }
    }

    public Restaurante crearRestaurante(Restaurante restaurante) {
        // Validaciones
        if (restaurante.getNombre() == null || restaurante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }
        if (restaurante.getHorario() == null || restaurante.getHorario().trim().isEmpty()) {
            throw new IllegalArgumentException("El horario es obligatorio");
        }
        if (restaurante.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido");
        }
        if (restaurante.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }
        // id_zona eliminado

        // Verificar que la solicitud existe
        try {
            if (!restauranteRepository.solicitudExists(restaurante.getId_solicitud(), restaurante.getId_restaurantero())) {
                throw new IllegalArgumentException("La solicitud especificada no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la solicitud: " + e.getMessage(), e);
        }

        // id_zona eliminado

        // Verificar que no existe ya un restaurante para esta solicitud
        try {
            if (restauranteRepository.existsBySolicitud(restaurante.getId_solicitud(), restaurante.getId_restaurantero())) {
                throw new IllegalArgumentException("Ya existe un restaurante para esta solicitud");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar duplicados: " + e.getMessage(), e);
        }

        try {
            // Crear zona automáticamente usando la dirección como nombre
            if (restaurante.getDireccion() != null && !restaurante.getDireccion().trim().isEmpty()) {
                Zona zona = new Zona(restaurante.getDireccion(), restaurante.getId_restaurantero());
                try {
                    zonaService.crearZona(zona);
                } catch (Exception ex) {
                    // Si la zona ya existe, ignorar el error
                    if (!ex.getMessage().contains("Ya existe una zona")) {
                        throw ex;
                    }
                }
            }
            return restauranteRepository.save(restaurante);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el restaurante: " + e.getMessage(), e);
        }
    }

    /**
     * Método especial para crear un restaurante automáticamente cuando se aprueba una solicitud
     * Crea automáticamente una zona con la dirección del restaurante
     */
    public Restaurante crearRestauranteDesdeAprobacion(int idSolicitud, int idRestaurantero, int idZonaIgnorada) {
        try {
            // Obtener los datos de la solicitud aprobada
            Optional<Solicitud_registro> solicitudOpt = solicitudRepository.findById(idSolicitud);
            if (solicitudOpt.isEmpty()) {
                throw new IllegalArgumentException("No se encontró la solicitud");
            }

            Solicitud_registro solicitud = solicitudOpt.get();

            // Verificar que la solicitud esté aprobada
            if (!"aprobada".equalsIgnoreCase(solicitud.getEstado())) {
                throw new IllegalArgumentException("La solicitud debe estar aprobada para crear el restaurante");
            }

            // Verificar que no existe ya un restaurante para esta solicitud
            if (restauranteRepository.existsBySolicitud(idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("Ya existe un restaurante para esta solicitud");
            }

            // Crear automáticamente la zona con el nombre del restaurante como ubicación
            String nombreZona = solicitud.getNombre_propuesto_restaurante();
            Zona nuevaZona = new Zona(nombreZona, idRestaurantero);
            Zona zonaCreada = zonaService.crearZona(nuevaZona);
            
            System.out.println("✅ Zona creada automáticamente: " + zonaCreada.getNombre() + " (ID: " + zonaCreada.getId_zona() + ")");

            // Crear el restaurante con los datos de la solicitud y la zona recién creada
            Restaurante restaurante = new Restaurante();
            restaurante.setNombre(solicitud.getNombre_propuesto_restaurante());
            restaurante.setHorario(solicitud.getHorario_atencion());
            restaurante.setTelefono(""); // Se puede completar después
            restaurante.setEtiquetas(""); // Se puede completar después  
            restaurante.setId_solicitud(idSolicitud);
            restaurante.setId_restaurantero(idRestaurantero);
            // id_zona eliminado
            restaurante.setDireccion(nombreZona); // Usar el nombre como dirección por ahora
            restaurante.setFacebook(""); // Se puede completar después
            restaurante.setInstagram(""); // Se puede completar después

            Restaurante restauranteCreado = restauranteRepository.save(restaurante);
            
            System.out.println("✅ Restaurante creado automáticamente: " + restauranteCreado.getNombre());
            
            return restauranteCreado;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el restaurante desde la aprobación: " + e.getMessage(), e);
        }
    }

    public boolean actualizarRestaurante(int id, Restaurante restauranteActualizado) {
        try {
            // Verificar que el restaurante existe
            if (!restauranteRepository.existsById(id)) {
                throw new IllegalArgumentException("El restaurante no existe");
            }

            // Validaciones
            if (restauranteActualizado.getNombre() == null || restauranteActualizado.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
            }
            if (restauranteActualizado.getHorario() == null || restauranteActualizado.getHorario().trim().isEmpty()) {
                throw new IllegalArgumentException("El horario es obligatorio");
            }

            // id_zona eliminado

            restauranteActualizado.setId_restaurante(id);
            return restauranteRepository.update(restauranteActualizado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el restaurante: " + e.getMessage(), e);
        }
    }

    public boolean eliminarRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            if (!restauranteRepository.existsById(idRestaurante)) {
                throw new IllegalArgumentException("El restaurante no existe");
            }

            return restauranteRepository.delete(idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el restaurante: " + e.getMessage(), e);
        }
    }

    public boolean existeRestaurante(int id) {
        try {
            return restauranteRepository.existsById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia del restaurante: " + e.getMessage(), e);
        }
    }

    public boolean existeRestaurantePorSolicitud(int idSolicitud, int idRestaurantero) {
        try {
            return restauranteRepository.existsBySolicitud(idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurante por solicitud: " + e.getMessage(), e);
        }
    }

    public boolean actualizarPorRestaurantero(int idRestaurantero, Restaurante restaurante) {
        try {
            return restauranteRepository.updateByRestaurantero(idRestaurantero, restaurante);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el restaurante: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza campos específicos de un restaurante
     */
    public boolean actualizarCamposEspecificos(int idRestaurante, Map<String, String> campos) {
        if (campos.isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos un campo para actualizar");
        }

        // Validar campos permitidos
        List<String> camposPermitidos = Arrays.asList("horario", "telefono", "etiquetas", "direccion", "facebook", "instagram");
        for (String campo : campos.keySet()) {
            if (!camposPermitidos.contains(campo)) {
                throw new IllegalArgumentException("Campo no permitido: " + campo);
            }
        }

        try {
            return restauranteRepository.actualizarCamposEspecificos(idRestaurante, campos);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar los campos del restaurante: " + e.getMessage(), e);
        }
    }
}
