package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.DescargaController;

public class DescargaRoutes {
    
    private final DescargaController descargaController;
    
    public DescargaRoutes(DescargaController descargaController) {
        this.descargaController = descargaController;
    }
    
    public void register(Javalin app) {
        // Rutas CRUD básicas para descargas
        app.get("/descargas", descargaController::obtenerTodasLasDescargas);
        app.post("/descargas", descargaController::crearDescarga);
        app.get("/descargas/{id}", descargaController::obtenerDescargaPorId);
        app.put("/descargas/{id}", descargaController::actualizarDescarga);
        app.delete("/descargas/{id}", descargaController::eliminarDescarga);

        // Rutas de filtrado por diferentes criterios
        app.get("/descargas/restaurantero/{id}", descargaController::obtenerDescargasPorRestaurantero);
        app.get("/descargas/origen/{origen}", descargaController::obtenerDescargasPorOrigen);
        app.get("/descargas/opinion/{opinion}", descargaController::obtenerDescargasPorOpinion);

        // Rutas de acciones especiales
        app.patch("/descargas/{id}/incrementar", descargaController::incrementarDescargas);
        app.post("/descargas/rapida", descargaController::crearDescargaRapida);

        // Rutas de estadísticas y reportes
        app.get("/descargas/estadisticas/restaurantero/{id}/total", descargaController::obtenerTotalDescargasPorRestaurantero);
        app.get("/descargas/estadisticas/top", descargaController::obtenerTopDescargasPorOrigen);
    }
}
