package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Usuario;
import org.sazonpt.service.UsuarioService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class UsuarioController {
    private final UsuarioService userService;

    public UsuarioController(UsuarioService userService){
        this.userService = userService;
    }

    public void getAll(Context ctx) {
        try {
            List<Usuario> users = userService.getAllUsers();
            ctx.json(users);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener usuarios");
        }
    }}