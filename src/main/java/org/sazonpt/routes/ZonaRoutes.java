package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.ZonaController;

public class ZonaRoutes {
    
    private final ZonaController zonaController;
    
    public ZonaRoutes(ZonaController zonaController) {
        this.zonaController = zonaController;
    }
    
    public void register(Javalin app) {
        app.get("/zonas", zonaController::obtenerTodasLasZonas);
        app.post("/zonas", zonaController::crearZona);
        app.get("/zonas/{id}", zonaController::obtenerZonaPorId);
        app.put("/zonas/{id}", zonaController::actualizarZona);
        app.delete("/zonas/{id}", zonaController::eliminarZona);
        
        app.get("/zonas/restaurantero/{idRestaurantero}", 
               zonaController::obtenerZonasPorRestaurantero);

        app.get("/zonas/buscar", zonaController::buscarZonasPorNombre);

        app.get("/zonas/estadisticas", zonaController::obtenerEstadisticas);
        app.get("/zonas/restaurantero/{idRestaurantero}/estadisticas", 
               zonaController::obtenerEstadisticasPorRestaurantero);
    }
}
