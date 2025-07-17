package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Menu;
import org.sazonpt.service.MenuService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public void getAll(Context ctx) {
        try {
            List<Menu> menus = menuService.getAllMenus();
            ctx.json(menus);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener menús");
        }
    }

    public void getById(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("id"));
            Menu menu = menuService.getById(idMenu);
            
            if (menu != null) {
                ctx.json(menu);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Menú no encontrado");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener menú");
        }
    }

    public void getByRestaurant(Context ctx) {
        try {
            int codigoRestaurante = Integer.parseInt(ctx.pathParam("restaurante"));
            List<Menu> menus = menuService.getMenusByRestaurant(codigoRestaurante);
            ctx.json(menus);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener menús del restaurante");
        }
    }

    public void create(Context ctx) {
        try {
            Menu menu = ctx.bodyAsClass(Menu.class);
            menuService.createMenu(menu);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Menú creado correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear menú: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("id"));
            Menu menu = ctx.bodyAsClass(Menu.class);
            // Asegurarse de que el ID del menú coincida con el parámetro de la URL
            Menu updatedMenu = new Menu(idMenu, menu.getCodigo_restaurante(), menu.getRuta_archivo(), menu.getEstado());
            menuService.updateMenu(updatedMenu);
            ctx.status(200).result("Menú actualizado");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar menú: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete menu with ID: " + idMenu);
            menuService.deleteMenu(idMenu);
            ctx.status(200).result("Menú eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid menu ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de menú inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting menu: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting menu: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }
}
