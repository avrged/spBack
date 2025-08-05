package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.sazonpt.model.dto.RegistroRestauranteDTO;
import org.sazonpt.service.RegistroRestauranteService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class RegistroRestauranteController {
    
    private final RegistroRestauranteService registroService;
    
    public RegistroRestauranteController(RegistroRestauranteService registroService) {
        this.registroService = registroService;
    }
    
    public void registrarRestaurante(Context ctx) {
        try {
            RegistroRestauranteDTO datos = extraerDatos(ctx);
            
            String validacion = validarDatos(datos);
            if (validacion != null) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Datos inválidos: " + validacion
                ));
                return;
            }
            
            String resultado = registroService.registrarRestaurante(datos);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "message", "Restaurante registrado exitosamente",
                "data", resultado
            ));
            
        } catch (SQLException e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al registrar restaurante: " + e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "Error en los datos enviados: " + e.getMessage()
            ));
        }
    }
    
    public void obtenerEstadoSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            String estado = registroService.obtenerEstadoSolicitud(idSolicitud);
            
            ctx.json(Map.of(
                "success", true,
                "data", Map.of("estado", estado)
            ));
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de solicitud inválido"
            ));
        } catch (SQLException e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al consultar estado: " + e.getMessage()
            ));
        }
    }
    
    public void obtenerSolicitudesRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("id"));
            var solicitudes = registroService.obtenerSolicitudesRestaurantero(idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", solicitudes
            ));
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (SQLException e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al consultar solicitudes: " + e.getMessage()
            ));
        }
    }
    
    private RegistroRestauranteDTO extraerDatos(Context ctx) {
        String contentType = ctx.header("Content-Type");
        
        if (contentType != null && contentType.contains("multipart/form-data")) {
            return extraerDatosConArchivos(ctx);
        }
        
        if (contentType != null && contentType.contains("application/json")) {
            return ctx.bodyAsClass(RegistroRestauranteDTO.class);
        }

        return extraerDatosFormulario(ctx);
    }
    
    private RegistroRestauranteDTO extraerDatosFormulario(Context ctx) {
        RegistroRestauranteDTO datos = new RegistroRestauranteDTO();

        datos.setNombreRestaurante(ctx.formParam("nombreRestaurante"));
        datos.setPropietario(ctx.formParam("propietario"));
        datos.setCorreoElectronico(ctx.formParam("correoElectronico"));
        datos.setNumeroCelular(ctx.formParam("numeroCelular"));
        datos.setFacebook(ctx.formParam("facebook"));
        datos.setInstagram(ctx.formParam("instagram"));
        datos.setDireccion(ctx.formParam("direccion"));
        datos.setHorarios(ctx.formParam("horarios"));
        
        datos.setImagenPrincipal(ctx.formParam("imagenPrincipal"));
        datos.setImagenSecundaria(ctx.formParam("imagenSecundaria"));
        datos.setImagenPlatillo(ctx.formParam("imagenPlatillo"));
        datos.setComprobanteDomicilio(ctx.formParam("comprobanteDomicilio"));
        datos.setMenuRestaurante(ctx.formParam("menuRestaurante"));
        
        String idRestaurantero = ctx.formParam("idRestaurantero");
        if (idRestaurantero != null) {
            datos.setIdRestaurantero(Integer.parseInt(idRestaurantero));
        }
        
        return datos;
    }
    
    private RegistroRestauranteDTO extraerDatosConArchivos(Context ctx) {
        RegistroRestauranteDTO datos = new RegistroRestauranteDTO();
        
        datos.setNombreRestaurante(ctx.formParam("nombreRestaurante"));
        datos.setPropietario(ctx.formParam("propietario"));
        datos.setCorreoElectronico(ctx.formParam("correoElectronico"));
        datos.setNumeroCelular(ctx.formParam("numeroCelular"));
        datos.setFacebook(ctx.formParam("facebook"));
        datos.setInstagram(ctx.formParam("instagram"));
        datos.setDireccion(ctx.formParam("direccion"));
        datos.setHorarios(ctx.formParam("horarios"));
        
        String idRestaurantero = ctx.formParam("idRestaurantero");
        if (idRestaurantero != null && !idRestaurantero.trim().isEmpty()) {
            datos.setIdRestaurantero(Integer.parseInt(idRestaurantero));
        }
        
        datos.setImagenPrincipal(procesarArchivo(ctx, "imagenPrincipal", "images"));
        datos.setImagenSecundaria(procesarArchivo(ctx, "imagenSecundaria", "images"));
        datos.setImagenPlatillo(procesarArchivo(ctx, "imagenPlatillo", "images"));
        datos.setComprobanteDomicilio(procesarArchivo(ctx, "comprobanteDomicilio", "documents"));
        datos.setMenuRestaurante(procesarArchivo(ctx, "menuRestaurante", "menus"));
        
        return datos;
    }
    
    private String procesarArchivo(Context ctx, String fieldName, String folder) {
        UploadedFile uploadedFile = ctx.uploadedFile(fieldName);
        
        if (uploadedFile == null) {
            return null;
        }
        
        try {
            String originalName = uploadedFile.filename();
            if (originalName == null || originalName.trim().isEmpty()) {
                throw new RuntimeException("El archivo " + fieldName + " debe tener un nombre válido");
            }
            
            String extension = "";
            int lastDotIndex = originalName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = originalName.substring(lastDotIndex);
            }
            
            if (!esExtensionValida(folder, extension)) {
                throw new RuntimeException("Tipo de archivo no válido para " + fieldName + ": " + extension);
            }
            
            String uniqueName = UUID.randomUUID().toString() + extension;
            
            File dir = new File("./uploads/" + folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            File file = new File(dir, uniqueName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(uploadedFile.content().readAllBytes());
            }
            
            return uniqueName;
            
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar archivo " + fieldName + ": " + e.getMessage());
        }
    }
    
    private boolean esExtensionValida(String folder, String extension) {
        extension = extension.toLowerCase();
        
        return switch (folder) {
            case "images" -> extension.matches("\\.(jpg|jpeg|png|gif|webp)");
            case "documents" -> extension.matches("\\.(pdf|doc|docx|txt)");
            case "menus" -> extension.matches("\\.(pdf|jpg|jpeg|png)");
            default -> false;
        };
    }
    
    private String validarDatos(RegistroRestauranteDTO datos) {
        
        if (datos.getNombreRestaurante() == null || datos.getNombreRestaurante().trim().isEmpty()) {
            return "Nombre del restaurante es obligatorio";
        }
        if (datos.getPropietario() == null || datos.getPropietario().trim().isEmpty()) {
            return "Nombre del propietario es obligatorio";
        }
        if (datos.getCorreoElectronico() == null || datos.getCorreoElectronico().trim().isEmpty()) {
            return "Correo electrónico es obligatorio";
        }
        if (datos.getNumeroCelular() == null || datos.getNumeroCelular().trim().isEmpty()) {
            return "Número de celular es obligatorio";
        }
        if (datos.getDireccion() == null || datos.getDireccion().trim().isEmpty()) {
            return "Dirección es obligatoria";
        }
        if (datos.getHorarios() == null || datos.getHorarios().trim().isEmpty()) {
            return "Horarios son obligatorios";
        }
        if (datos.getIdRestaurantero() <= 0) {
            return "ID de restaurantero inválido";
        }
        
        if ((datos.getImagenPrincipal() == null || datos.getImagenPrincipal().trim().isEmpty()) &&
            (datos.getImagenSecundaria() == null || datos.getImagenSecundaria().trim().isEmpty()) &&
            (datos.getImagenPlatillo() == null || datos.getImagenPlatillo().trim().isEmpty())) {
            return "Al menos una imagen es obligatoria";
        }
        
        return null;
    }
}
