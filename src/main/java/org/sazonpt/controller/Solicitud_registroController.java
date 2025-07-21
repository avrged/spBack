package org.sazonpt.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.service.Solicitud_registroService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;

public class Solicitud_registroController {
    private final Solicitud_registroService solicitudService;

    public Solicitud_registroController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public void getAll(Context ctx) {
        try {
            List<Solicitud_registro> solicitudes = solicitudService.getAllSolicitudes();
            ctx.json(solicitudes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener solicitudes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitud = solicitudService.getById(idSolicitud);
            
            if (solicitud != null) {
                ctx.json(solicitud);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Solicitud no encontrada");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener solicitud");
        }
    }

    public void create(Context ctx) {
        try {
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            solicitudService.createSolicitud(solicitud);
            ctx.status(201).result("Solicitud creada correctamente");
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear solicitud: " + e.getMessage()
            ));
        }
    }

    public void createWithFiles(Context ctx) {
        try {
            // Obtener datos del formulario
            var restaurante = ctx.formParam("restaurante");
            var propietario = ctx.formParam("propietario");
            var correo = ctx.formParam("correo");
            var numero = ctx.formParam("numero");
            var direccion = ctx.formParam("direccion");
            var horario = ctx.formParam("horario");
            var id_restaurantero = ctx.formParam("id_restaurantero");
            var estado = ctx.formParam("estado");

            // Validar campos obligatorios
            if (restaurante == null || restaurante.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                direccion == null || direccion.trim().isEmpty()) {

                ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Los campos restaurante, correo y direccion son obligatorios"
                ));
                return;
            }

            // Obtener archivos
            var imagen1 = ctx.uploadedFile("imagen1");
            var imagen2 = ctx.uploadedFile("imagen2");
            var imagen3 = ctx.uploadedFile("imagen3");
            var comprobante = ctx.uploadedFile("comprobante");

            // Guardar archivos y obtener URLs
            String urlImagen1 = imagen1 != null ? saveImageFile(imagen1) : null;
            String urlImagen2 = imagen2 != null ? saveImageFile(imagen2) : null;
            String urlImagen3 = imagen3 != null ? saveImageFile(imagen3) : null;
            String urlComprobante = comprobante != null ? saveDocumentFile(comprobante) : null;

            // Crear objeto solicitud
            Solicitud_registro solicitud = new Solicitud_registro();
            solicitud.setRestaurante(restaurante.trim());
            solicitud.setPropietario(propietario != null ? propietario.trim() : "");
            solicitud.setCorreo(correo.trim());
            solicitud.setNumero(numero != null ? numero.trim() : "");
            solicitud.setDireccion(direccion.trim());
            solicitud.setHorario(horario != null ? horario.trim() : "");

            // Usar un ID de restaurantero existente o crear uno temporal
            int idRestauranteroFinal;
            if (id_restaurantero != null && !id_restaurantero.trim().isEmpty()) {
                idRestauranteroFinal = Integer.parseInt(id_restaurantero);
            } else {
                idRestauranteroFinal = 1; // Cambia por un ID que exista en tu BD
            }

            solicitud.setId_restaurantero(idRestauranteroFinal);
            solicitud.setEstado("pendiente");
            solicitud.setFecha(LocalDate.now());
            solicitud.setImagen1(urlImagen1);
            solicitud.setImagen2(urlImagen2);
            solicitud.setImagen3(urlImagen3);
            solicitud.setComprobante(urlComprobante);

            // Guardar en base de datos
            solicitudService.createSolicitud(solicitud);

            ctx.status(201).json(java.util.Map.of(
                "success", true,
                "message", "Solicitud creada correctamente con archivos",
                "data", java.util.Map.of(
                    "restaurante", solicitud.getRestaurante(),
                    "correo", solicitud.getCorreo(),
                    "imagen1", urlImagen1 != null ? urlImagen1 : "No se subió imagen",
                    "imagen2", urlImagen2 != null ? urlImagen2 : "No se subió imagen",
                    "imagen3", urlImagen3 != null ? urlImagen3 : "No se subió imagen",
                    "comprobante", urlComprobante != null ? urlComprobante : "No se subió comprobante"
                )
            ));

        } catch (SQLException sqlEx) {
            System.err.println("Error de base de datos: " + sqlEx.getMessage());
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error de base de datos: " + sqlEx.getMessage()
            ));

        } catch (IllegalArgumentException argEx) {
            System.err.println("Error de validación: " + argEx.getMessage());
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", argEx.getMessage()
            ));

        } catch (Exception e) {
            System.err.println("Error interno: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error interno del servidor"
            ));
        }
    }

    private String saveImageFile(UploadedFile file) throws IOException {
        // Validar que sea una imagen
        String contentType = file.contentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Ruta de guardado para imágenes
        String staticPath = "uploads/images";
        File uploadDir = new File(staticPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("No se pudo crear el directorio de imágenes");
            }
        }

        // Nombre único del archivo
        String fileName = System.currentTimeMillis() + "_" + file.filename();
        File destino = new File(uploadDir, fileName);

        // Guardar el archivo
        try (InputStream is = file.content();
             FileOutputStream os = new FileOutputStream(destino)) {
            is.transferTo(os);
        }

        // Construcción de la URL completa
        String host = "localhost"; // Puedes configurar esto como variable de entorno
        return String.format("http://%s:7070/uploads/images/%s", host, fileName);
    }

    private String saveDocumentFile(UploadedFile file) throws IOException {
        // Validar que sea un PDF
        String contentType = file.contentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("El comprobante debe ser un archivo PDF");
        }

        // Ruta de guardado para documentos
        String staticPath = "uploads/documents";
        File uploadDir = new File(staticPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("No se pudo crear el directorio de documentos");
            }
        }

        // Nombre único del archivo
        String fileName = System.currentTimeMillis() + "_" + file.filename();
        File destino = new File(uploadDir, fileName);

        // Guardar el archivo
        try (InputStream is = file.content();
             FileOutputStream os = new FileOutputStream(destino)) {
            is.transferTo(os);
        }

        // Construcción de la URL completa
        String host = "localhost"; // Puedes configurar esto como variable de entorno
        return String.format("http://%s:7070/uploads/documents/%s", host, fileName);
    }

    public void update(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitud = ctx.bodyAsClass(Solicitud_registro.class);
            // Asegurarse de que el ID de la solicitud coincida con el parámetro de la URL
            solicitud.setId_solicitud(idSolicitud);
            solicitudService.updateSolicitud(solicitud);
            ctx.status(200).result("Solicitud actualizada");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar solicitud: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Intentando borrar la solicitud con id: " + idSolicitud);
            solicitudService.deleteSolicitud(idSolicitud);
            ctx.status(200).result("Solicitud eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Formato invalido de id: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de solicitud inválido");
        } catch (SQLException e) {
            System.out.println("Error sql al borrar el registro: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al borrar la solicitud: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    public void aprobarSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            solicitudService.aprobarSolicitud(idSolicitud);

            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Solicitud aprobada correctamente. El restaurante ha sido creado."
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "ID de solicitud inválido"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (SQLException e) {
            System.err.println("Error al aprobar solicitud: " + e.getMessage());
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al aprobar solicitud: " + e.getMessage()
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error interno del servidor"
            ));
        }
    }

    public void rechazarSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            solicitudService.rechazarSolicitud(idSolicitud);

            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Solicitud rechazada correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "ID de solicitud inválido"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (SQLException e) {
            System.err.println("Error al rechazar solicitud: " + e.getMessage());
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al rechazar solicitud: " + e.getMessage()
            ));
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error interno del servidor"
            ));
        }
    }
}
