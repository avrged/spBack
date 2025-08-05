package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.Revision_solicitudController;

public class Revision_solicitudRoutes {
    
    private final Revision_solicitudController revisionController;

    public Revision_solicitudRoutes(Revision_solicitudController revisionController) {
        this.revisionController = revisionController;
    }

    public void register(Javalin app) {
        app.get("/revisiones", revisionController::obtenerTodasLasRevisiones);
        app.post("/revisiones", revisionController::crearRevision);
        app.get("/revisiones/{id}", revisionController::obtenerRevisionPorId);
        app.put("/revisiones/{id}", revisionController::actualizarRevision);

        app.get("/revisiones/solicitud/{idSolicitud}", 
               revisionController::obtenerRevisionesPorSolicitud);

        app.get("/revisiones/administrador/{idAdministrador}", 
               revisionController::obtenerRevisionesPorAdministrador);

        app.post("/revisiones/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                revisionController::crearRevisionRapida);

        app.post("/revisiones/aprobar/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                revisionController::aprobarSolicitud);

        app.post("/revisiones-solicitud/aprobar", revisionController::aprobarSolicitudJSON);

        app.delete("/revisiones/{id}/solicitud/{idSolicitud}/administrador/{idAdministrador}", 
                  revisionController::eliminarRevision);
    }
}
