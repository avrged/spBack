package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.Solicitud_registroController;

public class Solicitud_registroRoutes {
    
    private final Solicitud_registroController solicitudController;

    public Solicitud_registroRoutes(Solicitud_registroController solicitudController) {
        this.solicitudController = solicitudController;
    }

    public void register(Javalin app) {
        app.get("/solicitudes", solicitudController::obtenerTodasLasSolicitudes);
        app.post("/solicitudes", solicitudController::crearSolicitud);
        app.get("/solicitudes/{id}", solicitudController::obtenerSolicitudPorId);
        app.put("/solicitudes/{id}", solicitudController::actualizarSolicitud);

        app.post("/solicitudes/{id}/aprobar", solicitudController::aprobarSolicitud);
        app.post("/solicitudes/{id}/rechazar", solicitudController::rechazarSolicitud);

        app.get("/solicitudes/restaurantero/{idRestaurantero}", solicitudController::obtenerSolicitudesPorRestaurantero);
        app.delete("/solicitudes/{id}/restaurantero/{idRestaurantero}", solicitudController::eliminarSolicitud);
        app.put("/solicitudes/restaurantero/{idRestaurantero}/pendiente", solicitudController::pendientePorRestaurantero);
    }
}
