package org.sazonpt.routes;

import org.sazonpt.controller.EstadisticaController;

import io.javalin.Javalin;

public class EstadisticaRoutes {
    private final EstadisticaController estadisticaController;

    public EstadisticaRoutes(EstadisticaController estadisticaController) {
        this.estadisticaController = estadisticaController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/estadisticas", estadisticaController::getAll);
        app.post("/estadisticas", estadisticaController::create);
        app.get("/estadisticas/{id}", estadisticaController::getById);
        app.put("/estadisticas/{id}", estadisticaController::update);
        app.delete("/estadisticas/{id}", estadisticaController::delete);
    }
}
