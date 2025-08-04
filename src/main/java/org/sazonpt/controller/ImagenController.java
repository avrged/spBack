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

    // Métodos simplificados usando solo id_restaurantero
    
    public void obtenerImagenPorRestaurantero(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Optional<Imagen> imagenOpt = imagenService.obtenerImagenPorRestaurantero(idImagen, idRestaurantero);
            
            if (imagenOpt.isPresent()) {
                ctx.json(Map.of(
                    "success", true,
                    "data", imagenOpt.get(),
                    "message", "Imagen encontrada"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Imagen no encontrada para el restaurantero especificado"
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

    public void crearImagenParaRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Imagen imagenCreada = imagenService.crearImagenParaRestaurantero(idRestaurantero);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", imagenCreada,
                "message", "Imagen creada para el restaurantero correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
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

    public void actualizarImagenPorRestaurantero(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            Imagen imagenActualizada = new Imagen();
            
            // Detectar si es form-data o JSON
            String contentType = ctx.header("Content-Type");
            
            if (contentType != null && contentType.contains("multipart/form-data")) {
                // Procesar form-data
                String rutaImagen = ctx.formParam("ruta_imagen");
                
                if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
                    imagenActualizada.setRuta_imagen(rutaImagen);
                }
            } else {
                // Procesar JSON
                imagenActualizada = ctx.bodyAsClass(Imagen.class);
            }
            
            boolean actualizada = imagenService.actualizarImagenPorRestaurantero(idImagen, idRestaurantero, imagenActualizada);
            
            if (actualizada) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Imagen actualizada correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar la imagen. Verifica que la imagen pertenezca al restaurantero"
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

    public void eliminarImagenPorRestaurantero(Context ctx) {
        try {
            int idImagen = Integer.parseInt(ctx.pathParam("idImagen"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminada = imagenService.eliminarImagenPorRestaurantero(idImagen, idRestaurantero);
            
            if (eliminada) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Imagen eliminada correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Imagen no encontrada para el restaurantero especificado"
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

    public void actualizarImagenesRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            // Obtener las rutas de las imágenes desde form-data
            String rutaImagen1 = ctx.formParam("ruta_imagen1");
            String rutaImagen2 = ctx.formParam("ruta_imagen2");
            String rutaImagen3 = ctx.formParam("ruta_imagen3");
            
            // Obtener todas las imágenes del restaurantero
            List<Imagen> imagenes = imagenService.obtenerImagenesPorRestaurantero(idRestaurantero);
            
            if (imagenes.isEmpty()) {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se encontraron imágenes para el restaurantero especificado"
                ));
                return;
            }
            
            // Limitar a las primeras 3 imágenes (o las que estén disponibles)
            int imagenesActualizadas = 0;
            String[] rutasNuevas = {rutaImagen1, rutaImagen2, rutaImagen3};
            
            for (int i = 0; i < Math.min(3, imagenes.size()); i++) {
                String nuevaRuta = rutasNuevas[i];
                
                // Solo actualizar si se proporciona una nueva ruta
                if (nuevaRuta != null && !nuevaRuta.trim().isEmpty()) {
                    Imagen imagen = imagenes.get(i);
                    Imagen imagenActualizada = new Imagen();
                    imagenActualizada.setRuta_imagen(nuevaRuta);
                    
                    boolean actualizada = imagenService.actualizarImagenPorRestaurantero(
                        imagen.getId_imagen(), idRestaurantero, imagenActualizada);
                    
                    if (actualizada) {
                        imagenesActualizadas++;
                    }
                }
            }
            
            if (imagenesActualizadas > 0) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Se actualizaron " + imagenesActualizadas + " imágenes correctamente",
                    "imagenes_actualizadas", imagenesActualizadas
                ));
            } else {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "No se actualizaron imágenes. Verifica que hayas proporcionado al menos una ruta válida"
                ));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar las imágenes: " + e.getMessage()
            ));
        }
    }
}
