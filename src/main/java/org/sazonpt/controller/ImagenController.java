package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Imagen;
import org.sazonpt.service.ImagenService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ImagenController {
    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    public void getAll(Context ctx) {
        try {
            List<Imagen> imagenes = imagenService.getAllImagenes();
            ctx.json(imagenes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener imágenes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("id"));
            Imagen imagen = imagenService.getById(idImagen);
            
            if (imagen != null) {
                ctx.json(imagen);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Imagen no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener imagen");
        }
    }

    public void getByRestaurant(Context ctx) {
        try {
            int codigoRestaurante = Integer.parseInt(ctx.pathParam("restaurante"));
            List<Imagen> imagenes = imagenService.getImagesByRestaurant(codigoRestaurante);
            ctx.json(imagenes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener imágenes del restaurante");
        }
    }

    public void create(Context ctx) {
        try {
            Imagen imagen = ctx.bodyAsClass(Imagen.class);
            imagenService.createImagen(imagen);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Imagen creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear imagen: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("id"));
            Imagen imagen = ctx.bodyAsClass(Imagen.class);
            // Asegurarse de que el ID de la imagen coincida con el parámetro de la URL
            Imagen updatedImagen = new Imagen(
                idImagen,
                imagen.getCodigo_restarante(),
                imagen.getFecha_subida(),
                imagen.getRuta_archivoI(),
                imagen.getEstado()
            );
            imagenService.updateImagen(updatedImagen);
            ctx.status(200).result("Imagen actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar imagen: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete imagen with ID: " + idImagen);
            imagenService.deleteImagen(idImagen);
            ctx.status(200).result("Imagen eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid imagen ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de imagen inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting imagen: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting imagen: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    /*
     * Pendiente, implementar lógica de subida de imágenes proporcionada por el repositorio mostrado en clase
     */
}
