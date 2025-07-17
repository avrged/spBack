package org.sazonpt.routes;

import org.sazonpt.controller.DescargaController;

import io.javalin.Javalin;

public class DescargaRoutes {
    private final DescargaController descargaController;

    public DescargaRoutes(DescargaController descargaController) {
        this.descargaController = descargaController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/descargas", descargaController::getAll);
        app.post("/descargas", descargaController::create);
        app.get("/descargas/{id}", descargaController::getById);
        app.put("/descargas/{id}", descargaController::update);
        app.delete("/descargas/{id}", descargaController::delete);
    }
}
