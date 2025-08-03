package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.sazonpt.di.AppModule;
import org.sazonpt.util.ImprovedStaticFileHandler;

public class Main {
    public static void main(String[] args) {
        // Configuración de Jetty antes de inicializar Javalin
        configureJettyLogging();
        
        // Inicializar directorios de upload
        ImprovedStaticFileHandler.initializeUploadDirectories();
        
        Javalin app = Javalin.create(config -> {
            // Configuración de CORS
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
            
            // Configuración mejorada de archivos estáticos y multipart
            config.http.maxRequestSize = 50_000_000L;
            config.http.defaultContentType = "application/json";
            
            // Configuración de archivos estáticos - método template
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads";
                staticFiles.directory = "./uploads";
                staticFiles.location = Location.EXTERNAL;
                staticFiles.precompress = false; // Evita problemas con archivos temporales
            });
        }).start("0.0.0.0", 7070);
        
        app.before(ctx -> {
            // Headers CORS mejorados siguiendo templates
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            ctx.header("Access-Control-Allow-Headers", 
                "Content-Type, Authorization, Accept, Origin, X-Requested-With, " +
                "X-User-Email, X-User-ID, Cache-Control, Pragma, Expires");
            ctx.header("Access-Control-Allow-Credentials", "true");
            ctx.header("Access-Control-Max-Age", "3600");
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
        
        // Registrar rutas de file upload
        AppModule.initFileUpload().register(app);

        // Manejo de archivos estáticos mejorado
        app.get("/uploads/*", ImprovedStaticFileHandler::handleStaticFile);
    }
    
    /**
     * Configuración mejorada de logging de Jetty basada en templates
     */
    private static void configureJettyLogging() {
        // Configuración básica de Jetty
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        
        // Configuraciones específicas para multipart (basado en templates)
        System.setProperty("org.eclipse.jetty.server.Request.LEVEL", "ERROR");
        System.setProperty("org.eclipse.jetty.server.MultiPartFormInputStream.LEVEL", "ERROR");
        
        // Configuraciones de rendimiento
        System.setProperty("org.eclipse.jetty.util.log.announce", "false");
        System.setProperty("org.eclipse.jetty.util.log.stderr.SOURCE", "false");
        
        // Configuración de directorio temporal personalizado
        String tempDir = System.getProperty("user.dir") + "/temp";
        System.setProperty("java.io.tmpdir", tempDir);
        
        // Crear directorio temporal si no existe
        java.io.File tempDirFile = new java.io.File(tempDir);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }
    }
}