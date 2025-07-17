package org.sazonpt.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Adquirir_membresia;
import org.sazonpt.repository.Adquirir_membresiaRepository;

public class Adquirir_membresiaService {
    private final Adquirir_membresiaRepository membresiaRepo;

    public Adquirir_membresiaService(Adquirir_membresiaRepository membresiaRepo) {
        this.membresiaRepo = membresiaRepo;
    }

    public List<Adquirir_membresia> getAllMembresias() throws SQLException {
        return membresiaRepo.findAll();
    }

    public Adquirir_membresia getById(int idAdquisicion) throws SQLException {
        return membresiaRepo.findById(idAdquisicion);
    }

    public List<Adquirir_membresia> getMembresiasByRestaurantero(int codigoRestaurantero) throws SQLException {
        return membresiaRepo.findByRestaurantero(codigoRestaurantero);
    }

    public void createMembresia(Adquirir_membresia membresia) throws SQLException {
        if (membresia.getCodigoRestaurantero() <= 0) {
            throw new IllegalArgumentException("Código de restaurantero inválido");
        }

        if (membresia.getCosto() < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        // Si no se especifica fecha, usar la fecha actual
        if (membresia.getFechaAdquisicion() == null) {
            membresia = new Adquirir_membresia(
                membresia.getIdAdquisicion(),
                membresia.getCodigoRestaurantero(),
                LocalDate.now(),
                membresia.getCosto(),
                membresia.getEstado()
            );
        }

        membresiaRepo.save(membresia);
    }

    public void updateMembresia(Adquirir_membresia membresia) throws SQLException {
        if (membresiaRepo.findById(membresia.getIdAdquisicion()) == null) {
            throw new IllegalArgumentException("No existe una membresía con este ID");
        }

        if (membresia.getCosto() < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        membresiaRepo.update(membresia);
    }

    public void deleteMembresia(int idAdquisicion) throws SQLException {
        if (membresiaRepo.findById(idAdquisicion) == null) {
            throw new IllegalArgumentException("No existe una membresía con este ID");
        }
        
        membresiaRepo.delete(idAdquisicion);
    }
}
