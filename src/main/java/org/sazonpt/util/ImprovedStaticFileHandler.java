package org.sazonpt.util;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImprovedStaticFileHandler {
    
    private static final String UPLOADS_DIR = "./uploads";
    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    
    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("webp", "image/webp");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
    }

    public static void handleStaticFile(Context ctx) {
        try {
            String requestPath = ctx.path();
            String filePath = requestPath.replace("/uploads/", "");

            if (filePath.contains("..") || filePath.contains("~")) {
                ctx.status(HttpStatus.FORBIDDEN);
                ctx.result("Acceso denegado");
                return;
            }
            
            Path fullPath = Paths.get(UPLOADS_DIR, filePath);
            File file = fullPath.toFile();

            if (!file.exists() || !file.isFile()) {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.result("Archivo no encontrado");
                return;
            }

            String canonicalFilePath = file.getCanonicalPath();
            String canonicalUploadsPath = new File(UPLOADS_DIR).getCanonicalPath();
            
            if (!canonicalFilePath.startsWith(canonicalUploadsPath)) {
                ctx.status(HttpStatus.FORBIDDEN);
                ctx.result("Acceso denegado");
                return;
            }

            String extension = getFileExtension(filePath);
            String mimeType = MIME_TYPES.getOrDefault(extension.toLowerCase(), "application/octet-stream");

            ctx.contentType(mimeType);
            ctx.header("Cache-Control", "public, max-age=31536000"); // Cache por 1 año
            ctx.header("Content-Length", String.valueOf(file.length()));

            ctx.result(Files.readAllBytes(fullPath));
            
        } catch (IOException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.result("Error interno del servidor");
            System.err.println("Error al servir archivo estático: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.result("Error interno del servidor");
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex + 1);
    }

    public static void ensureDirectoryExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directorio creado: " + dirPath);
            } else {
                System.err.println("No se pudo crear el directorio: " + dirPath);
            }
        }
    }

    public static void initializeUploadDirectories() {
        ensureDirectoryExists(UPLOADS_DIR);
        ensureDirectoryExists(UPLOADS_DIR + "/images");
        ensureDirectoryExists(UPLOADS_DIR + "/documents");
        ensureDirectoryExists(UPLOADS_DIR + "/menus");
        
        System.out.println("Directorios de upload inicializados correctamente");
    }
}
