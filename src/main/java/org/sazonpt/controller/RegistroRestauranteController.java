package org.sazonpt.controller;

import io.javalin.http.Context;
import org.sazonpt.model.dto.RegistroRestauranteDTO;
import org.sazonpt.service.RegistroRestauranteService;

import java.sql.SQLException;
import java.util.Map;

public class RegistroRestauranteController {
    
    private final RegistroRestauranteService registroService;
    
    public RegistroRestauranteController(RegistroRestauranteService registroService) {
        this.registroService = registroService;
    }
    
    /**
     * POST /api/registro-restaurante
     * Registra un nuevo restaurante con todos sus datos
     */
    public void registrarRestaurante(Context ctx) {
        try {
            // Obtener datos del formulario o JSON
            RegistroRestauranteDTO datos = extraerDatos(ctx);
            
            // Validar datos obligatorios
            String validacion = validarDatos(datos);
            if (validacion != null) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Datos inválidos: " + validacion
                ));
                return;
            }
            
            // Registrar restaurante
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
    
    /**
     * GET /api/solicitud/{id}/estado
     * Obtiene el estado de una solicitud
     */
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
    
    /**
     * GET /api/restaurantero/{id}/solicitudes
     * Obtiene todas las solicitudes de un restaurantero
     */
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
        
        // Si es JSON, parsear como JSON
        if (contentType != null && contentType.contains("application/json")) {
            return ctx.bodyAsClass(RegistroRestauranteDTO.class);
        }
        
        // Si es form data, parsear como formulario
        return extraerDatosFormulario(ctx);
    }
    
    private RegistroRestauranteDTO extraerDatosFormulario(Context ctx) {
        RegistroRestauranteDTO datos = new RegistroRestauranteDTO();
        
        // Datos del restaurante
        datos.setNombreRestaurante(ctx.formParam("nombreRestaurante"));
        datos.setPropietario(ctx.formParam("propietario"));
        datos.setCorreoElectronico(ctx.formParam("correoElectronico"));
        datos.setNumeroCelular(ctx.formParam("numeroCelular"));
        datos.setFacebook(ctx.formParam("facebook"));
        datos.setInstagram(ctx.formParam("instagram"));
        datos.setDireccion(ctx.formParam("direccion"));
        datos.setHorarios(ctx.formParam("horarios"));
        
        // Archivos (rutas de archivos subidos)
        datos.setImagenPrincipal(ctx.formParam("imagenPrincipal"));
        datos.setImagenSecundaria(ctx.formParam("imagenSecundaria"));
        datos.setImagenPlatillo(ctx.formParam("imagenPlatillo"));
        datos.setComprobanteDomicilio(ctx.formParam("comprobanteDomicilio"));
        datos.setMenuRestaurante(ctx.formParam("menuRestaurante"));
        
        // IDs
        String idRestaurantero = ctx.formParam("idRestaurantero");
        String idZona = ctx.formParam("idZona");
        
        if (idRestaurantero != null) {
            datos.setIdRestaurantero(Integer.parseInt(idRestaurantero));
        }
        if (idZona != null) {
            datos.setIdZona(Integer.parseInt(idZona));
        }
        
        return datos;
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
        if (datos.getIdZona() <= 0) {
            return "ID de zona inválido";
        }
        
        // Validar que al menos una imagen sea proporcionada
        if ((datos.getImagenPrincipal() == null || datos.getImagenPrincipal().trim().isEmpty()) &&
            (datos.getImagenSecundaria() == null || datos.getImagenSecundaria().trim().isEmpty()) &&
            (datos.getImagenPlatillo() == null || datos.getImagenPlatillo().trim().isEmpty())) {
            return "Al menos una imagen es obligatoria";
        }
        
        return null; // Todo válido
    }
}
