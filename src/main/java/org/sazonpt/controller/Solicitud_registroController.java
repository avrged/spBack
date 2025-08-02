package org.sazonpt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.service.Solicitud_registroService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Solicitud_registroController {
    
    private final Solicitud_registroService solicitudService;
    private final ObjectMapper objectMapper;

    public Solicitud_registroController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
        this.objectMapper = new ObjectMapper();
    }

    public void obtenerTodasLasSolicitudes(Context ctx) {
        try {
            List<Solicitud_registro> solicitudes = solicitudService.obtenerTodasLasSolicitudes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("message", "Solicitudes obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las solicitudes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerSolicitudPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Solicitud_registro> solicitud = solicitudService.obtenerSolicitudPorId(id);
            
            if (solicitud.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", solicitud.get());
                response.put("message", "Solicitud encontrada");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                
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
            response.put("message", "Error al obtener la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerSolicitudesPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Solicitud_registro> solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("message", "Solicitudes del restaurantero obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las solicitudes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearSolicitud(Context ctx) {
        try {
            Solicitud_registro solicitud = objectMapper.readValue(ctx.body(), Solicitud_registro.class);
            Solicitud_registro solicitudCreada = solicitudService.crearSolicitud(solicitud);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudCreada);
            response.put("message", "Solicitud de registro creada correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitudActualizada = objectMapper.readValue(ctx.body(), Solicitud_registro.class);
            
            boolean actualizada = solicitudService.actualizarSolicitud(id, solicitudActualizada);
            
            if (actualizada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud actualizada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar la solicitud");
                
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
            response.put("message", "Error al actualizar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void aprobarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean aprobada = solicitudService.aprobarSolicitud(id);
            
            if (aprobada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud aprobada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo aprobar la solicitud");
                
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
            response.put("message", "Error al aprobar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void rechazarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean rechazada = solicitudService.rechazarSolicitud(id);
            
            if (rechazada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud rechazada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo rechazar la solicitud");
                
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
            response.put("message", "Error al rechazar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void eliminarSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminada = solicitudService.eliminarSolicitud(idSolicitud, idRestaurantero);
            
            if (eliminada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud eliminada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la solicitud");
                
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
            response.put("message", "Error al eliminar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
}
