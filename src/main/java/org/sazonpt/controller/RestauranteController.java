package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurante.EstadoRestaurante;
import org.sazonpt.service.RestauranteService;
import org.sazonpt.service.RestauranteService.RestauranteStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de restaurantes
 * Siguiendo la arquitectura del template API con Javalin
 */
public class RestauranteController {
    
    private final RestauranteService restauranteService;
    
    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }
    
    /**
     * Crear un nuevo restaurante
     * POST /restaurantes
     */
    public void crearRestaurante(Context ctx) {
        try {
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);
            Restaurante nuevoRestaurante = restauranteService.crearRestaurante(restaurante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante creado exitosamente");
            response.put("data", nuevoRestaurante);
            
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
    
    /**
     * Obtener restaurante por ID
     * GET /restaurantes/{id}
     */
    public void obtenerRestaurantePorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Restaurante> restaurante = restauranteService.obtenerRestaurantePorId(id);
            
            if (restaurante.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", restaurante.get());
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Restaurante no encontrado");
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Obtener restaurante por ID de restaurantero
     * GET /restaurantes/restaurantero/{restauranteroId}
     */
    public void obtenerRestaurantePorRestaurantero(Context ctx) {
        try {
            int restauranteroId = Integer.parseInt(ctx.pathParam("restauranteroId"));
            Optional<Restaurante> restaurante = restauranteService.obtenerRestaurantePorRestaurantero(restauranteroId);
            
            if (restaurante.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", restaurante.get());
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El restaurantero no tiene restaurante registrado");
                
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
    
    /**
     * Obtener todos los restaurantes
     * GET /restaurantes
     */
    public void obtenerTodosLosRestaurantes(Context ctx) {
        try {
            // Verificar si hay filtro por estado
            String estadoParam = ctx.queryParam("estado");
            List<Restaurante> restaurantes;
            
            if (estadoParam != null && !estadoParam.trim().isEmpty()) {
                try {
                    EstadoRestaurante estado = EstadoRestaurante.fromValor(estadoParam.toLowerCase());
                    restaurantes = restauranteService.obtenerRestaurantesPorEstado(estado);
                } catch (IllegalArgumentException e) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estado inválido. Valores permitidos: pendiente, aprobado, rechazado");
                    
                    ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
                    return;
                }
            } else {
                restaurantes = restauranteService.obtenerTodosLosRestaurantes();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restaurantes);
            response.put("total", restaurantes.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Buscar restaurantes por nombre
     * GET /restaurantes/buscar?nombre={nombre}
     */
    public void buscarRestaurantesPorNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            
            if (nombre == null || nombre.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "El parámetro 'nombre' es requerido");
                
                ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
                return;
            }
            
            List<Restaurante> restaurantes = restauranteService.buscarRestaurantesPorNombre(nombre);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restaurantes);
            response.put("total", restaurantes.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    /**
     * Actualizar restaurante
     * PUT /restaurantes/{id}
     */
    public void actualizarRestaurante(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);
            restaurante.setId_restaurante(id);
            
            Restaurante restauranteActualizado = restauranteService.actualizarRestaurante(restaurante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante actualizado exitosamente");
            response.put("data", restauranteActualizado);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
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
     * Aprobar restaurante (solo administradores)
     * PUT /restaurantes/{id}/aprobar
     */
    public void aprobarRestaurante(Context ctx) {
        try {
            int restauranteId = Integer.parseInt(ctx.pathParam("id"));
            
            // Obtener ID del admin desde el cuerpo de la petición o header de autenticación
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            int adminId = (Integer) requestBody.get("adminId");
            
            Restaurante restauranteAprobado = restauranteService.aprobarRestaurante(restauranteId, adminId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante aprobado exitosamente");
            response.put("data", restauranteAprobado);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID inválido");
            
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
     * Rechazar restaurante (solo administradores)
     * PUT /restaurantes/{id}/rechazar
     */
    public void rechazarRestaurante(Context ctx) {
        try {
            int restauranteId = Integer.parseInt(ctx.pathParam("id"));
            
            // Obtener ID del admin desde el cuerpo de la petición
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            int adminId = (Integer) requestBody.get("adminId");
            
            Restaurante restauranteRechazado = restauranteService.rechazarRestaurante(restauranteId, adminId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante rechazado");
            response.put("data", restauranteRechazado);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID inválido");
            
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
     * Eliminar restaurante
     * DELETE /restaurantes/{id}
     */
    public void eliminarRestaurante(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean eliminado = restauranteService.eliminarRestaurante(id);
            
            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Restaurante eliminado exitosamente");
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar el restaurante");
                
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
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
     * Obtener estadísticas de restaurantes
     * GET /restaurantes/estadisticas
     */
    public void obtenerEstadisticas(Context ctx) {
        try {
            RestauranteStats estadisticas = restauranteService.obtenerEstadisticas();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "total", estadisticas.getTotal(),
                "aprobados", estadisticas.getAprobados(),
                "pendientes", estadisticas.getPendientes(),
                "rechazados", estadisticas.getRechazados()
            ));
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
}
