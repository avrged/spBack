package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Adquirir_membresia;
import org.sazonpt.service.Adquirir_membresiaService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Adquirir_membresiaController {
    private final Adquirir_membresiaService membresiaService;

    public Adquirir_membresiaController(Adquirir_membresiaService membresiaService) {
        this.membresiaService = membresiaService;
    }

    public void getAll(Context ctx) {
        try {
            List<Adquirir_membresia> membresias = membresiaService.getAllMembresias();
            ctx.json(membresias);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener membresías");
        }
    }

    public void getById(Context ctx) {
        try {
            int idAdquisicion = Integer.parseInt(ctx.pathParam("id"));
            Adquirir_membresia membresia = membresiaService.getById(idAdquisicion);
            
            if (membresia != null) {
                ctx.json(membresia);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Membresía no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener membresía");
        }
    }

    public void getByRestaurantero(Context ctx) {
        try {
            int codigoRestaurantero = Integer.parseInt(ctx.pathParam("restaurantero"));
            List<Adquirir_membresia> membresias = membresiaService.getMembresiasByRestaurantero(codigoRestaurantero);
            ctx.json(membresias);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener membresías del restaurantero");
        }
    }

    public void create(Context ctx) {
        try {
            Adquirir_membresia membresia = ctx.bodyAsClass(Adquirir_membresia.class);
            membresiaService.createMembresia(membresia);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Membresía creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear membresía: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idAdquisicion = Integer.parseInt(ctx.pathParam("id"));
            Adquirir_membresia membresia = ctx.bodyAsClass(Adquirir_membresia.class);
            // Asegurarse de que el ID de la membresía coincida con el parámetro de la URL
            Adquirir_membresia updatedMembresia = new Adquirir_membresia(
                idAdquisicion,
                membresia.getId_restaurantero(),
                membresia.getFecha_adquisicion(),
                membresia.getCosto(),
                membresia.getEstado()
            );
            membresiaService.updateMembresia(updatedMembresia);
            ctx.status(200).result("Membresía actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar membresía: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idAdquisicion = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete membresia with ID: " + idAdquisicion);
            membresiaService.deleteMembresia(idAdquisicion);
            ctx.status(200).result("Membresía eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid membresia ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de membresía inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting membresia: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting membresia: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
