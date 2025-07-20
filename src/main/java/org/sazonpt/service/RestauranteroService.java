package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.RestauranteroRepository;

public class RestauranteroService {
    private final RestauranteroRepository reoRepo;

    public RestauranteroService(RestauranteroRepository reoRepo) {
        this.reoRepo = reoRepo;
    }

    public List<Restaurantero> getAllRestauranteros() throws SQLException {
        return reoRepo.getAllRestauranteros();
    }

    public List<Restaurantero> findAllRestauranteros() throws SQLException {
        return reoRepo.findAllRestauranteros();
    }

    public void createRestaurantero(Restaurantero reo) throws SQLException {
        try {
            System.out.println("[RestauranteroService.createRestaurantero] Recibido:");
            System.out.println("nombre: " + reo.getNombre());
            System.out.println("correo: " + reo.getCorreo());
            System.out.println("contrasena: " + reo.getContrasena());
            System.out.println("id_usuario: " + reo.getId_usuario());
            System.out.println("tipo: " + reo.getTipo());
            System.out.println("status: " + reo.getStatus());

            if(reo.getNombre() == null || reo.getCorreo() == null || reo.getContrasena() == null) {
                throw new IllegalArgumentException("Nombre, correo y contraseña son obligatorios");
            }

            if (reoRepo.findRestauranteroById(reo.getId_usuario()) != null) {
                throw new IllegalArgumentException("Ya existe un restaurantero con este código");
            }

            if (!reo.getCorreo().contains("@")) {
                throw new IllegalArgumentException("El correo debe contener un '@'");
            }

            reoRepo.CreateRestaurantero(reo);
            System.out.println("[RestauranteroService.createRestaurantero] Restaurantero creado correctamente.");
        } catch (Exception e) {
            System.out.println("[RestauranteroService.createRestaurantero] Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void updateRestaurantero(Restaurantero reo) throws SQLException {
        if(reo.getNombre() == null || reo.getCorreo() == null || reo.getContrasena() == null) {
            throw new IllegalArgumentException("Nombre, correo y contraseña son obligatorios");
        }

        if (!reo.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }

        reoRepo.UpdateRestaurantero(reo);
    }

    public void deleteRestaurantero(int idReo) throws SQLException {
        reoRepo.DeleteUser(idReo);
    }

    public Restaurantero getByIdRestaurantero(int idReo) throws SQLException {
        return reoRepo.findRestauranteroById(idReo);
    }

    // Método temporal para migrar usuarios
    public void migrateUsersToRestaurantero() throws SQLException {
        reoRepo.migrateUsersToRestaurantero();
    }
}
