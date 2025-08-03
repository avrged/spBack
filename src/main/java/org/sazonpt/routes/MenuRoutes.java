package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.MenuController;

public class MenuRoutes {
    
    private final MenuController menuController;

    public MenuRoutes(MenuController menuController) {
        this.menuController = menuController;
    }

    public void register(Javalin app) {
        // Rutas para obtener menús
        app.get("/menus", menuController::obtenerTodosLosMenus);
        
        // Obtener menú específico por clave primaria compuesta
        app.get("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenuPorId);
        
        // Obtener menús por restaurante
        app.get("/menus/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenusPorRestaurante);
        
        // Obtener menús por restaurantero
        app.get("/menus/restaurantero/{idRestaurantero}", 
               menuController::obtenerMenusPorRestaurantero);
        
        // Obtener menús por estado
        app.get("/menus/estado/{estado}", 
               menuController::obtenerMenusPorEstado);
        
        // Obtener menú activo de un restaurante
        app.get("/menus/activo/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenuActivoPorRestaurante);
        
        // Obtener estados de menú disponibles
        app.get("/menus/estados", 
               menuController::obtenerEstadosMenu);
        
        // Crear nuevo menú
        app.post("/menus", menuController::crearMenu);
        
        // Crear menú específico para un restaurante
        app.post("/menus/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                menuController::crearMenuParaRestaurante);
        
        // Actualizar menú
        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::actualizarMenu);
        
        // Cambiar estado del menú
        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/estado", 
               menuController::cambiarEstadoMenu);
        
        // Activar menú
        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/activar", 
               menuController::activarMenu);
        
        // Desactivar menú
        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/desactivar", 
               menuController::desactivarMenu);
        
        // Eliminar menú
        app.delete("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  menuController::eliminarMenu);
    }
}
