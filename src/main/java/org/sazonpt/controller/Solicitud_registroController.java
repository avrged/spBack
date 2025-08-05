package org.sazonpt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.service.Solicitud_registroService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Context;

public class Solicitud_registroController {
    
    private final Solicitud_registroService solicitudService;
    private final ObjectMapper objectMapper;

    public Solicitud_registroController(Solicitud_registroService solicitudService) {
        this.solicitudService = solicitudService;
        this.objectMapper = new ObjectMapper();
    }

    public void obtenerTodasLasSolicitudes(Context ctx) {
        try {
            List<Solicitud_registro> solicitudes = solicitudService.obtenerTodasLasSolicitudes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("message", "Solicitudes obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las solicitudes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerSolicitudPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Solicitud_registro> solicitud = solicitudService.obtenerSolicitudPorId(id);
            
            if (solicitud.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", solicitud.get());
                response.put("message", "Solicitud encontrada");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerSolicitudesPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Solicitud_registro> solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudes);
            response.put("message", "Solicitudes del restaurantero obtenidas correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener las solicitudes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearSolicitud(Context ctx) {
        try {
            Solicitud_registro solicitud = objectMapper.readValue(ctx.body(), Solicitud_registro.class);
            Solicitud_registro solicitudCreada = solicitudService.crearSolicitud(solicitud);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", solicitudCreada);
            response.put("message", "Solicitud de registro creada correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Solicitud_registro solicitudActualizada = objectMapper.readValue(ctx.body(), Solicitud_registro.class);
            
            boolean actualizada = solicitudService.actualizarSolicitud(id, solicitudActualizada);
            
            if (actualizada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud actualizada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar la solicitud");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void aprobarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);

            Object idAdminObj = requestBody.get("id_administrador");
            int idAdministrador;
            if (idAdminObj instanceof Integer) {
                idAdministrador = (Integer) idAdminObj;
            } else if (idAdminObj instanceof String) {
                idAdministrador = Integer.parseInt((String) idAdminObj);
            } else {
                throw new IllegalArgumentException("id_administrador debe ser un número entero");
            }

            Optional<Solicitud_registro> solicitudOpt = solicitudService.obtenerSolicitudPorId(id);
            if (solicitudOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                ctx.status(404).json(response);
                return;
            }
            
            Solicitud_registro solicitud = solicitudOpt.get();

            boolean aprobada = solicitudService.aprobarSolicitudConRevision(id, solicitud.getId_restaurantero(), idAdministrador);
            
            if (aprobada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud aprobada correctamente");
                response.put("data", Map.of(
                    "id_solicitud", id,
                    "id_restaurantero", solicitud.getId_restaurantero(),
                    "id_administrador", idAdministrador,
                    "estado", "aprobada"
                ));
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo aprobar la solicitud");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al aprobar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void rechazarSolicitud(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            
            // Obtener datos del body
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            
            // Manejar el id_administrador de forma segura (puede venir como String o Integer)
            Object idAdminObj = requestBody.get("id_administrador");
            int idAdministrador;
            if (idAdminObj instanceof Integer) {
                idAdministrador = (Integer) idAdminObj;
            } else if (idAdminObj instanceof String) {
                idAdministrador = Integer.parseInt((String) idAdminObj);
            } else {
                throw new IllegalArgumentException("id_administrador debe ser un número entero");
            }
            
            String motivoRechazo = (String) requestBody.getOrDefault("motivo_rechazo", "");
            
            // Obtener la solicitud para obtener el id_restaurantero
            Optional<Solicitud_registro> solicitudOpt = solicitudService.obtenerSolicitudPorId(id);
            if (solicitudOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Solicitud no encontrada");
                ctx.status(404).json(response);
                return;
            }
            
            Solicitud_registro solicitud = solicitudOpt.get();
            
            // Rechazar la solicitud y crear la revisión
            boolean rechazada = solicitudService.rechazarSolicitudConRevision(id, solicitud.getId_restaurantero(), idAdministrador, motivoRechazo);
            
            if (rechazada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud rechazada correctamente");
                response.put("data", Map.of(
                    "id_solicitud", id,
                    "id_restaurantero", solicitud.getId_restaurantero(),
                    "id_administrador", idAdministrador,
                    "estado", "rechazada",
                    "motivo_rechazo", motivoRechazo
                ));
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo rechazar la solicitud");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al rechazar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void eliminarSolicitud(Context ctx) {
        try {
            int idSolicitud = Integer.parseInt(ctx.pathParam("id"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminada = solicitudService.eliminarSolicitud(idSolicitud, idRestaurantero);
            
            if (eliminada) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Solicitud eliminada correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar la solicitud");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar la solicitud: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void aprobarPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            // Sin necesidad de body - aprobar directamente
            var solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            var solicitudPendiente = solicitudes.stream()
                .filter(s -> "pendiente".equalsIgnoreCase(s.getEstado()))
                .findFirst();
                
            if (solicitudPendiente.isEmpty()) {
                ctx.status(404).json(Map.of("success", false, "message", "No hay solicitud pendiente para este restaurantero"));
                return;
            }
            int idZonaDefecto = 1;
            
            boolean ok = solicitudService.aprobarSolicitudConRestaurante(
                solicitudPendiente.get().getId_solicitud(), 
                idZonaDefecto
            );
            
            if (ok) {
                ctx.json(Map.of(
                    "success", true, 
                    "message", "Solicitud aprobada correctamente con restaurante y zona creados automáticamente",
                    "data", Map.of(
                        "id_solicitud", solicitudPendiente.get().getId_solicitud(),
                        "id_restaurantero", idRestaurantero,
                        "estado", "aprobado"
                    )
                ));
            } else {
                ctx.status(500).json(Map.of("success", false, "message", "No se pudo aprobar la solicitud"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("success", false, "message", "ID inválido"));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    public void pendientePorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            var solicitudes = solicitudService.obtenerSolicitudesPorRestaurantero(idRestaurantero);
            var solicitudAprobada = solicitudes.stream()
                .filter(s -> "aprobada".equalsIgnoreCase(s.getEstado()))
                .findFirst();
            if (solicitudAprobada.isEmpty()) {
                ctx.status(404).json(Map.of("success", false, "message", "No hay solicitud aprobada para este restaurantero"));
                return;
            }
            boolean ok = solicitudService.cambiarEstadoSolicitud(solicitudAprobada.get().getId_solicitud(), "pendiente");
            if (ok) {
                ctx.json(Map.of("success", true, "message", "Solicitud cambiada a pendiente correctamente"));
            } else {
                ctx.status(500).json(Map.of("success", false, "message", "No se pudo cambiar el estado de la solicitud"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("success", false, "message", "ID inválido"));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }
}
