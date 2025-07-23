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
        // Inicializar a 0 los nuevos atributos si no se envían
        if (estadistica.getDescargas() == 0) estadistica.setDescargas(0);
        if (estadistica.getComida() == 0) estadistica.setComida(0);
        if (estadistica.getUbicacion() == 0) estadistica.setUbicacion(0);
        if (estadistica.getRecomendacion() == 0) estadistica.setRecomendacion(0);
        if (estadistica.getHorario() == 0) estadistica.setHorario(0);
        if (estadistica.getVista() == 0) estadistica.setVista(0);
        estadisticaRepo.addEstadistica(estadistica);
    }

    public void updateEstadistica(Estadistica estadistica) throws SQLException {
        if (estadisticaRepo.findEstadistica(estadistica.getId_estadistica()) == null) {
            throw new IllegalArgumentException("No existe una estadística con este ID");
        }
        // Inicializar a 0 los nuevos atributos si no se envían
        if (estadistica.getDescargas() == 0) estadistica.setDescargas(0);
        if (estadistica.getComida() == 0) estadistica.setComida(0);
        if (estadistica.getUbicacion() == 0) estadistica.setUbicacion(0);
        if (estadistica.getRecomendacion() == 0) estadistica.setRecomendacion(0);
        if (estadistica.getHorario() == 0) estadistica.setHorario(0);
        if (estadistica.getVista() == 0) estadistica.setVista(0);
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
