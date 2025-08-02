package org.sazonpt.service;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurante.EstadoRestaurante;
import org.sazonpt.repository.RestauranteRepository;

import java.util.List;
import java.util.Optional;

public class RestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    
    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }
    
    // Crear un nuevo restaurante
    public Restaurante crearRestaurante(Restaurante restaurante) {
        // Validar datos del restaurante
        validateRestauranteData(restaurante);
        
        // Verificar que el restaurantero no tenga ya un restaurante
        Optional<Restaurante> restauranteExistente = restauranteRepository.findByRestauranteroId(restaurante.getId_restaurantero());
        if (restauranteExistente.isPresent()) {
            throw new IllegalStateException("El restaurantero ya tiene un restaurante registrado");
        }
        
        // Guardar el restaurante
        return restauranteRepository.save(restaurante);
    }
    
    // Obtener restaurante por ID
    public Optional<Restaurante> obtenerRestaurantePorId(int id) {
        return restauranteRepository.findById(id);
    }
    
    // Obtener restaurante por ID de restaurantero
    public Optional<Restaurante> obtenerRestaurantePorRestaurantero(int restauranteroId) {
        return restauranteRepository.findByRestauranteroId(restauranteroId);
    }
    
    // Obtener todos los restaurantes
    public List<Restaurante> obtenerTodosLosRestaurantes() {
        return restauranteRepository.findAll();
    }
    
    // Obtener restaurantes por estado
    public List<Restaurante> obtenerRestaurantesPorEstado(EstadoRestaurante estado) {
        return restauranteRepository.findByEstado(estado);
    }
    
    // Obtener restaurantes pendientes de aprobación
    public List<Restaurante> obtenerRestaurantesPendientes() {
        return restauranteRepository.findByEstado(EstadoRestaurante.PENDIENTE);
    }
    
    // Obtener restaurantes aprobados
    public List<Restaurante> obtenerRestaurantesAprobados() {
        return restauranteRepository.findByEstado(EstadoRestaurante.APROBADO);
    }
    
    // Buscar restaurantes por nombre
    public List<Restaurante> buscarRestaurantesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return restauranteRepository.findByNombreContaining(nombre.trim());
    }
    
    // Actualizar información del restaurante
    public Restaurante actualizarRestaurante(Restaurante restaurante) {
        // Verificar que el restaurante existe
        Optional<Restaurante> restauranteExistente = restauranteRepository.findById(restaurante.getId_restaurante());
        if (restauranteExistente.isEmpty()) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        
        // Validar datos
        validateRestauranteData(restaurante);
        
        // Actualizar
        return restauranteRepository.update(restaurante);
    }
    
    // Aprobar restaurante (solo para administradores)
    public Restaurante aprobarRestaurante(int restauranteId, int adminId) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(restauranteId);
        if (restauranteOpt.isEmpty()) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        
        Restaurante restaurante = restauranteOpt.get();
        
        // Verificar que está pendiente
        if (restaurante.getEstado() != EstadoRestaurante.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar restaurantes en estado PENDIENTE");
        }
        
        // Aprobar el restaurante
        restaurante.aprobar(adminId);
        
        return restauranteRepository.update(restaurante);
    }
    
    // Rechazar restaurante (solo para administradores)
    public Restaurante rechazarRestaurante(int restauranteId, int adminId) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(restauranteId);
        if (restauranteOpt.isEmpty()) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        
        Restaurante restaurante = restauranteOpt.get();
        
        // Verificar que está pendiente
        if (restaurante.getEstado() != EstadoRestaurante.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar restaurantes en estado PENDIENTE");
        }
        
        // Rechazar el restaurante
        restaurante.rechazar(adminId);
        
        return restauranteRepository.update(restaurante);
    }
    
    // Eliminar restaurante (soft delete)
    public boolean eliminarRestaurante(int restauranteId) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(restauranteId);
        if (restauranteOpt.isEmpty()) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        
        return restauranteRepository.delete(restauranteId);
    }
    
    // Contar restaurantes por estado
    public int contarRestaurantesPorEstado(EstadoRestaurante estado) {
        return restauranteRepository.countByEstado(estado);
    }
    
    // Obtener estadísticas de restaurantes
    public RestauranteStats obtenerEstadisticas() {
        int pendientes = restauranteRepository.countByEstado(EstadoRestaurante.PENDIENTE);
        int aprobados = restauranteRepository.countByEstado(EstadoRestaurante.APROBADO);
        int rechazados = restauranteRepository.countByEstado(EstadoRestaurante.RECHAZADO);
        int total = pendientes + aprobados + rechazados;
        
        return new RestauranteStats(total, aprobados, pendientes, rechazados);
    }
    
    // Validar datos del restaurante
    private void validateRestauranteData(Restaurante restaurante) {
        if (restaurante == null) {
            throw new IllegalArgumentException("Los datos del restaurante son requeridos");
        }
        
        // Validar nombre
        if (restaurante.getNombre() == null || restaurante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es requerido");
        }
        
        if (restaurante.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del restaurante no puede exceder 100 caracteres");
        }
        
        // Validar dirección
        if (restaurante.getDireccion() == null || restaurante.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección del restaurante es requerida");
        }
        
        if (restaurante.getDireccion().length() > 255) {
            throw new IllegalArgumentException("La dirección no puede exceder 255 caracteres");
        }
        
        // Validar teléfono
        if (restaurante.getTelefono() == null || restaurante.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del restaurante es requerido");
        }
        
        if (restaurante.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }
        
        // Validar ID de restaurantero
        if (restaurante.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("El ID del restaurantero debe ser válido");
        }
        
        // Validar horario (si se proporciona)
        if (restaurante.getHorario() != null && restaurante.getHorario().length() > 500) {
            throw new IllegalArgumentException("El horario no puede exceder 500 caracteres");
        }
        
        // Validar URL del menú (si se proporciona)
        if (restaurante.getMenu_url() != null && restaurante.getMenu_url().length() > 255) {
            throw new IllegalArgumentException("La URL del menú no puede exceder 255 caracteres");
        }
    }
    
    // Clase interna para estadísticas
    public static class RestauranteStats {
        private final int total;
        private final int aprobados;
        private final int pendientes;
        private final int rechazados;
        
        public RestauranteStats(int total, int aprobados, int pendientes, int rechazados) {
            this.total = total;
            this.aprobados = aprobados;
            this.pendientes = pendientes;
            this.rechazados = rechazados;
        }
        
        public int getTotal() { return total; }
        public int getAprobados() { return aprobados; }
        public int getPendientes() { return pendientes; }
        public int getRechazados() { return rechazados; }
        
        @Override
        public String toString() {
            return String.format("RestauranteStats{total=%d, aprobados=%d, pendientes=%d, rechazados=%d}", 
                               total, aprobados, pendientes, rechazados);
        }
    }
}
