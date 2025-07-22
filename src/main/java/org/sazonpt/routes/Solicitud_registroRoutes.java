package org.sazonpt.routes;

import org.sazonpt.controller.Solicitud_registroController;

import io.javalin.Javalin;

public class Solicitud_registroRoutes {
    private final Solicitud_registroController solicitudController;

    public Solicitud_registroRoutes(Solicitud_registroController solicitudController) {
        this.solicitudController = solicitudController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/solicitudes", solicitudController::getAll);
        app.post("/solicitudes", solicitudController::create);
        app.post("/solicitudes/with-files", solicitudController::createWithFiles);
        app.get("/solicitudes/{id}", solicitudController::getById);
        app.put("/solicitudes/{id}", solicitudController::updateWithFiles);
        app.delete("/solicitudes/{id}", solicitudController::delete);

        // Nuevas rutas para aprobar y rechazar solicitudes
        app.put("/solicitudes/aprobar/{id}", solicitudController::updateEstado);
        app.put("/solicitudes/{id}/rechazar", solicitudController::rechazarSolicitud);
    }
}
