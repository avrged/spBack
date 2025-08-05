package org.sazonpt.controller;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.sazonpt.model.Comprobante;
import org.sazonpt.service.ComprobanteService;
import org.sazonpt.util.ImprovedStaticFileHandler;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComprobanteController {
    
    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    public void obtenerTodosLosComprobantes(Context ctx) {
        try {
            List<Comprobante> comprobantes = comprobanteService.obtenerTodosLosComprobantes();
            ctx.json(Map.of(
                "success", true,
                "data", comprobantes,
                "message", "Comprobantes obtenidos correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los comprobantes: " + e.getMessage()
            ));
        }
    }

    public void obtenerComprobantePorId(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("idComprobante"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Optional<Comprobante> comprobanteOpt = comprobanteService.obtenerComprobantePorId(
                idComprobante, idRestaurante, idSolicitud, idRestaurantero);
            
            if (comprobanteOpt.isPresent()) {
                ctx.json(Map.of(
                    "success", true,
                    "data", comprobanteOpt.get(),
                    "message", "Comprobante encontrado"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Comprobante no encontrado"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener el comprobante: " + e.getMessage()
            ));
        }
    }

    public void obtenerComprobantesPorRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Comprobante> comprobantes = comprobanteService.obtenerComprobantesPorRestaurante(
                idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", comprobantes,
                "message", "Comprobantes del restaurante obtenidos correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los comprobantes del restaurante: " + e.getMessage()
            ));
        }
    }

    public void obtenerComprobantesPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Comprobante> comprobantes = comprobanteService.obtenerComprobantesPorRestaurantero(idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", comprobantes,
                "message", "Comprobantes del restaurantero obtenidos correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los comprobantes del restaurantero: " + e.getMessage()
            ));
        }
    }

    public void obtenerComprobantesPorTipo(Context ctx) {
        try {
            String tipo = ctx.pathParam("tipo");

            List<Comprobante> comprobantes = comprobanteService.obtenerComprobantesPorTipo(tipo);
            
            ctx.json(Map.of(
                "success", true,
                "data", comprobantes,
                "message", "Comprobantes del tipo '" + tipo + "' obtenidos correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los comprobantes por tipo: " + e.getMessage()
            ));
        }
    }

    public void crearComprobante(Context ctx) {
        try {
            Comprobante comprobante = ctx.bodyAsClass(Comprobante.class);
            Comprobante comprobanteCreado = comprobanteService.crearComprobante(comprobante);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", comprobanteCreado,
                "message", "Comprobante creado correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear el comprobante: " + e.getMessage()
            ));
        }
    }

    public void crearComprobanteParaRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            String tipo = (String) requestBody.get("tipo");
            String rutaArchivo = (String) requestBody.get("ruta_archivo");

            if (tipo == null || tipo.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "El tipo de comprobante es obligatorio"
                ));
                return;
            }

            if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "La ruta del archivo es obligatoria"
                ));
                return;
            }

            Comprobante comprobanteCreado = comprobanteService.crearComprobanteParaRestaurante(
                tipo, rutaArchivo, idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", comprobanteCreado,
                "message", "Comprobante creado para el restaurante correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear el comprobante: " + e.getMessage()
            ));
        }
    }

    public void actualizarComprobante(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("idComprobante"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            Comprobante comprobanteActualizado = ctx.bodyAsClass(Comprobante.class);
            
            boolean actualizado = comprobanteService.actualizarComprobante(idComprobante, idRestaurante, 
                idSolicitud, idRestaurantero, comprobanteActualizado);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Comprobante actualizado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar el comprobante"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar el comprobante: " + e.getMessage()
            ));
        }
    }

    public void eliminarComprobante(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("idComprobante"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminado = comprobanteService.eliminarComprobante(idComprobante, idRestaurante, 
                idSolicitud, idRestaurantero);
            
            if (eliminado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Comprobante eliminado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Comprobante no encontrado"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al eliminar el comprobante: " + e.getMessage()
            ));
        }
    }

    public void obtenerTiposComprobante(Context ctx) {
        try {
            List<String> tipos = comprobanteService.obtenerTiposComprobante();
            ctx.json(Map.of(
                "success", true,
                "data", tipos,
                "message", "Tipos de comprobante obtenidos correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los tipos de comprobante: " + e.getMessage()
            ));
        }
    }

    public void actualizarComprobanteSimplificado(Context ctx) {
        try {
            int idComprobante = Integer.parseInt(ctx.pathParam("idComprobante"));
            
            Comprobante comprobanteActualizado = new Comprobante();
            String nuevaRutaArchivo = null;

            String contentType = ctx.header("Content-Type");
            
            if (contentType != null && contentType.contains("multipart/form-data")) {
                if (!ctx.uploadedFiles().isEmpty()) {
                    var uploadedFile = ctx.uploadedFiles().get(0);
                    
                    try {
                        nuevaRutaArchivo = guardarArchivoPDF(uploadedFile);
                        comprobanteActualizado.setRuta_archivo(nuevaRutaArchivo);
                    } catch (Exception e) {
                        ctx.status(500).json(Map.of(
                            "success", false,
                            "message", "Error al guardar el archivo PDF: " + e.getMessage()
                        ));
                        return;
                    }
                } else {
                    String rutaArchivo = ctx.formParam("ruta_archivo");
                    String tipo = ctx.formParam("tipo");
                    
                    if (rutaArchivo != null && !rutaArchivo.trim().isEmpty()) {
                        comprobanteActualizado.setRuta_archivo(rutaArchivo);
                    }
                    if (tipo != null && !tipo.trim().isEmpty()) {
                        comprobanteActualizado.setTipo(tipo);
                    }
                }
            } else {
                comprobanteActualizado = ctx.bodyAsClass(Comprobante.class);
            }
            
            boolean actualizado = comprobanteService.actualizarComprobanteSimplificado(idComprobante, comprobanteActualizado);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Comprobante actualizado correctamente",
                    "nueva_ruta", nuevaRutaArchivo != null ? nuevaRutaArchivo : "Comprobante actualizado"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar el comprobante. Verifica que el comprobante exista"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de comprobante inválido"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar el comprobante: " + e.getMessage()
            ));
        }
    }

    private String guardarArchivoPDF(UploadedFile uploadedFile) throws Exception {

        String fileName = uploadedFile.filename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF");
        }

        String contentType = uploadedFile.contentType();
        if (contentType != null && !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("El archivo debe ser un PDF válido");
        }

        Path documentsDir = Paths.get("uploads/documents");
        if (!Files.exists(documentsDir)) {
            Files.createDirectories(documentsDir);
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = String.valueOf((int)(Math.random() * 1000000000));
        String newFileName = timestamp + "_" + randomSuffix + ".pdf";

        Path filePath = documentsDir.resolve(newFileName);

        try (InputStream inputStream = uploadedFile.content()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "http://localhost:7070/uploads/documents/" + newFileName;
    }
}
