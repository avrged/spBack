package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.service.Revision_soliService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Revision_solicitudController {
    private final Revision_soliService revisionService;

    public Revision_solicitudController(Revision_soliService revisionService) {
        this.revisionService = revisionService;
    }

    // Métodos para Revisiones
    public void getAllRevisiones(Context ctx) {
        try {
            List<Revision_solicitud> revisiones = revisionService.getAllRevisiones();
            ctx.json(revisiones);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener revisiones");
        }
    }

    public void getRevisionById(Context ctx) {
        try {
            int idRevision = Integer.parseInt(ctx.pathParam("id"));
            Revision_solicitud revision = revisionService.getRevisionById(idRevision);
            
            if (revision != null) {
                ctx.json(revision);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Revisión no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener revisión");
        }
    }

    public void createRevision(Context ctx) {
        try {
            Revision_solicitud revision = ctx.bodyAsClass(Revision_solicitud.class);
            revisionService.createRevision(revision);
            ctx.status(201).result("Revisión creada correctamente");
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear revisión: " + e.getMessage()
            ));
        }
    }

    public void updateRevision(Context ctx) {
        try {
            int idRevision = Integer.parseInt(ctx.pathParam("id"));
            Revision_solicitud revision = ctx.bodyAsClass(Revision_solicitud.class);
            // Asegurarse de que el ID de la revisión coincida con el parámetro de la URL
            Revision_solicitud updatedRevision = new Revision_solicitud(
                idRevision,
                revision.getCodigo_solicitud(),
                revision.getCodigo_admin(),
                revision.getFecha()
            );
            revisionService.updateRevision(updatedRevision);
            ctx.status(200).result("Revisión actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar revisión: " + e.getMessage());
        }
    }

    // Métodos para Solicitudes
    public void getAllSolicitudes(Context ctx) {
        try {
            List<Solicitud_registro> solicitudes = revisionService.getAllSolicitudes();
            ctx.json(solicitudes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener solicitudes");
        }
    }

    public void getSolicitudById(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitud = revisionService.getSolicitudById(idSolicitud);
            
            if (solicitud != null) {
                ctx.json(solicitud);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Solicitud no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener solicitud");
        }
    }

    public void createSolicitud(Context ctx) {
        try {
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            revisionService.createSolicitud(solicitud);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Solicitud creada correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear solicitud: " + e.getMessage()
            ));
        }
    }

    public void deleteSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete solicitud with ID: " + idSolicitud);
            revisionService.deleteSolicitud(idSolicitud);
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
