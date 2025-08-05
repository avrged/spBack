package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RegistroRestauranteController;

public class RegistroRestauranteRoutes {
    
    private final RegistroRestauranteController controller;
    
    public RegistroRestauranteRoutes(RegistroRestauranteController controller) {
        this.controller = controller;
    }
    
    public void registerRoutes(Javalin app) {
        app.post("/registro-restaurante", controller::registrarRestaurante);

        app.get("/solicitud/{id}/estado", controller::obtenerEstadoSolicitud);

        app.get("/restaurantero/{id}/solicitudes", controller::obtenerSolicitudesRestaurantero);
    }
}
