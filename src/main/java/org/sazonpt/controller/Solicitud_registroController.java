package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.service.Solicitud_registroService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Solicitud_registroController {
    private final Solicitud_registroService solicitudService;

    public Solicitud_registroController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public void getAll(Context ctx) {
        try {
            List<Solicitud_registro> solicitudes = solicitudService.getAllSolicitudes();
            ctx.json(solicitudes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener solicitudes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitud = solicitudService.getById(idSolicitud);
            
            if (solicitud != null) {
                ctx.json(solicitud);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Solicitud no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener solicitud");
        }
    }

    public void create(Context ctx) {
        try {
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            solicitudService.createSolicitud(solicitud);
            ctx.status(201).result("Solicitud creada correctamente");
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear solicitud: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            // Asegurarse de que el ID de la solicitud coincida con el parámetro de la URL
            Solicitud_registro updatedSolicitud = new Solicitud_registro(
                idSolicitud,
                solicitud.getId_restaurantero(),
                solicitud.getFecha(),
                solicitud.getEstado(),
                solicitud.getNombrePropuesto(),
                solicitud.getCorreo(),
                solicitud.getDireccionPropuesta(),
                solicitud.getRuta_imagen(),
                solicitud.getRuta_comprobante()
            );
            solicitudService.updateSolicitud(updatedSolicitud);
            ctx.status(200).result("Solicitud actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar solicitud: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete solicitud with ID: " + idSolicitud);
            solicitudService.deleteSolicitud(idSolicitud);
            ctx.status(200).result("Solicitud eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid solicitud ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de solicitud inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting solicitud: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting solicitud: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
