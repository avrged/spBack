package org.sazonpt.routes;

import org.sazonpt.controller.Adquirir_membresiaController;

import io.javalin.Javalin;

public class Adquirir_membresiaRoutes {
    private final Adquirir_membresiaController membresiaController;

    public Adquirir_membresiaRoutes(Adquirir_membresiaController membresiaController) {
        this.membresiaController = membresiaController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/membresias", membresiaController::getAll);
        app.post("/membresias", membresiaController::create);
        app.get("/membresias/{id}", membresiaController::getById);
        app.put("/membresias/{id}", membresiaController::update);
        app.delete("/membresias/{id}", membresiaController::delete);
        app.get("/membresias/restaurantero/{restaurantero}", membresiaController::getByRestaurantero);
    }
}
