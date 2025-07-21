package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import org.sazonpt.di.AppModule;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.allowHost("http://localhost:5501", "http://localhost:63342", "http://localhost:5500", "http://127.0.0.1:5501");
                    it.allowCredentials = true;
                });
            });
            config.staticFiles.add("./uploads", Location.EXTERNAL);
        }).start(7070);

        app.get("/", ctx -> ctx.result("API - Catalogo de Restaurantes"));
        
        AppModule.initAdmin().registerRoutes(app);
        AppModule.initUser().registerRoutes(app);
        AppModule.initRestaurantero().registerRoutes(app);
        AppModule.initMenu().registerRoutes(app);
        AppModule.initZona().registerRoutes(app);
        AppModule.initRestaurante().registerRoutes(app);
        AppModule.initDescarga().registerRoutes(app);
        AppModule.initSolicitudRegistro().registerRoutes(app);
        AppModule.initAdquirirMembresia().registerRoutes(app);
        AppModule.initRevisionSolicitud().registerRoutes(app);

        app.options("/*", ctx -> {
            ctx.status(200);
        });
    }
}