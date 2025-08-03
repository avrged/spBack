package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RestauranteController;

public class RestauranteRoutes {
    
    private final RestauranteController restauranteController;

    public RestauranteRoutes(RestauranteController restauranteController) {
        this.restauranteController = restauranteController;
    }

    public void register(Javalin app) {
        // Rutas CRUD para restaurantes
        app.get("/restaurantes", restauranteController::obtenerTodosLosRestaurantes);
        app.post("/restaurantes", restauranteController::crearRestaurante);
        app.get("/restaurantes/{id}", restauranteController::obtenerRestaurantePorId);
        app.put("/restaurantes/{id}", restauranteController::actualizarRestaurante);
        
        // Rutas específicas por restaurantero
        app.get("/restaurantes/restaurantero/{idRestaurantero}", 
               restauranteController::obtenerRestaurantesPorRestaurantero);
        
        // Rutas específicas por zona
        app.get("/restaurantes/zona/{idZona}", 
               restauranteController::obtenerRestaurantesPorZona);
        
        // Ruta para eliminar restaurante con todos los IDs
        app.delete("/restaurantes/{id}/solicitud/{idSolicitud}/restaurantero/{idRestaurantero}", 
                  restauranteController::eliminarRestaurante);
    }
}
