package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.Imagen_restauranteController;

public class Imagen_restauranteRoutes {
    
    private final Imagen_restauranteController imagenController;
    
    public Imagen_restauranteRoutes(Imagen_restauranteController imagenController) {
        this.imagenController = imagenController;
    }

    public void registerRoutes(Javalin app) {

        app.post("/imagenes-restaurante", imagenController::crearImagen);
        
        // Crear imagen simple desde JSON
        app.post("/imagenes-restaurante/simple", imagenController::crearImagenSimple);
        
        // Obtener todas las imágenes
        app.get("/imagenes-restaurante", imagenController::obtenerTodasLasImagenes);
        
        // Obtener imagen específica por ID
        app.get("/imagenes-restaurante/{id}", imagenController::obtenerImagenPorId);
        
        // Actualizar imagen
        app.put("/imagenes-restaurante/{id}", imagenController::actualizarImagen);
        
        // Eliminar imagen
        app.delete("/imagenes-restaurante/{id}", imagenController::eliminarImagen);

        app.get("/imagenes-restaurante/restaurante/{restauranteId}", imagenController::obtenerImagenesDeRestaurante);
        
        // Obtener imagen principal de un restaurante
        app.get("/imagenes-restaurante/restaurante/{restauranteId}/principal", imagenController::obtenerImagenPrincipal);
        
        // Obtener imágenes por tipo específico
        app.get("/imagenes-restaurante/restaurante/{restauranteId}/tipo/{tipo}", imagenController::obtenerImagenesPorTipo);
    }
}
