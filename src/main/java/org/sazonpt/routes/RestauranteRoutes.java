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

        // Rutas adicionales para gestión de restaurantes
        app.get("/restaurantes/{id}/dueno", restauranteController::getDueno);
        app.get("/restauranteros/{idRestaurantero}/restaurantes", restauranteController::getRestaurantesByDueno);
        app.get("/restaurantes/{id}/con-dueno", restauranteController::getRestauranteConDueno);

        // Ruta para obtener etiquetas válidas
        app.get("/restaurantes/etiquetas/validas", restauranteController::getEtiquetasValidas);

        // Ruta SEGURA para que restauranteros actualicen solo SU restaurante (por restaurantero ID)
        app.put("/restaurantero/{idRestaurantero}/restaurante", restauranteController::actualizarRestauranteSeguro);

        // Rutas NUEVAS usando ID de USUARIO en lugar de restaurantero
        app.get("/usuarios/{idUsuario}/restaurantes", restauranteController::getRestaurantesByUsuario);
        app.put("/usuario/{idUsuario}/restaurante", restauranteController::actualizarRestauranteSeguroPorUsuario);
    }
}
