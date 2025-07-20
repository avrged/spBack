package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Restaurantero;
import org.sazonpt.service.RestauranteroService;

import io.javalin.http.Context;

public class RestauranteroController {
    private final RestauranteroService restauranteroService;

    public RestauranteroController(RestauranteroService restauranteroService) {
        this.restauranteroService = restauranteroService;
    }

    public void getAll(Context ctx) {
        try {
            List<Restaurantero> restauranteros = restauranteroService.getAllRestauranteros();
            ctx.json(restauranteros);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener restauranteros: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int idReo = Integer.parseInt(ctx.pathParam("id"));
            Restaurantero reo = restauranteroService.getByIdRestaurantero(idReo);
            
            if (reo != null) {
                ctx.json(reo);
            } else {
                ctx.status(404).result("Restaurantero no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurantero inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener restaurantero: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            Restaurantero restaurantero = ctx.bodyAsClass(Restaurantero.class);
            restauranteroService.createRestaurantero(restaurantero);
            ctx.status(201).json(java.util.Map.of(
                "success", true,
                "message", "Restaurantero creado exitosamente",
                "data", java.util.Map.of(
                    "nombre", restaurantero.getNombre(),
                    "correo", restaurantero.getCorreo(),
                    "tipo", restaurantero.getTipo(),
                    "status", restaurantero.getStatus(),
                    "id_usuario", restaurantero.getId_usuario()
                )
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "Datos inválidos: " + e.getMessage()
            ));
        } catch (SQLException e) {
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al crear restaurantero: " + e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "Error al procesar la solicitud: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("id"));
            Restaurantero restaurantero = ctx.bodyAsClass(Restaurantero.class);
            // Asegurar que el ID del path param se use
            restaurantero.setIdUsuario(idRestaurantero);
            restauranteroService.updateRestaurantero(restaurantero);
            ctx.status(200).result("Restaurantero actualizado exitosamente");
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurantero inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Datos inválidos: " + e.getMessage());
        } catch (SQLException e) {
            ctx.status(500).result("Error al actualizar restaurantero: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(400).result("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete restaurantero with ID: " + idRestaurantero);
            restauranteroService.deleteRestaurantero(idRestaurantero);
            ctx.status(200).result("Restaurantero eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid restaurantero ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de restaurantero inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting restaurantero: " + e.getMessage());
            ctx.status(500).result("Error al eliminar restaurantero: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting restaurantero: " + e.getMessage());
            ctx.status(400).result("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    // Endpoint temporal para migrar usuarios a restauranteros
    public void migrateUsers(Context ctx) {
        try {
            restauranteroService.migrateUsersToRestaurantero();
            ctx.status(200).json(java.util.Map.of(
                    "success", true,
                    "message", "Usuarios migrados exitosamente a la tabla restaurantero"
            ));
        } catch (SQLException e) {
            ctx.status(500).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al migrar usuarios: " + e.getMessage()
            ));
        }
    }
}
