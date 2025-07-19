package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Descarga;
import org.sazonpt.service.DescargaService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class DescargaController {
    private final DescargaService descargaService;

    public DescargaController(DescargaService descargaService) {
        this.descargaService = descargaService;
    }

    public void getAll(Context ctx) {
        try {
            List<Descarga> descargas = descargaService.getAllDescargas();
            ctx.json(descargas);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener descargas");
        }
    }

    public void getById(Context ctx) {
        try {
            int idDescarga = Integer.parseInt(ctx.pathParam("id"));
            Descarga descarga = descargaService.getById(idDescarga);
            
            if (descarga != null) {
                ctx.json(descarga);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Descarga no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener descarga");
        }
    }

    public void create(Context ctx) {
        try {
            Descarga descarga = ctx.bodyAsClass(Descarga.class);
            descargaService.createDescarga(descarga);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Descarga creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear descarga: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idDescarga = Integer.parseInt(ctx.pathParam("id"));
            Descarga descarga = ctx.bodyAsClass(Descarga.class);
            // Asegurarse de que el ID de la descarga coincida con el parámetro de la URL
            Descarga updatedDescarga = new Descarga(
                idDescarga,
                descarga.getCantidad_descargas(),
                descarga.getId_adquisicion(),
                descarga.getLugar_origen(),
                descarga.getOpiniones()
            );
            descargaService.updateDescarga(updatedDescarga);
            ctx.status(200).result("Descarga actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar descarga: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idDescarga = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete descarga with ID: " + idDescarga);
            descargaService.deleteDescarga(idDescarga);
            ctx.status(200).result("Descarga eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid descarga ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de descarga inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting descarga: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting descarga: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
