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
        
        // Obtener imagen específica por clave primaria compuesta
        app.get("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::obtenerImagenPorId);
        
        // Obtener imágenes por restaurante
        app.get("/imagenes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::obtenerImagenesPorRestaurante);
        
        // Obtener imágenes por restaurantero
        app.get("/imagenes/restaurantero/{idRestaurantero}", 
               imagenController::obtenerImagenesPorRestaurantero);
        
        // Crear nueva imagen
        app.post("/imagenes", imagenController::crearImagen);
        
        // Crear imagen específica para un restaurante
        app.post("/imagenes/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                imagenController::crearImagenParaRestaurante);
        
        // Actualizar imagen
        app.put("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               imagenController::actualizarImagen);
        
        // Eliminar imagen
        app.delete("/imagenes/{idImagen}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  imagenController::eliminarImagen);
    }
}
