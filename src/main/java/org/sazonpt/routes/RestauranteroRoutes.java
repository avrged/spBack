package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RestauranteroController;

public class RestauranteroRoutes {
    private final RestauranteroController restauranteroController;
    
    public RestauranteroRoutes(RestauranteroController restauranteroController) {
        this.restauranteroController = restauranteroController;
    }
    
    public void register(Javalin app) {
        app.get("/restauranteros", restauranteroController::getAll);
        app.post("/restauranteros", restauranteroController::create);
        app.get("/restauranteros/{id}", restauranteroController::getById);
        app.put("/restauranteros/{id}", restauranteroController::update);

        app.post("/restauranteros/promover/{id}", restauranteroController::promoverUsuario);
        app.delete("/restauranteros/revocar/{id}", restauranteroController::revocarPermisos);
        app.get("/restauranteros/verificar/{id}", restauranteroController::verificarRestaurantero);

        app.post("/auth/restaurantero/login", restauranteroController::login);
    }
}
