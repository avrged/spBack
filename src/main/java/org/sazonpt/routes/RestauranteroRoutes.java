package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RestauranteroController;

public class RestauranteroRoutes {
    private final RestauranteroController restauranteroController;
    
    public RestauranteroRoutes(RestauranteroController restauranteroController) {
        this.restauranteroController = restauranteroController;
    }
    
    public void register(Javalin app) {
        // Rutas CRUD para restauranteros
        app.get("/restauranteros", restauranteroController::getAll);
        app.post("/restauranteros", restauranteroController::create);
        app.get("/restauranteros/{id}", restauranteroController::getById);
        app.put("/restauranteros/{id}", restauranteroController::update);
        
        // Rutas específicas de restauranteros
        app.post("/restauranteros/promover/{id}", restauranteroController::promoverUsuario);
        app.delete("/restauranteros/revocar/{id}", restauranteroController::revocarPermisos);
        app.get("/restauranteros/verificar/{id}", restauranteroController::verificarRestaurantero);
        
        // Ruta para autenticación específica de restauranteros
        app.post("/auth/restaurantero/login", restauranteroController::login);
    }
}
