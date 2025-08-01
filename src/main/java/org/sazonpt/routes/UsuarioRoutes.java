package org.sazonpt.routes;

import org.sazonpt.controller.UsuarioController;

import io.javalin.Javalin;

public class UsuarioRoutes {
    private final UsuarioController userController;

    public UsuarioRoutes(UsuarioController usuarioController){
        this.userController = usuarioController;
    }

    public void registerRoutes(Javalin app){

        app.post("/users", userController::createUser);
        app.get("/users", userController::getAll);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);
        app.post("/users/login", userController::login);
        
        app.get("/users/stats", userController::getUserStats);
        app.get("/users/with-roles", userController::getUsersWithRoles);
    }
}