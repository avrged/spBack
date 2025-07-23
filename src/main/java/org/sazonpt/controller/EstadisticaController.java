
package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Estadistica;
import org.sazonpt.service.EstadisticaService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class EstadisticaController {
    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    public void getAll(Context ctx) {
        try {
            List<Estadistica> estadisticas = estadisticaService.getAllEstadisticas();
            ctx.json(estadisticas);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener estadísticas");
        }
    }

    public void getById(Context ctx) {
        try {
            int idEstadistica = Integer.parseInt(ctx.pathParam("id"));
            Estadistica estadistica = estadisticaService.getById(idEstadistica);
            if (estadistica != null) {
                ctx.json(estadistica);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Estadística no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener estadística");
        }
    }

    public void create(Context ctx) {
        try {
            Estadistica estadistica = ctx.bodyAsClass(Estadistica.class);
            estadisticaService.createEstadistica(estadistica);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Estadística creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear estadística: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idEstadistica = Integer.parseInt(ctx.pathParam("id"));
            Estadistica estadistica = ctx.bodyAsClass(Estadistica.class);
            estadistica.setId_estadistica(idEstadistica);
            estadisticaService.updateEstadistica(estadistica);
            ctx.status(200).result("Estadística actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar estadística: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idEstadistica = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Intentando eliminar la estadística con id: " + idEstadistica);
            estadisticaService.deleteEstadistica(idEstadistica);
            ctx.status(200).result("Estadística eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Formato invalido de id: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de estadística inválido");
        } catch (SQLException e) {
            System.out.println("Error sql al borrar la estadística: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al intentar borrar la estadística: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
