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
    // Endpoint para actualizar solo el estado de una solicitud
    public void updateEstado(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            String nuevoEstado = ctx.bodyAsClass(EstadoRequest.class).getEstado();
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "El campo 'estado' es obligatorio"
                ));
                return;
            }
            solicitudService.updateEstado(idSolicitud, nuevoEstado.trim());
            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Estado actualizado correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "ID de solicitud inválido"
            ));
        } catch (SQLException e) {
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al actualizar estado: " + e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error interno del servidor"
            ));
        }
    }

    // Clase auxiliar para recibir solo el campo estado
    public static class EstadoRequest {
        private String estado;
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
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
            // Si etiquetas vienen null, se inicializan a ""
            if (solicitud.getEtiqueta1() == null) solicitud.setEtiqueta1("");
            if (solicitud.getEtiqueta2() == null) solicitud.setEtiqueta2("");
            if (solicitud.getEtiqueta3() == null) solicitud.setEtiqueta3("");
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
            // id_restaurantero eliminado
            var estado = ctx.formParam("estado");
            var facebook = ctx.formParam("facebook");
            var instagram = ctx.formParam("instagram");
            var menu = ctx.formParam("menu"); // solo para compatibilidad, pero el archivo se sube abajo
            var etiqueta1 = ctx.formParam("etiqueta1");
            var etiqueta2 = ctx.formParam("etiqueta2");
            var etiqueta3 = ctx.formParam("etiqueta3");

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
            var menuFile = ctx.uploadedFile("menu");

            // Guardar archivos y obtener URLs
            String urlImagen1 = imagen1 != null ? saveImageFile(imagen1) : null;
            String urlImagen2 = imagen2 != null ? saveImageFile(imagen2) : null;
            String urlImagen3 = imagen3 != null ? saveImageFile(imagen3) : null;
            String urlComprobante = comprobante != null ? saveDocumentFile(comprobante) : null;
            String urlMenu = menuFile != null ? saveDocumentFile(menuFile) : "";

            // Crear objeto solicitud
            Solicitud_registro solicitud = new Solicitud_registro();
            solicitud.setRestaurante(restaurante.trim());
            solicitud.setPropietario(propietario != null ? propietario.trim() : "");
            solicitud.setCorreo(correo.trim());
            solicitud.setNumero(numero != null ? numero.trim() : "");
            solicitud.setDireccion(direccion.trim());
            solicitud.setHorario(horario != null ? horario.trim() : "");
            solicitud.setFacebook(facebook != null ? facebook.trim() : "");
            solicitud.setInstagram(instagram != null ? instagram.trim() : "");
            solicitud.setMenu(urlMenu);
            solicitud.setEtiqueta1(etiqueta1 != null ? etiqueta1.trim() : "");
            solicitud.setEtiqueta2(etiqueta2 != null ? etiqueta2.trim() : "");
            solicitud.setEtiqueta3(etiqueta3 != null ? etiqueta3.trim() : "");

            // Usar un ID de restaurantero existente o crear uno temporal
            // idRestauranteroFinal y setId_restaurantero eliminados
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
                    "facebook", solicitud.getFacebook(),
                    "instagram", solicitud.getInstagram(),
                    "menu", urlMenu != null ? urlMenu : "No se subió menú",
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
        String host = "52.23.26.163"; // Puedes configurar esto como variable de entorno
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

    // Actualizar solicitud con archivos (multipart/form-data)
    public void updateWithFiles(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitudExistente = solicitudService.getById(idSolicitud);
            if (solicitudExistente == null) {
                ctx.status(404).json(java.util.Map.of(
                    "success", false,
                    "message", "No existe una solicitud con este ID"
                ));
                return;
            }

            // Obtener datos del formulario (si no vienen, se mantienen los existentes)
            var restaurante = ctx.formParam("restaurante");
            var propietario = ctx.formParam("propietario");
            var correo = ctx.formParam("correo");
            var numero = ctx.formParam("numero");
            var direccion = ctx.formParam("direccion");
            var horario = ctx.formParam("horario");
            // id_restaurantero eliminado
            var estado = ctx.formParam("estado");
            var facebook = ctx.formParam("facebook");
            var instagram = ctx.formParam("instagram");
            var menu = ctx.formParam("menu"); // solo para compatibilidad, pero el archivo se sube abajo
            var etiqueta1 = ctx.formParam("etiqueta1");
            var etiqueta2 = ctx.formParam("etiqueta2");
            var etiqueta3 = ctx.formParam("etiqueta3");

            // Archivos
            var imagen1 = ctx.uploadedFile("imagen1");
            var imagen2 = ctx.uploadedFile("imagen2");
            var imagen3 = ctx.uploadedFile("imagen3");
            var comprobante = ctx.uploadedFile("comprobante");
            var menuFile = ctx.uploadedFile("menu");

            // Guardar archivos y obtener URLs (si no se suben, se mantienen los existentes)
            String urlImagen1 = imagen1 != null ? saveImageFile(imagen1) : solicitudExistente.getImagen1();
            String urlImagen2 = imagen2 != null ? saveImageFile(imagen2) : solicitudExistente.getImagen2();
            String urlImagen3 = imagen3 != null ? saveImageFile(imagen3) : solicitudExistente.getImagen3();
            String urlComprobante = comprobante != null ? saveDocumentFile(comprobante) : solicitudExistente.getComprobante();
            String urlMenu = menuFile != null ? saveDocumentFile(menuFile) : solicitudExistente.getMenu();

            // Actualizar campos (si no vienen, se mantienen los existentes)
            Solicitud_registro solicitud = new Solicitud_registro();
            solicitud.setId_solicitud(idSolicitud);
            solicitud.setRestaurante(restaurante != null ? restaurante.trim() : solicitudExistente.getRestaurante());
            solicitud.setPropietario(propietario != null ? propietario.trim() : solicitudExistente.getPropietario());
            solicitud.setCorreo(correo != null ? correo.trim() : solicitudExistente.getCorreo());
            solicitud.setNumero(numero != null ? numero.trim() : solicitudExistente.getNumero());
            solicitud.setDireccion(direccion != null ? direccion.trim() : solicitudExistente.getDireccion());
            solicitud.setHorario(horario != null ? horario.trim() : solicitudExistente.getHorario());
            solicitud.setFacebook(facebook != null ? facebook.trim() : solicitudExistente.getFacebook());
            solicitud.setInstagram(instagram != null ? instagram.trim() : solicitudExistente.getInstagram());
            solicitud.setMenu(urlMenu);
            solicitud.setEtiqueta1(etiqueta1 != null ? etiqueta1.trim() : solicitudExistente.getEtiqueta1());
            solicitud.setEtiqueta2(etiqueta2 != null ? etiqueta2.trim() : solicitudExistente.getEtiqueta2());
            solicitud.setEtiqueta3(etiqueta3 != null ? etiqueta3.trim() : solicitudExistente.getEtiqueta3());
            solicitud.setEstado(estado != null ? estado.trim() : solicitudExistente.getEstado());
            solicitud.setFecha(solicitudExistente.getFecha());
            solicitud.setImagen1(urlImagen1);
            solicitud.setImagen2(urlImagen2);
            solicitud.setImagen3(urlImagen3);
            solicitud.setComprobante(urlComprobante);

            solicitudService.updateSolicitud(solicitud);

            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Solicitud actualizada correctamente con archivos",
                "data", java.util.Map.of(
                    "restaurante", solicitud.getRestaurante(),
                    "correo", solicitud.getCorreo(),
                    "facebook", solicitud.getFacebook(),
                    "instagram", solicitud.getInstagram(),
                    "menu", urlMenu != null ? urlMenu : "No se subió menú",
                    "imagen1", urlImagen1 != null ? urlImagen1 : "No se subió imagen",
                    "imagen2", urlImagen2 != null ? urlImagen2 : "No se subió imagen",
                    "imagen3", urlImagen3 != null ? urlImagen3 : "No se subió imagen",
                    "comprobante", urlComprobante != null ? urlComprobante : "No se subió comprobante"
                )
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "Error al actualizar solicitud: " + e.getMessage()
            ));
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
