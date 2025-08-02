package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.AdministradorController;

public class AdministradorRoutes {
    private final AdministradorController administradorController;
    
    public AdministradorRoutes(AdministradorController administradorController) {
        this.administradorController = administradorController;
    }
    
    public void register(Javalin app) {
        // Rutas CRUD para administradores
        app.get("/administradores", administradorController::getAll);
        app.post("/administradores", administradorController::create);
        app.get("/administradores/{id}", administradorController::getById);
        app.put("/administradores/{id}", administradorController::update);
        
        // Rutas específicas de administración
        app.post("/administradores/promover/{id}", administradorController::promoverUsuario);
        app.delete("/administradores/revocar/{id}", administradorController::revocarPermisos);
        app.get("/administradores/verificar/{id}", administradorController::verificarAdmin);
        
        // Ruta para autenticación específica de administradores
        app.post("/auth/admin/login", administradorController::login);
    }
}
