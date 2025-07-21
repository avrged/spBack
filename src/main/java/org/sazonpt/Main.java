package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.io.File;
import java.nio.file.Files;

import org.sazonpt.di.AppModule;

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
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Origin, X-Requested-With");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        app.options("/*", ctx -> {
            ctx.status(200);
        });

        app.get("/", ctx -> ctx.result("API - Catalogo de Restaurantes"));
        
        // Ruta manual para servir archivos estáticos
        app.get("/uploads/*", ctx -> {
            try {
                // Obtener la ruta del archivo y decodificar URL
                String requestPath = ctx.path();
                String decodedPath = java.net.URLDecoder.decode(requestPath, "UTF-8");
                String filePath = decodedPath.substring("/uploads/".length());

                // Crear archivo con la ruta decodificada
                File file = new File("uploads/" + filePath);

                if (file.exists() && file.isFile()) {
                    String contentType = getContentType(filePath);
                    ctx.contentType(contentType);
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    ctx.result(fileBytes);
                } else {
                    ctx.status(404).result("Archivo no encontrado");
                }
            } catch (Exception e) {
                System.err.println("Error sirviendo archivo: " + e.getMessage());
                ctx.status(500).result("Error interno del servidor");
            }
        });

        // Registrar rutas de módulos DESPUÉS de la ruta de archivos estáticos
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

    private static String getContentType(String fileName) {
        // Normalizar el nombre del archivo (decodificar URL y limpiar)
        try {
            fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
        } catch (Exception e) {
            // Si no se puede decodificar, usar el nombre original
        }

        // Convertir a minúsculas para la comparación
        String lowerFileName = fileName.toLowerCase();

        // Detectar extensión de manera más robusta
        String extension = "";
        int lastDotIndex = lowerFileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < lowerFileName.length() - 1) {
            extension = lowerFileName.substring(lastDotIndex + 1);
        }

        // Mapeo de extensiones a content types
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default:
                // Si el nombre contiene "pdf" en cualquier parte, asumir que es PDF
                if (lowerFileName.contains("pdf")) {
                    return "application/pdf";
                }
                // Si contiene extensiones de imagen conocidas, asumir imagen
                if (lowerFileName.contains("jpg") || lowerFileName.contains("jpeg") ||
                    lowerFileName.contains("png") || lowerFileName.contains("gif")) {
                    return "image/jpeg"; // Por defecto JPEG para imágenes
                }
                return "application/octet-stream";
        }
    }
}