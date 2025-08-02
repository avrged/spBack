package org.sazonpt.controller;

import org.sazonpt.model.Administrador;
import org.sazonpt.service.AdminService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de administradores
 * Siguiendo la arquitectura del template API con Javalin
 */
public class AdminController {
    
    private final AdminService adminService;
    
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    /**
     * Crea un nuevo administrador
     * POST /api/admins
     */
    public void createAdmin(Context ctx) {
        try {
            // Parsear el JSON del request
            Administrador administrador = ctx.bodyAsClass(Administrador.class);
            
            // Crear el administrador
            int adminId = adminService.createAdmin(administrador);
            
            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Administrador creado exitosamente");
            response.put("data", Map.of("id", adminId));
            
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
     * Obtiene un administrador por ID
     * GET /api/admins/{id}
     */
    public void getAdminById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            Administrador admin = adminService.getAdminById(id);
            
            if (admin == null) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Administrador no encontrado");
                return;
            }
            
            // Ocultar información sensible
            admin.setPassword_hash(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", admin);
            
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
     * Obtiene todos los administradores
     * GET /api/admins
     */
    public void getAllAdmins(Context ctx) {
        try {
            List<Administrador> admins = adminService.getAllAdmins();
            
            // Ocultar información sensible
            admins.forEach(admin -> admin.setPassword_hash(null));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", admins);
            response.put("total", admins.size());
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un administrador
     * PUT /api/admins/{id}
     */
    public void updateAdmin(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            // Parsear el JSON del request
            Administrador administrador = ctx.bodyAsClass(Administrador.class);
            administrador.setId_usuario(id); // Asegurar que el ID coincida con la URL
            
            // Actualizar el administrador
            boolean updated = adminService.updateAdmin(administrador);
            
            if (!updated) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Administrador no encontrado o no se pudo actualizar");
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Administrador actualizado exitosamente");
            
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
     * Elimina un administrador
     * DELETE /api/admins/{id}
     */
    public void deleteAdmin(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            boolean deleted = adminService.deleteAdmin(id);
            
            if (!deleted) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Administrador no encontrado");
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Administrador eliminado exitosamente");
            
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
     * Busca un administrador por email
     * GET /api/admins/search?email=valor
     */
    public void searchAdminByEmail(Context ctx) {
        try {
            String email = ctx.queryParam("email");
            
            if (email == null || email.trim().isEmpty()) {
                handleError(ctx, HttpStatus.BAD_REQUEST, "El parámetro 'email' es requerido");
                return;
            }
            
            Administrador admin = adminService.getAdminByEmail(email.trim());
            
            if (admin == null) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Administrador no encontrado");
                return;
            }
            
            // Ocultar información sensible
            admin.setPassword_hash(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", admin);
            
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
     * Cambia el nivel de permiso de un administrador
     * PATCH /api/admins/{id}/level
     */
    public void changeAdminLevel(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            
            // Parsear el request para obtener el nuevo nivel
            @SuppressWarnings("unchecked")
            Map<String, String> requestBody = ctx.bodyAsClass(Map.class);
            String nuevoNivel = requestBody.get("nivel_permiso");
            
            if (nuevoNivel == null || nuevoNivel.trim().isEmpty()) {
                handleError(ctx, HttpStatus.BAD_REQUEST, "El campo 'nivel_permiso' es requerido");
                return;
            }
            
            Administrador.NivelPermiso nivel = Administrador.NivelPermiso.fromValor(nuevoNivel.trim());
            
            boolean updated = adminService.changeAdminLevel(id, nivel);
            
            if (!updated) {
                handleError(ctx, HttpStatus.NOT_FOUND, "Administrador no encontrado");
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Nivel de permiso actualizado exitosamente");
            
            ctx.status(HttpStatus.OK).json(response);
            
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + e.getMessage());
        }
    }

    public void getAdminStats(Context ctx) {
        try {
            int totalAdmins = adminService.getTotalAdmins();
            boolean hasSuperAdmin = adminService.hasSuperAdmin();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_administradores", totalAdmins);
            stats.put("tiene_super_admin", hasSuperAdmin);
            
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

    private void handleError(Context ctx, HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        ctx.status(status).json(errorResponse);
    }
}
