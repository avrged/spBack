package org.sazonpt.routes;

import org.sazonpt.controller.ZonaController;

import io.javalin.Javalin;

public class ZonaRoutes {
    private final ZonaController zonaController;

    public ZonaRoutes(ZonaController zonaController) {
        this.zonaController = zonaController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/zonas", zonaController::getAll);
        app.post("/zonas", zonaController::create);
        app.get("/zonas/{id}", zonaController::getById);
        app.put("/zonas/{id}", zonaController::update);
        app.delete("/zonas/{id}", zonaController::delete);
    }
}
