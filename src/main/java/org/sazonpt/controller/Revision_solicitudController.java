package org.sazonpt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.service.Revision_solicitudService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Revision_solicitudController {
    
    private final Revision_solicitudService revisionService;
    private final ObjectMapper objectMapper;

    public Revision_solicitudController(Revision_solicitudService revisionService) {
        this.revisionService = revisionService;
        this.objectMapper = new ObjectMapper();
    }

    public void obtenerTodasLasRevisiones(Context ctx) {
        try {
            List<Revision_solicitud> revisiones = revisionService.obtenerTodasLasRevisiones();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", revisiones);
            response.put("message", "Revisiones obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las revisiones: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRevisionPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Revision_solicitud> revision = revisionService.obtenerRevisionPorId(id);
            
            if (revision.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", revision.get());
                response.put("message", "Revisión encontrada");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Revisión no encontrada");
                
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
            response.put("message", "Error al obtener la revisión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRevisionesPorSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Revision_solicitud> revisiones = revisionService.obtenerRevisionesPorSolicitud(idSolicitud, idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", revisiones);
            response.put("message", "Revisiones de la solicitud obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "IDs inválidos");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las revisiones: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRevisionesPorAdministrador(Context ctx) {
        try {
            int idAdministrador = Integer.parseInt(ctx.pathParam("idAdministrador"));
            List<Revision_solicitud> revisiones = revisionService.obtenerRevisionesPorAdministrador(idAdministrador);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", revisiones);
            response.put("message", "Revisiones del administrador obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de administrador inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las revisiones: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearRevision(Context ctx) {
        try {
            Revision_solicitud revision = objectMapper.readValue(ctx.body(), Revision_solicitud.class);
            Revision_solicitud revisionCreada = revisionService.crearRevision(revision);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", revisionCreada);
            response.put("message", "Revisión de solicitud creada correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la revisión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearRevisionRapida(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            int idAdministrador = Integer.parseInt(ctx.pathParam("idAdministrador"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = objectMapper.readValue(ctx.body(), Map.class);
            String contenidoRevision = (String) requestBody.get("contenido");
            
            if (contenidoRevision == null || contenidoRevision.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El contenido de la revisión es obligatorio");
                
                ctx.status(400).json(response);
                return;
            }
            
            Revision_solicitud revisionCreada = revisionService.crearRevisionPorAdministrador(
                idSolicitud, idRestaurantero, idAdministrador, contenidoRevision);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", revisionCreada);
            response.put("message", "Revisión creada correctamente");
            
            ctx.status(201).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "IDs inválidos");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la revisión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarRevision(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Revision_solicitud revisionActualizada = objectMapper.readValue(ctx.body(), Revision_solicitud.class);
            
            boolean actualizada = revisionService.actualizarRevision(id, revisionActualizada);
            
            if (actualizada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Revisión actualizada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar la revisión");
                
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
            response.put("message", "Error al actualizar la revisión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void eliminarRevision(Context ctx) {
        try {
            int idRevision = Integer.parseInt(ctx.pathParam("id"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            int idAdministrador = Integer.parseInt(ctx.pathParam("idAdministrador"));
            
            boolean eliminada = revisionService.eliminarRevision(idRevision, idSolicitud, idRestaurantero, idAdministrador);
            
            if (eliminada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Revisión eliminada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la revisión");
                
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
            response.put("message", "Error al eliminar la revisión: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
}
