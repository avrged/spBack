package org.sazonpt.routes;

import org.sazonpt.controller.ImagenController;

import io.javalin.Javalin;

public class ImagenRoutes {
    private final ImagenController imagenController;

    public ImagenRoutes(ImagenController imagenController) {
        this.imagenController = imagenController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/imagenes", imagenController::getAll);
        app.post("/imagenes", imagenController::create);
        app.get("/imagenes/{id}", imagenController::getById);
        app.put("/imagenes/{id}", imagenController::update);
        app.delete("/imagenes/{id}", imagenController::delete);
        app.get("/imagenes/restaurante/{restaurante}", imagenController::getByRestaurant);
    }
}
