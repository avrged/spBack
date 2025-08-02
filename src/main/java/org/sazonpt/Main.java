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
        }).start("0.0.0.0",7070);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Origin, X-Requested-With, X-User-Email, X-User-ID");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        app.options("/*", ctx -> {
            ctx.status(200);
        });

        app.get("/", ctx -> ctx.result("API - Hola mundo"));

        app.get("/uploads/*", StaticFileHandler::handleStaticFile);

        // Inicializar y registrar rutas de los módulos
        AppModule.initAdmin().registerRoutes(app);
        AppModule.initUser().registerRoutes(app);
        AppModule.initRestaurante().registerRoutes(app);
        AppModule.initSolicitudRegistro().registerRoutes(app);
        AppModule.initImagenRestaurante().registerRoutes(app);
    }
}