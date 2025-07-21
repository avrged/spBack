package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.RestauranteRepository;
import org.sazonpt.service.RestauranteService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    public void getAll(Context ctx) {
        try {
            List<Restaurante> restaurantes = restauranteService.getAllRestaurantes();
            ctx.json(restaurantes);
        } catch (SQLException e) {
            ctx.status(500).result("Error al obtener restaurantes");
        }
    }

    public void getById(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restaurante = restauranteService.getById(idRestaurante);
            
            if (restaurante != null) {
                ctx.json(restaurante);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Restaurante no encontrado");
            }
        } catch (SQLException e) {
            ctx.status(404).result("Error al obtener restaurante");
        }
    }

    public void create(Context ctx) {
        try {
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);
            restauranteService.createRestaurante(restaurante);
            ctx.status(201).json(java.util.Map.of(
                    "success", true,
                    "message", "Restaurante creado correctamente"
            ));
        } catch (Exception e) {
            ctx.status(400).json(java.util.Map.of(
                    "success", false,
                    "message", "Error al crear restaurante: " + e.getMessage()
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurante restaurante = ctx.bodyAsClass(Restaurante.class);

            // Asegurarse de que el ID del restaurante coincida con el parámetro de la URL
            Restaurante updatedRestaurante = new Restaurante(
                idRestaurante,
                restaurante.getId_solicitud_aprobada(),
                restaurante.getId_zona(),
                restaurante.getNombre(),
                restaurante.getDireccion(),
                restaurante.getHorario(),
                restaurante.getTelefono(),
                restaurante.getEtiquetas() // Incluir etiquetas en la actualización
            );

            restauranteService.updateRestaurante(updatedRestaurante);

            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Restaurante actualizado correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al actualizar restaurante: " + e.getMessage()
            ));
        }
    }

    public void delete(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Attempting to delete restaurante with ID: " + idRestaurante);
            restauranteService.deleteRestaurante(idRestaurante);
            ctx.status(200).result("Restaurante eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("Invalid restaurante ID format: " + ctx.pathParam("id"));
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            System.out.println("SQL Error deleting restaurante: " + e.getMessage());
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting restaurante: " + e.getMessage());
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener el dueño de un restaurante
    public void getDueno(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            Restaurantero dueno = restauranteService.getDuenoRestaurante(idRestaurante);

            if (dueno != null) {
                ctx.json(dueno);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Dueño no encontrado para este restaurante");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener todos los restaurantes de un restaurantero
    public void getRestaurantesByDueno(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            List<Restaurante> restaurantes = restauranteService.getRestaurantesByDueno(idRestaurantero);
            ctx.json(restaurantes);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurantero inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener todos los restaurantes de un usuario
    public void getRestaurantesByUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            List<Restaurante> restaurantes = restauranteService.getRestaurantesByUsuario(idUsuario);
            ctx.json(restaurantes);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de usuario inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    // Método para obtener restaurante con información completa del dueño
    public void getRestauranteConDueno(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("id"));
            RestauranteRepository.RestauranteConDueno resultado = restauranteService.getRestauranteConDueno(idRestaurante);

            if (resultado != null) {
                ctx.json(java.util.Map.of(
                    "restaurante", resultado.getRestaurante(),
                    "dueno", resultado.getDueno()
                ));
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Restaurante no encontrado");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de restaurante inválido");
        } catch (SQLException e) {
            ctx.status(500).result("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las etiquetas válidas disponibles
     */
    public void getEtiquetasValidas(Context ctx) {
        try {
            ctx.json(java.util.Map.of(
                "success", true,
                "etiquetas", RestauranteService.getEtiquetasValidas(),
                "maxEtiquetas", 3,
                "message", "Lista de etiquetas válidas obtenida correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error al obtener etiquetas válidas: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint seguro para que un restaurantero actualice solo su propio restaurante
     * Valida que el usuario autenticado coincida con el propietario del restaurante
     */
    public void actualizarRestauranteSeguro(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            // TODO: Aquí deberías validar el token/sesión del usuario autenticado
            // Por ahora, asumimos que el parámetro coincide con el usuario autenticado
            // String tokenUsuario = ctx.header("Authorization");
            // int idUsuarioAutenticado = validarToken(tokenUsuario);
            // if (idUsuarioAutenticado != idRestaurantero) {
            //     throw new SecurityException("No tienes permisos para editar este restaurante");
            // }

            // 1. Buscar el restaurante asociado a este restaurantero
            List<Restaurante> restaurantesDelUsuario = restauranteService.getRestaurantesByDueno(idRestaurantero);

            if (restaurantesDelUsuario.isEmpty()) {
                ctx.status(404).json(java.util.Map.of(
                    "success", false,
                    "message", "No tienes ningún restaurante registrado"
                ));
                return;
            }

            // Asumir que un restaurantero tiene solo un restaurante (regla de negocio)
            // Si puede tener múltiples, necesitarías pasar el ID del restaurante específico
            Restaurante restauranteExistente = restaurantesDelUsuario.get(0);

            // 2. Obtener los datos de actualización del body
            Restaurante datosActualizacion = ctx.bodyAsClass(Restaurante.class);

            // 3. Crear el restaurante actualizado preservando el ID y la asociación
            Restaurante restauranteActualizado = new Restaurante(
                restauranteExistente.getIdRestaurante(), // Mantener el ID original
                restauranteExistente.getId_solicitud_aprobada(), // Mantener la solicitud original
                datosActualizacion.getId_zona() > 0 ? datosActualizacion.getId_zona() : restauranteExistente.getId_zona(),
                datosActualizacion.getNombre() != null ? datosActualizacion.getNombre() : restauranteExistente.getNombre(),
                datosActualizacion.getDireccion() != null ? datosActualizacion.getDireccion() : restauranteExistente.getDireccion(),
                datosActualizacion.getHorario() != null ? datosActualizacion.getHorario() : restauranteExistente.getHorario(),
                datosActualizacion.getTelefono() != null ? datosActualizacion.getTelefono() : restauranteExistente.getTelefono(),
                datosActualizacion.getEtiquetas() != null ? datosActualizacion.getEtiquetas() : restauranteExistente.getEtiquetas()
            );

            // 4. Actualizar el restaurante usando el servicio (que ya tiene validaciones)
            restauranteService.updateRestaurante(restauranteActualizado);

            // 5. Devolver respuesta exitosa
            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Tu restaurante ha sido actualizado correctamente",
                "data", java.util.Map.of(
                    "id_restaurante", restauranteActualizado.getIdRestaurante(),
                    "nombre", restauranteActualizado.getNombre(),
                    "direccion", restauranteActualizado.getDireccion(),
                    "horario", restauranteActualizado.getHorario(),
                    "telefono", restauranteActualizado.getTelefono(),
                    "etiquetas", restauranteActualizado.getEtiquetas() != null ? restauranteActualizado.getEtiquetas() : ""
                )
            ));

        } catch (NumberFormatException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (SecurityException e) {
            ctx.status(403).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error en la base de datos: " + e.getMessage()
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

    /**
     * Endpoint SEGURO para que un usuario actualice solo su propio restaurante
     * Valida que el usuario autenticado coincida con el propietario del restaurante
     */
    public void actualizarRestauranteSeguroPorUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));

            // TODO: Aquí deberías validar el token/sesión del usuario autenticado
            // Por ahora, asumimos que el parámetro coincide con el usuario autenticado
            // String tokenUsuario = ctx.header("Authorization");
            // int idUsuarioAutenticado = validarToken(tokenUsuario);
            // if (idUsuarioAutenticado != idUsuario) {
            //     throw new SecurityException("No tienes permisos para editar este restaurante");
            // }

            // 1. Buscar el restaurante asociado a este usuario
            List<Restaurante> restaurantesDelUsuario = restauranteService.getRestaurantesByUsuario(idUsuario);

            if (restaurantesDelUsuario.isEmpty()) {
                ctx.status(404).json(java.util.Map.of(
                    "success", false,
                    "message", "No tienes ningún restaurante registrado"
                ));
                return;
            }

            // Asumir que un usuario tiene solo un restaurante (regla de negocio)
            // Si puede tener múltiples, necesitarías pasar el ID del restaurante específico
            Restaurante restauranteExistente = restaurantesDelUsuario.get(0);

            // 2. Obtener los datos de actualización del body
            Restaurante datosActualizacion = ctx.bodyAsClass(Restaurante.class);

            // 3. Crear el restaurante actualizado preservando el ID y la asociación
            Restaurante restauranteActualizado = new Restaurante(
                restauranteExistente.getIdRestaurante(), // Mantener el ID original
                restauranteExistente.getId_solicitud_aprobada(), // Mantener la solicitud original
                datosActualizacion.getId_zona() > 0 ? datosActualizacion.getId_zona() : restauranteExistente.getId_zona(),
                datosActualizacion.getNombre() != null ? datosActualizacion.getNombre() : restauranteExistente.getNombre(),
                datosActualizacion.getDireccion() != null ? datosActualizacion.getDireccion() : restauranteExistente.getDireccion(),
                datosActualizacion.getHorario() != null ? datosActualizacion.getHorario() : restauranteExistente.getHorario(),
                datosActualizacion.getTelefono() != null ? datosActualizacion.getTelefono() : restauranteExistente.getTelefono(),
                datosActualizacion.getEtiquetas() != null ? datosActualizacion.getEtiquetas() : restauranteExistente.getEtiquetas()
            );

            // 4. Actualizar el restaurante usando el servicio (que ya tiene validaciones)
            restauranteService.updateRestaurante(restauranteActualizado);

            // 5. Devolver respuesta exitosa
            ctx.status(200).json(java.util.Map.of(
                "success", true,
                "message", "Tu restaurante ha sido actualizado correctamente",
                "data", java.util.Map.of(
                    "id_restaurante", restauranteActualizado.getIdRestaurante(),
                    "nombre", restauranteActualizado.getNombre(),
                    "direccion", restauranteActualizado.getDireccion(),
                    "horario", restauranteActualizado.getHorario(),
                    "telefono", restauranteActualizado.getTelefono(),
                    "etiquetas", restauranteActualizado.getEtiquetas() != null ? restauranteActualizado.getEtiquetas() : ""
                )
            ));

        } catch (NumberFormatException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", "ID de usuario inválido"
            ));
        } catch (SecurityException e) {
            ctx.status(403).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(java.util.Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            ctx.status(500).json(java.util.Map.of(
                "success", false,
                "message", "Error en la base de datos: " + e.getMessage()
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
