package org.sazonpt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.sazonpt.model.Restaurante;
import org.sazonpt.service.RestauranteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestauranteController {
    
    private final RestauranteService restauranteService;
    private final ObjectMapper objectMapper;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
        this.objectMapper = new ObjectMapper();
    }

    public void obtenerTodosLosRestaurantes(Context ctx) {
        try {
            List<Restaurante> restaurantes = restauranteService.obtenerTodosLosRestaurantes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restaurantes);
            response.put("message", "Restaurantes obtenidos correctamente");
            
            ctx.status(200).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener los restaurantes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRestaurantePorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Restaurante> restaurante = restauranteService.obtenerRestaurantePorId(id);
            
            if (restaurante.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", restaurante.get());
                response.put("message", "Restaurante encontrado");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Restaurante no encontrado");
                
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
            response.put("message", "Error al obtener el restaurante: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRestaurantesPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Restaurante> restaurantes = restauranteService.obtenerRestaurantesPorRestaurantero(idRestaurantero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restaurantes);
            response.put("message", "Restaurantes del restaurantero obtenidos correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurantero inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener los restaurantes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void obtenerRestaurantesPorZona(Context ctx) {
        try {
            int idZona = Integer.parseInt(ctx.pathParam("idZona"));
            List<Restaurante> restaurantes = restauranteService.obtenerRestaurantesPorZona(idZona);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restaurantes);
            response.put("message", "Restaurantes de la zona obtenidos correctamente");
            
            ctx.status(200).json(response);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de zona inválido");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener los restaurantes: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void crearRestaurante(Context ctx) {
        try {
            Restaurante restaurante = objectMapper.readValue(ctx.body(), Restaurante.class);
            Restaurante restauranteCreado = restauranteService.crearRestaurante(restaurante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", restauranteCreado);
            response.put("message", "Restaurante creado correctamente");
            
            ctx.status(201).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear el restaurante: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarRestaurante(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restauranteActualizado = objectMapper.readValue(ctx.body(), Restaurante.class);
            
            boolean actualizado = restauranteService.actualizarRestaurante(id, restauranteActualizado);
            
            if (actualizado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Restaurante actualizado correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo actualizar el restaurante");
                
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
            response.put("message", "Error al actualizar el restaurante: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    public void actualizarRestaurantePorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            Restaurante restauranteActualizado = ctx.bodyAsClass(Restaurante.class);
            boolean actualizado = restauranteService.actualizarPorRestaurantero(idRestaurantero, restauranteActualizado);
            if (actualizado) {
                ctx.json(Map.of("success", true, "message", "Restaurante actualizado correctamente"));
            } else {
                ctx.status(404).json(Map.of("success", false, "message", "No se encontró restaurante para ese restaurantero"));
            }
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    public void eliminarRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminado = restauranteService.eliminarRestaurante(idRestaurante, idSolicitud, idRestaurantero);
            
            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Restaurante eliminado correctamente");
                
                ctx.status(200).json(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se pudo eliminar el restaurante");
                
                ctx.status(404).json(response);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "IDs inválidos");
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el restaurante: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }

    /**
     * Actualiza campos específicos de un restaurante
     * PUT /restaurantes/{id}/campos
     */
    public void actualizarCamposEspecificos(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            Map<String, String> campos = new HashMap<>();
            
            // Procesar solo los campos permitidos
            String[] camposPermitidos = {"horario", "telefono", "etiquetas", "direccion", "facebook", "instagram"};
            
            for (String campo : camposPermitidos) {
                if (body.containsKey(campo) && body.get(campo) != null) {
                    campos.put(campo, body.get(campo).toString());
                }
            }
            
            if (campos.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No se proporcionaron campos válidos para actualizar");
                
                ctx.status(400).json(response);
                return;
            }
            
            boolean actualizado = restauranteService.actualizarCamposEspecificos(idRestaurante, campos);
            
            Map<String, Object> response = new HashMap<>();
            if (actualizado) {
                response.put("success", true);
                response.put("message", "Campos del restaurante actualizados correctamente");
                response.put("campos_actualizados", campos.keySet());
                
                ctx.status(200).json(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo actualizar el restaurante");
                
                ctx.status(404).json(response);
            }
            
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID de restaurante inválido");
            
            ctx.status(400).json(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            ctx.status(400).json(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar el restaurante: " + e.getMessage());
            
            ctx.status(500).json(response);
        }
    }
}
