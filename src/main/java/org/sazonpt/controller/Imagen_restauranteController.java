package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Imagen_restaurante;
import org.sazonpt.model.Imagen_restaurante.TipoImagen;
import org.sazonpt.service.Imagen_restauranteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Imagen_restauranteController {
    
    private final Imagen_restauranteService imagenService;
    
    public Imagen_restauranteController(Imagen_restauranteService imagenService) {
        this.imagenService = imagenService;
    }

    public void crearImagen(Context ctx) {
        try {
            Imagen_restaurante imagen = ctx.bodyAsClass(Imagen_restaurante.class);
            Imagen_restaurante nuevaImagen = imagenService.crearImagen(imagen);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagen creada exitosamente");
            response.put("data", nuevaImagen);
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void crearImagenSimple(Context ctx) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            
            int id_restaurante = (Integer) requestBody.get("id_restaurante");
            String url = (String) requestBody.get("url");
            String tipo = (String) requestBody.get("tipo");
            
            Imagen_restaurante imagen = new Imagen_restaurante(id_restaurante, url, tipo);
            Imagen_restaurante nuevaImagen = imagenService.crearImagen(imagen);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagen creada exitosamente");
            response.put("data", nuevaImagen);
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (ClassCastException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Datos de entrada inválidos. Se requieren: id_restaurante, url, tipo");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerTodasLasImagenes(Context ctx) {
        try {
            List<Imagen_restaurante> imagenes = imagenService.obtenerTodasLasImagenes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", imagenes);
            response.put("total", imagenes.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerImagenPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Imagen_restaurante> imagenOpt = imagenService.obtenerImagenPorId(id);
            
            if (imagenOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", imagenOpt.get());
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Imagen no encontrada");
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de imagen inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
    
    public void obtenerImagenesDeRestaurante(Context ctx) {
        try {
            int id_restaurante = Integer.parseInt(ctx.pathParam("id_restaurante"));
            List<Imagen_restaurante> imagenes = imagenService.obtenerImagenesDeRestaurante(id_restaurante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", imagenes);
            response.put("total", imagenes.size());
            response.put("restaurante_id", id_restaurante);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerImagenesPorTipo(Context ctx) {
        try {
            int id_restaurante = Integer.parseInt(ctx.pathParam("id_restaurante"));
            String tipoStr = ctx.pathParam("tipo");
            
            TipoImagen tipo = TipoImagen.fromValor(tipoStr);
            List<Imagen_restaurante> imagenes = imagenService.obtenerImagenesPorTipo(id_restaurante, tipo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", imagenes);
            response.put("total", imagenes.size());
            response.put("restaurante_id", id_restaurante);
            response.put("tipo", tipoStr);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Tipo de imagen inválido. Valores permitidos: principal, secundaria, platillo");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void obtenerImagenPrincipal(Context ctx) {
        try {
            int id_restaurante = Integer.parseInt(ctx.pathParam("id_restaurante"));
            Optional<Imagen_restaurante> imagenOpt = imagenService.obtenerImagenPrincipal(id_restaurante);
            
            if (imagenOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", imagenOpt.get());
                response.put("restaurante_id", id_restaurante);
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El restaurante no tiene imagen principal");
                response.put("restaurante_id", id_restaurante);
                
                ctx.status(HttpStatus.NOT_FOUND).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de restaurante inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void actualizarImagen(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Imagen_restaurante imagen = ctx.bodyAsClass(Imagen_restaurante.class);
            imagen.setId_imagen(id);
            
            Imagen_restaurante imagenActualizada = imagenService.actualizarImagen(imagen);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagen actualizada exitosamente");
            response.put("data", imagenActualizada);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de imagen inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }

    public void eliminarImagen(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean eliminada = imagenService.eliminarImagen(id);
            
            if (eliminada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Imagen eliminada exitosamente");
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la imagen");
                
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "ID de imagen inválido");
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
        }
    }
}
