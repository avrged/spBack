package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import org.sazonpt.di.AppModule;
import org.sazonpt.util.StaticFileHandler;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.http.maxRequestSize = 50_000_000L;
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads";
                staticFiles.directory = "./uploads";
                staticFiles.location = Location.EXTERNAL;
            });
        }).start(7070);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Origin, X-Requested-With, X-User-Email, X-User-ID");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });
        

        app.options("/*", ctx -> {
            ctx.status(200);
        });

        app.get("/", ctx -> ctx.result("API - Catalogo de Restaurantes"));

        app.get("/uploads/*", StaticFileHandler::handleStaticFile);

        AppModule.initAdmin().registerRoutes(app);
        AppModule.initUser().registerRoutes(app);
        // Eliminada la línea de rutas de restaurantero
        AppModule.initMenu().registerRoutes(app);
        AppModule.initDescarga().registerRoutes(app);
        AppModule.initSolicitudRegistro().registerRoutes(app);
        AppModule.initAdquirirMembresia().registerRoutes(app);
    }
}