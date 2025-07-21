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
            int id_usuario = userService.createUser(user); // Debe retornar el id generado

            // Construir datos de respuesta
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("id_usuario", id_usuario);
            data.put("nombre", user.getNombre());
            data.put("correo", user.getCorreo());
            data.put("tipo", user.getTipo());
            data.put("status", user.getStatus());

            ctx.status(201).json(java.util.Map.of(
                "success", true,
                "message", "Usuario creado correctamente",
                "data", data
            ));
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && (msg.contains("Duplicate entry") || msg.contains("UNIQUE") || msg.contains("duplicate key"))) {
                ctx.status(409).json(java.util.Map.of(
                    "success", false,
                    "message", "Ya existe un usuario con ese correo"
                ));
            } else {
                ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear usuario: " + msg
                ));
            }
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
            System.out.println("Intentando borrar el usuario con id: " + idUser);
            userService.deleteUser(idUser);
            ctx.status(200).result("Usuario eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Formato invalido de id: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de usuario inválido");
        } catch (SQLException e) {
            System.out.println("Error sql al borrar el usuario: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al borrar el usuario: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    public void login(Context ctx) {
        try {
            var body = ctx.bodyAsClass(java.util.Map.class);
            String correo = (String) body.get("correo");
            String contrasena = (String) body.get("contrasena");
            String rol = (String) body.get("rol");

            Usuario usuario = userService.login(correo, contrasena, rol);

            if (usuario != null) {
                ctx.json(java.util.Map.of(
                        "success", true,
                        "rol", usuario.getTipo(),
                        "message", "Login exitoso"
                ));
            } else {
                ctx.json(java.util.Map.of(
                        "success", false,
                        "message", "Credenciales incorrectas o rol no válido"
                ));
            }
        } catch (Exception e) {
            ctx.status(500).json(java.util.Map.of(
                    "success", false,
                    "message", "Error en el servidor"
            ));
        }
    }
}
