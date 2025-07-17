package org.sazonpt.routes;

import org.sazonpt.controller.RestauranteroController;

import io.javalin.Javalin;

public class RestauranteroRoutes {
    private final RestauranteroController restauranteroController;

    public RestauranteroRoutes(RestauranteroController restauranteroController){
        this.restauranteroController = restauranteroController;
    }

    public void registerRoutes(Javalin app){
        app.get("/restauranteros", restauranteroController::getAll);
        app.post("/restauranteros", restauranteroController::create);
        app.get("/restauranteros/{id}", restauranteroController::getById);
        app.put("/restauranteros/{id}", restauranteroController::update);
        app.delete("/restauranteros/{id}", restauranteroController::delete);
        
        // Ruta temporal para migrar usuarios
        app.post("/restauranteros/migrate", restauranteroController::migrateUsers);
    }
}
