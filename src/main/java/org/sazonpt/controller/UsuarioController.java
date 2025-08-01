package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sazonpt.model.Usuario;
import org.sazonpt.service.UsuarioService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

/**
 * Controlador REST para la gestión de usuarios
 * Siguiendo la arquitectura del template API con Javalin
 */
public class UsuarioController {
    
    private final UsuarioService userService;

    public UsuarioController(UsuarioService userService) {
        this.userService = userService;
    }

    /**
     * Crea un nuevo usuario
     * POST /users
     */
    public void createUser(Context ctx) {
        try {
            // Parsear el JSON del request
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            
            // Crear el usuario
            int userId = userService.createUser(usuario);
            
            // Respuesta exitosa con timestamp para evitar cache
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", Map.of("id", userId));
            response.put("timestamp", System.currentTimeMillis());
            
            // Headers para evitar cache
            ctx.header("Cache-Control", "no-cache, no-store, must-revalidate");
            ctx.header("Pragma", "no-cache");
            ctx.header("Expires", "0");
            
            ctx.status(HttpStatus.CREATED).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Obtiene un usuario por ID
     * GET /api/users/{id}
     */
    public void getUserById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            Usuario usuario = userService.getUserById(id);
            
            if (usuario == null) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            // Ocultar información sensible
            usuario.setPassword_hash(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuario);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los usuarios
     * GET /api/users
     */
    public void getAll(Context ctx) {
        try {
            List<Usuario> users = userService.getAllUsers();
            
            // Ocultar información sensible
            users.forEach(user -> user.setPassword_hash(null));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", users);
            response.put("total", users.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Actualiza un usuario
     * PUT /api/users/{id}
     */
    public void updateUser(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            // Parsear el JSON del request
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuario.setId_usuario(id); // Asegurar que el ID coincida con la URL
            
            // Actualizar el usuario
            boolean updated = userService.updateUser(usuario);
            
            if (!updated) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Usuario no encontrado o no se pudo actualizar");
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario
     * DELETE /api/users/{id}
     */
    public void deleteUser(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            boolean deleted = userService.deleteUser(id);
            
            if (!deleted) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Busca un usuario por email
     * GET /api/users/search?email=valor
     */
    public void searchUserByEmail(Context ctx) {
        try {
            String email = ctx.queryParam("email");
            
            if (email == null || email.trim().isEmpty()) {
                handleError(ctx, HttpStatus.BAD_REQUEST, "El parámetro 'email' es requerido");
                return;
            }
            
            Usuario usuario = userService.getUserByEmail(email.trim());
            
            if (usuario == null) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            // Ocultar información sensible
            usuario.setPassword_hash(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuario);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Login unificado para usuarios (administradores y restauranteros)
     * POST /users/login
     */
    public void login(Context ctx) {
        try {
            // Obtener credenciales del body
            @SuppressWarnings("unchecked")
            Map<String, Object> loginData = ctx.bodyAsClass(Map.class);
            String email = (String) loginData.get("email");
            String password = (String) loginData.get("password");

            if (email == null || email.trim().isEmpty()) {
                handleError(ctx, HttpStatus.BAD_REQUEST, "El email es obligatorio");
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                handleError(ctx, HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
                return;
            }

            // Buscar usuario por email y validar contraseña
            Usuario usuario = userService.loginUser(email.trim(), password);
            
            if (usuario != null) {
                // Obtener información completa del usuario (tipo, roles, etc.)
                Object[] userInfo = userService.getUserLoginInfo(email.trim());
                
                // Crear respuesta con información completa
                Map<String, Object> userData = new HashMap<>();
                userData.put("id_usuario", usuario.getId_usuario());
                userData.put("email", usuario.getEmail());
                userData.put("nombre", usuario.getNombre());
                userData.put("telefono", usuario.getTelefono());
                userData.put("avatar_url", usuario.getAvatar_url());
                userData.put("activo", usuario.isActivo());
                
                // Agregar información de rol si está disponible
                if (userInfo != null) {
                    userData.put("tipo_usuario", userInfo[11]); // tipo_usuario
                    
                    // Si es administrador, agregar nivel de permiso
                    if ("Administrador".equals(userInfo[11]) && userInfo[8] != null) {
                        userData.put("nivel_permiso", userInfo[8]);
                    }
                    
                    // Si es restaurantero, agregar información de verificación
                    if ("Restaurantero".equals(userInfo[11])) {
                        userData.put("rfc", userInfo[9]);
                        userData.put("verificado", userInfo[10]);
                    }
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login exitoso");
                response.put("data", userData);
                
                ctx.status(HttpStatus.OK).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Credenciales inválidas");
                
                ctx.status(HttpStatus.UNAUTHORIZED).json(response);
            }

        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Obtiene estadísticas básicas de usuarios
     * GET /api/users/stats
     */
    public void getUserStats(Context ctx) {
        try {
            int totalUsers = userService.getTotalUsers();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_usuarios", totalUsers);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Obtiene usuarios con información de roles
     * GET /api/users/with-roles
     */
    public void getUsersWithRoles(Context ctx) {
        try {
            List<Object[]> usersWithRoles = userService.getUsersWithRoles();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usersWithRoles);
            response.put("total", usersWithRoles.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Maneja errores y devuelve respuesta JSON consistente
     */
    private void handleError(Context ctx, HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        ctx.status(status).json(errorResponse);
    }
}