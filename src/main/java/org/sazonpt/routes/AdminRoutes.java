package org.sazonpt.routes;

import org.sazonpt.controller.AdminController;
import io.javalin.Javalin;

public class AdminRoutes {
    
    private final AdminController adminController;

    public AdminRoutes(AdminController adminController) {
        this.adminController = adminController;
    }

    public void registerRoutes(Javalin app) {
        // CRUD básico de administradores
        app.post("/api/admins", adminController::createAdmin);
        app.get("/api/admins/{id}", adminController::getAdminById);
        app.get("/api/admins", adminController::getAllAdmins);
        app.put("/api/admins/{id}", adminController::updateAdmin);
        app.delete("/api/admins/{id}", adminController::deleteAdmin);

        app.get("/api/admins/search", adminController::searchAdminByEmail);
        app.patch("/api/admins/{id}/level", adminController::changeAdminLevel);
        app.get("/api/admins/stats", adminController::getAdminStats);
    }
}
