package org.sazonpt.routes;

import io.javalin.Javalin;
import org.sazonpt.controller.MenuController;

public class MenuRoutes {
    
    private final MenuController menuController;

    public MenuRoutes(MenuController menuController) {
        this.menuController = menuController;
    }

    public void register(Javalin app) {
        app.get("/menus", menuController::obtenerTodosLosMenus);

        app.get("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenuPorId);
        
        app.get("/menus/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenusPorRestaurante);

        app.get("/menus/restaurantero/{idRestaurantero}", 
               menuController::obtenerMenusPorRestaurantero);

        app.put("/menus/restaurantero/{idRestaurantero}", 
               menuController::actualizarMenuPorRestaurantero);

        app.get("/menus/estado/{estado}", 
               menuController::obtenerMenusPorEstado);

        app.get("/menus/activo/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::obtenerMenuActivoPorRestaurante);

        app.get("/menus/estados", 
               menuController::obtenerEstadosMenu);

        app.post("/menus", menuController::crearMenu);

        app.post("/menus/restaurante/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                menuController::crearMenuParaRestaurante);

        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
               menuController::actualizarMenu);

        app.put("/menus/{idMenu}", 
               menuController::actualizarMenuSimplificado);

        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/estado", 
               menuController::cambiarEstadoMenu);

        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/activar", 
               menuController::activarMenu);

        app.put("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}/desactivar", 
               menuController::desactivarMenu);

        app.delete("/menus/{idMenu}/{idRestaurante}/{idSolicitud}/{idRestaurantero}", 
                  menuController::eliminarMenu);
    }
}
