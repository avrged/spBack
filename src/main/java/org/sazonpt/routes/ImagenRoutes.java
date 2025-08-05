package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.ImagenController;

public class ImagenRoutes {
    
    private final ImagenController imagenController;

    public ImagenRoutes(ImagenController imagenController) {
        this.imagenController = imagenController;
    }

    public void register(Javalin app) {
        app.get("/imagenes", imagenController::obtenerTodasLasImagenes);

        app.get("/imagenes/restaurantero/{idRestaurantero}", 
               imagenController::obtenerImagenesPorRestaurantero);

        app.get("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
               imagenController::obtenerImagenPorRestaurantero);

        app.post("/imagenes/restaurantero/{idRestaurantero}", 
                imagenController::crearImagenParaRestaurantero);

        app.put("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
               imagenController::actualizarImagenPorRestaurantero);
        
        app.put("/imagenes/{idImagen}", 
               imagenController::actualizarImagenSimplificado);

        app.put("/imagenes/restaurantero/{idRestaurantero}", 
               imagenController::actualizarImagenesRestaurantero);
        
        app.delete("/imagenes/{idImagen}/restaurantero/{idRestaurantero}", 
                  imagenController::eliminarImagenPorRestaurantero);

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
