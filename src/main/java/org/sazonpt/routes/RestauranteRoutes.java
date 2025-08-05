package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RestauranteController;

public class RestauranteRoutes {
    
    private final RestauranteController restauranteController;

    public RestauranteRoutes(RestauranteController restauranteController) {
        this.restauranteController = restauranteController;
    }

    public void register(Javalin app) {
        app.get("/restaurantes", restauranteController::obtenerTodosLosRestaurantes);
        app.post("/restaurantes", restauranteController::crearRestaurante);
        app.get("/restaurantes/{id}", restauranteController::obtenerRestaurantePorId);
        app.put("/restaurantes/{id}", restauranteController::actualizarRestaurante);

        app.get("/restaurantes/restaurantero/{idRestaurantero}", 
               restauranteController::obtenerRestaurantesPorRestaurantero);

        app.get("/restaurantes/zona/{idZona}", 
               restauranteController::obtenerRestaurantesPorZona);

        app.delete("/restaurantes/{id}/solicitud/{idSolicitud}/restaurantero/{idRestaurantero}", 
                  restauranteController::eliminarRestaurante);

        app.put("/restaurantes/restaurantero/{idRestaurantero}", restauranteController::actualizarRestaurantePorRestaurantero);

        app.put("/restaurantes/actualizar/{id}", restauranteController::actualizarCamposEspecificos);
    }
}
