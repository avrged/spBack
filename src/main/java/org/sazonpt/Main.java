package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.sazonpt.di.AppModule;
import org.sazonpt.util.ImprovedStaticFileHandler;

public class Main {
    public static void main(String[] args) {
        configureJettyLogging();
        
        ImprovedStaticFileHandler.initializeUploadDirectories();
        
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
            
            config.http.maxRequestSize = 50_000_000L;
            config.http.defaultContentType = "application/json";
            
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads";
                staticFiles.directory = "./uploads";
                staticFiles.location = Location.EXTERNAL;
                staticFiles.precompress = false;
            });
        }).start("0.0.0.0", 7070);
        
        app.before(ctx -> {
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

        app.get("/", ctx -> ctx.result("API Sazón Patrimonial - Backend funcionando correctamente"));
        AppModule.initUsuario().register(app);
        AppModule.initAdministrador().register(app);
        AppModule.initRestaurantero().register(app);
        AppModule.initSolicitudRegistro().register(app);
        AppModule.initSolicitudRegistroEstado().register(app);
        AppModule.initRevisionSolicitud().register(app);
        AppModule.initRestaurante().register(app);
        AppModule.initImagen().register(app);
        AppModule.initComprobante().register(app);
        AppModule.initMenu().register(app);
        AppModule.initRegistroRestaurante().registerRoutes(app);
        AppModule.initFileUpload().register(app);
        AppModule.initDescarga().register(app);
        AppModule.initZona().register(app);
        app.get("/uploads/*", ImprovedStaticFileHandler::handleStaticFile);
    }

    private static void configureJettyLogging() {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        
        System.setProperty("org.eclipse.jetty.server.Request.LEVEL", "ERROR");
        System.setProperty("org.eclipse.jetty.server.MultiPartFormInputStream.LEVEL", "ERROR");
        
        System.setProperty("org.eclipse.jetty.util.log.announce", "false");
        System.setProperty("org.eclipse.jetty.util.log.stderr.SOURCE", "false");
        
        String tempDir = System.getProperty("user.dir") + "/temp";
        System.setProperty("java.io.tmpdir", tempDir);
        
        java.io.File tempDirFile = new java.io.File(tempDir);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }
    }
}