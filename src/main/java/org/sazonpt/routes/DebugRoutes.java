package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.DebugController;

public class DebugRoutes {
    private final DebugController debugController;
    
    public DebugRoutes(DebugController debugController) {
        this.debugController = debugController;
    }
    
    public void register(Javalin app) {
        app.get("/api/debug/check", debugController::checkDatabase);
        app.post("/api/debug/fix", debugController::fixForeignKeys);
    }
}
