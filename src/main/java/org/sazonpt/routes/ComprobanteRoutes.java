package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.ComprobanteController;

public class ComprobanteRoutes {
    
    private final ComprobanteController comprobanteController;

    public ComprobanteRoutes(ComprobanteController comprobanteController) {
        this.comprobanteController = comprobanteController;
    }

    public void register(Javalin app) {
        // Rutas para obtener comprobantes
        app.get("/comprobantes", comprobanteController::obtenerTodosLosComprobantes);
        
        // Obtener comprobante específico por clave primaria compuesta
        app.get("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::obtenerComprobantePorId);
        
        // Obtener comprobantes por restaurante
        app.get("/comprobantes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::obtenerComprobantesPorRestaurante);
        
        // Obtener comprobantes por restaurantero
        app.get("/comprobantes/restaurantero/{idRestaurantero}", 
               comprobanteController::obtenerComprobantesPorRestaurantero);
        
        // Obtener comprobantes por tipo
        app.get("/comprobantes/tipo/{tipo}", 
               comprobanteController::obtenerComprobantesPorTipo);
        
        // Ruta de comprobantes por zona eliminada porque id_zona ya no existe
        
        // Obtener tipos de comprobante disponibles
        app.get("/comprobantes/tipos", 
               comprobanteController::obtenerTiposComprobante);
        
        // Crear nuevo comprobante
        app.post("/comprobantes", comprobanteController::crearComprobante);
        
        // Crear comprobante específico para un restaurante
        app.post("/comprobantes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                comprobanteController::crearComprobanteParaRestaurante);
        
        // Actualizar comprobante
        app.put("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               comprobanteController::actualizarComprobante);
        
        // Actualizar comprobante de forma simplificada (solo requiere ID del comprobante)
        app.put("/comprobantes/{idComprobante}", 
               comprobanteController::actualizarComprobanteSimplificado);
        
        // Eliminar comprobante
        app.delete("/comprobantes/{idComprobante}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  comprobanteController::eliminarComprobante);
    }
}
