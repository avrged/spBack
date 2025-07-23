package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Estadistica;
import org.sazonpt.repository.EstadisticaRepository;

public class EstadisticaService {
    private final EstadisticaRepository estadisticaRepo;

    public EstadisticaService(EstadisticaRepository estadisticaRepo) {
        this.estadisticaRepo = estadisticaRepo;
    }

    public List<Estadistica> getAllEstadisticas() throws SQLException {
        return estadisticaRepo.listAllEstadisticas();
    }

    public Estadistica getById(int idEstadistica) throws SQLException {
        return estadisticaRepo.findEstadistica(idEstadistica);
    }

    public void createEstadistica(Estadistica estadistica) throws SQLException {
        if (estadistica.getCorreo() == null || estadistica.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        estadisticaRepo.addEstadistica(estadistica);
    }

    public void updateEstadistica(Estadistica estadistica) throws SQLException {
        if (estadisticaRepo.findEstadistica(estadistica.getId_estadistica()) == null) {
            throw new IllegalArgumentException("No existe una estadística con este ID");
        }
        estadisticaRepo.updateEstadistica(estadistica);
    }

    public void deleteEstadistica(int idEstadistica) throws SQLException {
        if (estadisticaRepo.findEstadistica(idEstadistica) == null) {
            throw new IllegalArgumentException("No existe una estadística con este ID");
        }
        boolean deleted = estadisticaRepo.deleteEstadistica(idEstadistica);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la estadística");
        }
    }
}
