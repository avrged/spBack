package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.ImagenController;

public class ImagenRoutes {
    
    private final ImagenController imagenController;

    public ImagenRoutes(ImagenController imagenController) {
        this.imagenController = imagenController;
    }

    public void register(Javalin app) {
        // Rutas para obtener imágenes
        app.get("/imagenes", imagenController::obtenerTodasLasImagenes);
        
        // Obtener imágenes por restaurantero (simplificado)
        app.get("/imagenes/restaurantero/{idRestaurantero}", 
               imagenController::obtenerImagenesPorRestaurantero);
        
        // Obtener imagen específica por ID y restaurantero
        app.get("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
               imagenController::obtenerImagenPorRestaurantero);
        
        // Crear nueva imagen para un restaurantero
        app.post("/imagenes/restaurantero/{idRestaurantero}", 
                imagenController::crearImagenParaRestaurantero);
        
        // Actualizar imagen por ID y restaurantero (soporta JSON y form-data)
        app.put("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
               imagenController::actualizarImagenPorRestaurantero);
        
        // este es el bueno
        app.put("/imagenes/restaurantero/{idRestaurantero}", 
               imagenController::actualizarImagenesRestaurantero);
        
        // Eliminar imagen por ID y restaurantero
        app.delete("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
                  imagenController::eliminarImagenPorRestaurantero);
        
        // Rutas adicionales (mantener compatibilidad)
        app.get("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::obtenerImagenPorId);
        app.get("/imagenes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::obtenerImagenesPorRestaurante);
        app.post("/imagenes", imagenController::crearImagen);
        app.post("/imagenes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                imagenController::crearImagenParaRestaurante);
        app.put("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::actualizarImagen);
        app.delete("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  imagenController::eliminarImagen);
    }
}
