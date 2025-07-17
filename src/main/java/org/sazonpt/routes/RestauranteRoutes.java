package org.sazonpt.routes;

import org.sazonpt.controller.RestauranteController;

import io.javalin.Javalin;

public class RestauranteRoutes {
    private final RestauranteController restauranteController;

    public RestauranteRoutes(RestauranteController restauranteController) {
        this.restauranteController = restauranteController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/restaurantes", restauranteController::getAll);
        app.post("/restaurantes", restauranteController::create);
        app.get("/restaurantes/{id}", restauranteController::getById);
        app.put("/restaurantes/{id}", restauranteController::update);
        app.delete("/restaurantes/{id}", restauranteController::delete);
    }
}
