package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Comprobante;
import org.sazonpt.service.ComprobanteService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ComprobanteController {
    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    public void getAll(Context ctx) {
        try {
            List<Comprobante> comprobantes = comprobanteService.getAllComprobantes();
            ctx.json(comprobantes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener comprobantes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("id"));
            Comprobante comprobante = comprobanteService.getById(idComprobante);
            
            if (comprobante != null) {
                ctx.json(comprobante);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Comprobante no encontrado");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener comprobante");
        }
    }

    public void create(Context ctx) {
        try {
            Comprobante comprobante = ctx.bodyAsClass(Comprobante.class);
            comprobanteService.createComprobante(comprobante);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Comprobante creado correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear comprobante: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("id"));
            Comprobante comprobante = ctx.bodyAsClass(Comprobante.class);
            // Asegurarse de que el ID del comprobante coincida con el parámetro de la URL
            Comprobante updatedComprobante = new Comprobante(
                idComprobante,
                comprobante.getCodigo_Rest(),
                comprobante.getRuta_Archivo(),
                comprobante.getFecha()
            );
            comprobanteService.updateComprobante(updatedComprobante);
            ctx.status(200).result("Comprobante actualizado");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar comprobante: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete comprobante with ID: " + idComprobante);
            comprobanteService.deleteComprobante(idComprobante);
            ctx.status(200).result("Comprobante eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid comprobante ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de comprobante inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting comprobante: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting comprobante: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
