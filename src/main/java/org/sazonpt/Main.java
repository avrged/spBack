package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.sazonpt.di.AppModule;
import org.sazonpt.util.StaticFileHandler;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            // Configuración de CORS
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
            
            // Configuración de archivos estáticos
            config.http.maxRequestSize = 50_000_000L;
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads";
                staticFiles.directory = "./uploads";
                staticFiles.location = Location.EXTERNAL;
            });
        }).start("0.0.0.0", 7070);
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Origin, X-Requested-With, X-User-Email, X-User-ID");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        app.options("/*", ctx -> {
            ctx.status(200);
        });

        // Ruta raíz
        app.get("/", ctx -> ctx.result("API Sazón Patrimonial - Backend funcionando correctamente"));

        // Registrar rutas de usuario
        AppModule.initUsuario().register(app);
        
        // Registrar rutas de administrador
        AppModule.initAdministrador().register(app);
        
        // Registrar rutas de restaurantero
        AppModule.initRestaurantero().register(app);
        
        // Registrar rutas de solicitudes de registro
        AppModule.initSolicitudRegistro().register(app);
        
        // Registrar rutas de revisiones de solicitud
        AppModule.initRevisionSolicitud().register(app);
        
        // Registrar rutas de restaurantes
        AppModule.initRestaurante().register(app);
        
        // Registrar rutas de imágenes
        AppModule.initImagen().register(app);
        
        // Registrar rutas de comprobantes
        AppModule.initComprobante().register(app);
        
        // Registrar rutas de menús
        AppModule.initMenu().register(app);
        
        // Registrar rutas de registro de restaurante
        AppModule.initRegistroRestaurante().registerRoutes(app);

        // Manejo de archivos estáticos
        app.get("/uploads/*", StaticFileHandler::handleStaticFile);
    }
}