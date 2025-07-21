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
        System.out.println("=== INICIO createWithFiles ===");
        System.out.println("Content-Type: " + ctx.contentType());
        System.out.println("Content-Length: " + ctx.header("Content-Length"));

        try {
            // Obtener datos del formulario con logs detallados
            System.out.println("=== OBTENIENDO PARÁMETROS ===");
            var restaurante = ctx.formParam("restaurante");
            System.out.println("restaurante obtenido: " + restaurante);

            var propietario = ctx.formParam("propietario");
            System.out.println("propietario obtenido: " + propietario);

            var correo = ctx.formParam("correo");
            System.out.println("correo obtenido: " + correo);

            var numero = ctx.formParam("numero");
            var direccion = ctx.formParam("direccion");
            var horario = ctx.formParam("horario");
            var id_restaurantero = ctx.formParam("id_restaurantero");
            var estado = ctx.formParam("estado");

            System.out.println("=== TODOS LOS DATOS RECIBIDOS ===");
            System.out.println("restaurante: " + restaurante);
            System.out.println("propietario: " + propietario);
            System.out.println("correo: " + correo);
            System.out.println("numero: " + numero);
            System.out.println("direccion: " + direccion);
            System.out.println("horario: " + horario);
            System.out.println("id_restaurantero: " + id_restaurantero);
            System.out.println("estado: " + estado);

            // Validar campos obligatorios
            if (restaurante == null || restaurante.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                direccion == null || direccion.trim().isEmpty()) {

                System.out.println("ERROR: Campos obligatorios faltantes");
                ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Los campos restaurante, correo y direccion son obligatorios"
                ));
                return;
            }

            System.out.println("=== OBTENIENDO ARCHIVOS ===");
            // Obtener archivos
            var imagen1 = ctx.uploadedFile("imagen1");
            var imagen2 = ctx.uploadedFile("imagen2");
            var imagen3 = ctx.uploadedFile("imagen3");
            var comprobante = ctx.uploadedFile("comprobante");

            System.out.println("imagen1: " + (imagen1 != null ? imagen1.filename() : "null"));
            System.out.println("imagen2: " + (imagen2 != null ? imagen2.filename() : "null"));
            System.out.println("imagen3: " + (imagen3 != null ? imagen3.filename() : "null"));
            System.out.println("comprobante: " + (comprobante != null ? comprobante.filename() : "null"));

            System.out.println("=== GUARDANDO ARCHIVOS ===");
            // Guardar archivos y obtener URLs
            String urlImagen1 = null;
            String urlImagen2 = null;
            String urlImagen3 = null;
            String urlComprobante = null;

            try {
                urlImagen1 = imagen1 != null ? saveImageFile(imagen1) : null;
                System.out.println("urlImagen1 guardada: " + urlImagen1);
            } catch (Exception e) {
                System.out.println("Error guardando imagen1: " + e.getMessage());
            }

            try {
                urlImagen2 = imagen2 != null ? saveImageFile(imagen2) : null;
                System.out.println("urlImagen2 guardada: " + urlImagen2);
            } catch (Exception e) {
                System.out.println("Error guardando imagen2: " + e.getMessage());
            }

            try {
                urlImagen3 = imagen3 != null ? saveImageFile(imagen3) : null;
                System.out.println("urlImagen3 guardada: " + urlImagen3);
            } catch (Exception e) {
                System.out.println("Error guardando imagen3: " + e.getMessage());
            }

            try {
                urlComprobante = comprobante != null ? saveDocumentFile(comprobante) : null;
                System.out.println("urlComprobante guardado: " + urlComprobante);
            } catch (Exception e) {
                System.out.println("Error guardando comprobante: " + e.getMessage());
            }

            System.out.println("=== CREANDO OBJETO SOLICITUD ===");
            // Crear objeto solicitud
            Solicitud_registro solicitud = new Solicitud_registro();

            System.out.println("Estableciendo valores en el objeto...");
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

            System.out.println("=== LLAMANDO AL SERVICE ===");
            System.out.println("Objeto solicitud creado, datos:");
            System.out.println("- Restaurante: " + solicitud.getRestaurante());
            System.out.println("- Correo: " + solicitud.getCorreo());
            System.out.println("- ID Restaurantero: " + solicitud.getId_restaurantero());

            // Llamar al service
            solicitudService.createSolicitud(solicitud);

            System.out.println("=== ÉXITO TOTAL ===");
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
            System.out.println("=== ERROR SQL DETALLADO ===");
            System.out.println("SQLException: " + sqlEx.getClass().getSimpleName());
            System.out.println("Message: " + sqlEx.getMessage());
            System.out.println("SQL State: " + sqlEx.getSQLState());
            System.out.println("Error Code: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();

            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error de base de datos: " + sqlEx.getMessage()
            ));

        } catch (IllegalArgumentException argEx) {
            System.out.println("=== ERROR DE ARGUMENTOS ===");
            System.out.println("IllegalArgumentException: " + argEx.getMessage());
            argEx.printStackTrace();

            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", argEx.getMessage()
            ));

        } catch (Exception e) {
            System.out.println("=== ERROR GENERAL DETALLADO ===");
            System.out.println("Exception type: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();

            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error interno del servidor: " + e.getMessage()
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
}
