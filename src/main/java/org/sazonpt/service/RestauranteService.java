package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.RestauranteRepository;

public class RestauranteService {
    private final RestauranteRepository restauranteRepo;

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
}
