package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import org.sazonpt.di.AppModule;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            // Configurar límites para archivos (en bytes)
            config.http.maxRequestSize = 50_000_000L; // 50MB máximo para toda la petición

            // Configurar archivos estáticos para servir las imágenes y documentos
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads";
                staticFiles.directory = "./uploads";
                staticFiles.location = Location.EXTERNAL;
            });
        }).start(7070);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Origin, X-Requested-With");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        app.options("/*", ctx -> {
            ctx.status(200);
        });

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
    }
}