package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.Revision_solicitudController;

public class Revision_solicitudRoutes {
    
    private final Revision_solicitudController revisionController;

    public Revision_solicitudRoutes(Revision_solicitudController revisionController) {
        this.revisionController = revisionController;
    }

    public void register(Javalin app) {
        // Rutas CRUD para revisiones de solicitud
        app.get("/revisiones", revisionController::obtenerTodasLasRevisiones);
        app.post("/revisiones", revisionController::crearRevision);
        app.get("/revisiones/{id}", revisionController::obtenerRevisionPorId);
        app.put("/revisiones/{id}", revisionController::actualizarRevision);
        
        // Rutas específicas por solicitud
        app.get("/revisiones/solicitud/{idSolicitud}", 
               revisionController::obtenerRevisionesPorSolicitud);
        
        // Rutas específicas por administrador
        app.get("/revisiones/administrador/{idAdministrador}", 
               revisionController::obtenerRevisionesPorAdministrador);
        
        // Ruta para crear revisión rápida
        app.post("/revisiones/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                revisionController::crearRevisionRapida);
        
        // Ruta para aprobar solicitud (con parámetros en URL)
        app.post("/revisiones/aprobar/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                revisionController::aprobarSolicitud);
        
        // Ruta para aprobar solicitud (con JSON en body)
        app.post("/revisiones-solicitud/aprobar", revisionController::aprobarSolicitudJSON);
        
        // Ruta para eliminar revisión con todos los IDs
        app.delete("/revisiones/{id}/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                  revisionController::eliminarRevision);
    }
}
