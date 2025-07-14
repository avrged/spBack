package org.sazonpt.routes;

import org.sazonpt.controller.UsuarioController;

import io.javalin.Javalin;

public class UsuarioRoutes {
    private final UsuarioController userController;

    public UsuarioRoutes(UsuarioController usuarioController){
        this.userController = usuarioController;
    }

    public void registerRoutes(Javalin app){
        app.get("/users", userController::getAllUsers);
        app.post("/users", userController::CreateUser);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::UpdateUser);
        app.delete("/users/{id}", userController::DeleteUser);
    }
}