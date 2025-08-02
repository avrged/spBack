package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.Solicitud_registroController;

public class Solicitud_registroRoutes {
    
    private final Solicitud_registroController solicitudController;
    
    public Solicitud_registroRoutes(Solicitud_registroController solicitudController) {
        this.solicitudController = solicitudController;
    }

    public void registerRoutes(Javalin app) {

        // Crear solicitud completa con datos del restaurante
        app.post("/solicitudes-registro", solicitudController::crearSolicitudCompleta);
        
        // Obtener todas las solicitudes (con filtro opcional por estado)
        app.get("/solicitudes-registro", solicitudController::obtenerTodasLasSolicitudes);
        
        // Obtener solicitud específica por ID
        app.get("/solicitudes-registro/{id}", solicitudController::obtenerSolicitudPorId);
        
        // Aprobar solicitud (solo administradores)
        app.put("/solicitudes-registro/{id}/aprobar", solicitudController::aprobarSolicitud);
        
        // Rechazar solicitud (solo administradores)
        app.put("/solicitudes-registro/{id}/rechazar", solicitudController::rechazarSolicitud);

        // Obtener solicitud de un restaurantero específico
        app.get("/solicitudes-registro/restaurantero/{restauranteroId}", solicitudController::obtenerSolicitudPorRestaurantero);
        
        // Verificar si un restaurantero puede crear restaurante
        app.get("/solicitudes-registro/restaurantero/{restauranteroId}/puede-crear-restaurante", solicitudController::verificarPuedeCrearRestaurante);
    }
}
