package org.sazonpt.controller;

import io.javalin.http.Context;
import org.sazonpt.model.Imagen;
import org.sazonpt.service.ImagenService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImagenController {
    
    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    public void obtenerTodasLasImagenes(Context ctx) {
        try {
            List<Imagen> imagenes = imagenService.obtenerTodasLasImagenes();
            ctx.json(Map.of(
                "success", true,
                "data", imagenes,
                "message", "Imágenes obtenidas correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener las imágenes: " + e.getMessage()
            ));
        }
    }

    public void obtenerImagenPorId(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Optional<Imagen> imagenOpt = imagenService.obtenerImagenPorId(idImagen, idRestaurante, idSolicitud, idRestaurantero);
            
            if (imagenOpt.isPresent()) {
                ctx.json(Map.of(
                    "success", true,
                    "data", imagenOpt.get(),
                    "message", "Imagen encontrada"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Imagen no encontrada"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener la imagen: " + e.getMessage()
            ));
        }
    }

    public void obtenerImagenesPorRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Imagen> imagenes = imagenService.obtenerImagenesPorRestaurante(idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", imagenes,
                "message", "Imágenes del restaurante obtenidas correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener las imágenes del restaurante: " + e.getMessage()
            ));
        }
    }

    public void obtenerImagenesPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Imagen> imagenes = imagenService.obtenerImagenesPorRestaurantero(idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", imagenes,
                "message", "Imágenes del restaurantero obtenidas correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener las imágenes del restaurantero: " + e.getMessage()
            ));
        }
    }

    public void crearImagen(Context ctx) {
        try {
            Imagen imagen = ctx.bodyAsClass(Imagen.class);
            Imagen imagenCreada = imagenService.crearImagen(imagen);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", imagenCreada,
                "message", "Imagen creada correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear la imagen: " + e.getMessage()
            ));
        }
    }

    public void crearImagenParaRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Imagen imagenCreada = imagenService.crearImagenParaRestaurante(idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", imagenCreada,
                "message", "Imagen creada para el restaurante correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear la imagen: " + e.getMessage()
            ));
        }
    }

    public void actualizarImagen(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            Imagen imagenActualizada = ctx.bodyAsClass(Imagen.class);
            
            boolean actualizada = imagenService.actualizarImagen(idImagen, idRestaurante, idSolicitud, 
                                                               idRestaurantero, imagenActualizada);
            
            if (actualizada) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Imagen actualizada correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar la imagen"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar la imagen: " + e.getMessage()
            ));
        }
    }

    public void eliminarImagen(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminada = imagenService.eliminarImagen(idImagen, idRestaurante, idSolicitud, idRestaurantero);
            
            if (eliminada) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Imagen eliminada correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Imagen no encontrada"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al eliminar la imagen: " + e.getMessage()
            ));
        }
    }
}
