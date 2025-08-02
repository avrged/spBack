package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Usuario;
import org.sazonpt.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.objectMapper = new ObjectMapper();
    }

    public void getAll(Context ctx) {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            ctx.json(usuarios);
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener usuarios");
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuario = usuarioService.getByIdUsuario(id);
            if (usuario != null) {
                ctx.json(usuario);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Usuario no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("ID de usuario inválido");
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener usuario");
        }
    }

    public void create(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            Usuario usuarioCreado = usuarioService.createUsuario(usuario);
            ctx.status(HttpStatus.CREATED).json(Map.of(
                "message", "Usuario creado exitosamente",
                "success", true,
                "usuario", usuarioCreado
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al crear usuario: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuario.setId_usuario(id); // Asegurar que el ID coincida con el parámetro de la URL
            
            usuarioService.updateUsuario(usuario);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Usuario actualizado exitosamente",
                "success", true
            ));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de usuario inválido",
                "success", false
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar usuario: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = usuarioService.deleteUsuario(id);
            
            if (deleted) {
                ctx.status(HttpStatus.OK).json(Map.of(
                    "message", "Usuario eliminado exitosamente",
                    "success", true
                ));
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(Map.of(
                    "message", "Usuario no encontrado",
                    "success", false
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de usuario inválido",
                "success", false
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        }
    }

    public void login(Context ctx) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> credentials = objectMapper.readValue(ctx.body(), Map.class);
            String correo = credentials.get("correo");
            String contrasena = credentials.get("contrasena");

            Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);
            
            if (usuario != null) {
                // Crear respuesta sin incluir la contraseña
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Autenticación exitosa",
                    "usuario", Map.of(
                        "id_usuario", usuario.getId_usuario(),
                        "nombre", usuario.getNombre(),
                        "correo", usuario.getCorreo(),
                        "tipo", usuario.getTipo()
                    )
                );
                ctx.status(HttpStatus.OK).json(response);
            } else {
                ctx.status(HttpStatus.UNAUTHORIZED).json(Map.of(
                    "success", false,
                    "message", "Credenciales inválidas"
                ));
            }
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "success", false,
                "message", "Error al procesar la solicitud"
            ));
        }
    }
}
