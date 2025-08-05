package org.sazonpt.routes;

import org.sazonpt.controller.DescargaController;

import io.javalin.Javalin;

public class DescargaRoutes {
    
    private final DescargaController descargaController;
    
    public DescargaRoutes(DescargaController descargaController) {
        this.descargaController = descargaController;
    }
    
    public void register(Javalin app) {
       
        app.get("/descargas", descargaController::obtenerTodasLasDescargas);
        app.post("/descargas", descargaController::crearDescarga);
        app.get("/descargas/{id}", descargaController::obtenerDescargaPorId);
        app.put("/descargas/{id}", descargaController::actualizarDescarga);
        app.delete("/descargas/{id}", descargaController::eliminarDescarga);

        
        app.put("/descargas/restaurantero/{idRestaurantero}", descargaController::actualizarDescargaPorRestaurantero);

        app.get("/descargas/restaurantero/{idRestaurantero}", descargaController::obtenerDescargasPorRestaurantero);
        app.get("/descargas/origen/{origen}", descargaController::obtenerDescargasPorOrigen);
        app.get("/descargas/opinion/{opinion}", descargaController::obtenerDescargasPorOpinion);

        app.patch("/descargas/{id}/incrementar", descargaController::incrementarDescargas);
        app.post("/descargas/rapida", descargaController::crearDescargaRapida);

        app.get("/descargas/estadisticas/restaurantero/{idRestaurantero}/total", descargaController::obtenerTotalDescargasPorRestaurantero);
        app.get("/descargas/estadisticas/top", descargaController::obtenerTopDescargasPorOrigen);
    }
}
