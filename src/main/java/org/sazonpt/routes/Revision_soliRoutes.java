package org.sazonpt.routes;

import org.sazonpt.controller.Revision_solicitudController;

import io.javalin.Javalin;

public class Revision_soliRoutes {
    private final Revision_solicitudController revisionController;

    public Revision_soliRoutes(Revision_solicitudController revisionController) {
        this.revisionController = revisionController;
    }

    public void registerRoutes(Javalin app) {
        
        // ==================== RUTAS PARA REVISIONES ====================
        // GET - Obtener todas las revisiones
        app.get("/revisiones", revisionController::getAllRevisiones);
        
        // GET - Obtener revisión por ID
        app.get("/revisiones/{id}", revisionController::getRevisionById);
        
        // POST - Crear nueva revisión
        app.post("/revisiones", revisionController::createRevision);
        
        // PUT - Actualizar revisión
        app.put("/revisiones/{id}", revisionController::updateRevision);
    }
}
