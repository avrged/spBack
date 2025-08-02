package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RestauranteController;

public class RestauranteRoutes {
    
    private final RestauranteController restauranteController;
    
    public RestauranteRoutes(RestauranteController restauranteController) {
        this.restauranteController = restauranteController;
    }
 
    public void registerRoutes(Javalin app) {
        app.post("/restaurantes", restauranteController::crearRestaurante);
        
        app.get("/restaurantes", restauranteController::obtenerTodosLosRestaurantes);
        app.get("/restaurantes/{id}", restauranteController::obtenerRestaurantePorId);
        app.put("/restaurantes/{id}", restauranteController::actualizarRestaurante);

        app.delete("/restaurantes/{id}", restauranteController::eliminarRestaurante);

        app.get("/restaurantes/buscar", restauranteController::buscarRestaurantesPorNombre);

        app.get("/restaurantes/restaurantero/{restauranteroId}", restauranteController::obtenerRestaurantePorRestaurantero);

        app.get("/restaurantes/estadisticas", restauranteController::obtenerEstadisticas);

        app.put("/restaurantes/{id}/aprobar", restauranteController::aprobarRestaurante);

        app.put("/restaurantes/{id}/rechazar", restauranteController::rechazarRestaurante);
    }
}
