package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.AdminController;

public class AdminRoutes {
    private final AdminController adminController;
    
    public AdminRoutes(AdminController adminController) {
        this.adminController = adminController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/admins", adminController::getAll);
        app.post("/admins", adminController::create);
        app.get("/admins/{id}", adminController::getAdminById);
        app.put("/admins/{id}", adminController::update);
        app.delete("/admins/{id}", adminController::delete);
    }


}