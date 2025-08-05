package org.sazonpt.controller;

import java.util.Map;

import org.sazonpt.service.Solicitud_registroService;

import io.javalin.http.Context;

public class Solicitud_registroEstadoController {
    private final Solicitud_registroService solicitudService;

    public Solicitud_registroEstadoController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public void aprobarPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            var solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            var solicitudPendiente = solicitudes.stream()
                .filter(s -> "pendiente".equalsIgnoreCase(s.getEstado()))
                .findFirst();
            if (solicitudPendiente.isEmpty()) {
                ctx.status(404).json(Map.of("success", false, "message", "No hay solicitud pendiente para este restaurantero"));
                return;
            }
            int idSolicitud = solicitudPendiente.get().getId_solicitud();
            int idRestauranteroSolicitud = solicitudPendiente.get().getId_restaurantero();
            int idAdministrador = 1;
            boolean ok = solicitudService.aprobarSolicitudConRevision(idSolicitud, idRestauranteroSolicitud, idAdministrador);
            if (ok) {
                ctx.json(Map.of("success", true, "message", "Solicitud aprobada correctamente y revisión registrada"));
            } else {
                ctx.status(500).json(Map.of("success", false, "message", "No se pudo aprobar la solicitud"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("success", false, "message", "ID inválido"));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    public void rechazarPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            // Obtener TODAS las solicitudes del restaurantero (sin filtrar por estado)
            var solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            
            if (solicitudes.isEmpty()) {
                ctx.status(404).json(Map.of("success", false, "message", "No hay solicitudes para este restaurantero"));
                return;
            }
            
            // Tomar la primera solicitud encontrada (sin importar el estado)
            var solicitud = solicitudes.get(0);
            int idSolicitud = solicitud.getId_solicitud();
            String estadoActual = solicitud.getEstado();
            
            // Eliminar la solicitud completamente (incluyendo revisiones asociadas)
            boolean eliminada = solicitudService.eliminarSolicitudCompleta(idSolicitud, idRestaurantero);
            
            if (eliminada) {
                ctx.json(Map.of(
                    "success", true, 
                    "message", "Solicitud eliminada correctamente (estado anterior: " + estadoActual + ")",
                    "id_solicitud", idSolicitud,
                    "estado_anterior", estadoActual
                ));
            } else {
                ctx.status(500).json(Map.of("success", false, "message", "No se pudo eliminar la solicitud"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("success", false, "message", "ID de restaurantero inválido"));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }
}
