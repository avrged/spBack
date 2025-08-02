package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.model.Usuario;
import org.sazonpt.service.RestauranteroService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RestauranteroController {
    private final RestauranteroService restauranteroService;
    private final ObjectMapper objectMapper;

    public RestauranteroController(RestauranteroService restauranteroService) {
        this.restauranteroService = restauranteroService;
        this.objectMapper = new ObjectMapper();
    }

    public void getAll(Context ctx) {
        try {
            List<Restaurantero> restauranteros = restauranteroService.getAllRestauranteros();
            ctx.json(restauranteros);
        } catch (SQLException e) {
            System.err.println("Error al obtener restauranteros: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener restauranteros");
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurantero restaurantero = restauranteroService.getByIdUsuario(id);
            if (restaurantero != null) {
                ctx.json(restaurantero);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Restaurantero no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("ID de restaurantero inválido");
        } catch (SQLException e) {
            System.err.println("Error al obtener restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error al obtener restaurantero");
        }
    }

    public void create(Context ctx) {
        try {
            Usuario usuarioData = ctx.bodyAsClass(Usuario.class);
            restauranteroService.crearRestaurantero(usuarioData);
            ctx.status(HttpStatus.CREATED).json(Map.of(
                "message", "Restaurantero creado exitosamente",
                "success", true
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al crear restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al crear restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void promoverUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            restauranteroService.promoverUsuarioARestaurantero(idUsuario);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Usuario promovido a restaurantero exitosamente",
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
            
            restauranteroService.actualizarDatosRestaurantero(id, usuarioData);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Restaurantero actualizado exitosamente",
                "success", true
            ));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de restaurantero inválido",
                "success", false
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", e.getMessage(),
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al actualizar restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "Error al procesar la solicitud",
                "success", false
            ));
        }
    }

    public void revocarPermisos(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            restauranteroService.revocarRestaurantero(idUsuario);
            ctx.status(HttpStatus.OK).json(Map.of(
                "message", "Permisos de restaurantero revocados exitosamente",
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

            Restaurantero restaurantero = restauranteroService.autenticarRestaurantero(correo, contrasena);
            
            if (restaurantero != null && restaurantero.getUsuario() != null) {
                Usuario usuario = restaurantero.getUsuario();
                // Crear respuesta sin incluir la contraseña
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Autenticación de restaurantero exitosa",
                    "restaurantero", Map.of(
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
                    "message", "Credenciales inválidas o no es restaurantero"
                ));
            }
        } catch (Exception e) {
            System.err.println("Error en login de restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "success", false,
                "message", "Error al procesar la solicitud"
            ));
        }
    }

    public void verificarRestaurantero(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("id"));
            boolean esRestaurantero = restauranteroService.esRestaurantero(idUsuario);
            ctx.json(Map.of(
                "id_usuario", idUsuario,
                "esRestaurantero", esRestaurantero,
                "success", true
            ));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of(
                "message", "ID de usuario inválido",
                "success", false
            ));
        } catch (SQLException e) {
            System.err.println("Error al verificar restaurantero: " + e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of(
                "message", "Error interno del servidor",
                "success", false
            ));
        }
    }
}
