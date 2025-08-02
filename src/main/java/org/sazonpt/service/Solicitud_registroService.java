package org.sazonpt.service;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.model.Solicitud_registro.EstadoSolicitud;
import org.sazonpt.model.Imagen_restaurante;
import org.sazonpt.model.Restaurante;
import org.sazonpt.repository.Solicitud_registroRepository;
import org.sazonpt.repository.Solicitud_registroRepository.SolicitudConRestaurantero;
import org.sazonpt.repository.Imagen_restauranteRepository;
import org.sazonpt.repository.RestauranteRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class Solicitud_registroService {
    
    private final Solicitud_registroRepository solicitudRepository;
    private final RestauranteRepository restauranteRepository;
    
    public Solicitud_registroService(Solicitud_registroRepository solicitudRepository, RestauranteRepository restauranteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.restauranteRepository = restauranteRepository;
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

        int restauranteId = 0;
        
        // 1. Crear restaurante si hay datos_restaurante
        if (solicitud.tieneDatosRestaurante()) {
            try {
                restauranteId = crearRestauranteDesdeJSON(solicitud, id_administrador);
                System.out.println("✅ Restaurante creado con ID: " + restauranteId);
            } catch (Exception e) {
                System.err.println("❌ Error al crear restaurante para solicitud " + solicitudId + ": " + e.getMessage());
                throw new RuntimeException("Error al crear restaurante: " + e.getMessage(), e);
            }
        }
        
        // 2. Crear imágenes del restaurante usando el ID real
        if (solicitud.tieneDatosRestaurante() && restauranteId > 0) {
            try {
                crearImagenesDesdeJSON(solicitud, restauranteId);
                System.out.println("✅ Imágenes creadas para restaurante ID: " + restauranteId);
            } catch (Exception e) {
                System.err.println("⚠️ Error al crear imágenes para solicitud " + solicitudId + ": " + e.getMessage());
                // No falla la aprobación si hay error con las imágenes
            }
        }

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
    
    /**
     * ✅ CORREGIDO: Crear restaurante desde datos JSON con nombres de campos correctos
     */
    private int crearRestauranteDesdeJSON(Solicitud_registro solicitud, int id_administrador) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode datosRestaurante = objectMapper.readTree(solicitud.getDatos_restaurante());
            
            // Crear objeto Restaurante con datos del JSON
            Restaurante restaurante = new Restaurante();
            
            // ✅ CORREGIR campos según el JSON correcto
            restaurante.setNombre(datosRestaurante.get("nombre_restaurante").asText());
            restaurante.setDireccion(datosRestaurante.get("direccion").asText());
            restaurante.setTelefono(datosRestaurante.get("telefono").asText());
            
            // ✅ Horario desde el campo correcto del JSON
            String horarioCompleto = datosRestaurante.has("horarios_cierre_apertura") ? 
                datosRestaurante.get("horarios_cierre_apertura").asText() : 
                "Lunes a Domingo de 09:00 AM a 09:00 PM";
                
            // Convertir a JSON estructurado para la BD
            String horarioJson = String.format(
                "{\"descripcion\":\"%s\"}", 
                horarioCompleto
            );
            restaurante.setHorario(horarioJson);
            
            // ✅ Menu URL desde el JSON si existe
            if (datosRestaurante.has("menu_restaurante") && !datosRestaurante.get("menu_restaurante").isNull()) {
                restaurante.setMenu_url(datosRestaurante.get("menu_restaurante").asText());
            }
            
            // ✅ CORREGIR: El restaurante debe crearse ya APROBADO porque viene de solicitud aprobada
            // Foreign key
            restaurante.setId_restaurantero(solicitud.getId_restaurantero());
            
            // ✅ APROBAR AUTOMÁTICAMENTE el restaurante al crearlo desde solicitud aprobada
            restaurante.aprobar(id_administrador);
            
            // Crear el restaurante usando save() que devuelve el objeto con ID
            Restaurante restauranteGuardado = restauranteRepository.save(restaurante);
            int restauranteId = restauranteGuardado.getId_restaurante();
            
            System.out.println("🏪 Restaurante '" + restaurante.getNombre() + "' creado y APROBADO con ID: " + restauranteId);
            return restauranteId;
            
        } catch (Exception e) {
            System.err.println("❌ Error creando restaurante: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear restaurante desde solicitud", e);
        }
    }

    /**
     * ✅ DEPURACIÓN: Crear imágenes del restaurante desde el JSON de la solicitud
     * Este método se ejecuta cuando se aprueba una solicitud
     */
    private void crearImagenesDesdeJSON(Solicitud_registro solicitud, int restauranteId) {
        try {
            System.out.println("🖼️ INICIANDO creación de imágenes para restaurante ID: " + restauranteId);
            
            // 1. Obtener JSON raw para debugging
            String jsonRaw = solicitud.getDatos_restaurante();
            System.out.println("📄 JSON raw: " + jsonRaw);
            
            // 2. Parsear datos del restaurante desde JSON
            Map<String, Object> datosRestaurante = parsearDatosRestaurante(jsonRaw);
            System.out.println("🔍 Datos parseados: " + datosRestaurante);
            
            // 3. Verificar que las claves de imagen existen
            System.out.println("� Claves disponibles: " + datosRestaurante.keySet());
            
            // 4. Crear instancia del repositorio de imágenes
            Imagen_restauranteRepository imagenRepository = new Imagen_restauranteRepository();
            
            // 5. Crear imagen principal
            String imagenPrincipal = (String) datosRestaurante.get("imagen_principal");
            System.out.println("🖼️ Imagen principal encontrada: " + imagenPrincipal);
            if (imagenPrincipal != null && !imagenPrincipal.trim().isEmpty()) {
                try {
                    Imagen_restaurante principal = new Imagen_restaurante(
                        restauranteId, 
                        imagenPrincipal, 
                        Imagen_restaurante.TipoImagen.PRINCIPAL
                    );
                    System.out.println("🔍 Objeto imagen principal creado: " + principal);
                    principal.validar(); // Validar antes de guardar
                    System.out.println("✅ Validación de imagen principal PASADA");
                    Imagen_restaurante imagenGuardada = imagenRepository.save(principal);
                    System.out.println("✅ Imagen principal GUARDADA con ID: " + imagenGuardada.getId_imagen());
                } catch (Exception e) {
                    System.err.println("❌ Error específico con imagen principal: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ Imagen principal NO encontrada o vacía");
            }
            
            // 6. Crear imagen secundaria
            String imagenSecundaria = (String) datosRestaurante.get("imagen_secundaria");
            System.out.println("🖼️ Imagen secundaria encontrada: " + imagenSecundaria);
            if (imagenSecundaria != null && !imagenSecundaria.trim().isEmpty()) {
                try {
                    Imagen_restaurante secundaria = new Imagen_restaurante(
                        restauranteId, 
                        imagenSecundaria, 
                        Imagen_restaurante.TipoImagen.SECUNDARIA
                    );
                    System.out.println("🔍 Objeto imagen secundaria creado: " + secundaria);
                    secundaria.validar(); // Validar antes de guardar
                    System.out.println("✅ Validación de imagen secundaria PASADA");
                    Imagen_restaurante imagenGuardada = imagenRepository.save(secundaria);
                    System.out.println("✅ Imagen secundaria GUARDADA con ID: " + imagenGuardada.getId_imagen());
                } catch (Exception e) {
                    System.err.println("❌ Error específico con imagen secundaria: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ Imagen secundaria NO encontrada o vacía");
            }
            
            // 7. Crear imagen de platillo
            String imagenPlatillo = (String) datosRestaurante.get("imagen_platillo");
            System.out.println("🖼️ Imagen platillo encontrada: " + imagenPlatillo);
            if (imagenPlatillo != null && !imagenPlatillo.trim().isEmpty()) {
                try {
                    Imagen_restaurante platillo = new Imagen_restaurante(
                        restauranteId, 
                        imagenPlatillo, 
                        Imagen_restaurante.TipoImagen.PLATILLO
                    );
                    System.out.println("🔍 Objeto imagen platillo creado: " + platillo);
                    platillo.validar(); // Validar antes de guardar
                    System.out.println("✅ Validación de imagen platillo PASADA");
                    Imagen_restaurante imagenGuardada = imagenRepository.save(platillo);
                    System.out.println("✅ Imagen platillo GUARDADA con ID: " + imagenGuardada.getId_imagen());
                } catch (Exception e) {
                    System.err.println("❌ Error específico con imagen platillo: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ Imagen platillo NO encontrada o vacía");
            }
            
            System.out.println("🏁 FINALIZADO proceso de creación de imágenes");
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear imágenes desde JSON: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear imágenes del restaurante: " + e.getMessage(), e);
        }
    }
    
    /**
     * Método temporal para obtener el ID del restaurante
     * NOTA: Ya no se necesita, se usa el ID real del restaurante creado
     */
}
