package org.sazonpt.util;

import io.javalin.http.Context;
import java.io.File;
import java.nio.file.Files;

public class StaticFileHandler {

    public static void handleStaticFile(Context ctx) {
        try {
            // Obtener la ruta del archivo y decodificar URL
            String requestPath = ctx.path();
            String decodedPath = java.net.URLDecoder.decode(requestPath, "UTF-8");
            String filePath = decodedPath.substring("/uploads/".length());

            // Crear archivo con la ruta decodificada
            File file = new File("uploads/" + filePath);

            if (file.exists() && file.isFile()) {
                String contentType = detectContentType(filePath);
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
    }

    private static String detectContentType(String fileName) {
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
