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
    }

    public void getById(Context ctx) {
        try {
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            Usuario user = userService.getByIdUser(idUser);
            
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Usuario no encontrado");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener usuario");
        }
    }

    public void create(Context ctx) {
        try {
            Usuario user = ctx.bodyAsClass(Usuario.class);
            userService.createUser(user);
            ctx.status(201).result("Usuario creado");
        } catch (Exception e) {
            ctx.status(400).result("Error al crear usuario");
        }
    }

    public void update(Context ctx) {
        try {
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            Usuario user = ctx.bodyAsClass(Usuario.class);
            user.setIdUsuario(idUser);
            userService.updateUser(user);
            ctx.status(200).result("Usuario actualizado");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar usuario");
        }
    }

    public void delete(Context ctx) {
        try {
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete user with ID: " + idUser);
            userService.deleteUser(idUser);
            ctx.status(200).result("Usuario eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de usuario inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting user: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting user: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
