package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.RestauranteRepository;
import org.sazonpt.service.RestauranteService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    public void getAll(Context ctx) {
        try {
            List<Restaurante> restaurantes = restauranteService.getAllRestaurantes();
            ctx.json(restaurantes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener restaurantes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restaurante = restauranteService.getById(idRestaurante);
            
            if (restaurante != null) {
                ctx.json(restaurante);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Restaurante no encontrado");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener restaurante");
        }
    }

    public void create(Context ctx) {
        try {
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);
            restauranteService.createRestaurante(restaurante);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Restaurante creado correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear restaurante: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);
            // Asegurarse de que el ID del restaurante coincida con el parámetro de la URL
            Restaurante updatedRestaurante = new Restaurante(
                idRestaurante, 
                restaurante.getId_solicitud_aprobada(),
                restaurante.getId_zona(),
                restaurante.getNombre(),
                restaurante.getDireccion(),
                restaurante.getHorario(),
                restaurante.getTelefono(),
                restaurante.getEtiquetas()
            );
            restauranteService.updateRestaurante(updatedRestaurante);
            ctx.status(200).result("Restaurante actualizado");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar restaurante: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete restaurante with ID: " + idRestaurante);
            restauranteService.deleteRestaurante(idRestaurante);
            ctx.status(200).result("Restaurante eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid restaurante ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting restaurante: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting restaurante: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener el dueño de un restaurante
    public void getDueno(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurantero dueno = restauranteService.getDuenoRestaurante(idRestaurante);
            
            if (dueno != null) {
                ctx.json(dueno);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Dueño no encontrado para este restaurante");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener todos los restaurantes de un restaurantero
    public void getRestaurantesByDueno(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Restaurante> restaurantes = restauranteService.getRestaurantesByDueno(idRestaurantero);
            ctx.json(restaurantes);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurantero inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener restaurante con información completa del dueño
    public void getRestauranteConDueno(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            RestauranteRepository.RestauranteConDueno resultado = restauranteService.getRestauranteConDueno(idRestaurante);
            
            if (resultado != null) {
                ctx.json(java.util.Map.of(
                    "restaurante", resultado.getRestaurante(),
                    "dueno", resultado.getDueno()
                ));
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Restaurante no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
