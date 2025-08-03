package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.RegistroRestauranteController;

public class RegistroRestauranteRoutes {
    
    private final RegistroRestauranteController controller;
    
    public RegistroRestauranteRoutes(RegistroRestauranteController controller) {
        this.controller = controller;
    }
    
    public void registerRoutes(Javalin app) {
        // Registro de restaurante
        app.post("/registro-restaurante", controller::registrarRestaurante);
        
        // Consultar estado de solicitud
        app.get("/solicitud/{id}/estado", controller::obtenerEstadoSolicitud);

        // Consultar solicitudes de un restaurantero
        app.get("/restaurantero/{id}/solicitudes", controller::obtenerSolicitudesRestaurantero);
    }
}
