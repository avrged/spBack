package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Administrador;
import org.sazonpt.model.Usuario;
import org.sazonpt.service.AdministradorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdministradorController {
    private final AdministradorService administradorService;
    private final ObjectMapper objectMapper;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
        this.objectMapper = new ObjectMapper();
    }

    public void getAll(Context ctx) {
        try {
            List<Administrador> administradores = administradorService.getAllAdministradores();
            ctx.json(administradores);
        } catch (SQLException e) {
            System.err.println("Error al obtener administradores: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener administradores");
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Administrador administrador = administradorService.getByIdUsuario(id);
            if (administrador != null) {
                ctx.json(administrador);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Administrador no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("ID de administrador inválido");
        } catch (SQLException e) {
            System.err.println("Error al obtener administrador: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener administrador");
        }
    }

    public void create(Context ctx) {
        try {
            Usuario usuarioData = ctx.bodyAsClass(Usuario.class);
            administradorService.crearAdministrador(usuarioData);
            ctx.status(HttpStatus.CREATED).json(Map.of(
                "message", "Administrador creado exitosamente",
                "success", true
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al crear administrador: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al crear administrador: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void promoverUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            administradorService.promoverUsuarioAAdmin(idUsuario);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Usuario promovido a administrador exitosamente",
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
            System.err.println("Error al promover usuario: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuarioData = ctx.bodyAsClass(Usuario.class);
            
            administradorService.actualizarDatosAdministrador(id, usuarioData);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Administrador actualizado exitosamente",
                "success", true
            ));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de administrador inválido",
                "success", false
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al actualizar administrador: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar administrador: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void revocarPermisos(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            administradorService.revocarAdministrador(idUsuario);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Permisos de administrador revocados exitosamente",
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
            System.err.println("Error al revocar permisos: " + e.getMessage());
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

            Administrador administrador = administradorService.autenticarAdministrador(correo, contrasena);
            
            if (administrador != null && administrador.getUsuario() != null) {
                Usuario usuario = administrador.getUsuario();
                // Crear respuesta sin incluir la contraseña
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Autenticación de administrador exitosa",
                    "administrador", Map.of(
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
                    "message", "Credenciales inválidas o no es administrador"
                ));
            }
        } catch (Exception e) {
            System.err.println("Error en login de administrador: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "success", false,
                "message", "Error al procesar la solicitud"
            ));
        }
    }

    public void verificarAdmin(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            boolean esAdmin = administradorService.esAdministrador(idUsuario);
            ctx.json(Map.of(
                "idUsuario", idUsuario,
                "esAdministrador", esAdmin,
                "success", true
            ));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de usuario inválido",
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al verificar administrador: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        }
    }
}
