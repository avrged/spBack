package org.sazonpt.service;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.model.Solicitud_registro.EstadoSolicitud;
import org.sazonpt.repository.Solicitud_registroRepository;
import org.sazonpt.repository.Solicitud_registroRepository.SolicitudConRestaurantero;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Solicitud_registroService {
    
    private final Solicitud_registroRepository solicitudRepository;
    
    public Solicitud_registroService(Solicitud_registroRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }
    
    public Solicitud_registro crearSolicitud(Solicitud_registro solicitud) {
        // Validar datos básicos
        validateSolicitudData(solicitud);
        
        // Verificar que no tenga una solicitud pendiente
        if (solicitudRepository.hasPendingSolicitud(solicitud.getId_restaurantero())) {
            throw new IllegalStateException("Ya tienes una solicitud pendiente. Espera a que sea revisada.");
        }
        
        // Crear la solicitud
        return solicitudRepository.save(solicitud);
    }

    public Solicitud_registro crearSolicitudCompleta(int restauranteroId, Map<String, Object> datosRestaurante) {
        // Validar que se proporcionen datos del restaurante
        if (datosRestaurante == null || datosRestaurante.isEmpty()) {
            throw new IllegalArgumentException("Los datos del restaurante son obligatorios");
        }
        
        // Validar campos mínimos requeridos en los datos del restaurante
        validateDatosRestaurante(datosRestaurante);
        
        // Convertir el Map a JSON String
        String datosRestauranteJson = convertirMapAJson(datosRestaurante);
        
        // Crear la solicitud con los datos del restaurante
        Solicitud_registro solicitud = new Solicitud_registro(restauranteroId, datosRestauranteJson);
        return crearSolicitud(solicitud);
    }
    
    /**
     * Crear solicitud solo con restaurantero
     */
    public Solicitud_registro crearSolicitud(int restauranteroId) {
        Solicitud_registro solicitud = new Solicitud_registro(restauranteroId);
        return crearSolicitud(solicitud);
    }

    public Optional<Solicitud_registro> obtenerSolicitudPorId(int id) {
        return solicitudRepository.findById(id);
    }

    public Optional<Solicitud_registro> obtenerSolicitudPorRestaurantero(int restauranteroId) {
        return solicitudRepository.findByRestauranteroId(restauranteroId);
    }
    
    public List<Solicitud_registro> obtenerTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud_registro> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public List<Solicitud_registro> obtenerSolicitudesPendientes() {
        return solicitudRepository.findPendientes();
    }

    public List<Solicitud_registro> obtenerSolicitudesAprobadas() {
        return solicitudRepository.findAprobadas();
    }

    public List<Solicitud_registro> obtenerSolicitudesRechazadas() {
        return solicitudRepository.findRechazadas();
    }

    public List<SolicitudConRestaurantero> obtenerSolicitudesConInfoCompleta() {
        return solicitudRepository.findAllWithRestauranteroInfo();
    }

    public Solicitud_registro aprobarSolicitud(int solicitudId, int id_administrador) {
        Optional<Solicitud_registro> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("La solicitud no existe");
        }
        
        Solicitud_registro solicitud = solicitudOpt.get();
        
        // Verificar que está pendiente
        if (!solicitud.estaPendiente()) {
            throw new IllegalStateException("Solo se pueden aprobar solicitudes en estado PENDIENTE");
        }
        
        // Aprobar la solicitud
        solicitud.aprobar(id_administrador);
        
        return solicitudRepository.update(solicitud);
    }
    
    public Solicitud_registro rechazarSolicitud(int solicitudId, int id_administrador, String motivo) {
        Optional<Solicitud_registro> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("La solicitud no existe");
        }
        
        Solicitud_registro solicitud = solicitudOpt.get();
        
        // Verificar que está pendiente
        if (!solicitud.estaPendiente()) {
            throw new IllegalStateException("Solo se pueden rechazar solicitudes en estado PENDIENTE");
        }
        
        // Rechazar la solicitud
        solicitud.rechazar(id_administrador, motivo);
        
        return solicitudRepository.update(solicitud);
    }
    
    /**
     * Rechazar solicitud sin motivo específico
     */
    public Solicitud_registro rechazarSolicitud(int solicitudId, int id_administrador) {
        return rechazarSolicitud(solicitudId, id_administrador, null);
    }

    public boolean puedeCrearRestaurante(int restauranteroId) {
        return solicitudRepository.hasApprovedSolicitud(restauranteroId);
    }

    public boolean tieneSolicitudPendiente(int restauranteroId) {
        return solicitudRepository.hasPendingSolicitud(restauranteroId);
    }

    public boolean tieneSolicitudAprobada(int restauranteroId) {
        return solicitudRepository.hasApprovedSolicitud(restauranteroId);
    }

    public EstadoSolicitudRestaurantero obtenerEstadoSolicitudRestaurantero(int restauranteroId) {
        Optional<Solicitud_registro> solicitudOpt = solicitudRepository.findByRestauranteroId(restauranteroId);
        
        if (solicitudOpt.isEmpty()) {
            return EstadoSolicitudRestaurantero.SIN_SOLICITUD;
        }
        
        Solicitud_registro solicitud = solicitudOpt.get();
        switch (solicitud.getEstado()) {
            case PENDIENTE:
                return EstadoSolicitudRestaurantero.PENDIENTE;
            case APROBADO:
                return EstadoSolicitudRestaurantero.APROBADO;
            case RECHAZADO:
                return EstadoSolicitudRestaurantero.RECHAZADO;
            default:
                return EstadoSolicitudRestaurantero.SIN_SOLICITUD;
        }
    }

    public SolicitudStats obtenerEstadisticas() {
        int pendientes = solicitudRepository.countByEstado(EstadoSolicitud.PENDIENTE);
        int aprobadas = solicitudRepository.countByEstado(EstadoSolicitud.APROBADO);
        int rechazadas = solicitudRepository.countByEstado(EstadoSolicitud.RECHAZADO);
        int total = pendientes + aprobadas + rechazadas;
        
        return new SolicitudStats(total, aprobadas, pendientes, rechazadas);
    }

    public int contarSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.countByEstado(estado);
    }

    private void validateSolicitudData(Solicitud_registro solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("Los datos de la solicitud son requeridos");
        }
        
        // Validar ID de restaurantero
        if (solicitud.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("El ID del restaurantero debe ser válido");
        }
    }

    public enum EstadoSolicitudRestaurantero {
        SIN_SOLICITUD("sin_solicitud"),
        PENDIENTE("pendiente"),
        APROBADO("aprobado"),
        RECHAZADO("rechazado");
        
        private final String valor;
        
        EstadoSolicitudRestaurantero(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }

    public static class SolicitudStats {
        private final int total;
        private final int aprobadas;
        private final int pendientes;
        private final int rechazadas;
        
        public SolicitudStats(int total, int aprobadas, int pendientes, int rechazadas) {
            this.total = total;
            this.aprobadas = aprobadas;
            this.pendientes = pendientes;
            this.rechazadas = rechazadas;
        }
        
        public int getTotal() { return total; }
        public int getAprobadas() { return aprobadas; }
        public int getPendientes() { return pendientes; }
        public int getRechazadas() { return rechazadas; }
        
        @Override
        public String toString() {
            return String.format("SolicitudStats{total=%d, aprobadas=%d, pendientes=%d, rechazadas=%d}", 
                               total, aprobadas, pendientes, rechazadas);
        }
    }

    private void validateDatosRestaurante(Map<String, Object> datosRestaurante) {
        // Campos obligatorios
        String[] camposObligatorios = {
            "nombre_restaurante", "propietario", "correo", "telefono", "direccion"
        };
        
        for (String campo : camposObligatorios) {
            if (!datosRestaurante.containsKey(campo) || 
                datosRestaurante.get(campo) == null || 
                datosRestaurante.get(campo).toString().trim().isEmpty()) {
                throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio en los datos del restaurante");
            }
        }
        
        // Validar formato de correo (básico)
        String correo = datosRestaurante.get("correo").toString();
        if (!correo.contains("@") || !correo.contains(".")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
        }
        
        // Validar que el nombre del restaurante no esté vacío
        String nombreRestaurante = datosRestaurante.get("nombre_restaurante").toString().trim();
        if (nombreRestaurante.length() < 3) {
            throw new IllegalArgumentException("El nombre del restaurante debe tener al menos 3 caracteres");
        }
    }
    
    /**
     * Convertir Map a JSON String usando Jackson
     */
    private String convertirMapAJson(Map<String, Object> datosRestaurante) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(datosRestaurante);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir datos del restaurante a JSON: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parsear JSON String a Map usando Jackson
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parsearDatosRestaurante(String datosRestauranteJson) {
        try {
            if (datosRestauranteJson == null || datosRestauranteJson.trim().isEmpty()) {
                return new HashMap<>();
            }
            
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(datosRestauranteJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear datos del restaurante desde JSON: " + e.getMessage(), e);
        }
    }
}
