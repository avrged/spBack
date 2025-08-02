package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.model.Solicitud_registro.EstadoSolicitud;
import org.sazonpt.service.Solicitud_registroService;
import org.sazonpt.service.Solicitud_registroService.EstadoSolicitudRestaurantero;
import org.sazonpt.service.Solicitud_registroService.SolicitudStats;
import org.sazonpt.repository.Solicitud_registroRepository.SolicitudConRestaurantero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de solicitudes de registro
 * Siguiendo la arquitectura del template API con Javalin
 */
public class Solicitud_registroController {
    
    private final Solicitud_registroService solicitudService;
    
    public Solicitud_registroController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public void crearSolicitud(Context ctx) {
        try {
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            Solicitud_registro nuevaSolicitud = solicitudService.crearSolicitud(solicitud);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Solicitud de registro creada exitosamente");
            response.put("data", nuevaSolicitud);
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void crearSolicitudSimple(Context ctx) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            
            int restauranteroId = (Integer) requestBody.get("restauranteroId");
            
            Solicitud_registro nuevaSolicitud = solicitudService.crearSolicitud(restauranteroId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Solicitud de registro creada exitosamente");
            response.put("data", nuevaSolicitud);
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (ClassCastException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Datos de entrada inválidos. Se requiere 'restauranteroId'");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void crearSolicitudCompleta(Context ctx) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            
            // Validar campos requeridos
            if (!requestBody.containsKey("id_restaurantero")) {
                throw new IllegalArgumentException("Se requiere 'id_restaurantero'");
            }
            
            int id_restaurantero = (Integer) requestBody.get("id_restaurantero");

            @SuppressWarnings("unchecked")
            Map<String, Object> datos_restaurante = (Map<String, Object>) requestBody.get("datos_restaurante");
            
            if (datos_restaurante == null || datos_restaurante.isEmpty()) {
                throw new IllegalArgumentException("Se requieren los 'datos_restaurante'");
            }

            Solicitud_registro nuevaSolicitud = solicitudService.crearSolicitudCompleta(
                id_restaurantero, datos_restaurante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Solicitud de registro completa creada exitosamente");
            response.put("data", nuevaSolicitud);
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (ClassCastException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Datos de entrada inválidos. Verifique el formato del JSON");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor: " + e.getMessage());
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerEstadoSolicitudRestaurantero(Context ctx) {
        try {
            int restauranteroId = Integer.parseInt(ctx.pathParam("restauranteroId"));
            EstadoSolicitudRestaurantero estado = solicitudService.obtenerEstadoSolicitudRestaurantero(restauranteroId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "restaurantero_id", restauranteroId,
                "estado", estado.getValor(),
                "puede_crear_restaurante", solicitudService.puedeCrearRestaurante(restauranteroId),
                "tiene_pendiente", solicitudService.tieneSolicitudPendiente(restauranteroId)
            ));
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurantero inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerSolicitudPorRestaurantero(Context ctx) {
        try {
            int restauranteroId = Integer.parseInt(ctx.pathParam("restauranteroId"));
            Optional<Solicitud_registro> solicitudOpt = solicitudService.obtenerSolicitudPorRestaurantero(restauranteroId);
            
            if (solicitudOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", solicitudOpt.get());
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El restaurantero no tiene solicitudes");
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurantero inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerSolicitudDetalle(Context ctx) {
        try {
            int solicitudId = Integer.parseInt(ctx.pathParam("solicitudId"));
            Optional<Solicitud_registro> solicitudOpt = solicitudService.obtenerSolicitudPorId(solicitudId);
            
            if (solicitudOpt.isPresent()) {
                Solicitud_registro solicitud = solicitudOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                
                // Crear objeto de respuesta con datos parseados
                Map<String, Object> solicitudDetalle = new HashMap<>();
                solicitudDetalle.put("id_solicitud", solicitud.getId_solicitud());
                solicitudDetalle.put("id_restaurantero", solicitud.getId_restaurantero());
                solicitudDetalle.put("estado", solicitud.getEstado().getValor());
                solicitudDetalle.put("revisado_por", solicitud.getRevisado_por());
                solicitudDetalle.put("revisado_en", solicitud.getRevisado_en());
                solicitudDetalle.put("creado_en", solicitud.getCreado_en());
                
                // Parsear datos del restaurante si existen
                if (solicitud.tieneDatosRestaurante()) {
                    try {
                        Map<String, Object> datosRestaurante = solicitudService.parsearDatosRestaurante(solicitud.getDatos_restaurante());
                        solicitudDetalle.put("datos_restaurante", datosRestaurante);
                    } catch (Exception e) {
                        solicitudDetalle.put("datos_restaurante", solicitud.getDatos_restaurante()); // JSON crudo si hay error
                        solicitudDetalle.put("error_parsing", "Error al parsear datos del restaurante");
                    }
                } else {
                    solicitudDetalle.put("datos_restaurante", null);
                }
                
                response.put("data", solicitudDetalle);
                ctx.status(HttpStatus.OK).json(response);
                
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de solicitud inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor: " + e.getMessage());
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerTodasLasSolicitudes(Context ctx) {
        try {
            String estadoParam = ctx.queryParam("estado");
            List<Solicitud_registro> solicitudes;
            
            if (estadoParam != null && !estadoParam.trim().isEmpty()) {
                try {
                    EstadoSolicitud estado = EstadoSolicitud.fromValor(estadoParam.toLowerCase());
                    solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado);
                } catch (IllegalArgumentException e) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estado inválido. Valores permitidos: pendiente, aprobado, rechazado");
                    
                    ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
                    return;
                }
            } else {
                solicitudes = solicitudService.obtenerTodasLasSolicitudes();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("total", solicitudes.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Obtener solicitudes con información completa del restaurantero
     * GET /solicitudes-registro/completas
     */
    public void obtenerSolicitudesCompletas(Context ctx) {
        try {
            List<SolicitudConRestaurantero> solicitudes = solicitudService.obtenerSolicitudesConInfoCompleta();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("total", solicitudes.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Obtener solicitud por ID
     * GET /solicitudes-registro/{id}
     */
    public void obtenerSolicitudPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Solicitud_registro> solicitudOpt = solicitudService.obtenerSolicitudPorId(id);
            
            if (solicitudOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", solicitudOpt.get());
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de solicitud inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Aprobar una solicitud (solo administradores)
     * PUT /solicitudes-registro/{id}/aprobar
     */
    public void aprobarSolicitud(Context ctx) {
        try {
            int solicitudId = Integer.parseInt(ctx.pathParam("id"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            int id_administrador = (Integer) requestBody.get("id_administrador");
            
            Solicitud_registro solicitudAprobada = solicitudService.aprobarSolicitud(solicitudId, id_administrador);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Solicitud aprobada exitosamente");
            response.put("data", solicitudAprobada);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (ClassCastException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Se requiere 'id_administrador' en el cuerpo de la petición");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Rechazar una solicitud (solo administradores)
     * PUT /solicitudes-registro/{id}/rechazar
     */
    public void rechazarSolicitud(Context ctx) {
        try {
            int solicitudId = Integer.parseInt(ctx.pathParam("id"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            int id_administrador = (Integer) requestBody.get("id_administrador");
            String motivo = (String) requestBody.getOrDefault("motivo", null);
            
            Solicitud_registro solicitudRechazada = solicitudService.rechazarSolicitud(solicitudId, id_administrador, motivo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Solicitud rechazada");
            response.put("data", solicitudRechazada);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (ClassCastException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Se requiere 'id_administrador' en el cuerpo de la petición");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Obtener estadísticas de solicitudes
     * GET /solicitudes-registro/estadisticas
     */
    public void obtenerEstadisticas(Context ctx) {
        try {
            SolicitudStats estadisticas = solicitudService.obtenerEstadisticas();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "total", estadisticas.getTotal(),
                "aprobadas", estadisticas.getAprobadas(),
                "pendientes", estadisticas.getPendientes(),
                "rechazadas", estadisticas.getRechazadas()
            ));
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    // ===============================
    // ENDPOINTS DE VERIFICACIÓN
    // ===============================
    
    /**
     * Verificar si un restaurantero puede crear restaurante
     * GET /solicitudes-registro/restaurantero/{restauranteroId}/puede-crear-restaurante
     */
    public void verificarPuedeCrearRestaurante(Context ctx) {
        try {
            int restauranteroId = Integer.parseInt(ctx.pathParam("restauranteroId"));
            boolean puedeCrear = solicitudService.puedeCrearRestaurante(restauranteroId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "restaurantero_id", restauranteroId,
                "puede_crear_restaurante", puedeCrear,
                "mensaje", puedeCrear ? 
                    "El restaurantero puede crear un restaurante" : 
                    "El restaurantero necesita una solicitud aprobada primero"
            ));
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurantero inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
}
