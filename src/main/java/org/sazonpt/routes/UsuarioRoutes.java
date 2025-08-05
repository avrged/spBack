package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.UsuarioController;

public class UsuarioRoutes {
    private final UsuarioController usuarioController;
    
    public UsuarioRoutes(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }
    
    public void register(Javalin app) {
        app.get("/usuarios", usuarioController::getAll);
        app.post("/usuarios", usuarioController::create);
        app.get("/usuarios/{id}", usuarioController::getById);
        app.put("/usuarios/{id}", usuarioController::update);
        app.delete("/usuarios/{id}", usuarioController::delete);
        app.post("/auth/login", usuarioController::login);
    }
}
