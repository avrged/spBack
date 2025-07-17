package org.sazonpt.routes;

import org.sazonpt.controller.MenuController;

import io.javalin.Javalin;

public class MenuRoutes {
    private final MenuController menuController;

    public MenuRoutes(MenuController menuController) {
        this.menuController = menuController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/menus", menuController::getAll);
        app.post("/menus", menuController::create);
        app.get("/menus/{id}", menuController::getById);
        app.put("/menus/{id}", menuController::update);
        app.delete("/menus/{id}", menuController::delete);
        app.get("/menus/restaurante/{restaurante}", menuController::getByRestaurant);
    }
}
