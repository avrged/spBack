package org.sazonpt.routes;

import org.sazonpt.controller.Solicitud_registroEstadoController;

import io.javalin.Javalin;

public class Solicitud_registroEstadoRoutes {
    private final Solicitud_registroEstadoController estadoController;

    public Solicitud_registroEstadoRoutes(Solicitud_registroEstadoController estadoController) {
        this.estadoController = estadoController;
    }

    public void register(Javalin app) {
        app.put("/solicitudes/restaurantero/{idRestaurantero}/aprobar", estadoController::aprobarPorRestaurantero);
    }
}
