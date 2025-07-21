package org.sazonpt.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.RestauranteRepository;

public class RestauranteService {
    private final RestauranteRepository restauranteRepo;

    // Lista de etiquetas válidas predefinidas
    private static final Set<String> ETIQUETAS_VALIDAS = new HashSet<>(Arrays.asList(
        "Comida Rápida",
        "Pet Friendly",
        "Familiar",
        "Económico",
        "Gourmet",
        "Vegetariano",
        "Delivery",
        "Terraza",
        "WiFi Gratuito",
        "Estacionamiento"
    ));

    private static final int MAX_ETIQUETAS = 3;

    public RestauranteService(RestauranteRepository restauranteRepo) {
        this.restauranteRepo = restauranteRepo;
    }

    public List<Restaurante> getAllRestaurantes() throws SQLException {
        return restauranteRepo.ListAllRestaurantes();
    }

    public Restaurante getById(int idRestaurante) throws SQLException {
        return restauranteRepo.FindRestaurante(idRestaurante);
    }

    public void createRestaurante(Restaurante restaurante) throws SQLException {
        if (restaurante.getNombre() == null || restaurante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }

        if (restaurante.getDireccion() == null || restaurante.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección del restaurante es obligatoria");
        }

        if (restaurante.getTelefono() == null || restaurante.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del restaurante es obligatorio");
        }

        if (restaurante.getId_zona() <= 0) {
            throw new IllegalArgumentException("Código de zona inválido");
        }

        // Validar etiquetas
        validarEtiquetas(restaurante.getEtiquetas());

        restauranteRepo.AddRestaurante(restaurante);
    }

    public void updateRestaurante(Restaurante restaurante) throws SQLException {
        if (restauranteRepo.FindRestaurante(restaurante.getIdRestaurante()) == null) {
            throw new IllegalArgumentException("No existe un restaurante con este ID");
        }

        if (restaurante.getNombre() == null || restaurante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }

        if (restaurante.getDireccion() == null || restaurante.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección del restaurante es obligatoria");
        }

        if (restaurante.getTelefono() == null || restaurante.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del restaurante es obligatorio");
        }

        // Validar etiquetas
        validarEtiquetas(restaurante.getEtiquetas());

        restauranteRepo.UpdateRestaurante(restaurante);
    }

    public void deleteRestaurante(int idRestaurante) throws SQLException {
        if (restauranteRepo.FindRestaurante(idRestaurante) == null) {
            throw new IllegalArgumentException("No existe un restaurante con este ID");
        }
        
        boolean deleted = restauranteRepo.DeleteRestaurante(idRestaurante);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar el restaurante");
        }
    }

    // Método para obtener el dueño de un restaurante
    public Restaurantero getDuenoRestaurante(int idRestaurante) throws SQLException {
        return restauranteRepo.findDuenoByRestauranteId(idRestaurante);
    }

    // Método para obtener todos los restaurantes de un restaurantero
    public List<Restaurante> getRestaurantesByDueno(int idRestaurantero) throws SQLException {
        return restauranteRepo.findRestaurantesByDueno(idRestaurantero);
    }

    // Método para obtener restaurante con información del dueño
    public RestauranteRepository.RestauranteConDueno getRestauranteConDueno(int idRestaurante) throws SQLException {
        return restauranteRepo.findRestauranteConDueno(idRestaurante);
    }

    // Método para obtener todos los restaurantes de un usuario
    public List<Restaurante> getRestaurantesByUsuario(int idUsuario) throws SQLException {
        return restauranteRepo.findRestaurantesByUsuario(idUsuario);
    }

    /**
     * Valida que las etiquetas cumplan con las reglas de negocio
     * @param etiquetas String con etiquetas separadas por comas
     * @throws IllegalArgumentException si las etiquetas no son válidas
     */
    private void validarEtiquetas(String etiquetas) {
        // Las etiquetas son opcionales
        if (etiquetas == null || etiquetas.trim().isEmpty()) {
            return;
        }

        // Dividir etiquetas por comas y limpiar espacios
        String[] etiquetasArray = etiquetas.split(",");

        // Validar número máximo de etiquetas
        if (etiquetasArray.length > MAX_ETIQUETAS) {
            throw new IllegalArgumentException("Máximo " + MAX_ETIQUETAS + " etiquetas permitidas");
        }

        // Validar que cada etiqueta sea válida
        for (String etiqueta : etiquetasArray) {
            String etiquetaLimpia = etiqueta.trim();
            if (!ETIQUETAS_VALIDAS.contains(etiquetaLimpia)) {
                throw new IllegalArgumentException("Etiqueta no válida: " + etiquetaLimpia +
                    ". Etiquetas válidas: " + String.join(", ", ETIQUETAS_VALIDAS));
            }
        }
    }

    /**
     * Obtiene las etiquetas válidas disponibles
     * @return Set con las etiquetas válidas
     */
    public static Set<String> getEtiquetasValidas() {
        return new HashSet<>(ETIQUETAS_VALIDAS);
    }
}
