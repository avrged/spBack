package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class FileUploadController {
    
    private static final String UPLOADS_BASE_PATH = "./uploads/";
    
    public void uploadFile(Context ctx) {
        try {
            UploadedFile uploadedFile = ctx.uploadedFile("file");
            
            // Obtener tipo desde formParam o desde attribute (rutas específicas)
            String type = ctx.formParam("type");
            if (type == null || type.trim().isEmpty()) {
                type = ctx.attribute("predefined-type");
            }
            
            if (uploadedFile == null) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "No se recibió ningún archivo"
                ));
                return;
            }
            
            if (type == null || type.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "El parámetro 'type' es requerido (image, document, menu)"
                ));
                return;
            }
            
            // Validar tipo de archivo
            String originalName = uploadedFile.filename();
            if (originalName == null || originalName.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "El archivo debe tener un nombre válido"
                ));
                return;
            }
            
            // Obtener extensión
            String extension = "";
            int lastDotIndex = originalName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = originalName.substring(lastDotIndex);
            }
            
            // Validar extensiones según tipo
            if (!isValidFileType(type, extension)) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "Tipo de archivo no válido para la categoría: " + type
                ));
                return;
            }
            
            // Generar nombre único
            String uniqueName = UUID.randomUUID().toString() + extension;
            
            // Determinar carpeta según tipo
            String folder = switch (type.toLowerCase()) {
                case "image" -> "images";
                case "document" -> "documents";
                case "menu" -> "menus";
                default -> {
                    ctx.status(400).json(Map.of(
                        "success", false, 
                        "message", "Tipo no válido. Use: image, document o menu"
                    ));
                    yield null;
                }
            };
            
            if (folder == null) return;
            
            // Crear directorio si no existe
            File dir = new File(UPLOADS_BASE_PATH + folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Guardar archivo
            File file = new File(dir, uniqueName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(uploadedFile.content().readAllBytes());
            }
            
            // Devolver ruta relativa
            String relativePath = folder + "/" + uniqueName;
            
            // Construir URL completa (usando variable de entorno si existe)
            String staticIp = System.getenv("STATIC_IP");
            if (staticIp == null) {
                staticIp = "localhost:7070"; // Fallback para desarrollo local
            }
            String fullUrl = "http://" + staticIp + "/uploads/" + relativePath;
            
            ctx.json(Map.of(
                "success", true,
                "message", "Archivo subido exitosamente",
                "data", Map.of(
                    "filename", uniqueName,
                    "originalName", originalName,
                    "path", relativePath,
                    "url", fullUrl,
                    "type", type,
                    "size", uploadedFile.size()
                )
            ));
            
        } catch (IOException e) {
            ctx.status(500).json(Map.of(
                "success", false, 
                "message", "Error al subir archivo: " + e.getMessage()
            ));
        }
    }
    
    public void uploadMultipleFiles(Context ctx) {
        try {
            var uploadedFiles = ctx.uploadedFiles("files");
            String type = ctx.formParam("type");
            
            if (uploadedFiles.isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "No se recibieron archivos"
                ));
                return;
            }
            
            if (type == null || type.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false, 
                    "message", "El parámetro 'type' es requerido"
                ));
                return;
            }
            
            var results = uploadedFiles.stream().map(uploadedFile -> {
                try {
                    String originalName = uploadedFile.filename();
                    String extension = "";
                    int lastDotIndex = originalName.lastIndexOf('.');
                    if (lastDotIndex > 0) {
                        extension = originalName.substring(lastDotIndex);
                    }
                    
                    if (!isValidFileType(type, extension)) {
                        return Map.of(
                            "success", false,
                            "filename", originalName,
                            "message", "Tipo de archivo no válido"
                        );
                    }
                    
                    String uniqueName = UUID.randomUUID().toString() + extension;
                    String folder = switch (type.toLowerCase()) {
                        case "image" -> "images";
                        case "document" -> "documents";
                        case "menu" -> "menus";
                        default -> null;
                    };
                    
                    if (folder == null) {
                        return Map.of(
                            "success", false,
                            "filename", originalName,
                            "message", "Tipo no válido"
                        );
                    }
                    
                    File dir = new File(UPLOADS_BASE_PATH + folder);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    
                    File file = new File(dir, uniqueName);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(uploadedFile.content().readAllBytes());
                    }
                    
                    String relativePath = folder + "/" + uniqueName;
                    String staticIp = System.getenv("STATIC_IP");
                    if (staticIp == null) {
                        staticIp = "localhost:7070";
                    }
                    String fullUrl = "http://" + staticIp + "/uploads/" + relativePath;
                    
                    return Map.of(
                        "success", true,
                        "filename", uniqueName,
                        "originalName", originalName,
                        "path", relativePath,
                        "url", fullUrl,
                        "size", uploadedFile.size()
                    );
                    
                } catch (IOException e) {
                    return Map.of(
                        "success", false,
                        "filename", uploadedFile.filename(),
                        "message", "Error al procesar archivo: " + e.getMessage()
                    );
                }
            }).toList();
            
            ctx.json(Map.of(
                "success", true,
                "message", "Archivos procesados",
                "results", results
            ));
            
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false, 
                "message", "Error al procesar archivos: " + e.getMessage()
            ));
        }
    }
    
    private boolean isValidFileType(String type, String extension) {
        extension = extension.toLowerCase();
        
        return switch (type.toLowerCase()) {
            case "image" -> extension.matches("\\.(jpg|jpeg|png|gif|webp)");
            case "document" -> extension.matches("\\.(pdf|doc|docx|txt)");
            case "menu" -> extension.matches("\\.(pdf|jpg|jpeg|png)");
            default -> false;
        };
    }
}
