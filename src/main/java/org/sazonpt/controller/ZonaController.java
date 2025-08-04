package org.sazonpt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.sazonpt.model.Zona;
import org.sazonpt.service.ZonaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ZonaController {
    
    private final ZonaService zonaService;
    private final ObjectMapper objectMapper;
    
    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * GET /zonas - Obtiene todas las zonas
     */
    public void obtenerTodasLasZonas(Context ctx) {
        try {
            List<Zona> zonas = zonaService.obtenerTodasLasZonas();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", zonas);
            response.put("message", "Zonas obtenidas correctamente");
            response.put("total", zonas.size());
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las zonas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * GET /zonas/{id} - Obtiene una zona por ID
     */
    public void obtenerZonaPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Zona> zona = zonaService.obtenerZonaPorId(id);
            
            if (zona.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", zona.get());
                response.put("message", "Zona encontrada");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Zona no encontrada");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener la zona: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * GET /zonas/restaurantero/{idRestaurantero} - Obtiene zonas por restaurantero
     */
    public void obtenerZonasPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Zona> zonas = zonaService.obtenerZonasPorRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", zonas);
            response.put("message", "Zonas del restaurantero obtenidas correctamente");
            response.put("total", zonas.size());
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las zonas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * POST /zonas - Crea una nueva zona
     */
    public void crearZona(Context ctx) {
        try {
            Zona zona = objectMapper.readValue(ctx.body(), Zona.class);
            Zona zonaCreada = zonaService.crearZona(zona);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", zonaCreada);
            response.put("message", "Zona creada correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la zona: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * PUT /zonas/{id} - Actualiza una zona existente
     */
    public void actualizarZona(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Zona zonaActualizada = objectMapper.readValue(ctx.body(), Zona.class);
            
            boolean actualizado = zonaService.actualizarZona(id, zonaActualizada);
            
            if (actualizado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Zona actualizada correctamente");
                response.put("data", Map.of("id_zona", id));
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar la zona");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar la zona: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * DELETE /zonas/{id} - Elimina una zona
     */
    public void eliminarZona(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean eliminado = zonaService.eliminarZona(id);
            
            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Zona eliminada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la zona");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar la zona: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * GET /zonas/buscar?nombre={nombre} - Busca zonas por nombre
     */
    public void buscarZonasPorNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            List<Zona> zonas = zonaService.buscarZonasPorNombre(nombre);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", zonas);
            response.put("message", "Búsqueda completada");
            response.put("total", zonas.size());
            response.put("termino_busqueda", nombre);
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error en la búsqueda: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * GET /zonas/estadisticas - Obtiene estadísticas de zonas
     */
    public void obtenerEstadisticas(Context ctx) {
        try {
            int totalZonas = zonaService.contarZonas();
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("total_zonas", totalZonas);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", estadisticas);
            response.put("message", "Estadísticas obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
    
    /**
     * GET /zonas/restaurantero/{idRestaurantero}/estadisticas - Estadísticas por restaurantero
     */
    public void obtenerEstadisticasPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            int totalZonas = zonaService.contarZonasPorRestaurantero(idRestaurantero);
            boolean puedeCrearMas = zonaService.puedeCrearMasZonas(idRestaurantero);
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("id_restaurantero", idRestaurantero);
            estadisticas.put("total_zonas", totalZonas);
            estadisticas.put("puede_crear_mas", puedeCrearMas);
            estadisticas.put("limite_maximo", 10);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", estadisticas);
            response.put("message", "Estadísticas del restaurantero obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
}
