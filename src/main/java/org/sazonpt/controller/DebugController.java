package org.sazonpt.controller;

import io.javalin.http.Context;
import org.sazonpt.service.DebugService;

public class DebugController {
    private final DebugService debugService;
    
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }
    
    public void checkDatabase(Context ctx) {
        try {
            String result = debugService.checkDatabaseIntegrity();
            ctx.json(result);
        } catch (Exception e) {
            ctx.status(500).json("Error: " + e.getMessage());
        }
    }
    
    public void fixForeignKeys(Context ctx) {
        try {
            String result = debugService.fixForeignKeyIssues();
            ctx.json(result);
        } catch (Exception e) {
            ctx.status(500).json("Error: " + e.getMessage());
        }
    }
}
