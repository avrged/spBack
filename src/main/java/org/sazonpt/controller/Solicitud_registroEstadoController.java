package org.sazonpt.controller;

import java.util.Map;

import org.sazonpt.service.Solicitud_registroService;

import io.javalin.http.Context;

public class Solicitud_registroEstadoController {
    private final Solicitud_registroService solicitudService;

    public Solicitud_registroEstadoController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // PUT /solicitudes/restaurantero/{idRestaurantero}/aprobar
    public void aprobarPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            // Buscar la solicitud pendiente de ese restaurantero
            var solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            var solicitudPendiente = solicitudes.stream()
                .filter(s -> "pendiente".equalsIgnoreCase(s.getEstado()))
                .findFirst();
            if (solicitudPendiente.isEmpty()) {
                ctx.status(404).json(Map.of("success", false, "message", "No hay solicitud pendiente para este restaurantero"));
                return;
            }
            boolean ok = solicitudService.cambiarEstadoSolicitud(solicitudPendiente.get().getId_solicitud(), "aprobada");
            if (ok) {
                ctx.json(Map.of("success", true, "message", "Solicitud aprobada correctamente"));
            } else {
                ctx.status(500).json(Map.of("success", false, "message", "No se pudo aprobar la solicitud"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("success", false, "message", "ID inválido"));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }
}
