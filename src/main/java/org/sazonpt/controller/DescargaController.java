package org.sazonpt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.sazonpt.model.Descarga;
import org.sazonpt.service.DescargaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DescargaController {
    public void actualizarDescargaPorRestaurantero(io.javalin.http.Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            Descarga body = objectMapper.readValue(ctx.body(), Descarga.class);
            boolean actualizada = descargaService.actualizarPrimeraDescargaPorRestaurantero(idRestaurantero, body);
            if (actualizada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Descarga actualizada correctamente");
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se encontró descarga para ese restaurantero");
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar la descarga: " + e.getMessage());
            ctx.status(500).json(response);
        }
    }
    
    private final DescargaService descargaService;
    private final ObjectMapper objectMapper;

    public DescargaController(DescargaService descargaService) {
        this.descargaService = descargaService;
        this.objectMapper = new ObjectMapper();
    }

    public void obtenerTodasLasDescargas(Context ctx) {
        try {
            List<Descarga> descargas = descargaService.obtenerTodasLasDescargas();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargas);
            response.put("message", "Descargas obtenidas correctamente");
            response.put("total", descargas.size());
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las descargas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerDescargaPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Descarga> descarga = descargaService.obtenerDescargaPorId(id);
            
            if (descarga.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", descarga.get());
                response.put("message", "Descarga encontrada");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Descarga no encontrada");
                
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
            response.put("message", "Error al obtener la descarga: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerDescargasPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Descarga> descargas = descargaService.obtenerDescargasPorRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargas);
            response.put("message", "Descargas del restaurantero obtenidas correctamente");
            response.put("total", descargas.size());
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las descargas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerDescargasPorOrigen(Context ctx) {
        try {
            String origen = ctx.pathParam("origen");
            List<Descarga> descargas = descargaService.obtenerDescargasPorOrigen(origen);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargas);
            response.put("message", "Descargas filtradas por origen: " + origen);
            response.put("total", descargas.size());
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las descargas por origen: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerDescargasPorOpinion(Context ctx) {
        try {
            String opinion = ctx.pathParam("opinion");
            List<Descarga> descargas = descargaService.obtenerDescargasPorOpinion(opinion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargas);
            response.put("message", "Descargas filtradas por opinión: " + opinion);
            response.put("total", descargas.size());
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las descargas por opinión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearDescarga(Context ctx) {
        try {
            Descarga descarga = objectMapper.readValue(ctx.body(), Descarga.class);
            Descarga descargaCreada = descargaService.crearDescarga(descarga);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargaCreada);
            response.put("message", "Descarga creada correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la descarga: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarDescarga(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Descarga descarga = objectMapper.readValue(ctx.body(), Descarga.class);
            
            boolean actualizada = descargaService.actualizarDescarga(id, descarga);
            
            if (actualizada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Descarga actualizada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar la descarga");
                
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
            response.put("message", "Error al actualizar la descarga: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void incrementarDescargas(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean incrementada = descargaService.incrementarDescargas(id);
            
            if (incrementada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Contador de descargas incrementado correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo incrementar el contador");
                
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
            response.put("message", "Error al incrementar las descargas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void eliminarDescarga(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminada = descargaService.eliminarDescarga(id, idRestaurantero);
            
            if (eliminada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Descarga eliminada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la descarga");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "IDs inválidos");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar la descarga: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    // Endpoints de estadísticas
    public void obtenerTotalDescargasPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            int total = descargaService.getTotalDescargasByRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "id_restaurantero", idRestaurantero,
                "total_descargas", total
            ));
            response.put("message", "Total de descargas obtenido correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener el total de descargas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerTopDescargasPorOrigen(Context ctx) {
        try {
            String origen = ctx.pathParam("origen");
            String limitParam = ctx.queryParam("limit");
            int limit = limitParam != null ? Integer.parseInt(limitParam) : 10;
            
            List<Descarga> topDescargas = descargaService.getTopDescargasByOrigen(origen, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", topDescargas);
            response.put("message", "Top " + limit + " descargas por origen: " + origen);
            response.put("total", topDescargas.size());
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Parámetro limit inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener top descargas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearDescargaRapida(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = objectMapper.readValue(ctx.body(), Map.class);
            
            int cantidadDescargas = (Integer) requestBody.get("cantidad_descargas");
            String origen = (String) requestBody.get("origen");
            String opinion = (String) requestBody.get("opinion");
            
            Descarga descarga = new Descarga(cantidadDescargas, origen, opinion, idRestaurantero);
            Descarga descargaCreada = descargaService.crearDescarga(descarga);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", descargaCreada);
            response.put("message", "Descarga creada correctamente para el restaurantero");
            
            ctx.status(201).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (ClassCastException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Formato de datos inválido");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la descarga: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerEstadisticasAgrupadasPorOrigenYOpinion(Context ctx) {
        try {
            var estadisticas = descargaService.obtenerEstadisticasAgrupadasPorOrigenYOpinion();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", estadisticas);
            response.put("message", "Estadísticas agrupadas por origen y opinión obtenidas correctamente");
            response.put("total", estadisticas.size());
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas agrupadas: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
}
