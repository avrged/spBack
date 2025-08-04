package org.sazonpt.service;

import org.sazonpt.model.Zona;
import org.sazonpt.repository.ZonaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ZonaService {
    
    private final ZonaRepository zonaRepository;
    
    public ZonaService(ZonaRepository zonaRepository) {
        this.zonaRepository = zonaRepository;
    }
    
    /**
     * Obtiene todas las zonas
     */
    public List<Zona> obtenerTodasLasZonas() {
        try {
            return zonaRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las zonas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene una zona por ID
     */
    public Optional<Zona> obtenerZonaPorId(int idZona) {
        try {
            return zonaRepository.findById(idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la zona por ID: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene todas las zonas de un restaurantero específico
     */
    public List<Zona> obtenerZonasPorRestaurantero(int idRestaurantero) {
        try {
            return zonaRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las zonas del restaurantero: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea una nueva zona con validaciones
     */
    public Zona crearZona(Zona zona) {
        // Validaciones de entrada
        validarZona(zona);
        
        try {
            // Verificar que no exista una zona con el mismo nombre para el mismo restaurantero
            if (zonaRepository.existsByNombreAndRestaurantero(zona.getNombre(), zona.getId_restaurantero())) {
                throw new IllegalArgumentException("Ya existe una zona con el nombre '" + zona.getNombre() + "' para este restaurantero");
            }
            
            return zonaRepository.save(zona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la zona: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza una zona existente con validaciones
     */
    public boolean actualizarZona(int idZona, Zona zona) {
        // Validaciones de entrada
        validarZona(zona);
        
        try {
            // Verificar que la zona existe
            if (!zonaRepository.existsById(idZona)) {
                throw new IllegalArgumentException("No se encontró la zona con ID: " + idZona);
            }
            
            // Verificar que no exista otra zona con el mismo nombre para el mismo restaurantero
            if (zonaRepository.existsByNombreAndRestauranteroExcludingId(
                    zona.getNombre(), zona.getId_restaurantero(), idZona)) {
                throw new IllegalArgumentException("Ya existe otra zona con el nombre '" + zona.getNombre() + "' para este restaurantero");
            }
            
            return zonaRepository.update(idZona, zona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la zona: " + e.getMessage(), e);
        }
    }
    
    /**
     * Elimina una zona por ID
     */
    public boolean eliminarZona(int idZona) {
        try {
            // Verificar que la zona existe
            if (!zonaRepository.existsById(idZona)) {
                throw new IllegalArgumentException("No se encontró la zona con ID: " + idZona);
            }
            
            // TODO: Aquí podrías agregar validación para verificar si hay restaurantes asociados
            // y decidir si permitir la eliminación o no
            
            return zonaRepository.deleteById(idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la zona: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si una zona existe
     */
    public boolean existeZona(int idZona) {
        try {
            return zonaRepository.existsById(idZona);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia de la zona: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el conteo total de zonas
     */
    public int contarZonas() {
        try {
            return zonaRepository.count();
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las zonas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el conteo de zonas por restaurantero
     */
    public int contarZonasPorRestaurantero(int idRestaurantero) {
        try {
            return zonaRepository.countByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las zonas del restaurantero: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si un restaurantero puede crear más zonas (opcional: límite de zonas)
     */
    public boolean puedeCrearMasZonas(int idRestaurantero) {
        try {
            int zonasActuales = zonaRepository.countByRestaurantero(idRestaurantero);
            int limiteZonas = 10; // Límite configurable
            
            return zonasActuales < limiteZonas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el límite de zonas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validaciones comunes para la zona
     */
    private void validarZona(Zona zona) {
        if (zona == null) {
            throw new IllegalArgumentException("La zona no puede ser nula");
        }
        
        if (zona.getNombre() == null || zona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la zona es obligatorio");
        }
        
        if (zona.getNombre().length() > 200) {
            throw new IllegalArgumentException("El nombre de la zona no puede exceder 200 caracteres");
        }
        
        if (zona.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("El ID del restaurantero debe ser un número positivo");
        }
        
        // Limpiar espacios en blanco
        zona.setNombre(zona.getNombre().trim());
        
        // Validación adicional: nombres no pueden ser solo números o caracteres especiales
        if (!zona.getNombre().matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*")) {
            throw new IllegalArgumentException("El nombre de la zona debe contener al menos una letra");
        }
    }
    
    /**
     * Busca zonas por nombre (búsqueda parcial)
     */
    public List<Zona> buscarZonasPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodasLasZonas();
        }
        
        try {
            // Para implementar búsqueda por nombre, necesitarías agregar este método al repository
            // Por ahora, devuelve todas y filtra en memoria (no óptimo para muchos registros)
            return obtenerTodasLasZonas().stream()
                    .filter(zona -> zona.getNombre().toLowerCase().contains(nombre.toLowerCase().trim()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar zonas por nombre: " + e.getMessage(), e);
        }
    }
}
