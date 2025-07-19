package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Zona;
import org.sazonpt.service.ZonaService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ZonaController {
    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    public void getAll(Context ctx) {
        try {
            List<Zona> zonas = zonaService.getAllZonas();
            ctx.json(zonas);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener zonas");
        }
    }

    public void getById(Context ctx) {
        try {
            int idZona = Integer.parseInt(ctx.pathParam("id"));
            Zona zona = zonaService.getById(idZona);
            
            if (zona != null) {
                ctx.json(zona);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Zona no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener zona");
        }
    }

    public void create(Context ctx) {
        try {
            Zona zona = ctx.bodyAsClass(Zona.class);
            zonaService.createZona(zona);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Zona creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear zona: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idZona = Integer.parseInt(ctx.pathParam("id"));
            Zona zona = ctx.bodyAsClass(Zona.class);
            // Asegurarse de que el ID de la zona coincida con el parámetro de la URL
            Zona updatedZona = new Zona(idZona, zona.getNombre(), zona.getDescripcion());
            zonaService.updateZona(updatedZona);
            ctx.status(200).result("Zona actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar zona: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idZona = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Intentando borrar la zona con id: " + idZona);
            zonaService.deleteZona(idZona);
            ctx.status(200).result("Zona eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("formato invalido de id: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de zona inválido");
        } catch (SQLException e) {
            System.out.println("Error SQL al borrar la zona: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al borrar la zona: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
