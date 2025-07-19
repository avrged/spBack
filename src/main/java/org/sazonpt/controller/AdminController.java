package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.sazonpt.model.Administrador;
import org.sazonpt.service.AdminService;

import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void getAll(Context ctx) {
        try {
            List<Administrador> admins = adminService.getAllAdmins();
            ctx.json(admins);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener administradores");
        }
    }

    public void getAdminById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Administrador admin = adminService.getByIdAdmin(id);
            if (admin != null) {
                ctx.json(admin);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Administrador no encontrado");
            }
        } catch (Exception e) {
            ctx.status(404).result("Error al obtener administrador");
        }
    }

    public void create(Context ctx) {
        try {
            Administrador admin = ctx.bodyAsClass(Administrador.class);
            adminService.createAdmin(admin);
            ctx.status(201).result("Administrador creado");
        } catch (Exception e) {
            ctx.status(400).result("Error al crear administrador");
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Administrador admin = ctx.bodyAsClass(Administrador.class);
            admin.setIdUsuario(id);
            adminService.updateAdmin(admin);
            ctx.status(200).result("Administrador actualizado");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar administrador");
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Intentando eliminar el administrador con Id: " + id);
            adminService.deleteAdmin(id);
            ctx.status(200).result("Administrador eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("formato de Id invalido: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de administrador inválido");
        } catch (SQLException e) {
            System.out.println("Error de SQL al eliminar el Id: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al borrar el administrador: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
