package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.ComprobanteController;

public class ComprobanteRoutes {
    
    private final ComprobanteController comprobanteController;

    public ComprobanteRoutes(ComprobanteController comprobanteController) {
        this.comprobanteController = comprobanteController;
    }

    public void register(Javalin app) {
        app.get("/comprobantes", comprobanteController::obtenerTodosLosComprobantes);

        app.get("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::obtenerComprobantePorId);

        app.get("/comprobantes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::obtenerComprobantesPorRestaurante);

        app.get("/comprobantes/restaurantero/{idRestaurantero}", 
               comprobanteController::obtenerComprobantesPorRestaurantero);

        app.get("/comprobantes/tipo/{tipo}", 
               comprobanteController::obtenerComprobantesPorTipo);


        app.get("/comprobantes/tipos", 
               comprobanteController::obtenerTiposComprobante);

        app.post("/comprobantes", comprobanteController::crearComprobante);

        app.post("/comprobantes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                comprobanteController::crearComprobanteParaRestaurante);

        app.put("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::actualizarComprobante);

        app.put("/comprobantes/{idComprobante}", 
               comprobanteController::actualizarComprobanteSimplificado);

        app.delete("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  comprobanteController::eliminarComprobante);
    }
}
