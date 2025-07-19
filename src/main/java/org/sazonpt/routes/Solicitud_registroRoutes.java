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
        app.put("/solicitudes/{id}", solicitudController::update);
        app.delete("/solicitudes/{id}", solicitudController::delete);
    }
}
