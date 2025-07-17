package org.sazonpt.routes;

import org.sazonpt.controller.ComprobanteController;

import io.javalin.Javalin;

public class ComprobanteRoutes {
    private final ComprobanteController comprobanteController;

    public ComprobanteRoutes(ComprobanteController comprobanteController) {
        this.comprobanteController = comprobanteController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/comprobantes", comprobanteController::getAll);
        app.post("/comprobantes", comprobanteController::create);
        app.get("/comprobantes/{id}", comprobanteController::getById);
        app.put("/comprobantes/{id}", comprobanteController::update);
        app.delete("/comprobantes/{id}", comprobanteController::delete);
    }
}
