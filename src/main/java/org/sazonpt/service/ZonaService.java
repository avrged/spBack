package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Zona;
import org.sazonpt.repository.ZonaRepository;

public class ZonaService {
    private final ZonaRepository zonaRepo;

    public ZonaService(ZonaRepository zonaRepo) {
        this.zonaRepo = zonaRepo;
    }

    public List<Zona> getAllZonas() throws SQLException {
        return zonaRepo.listAllZones();
    }

    public Zona getById(int idZona) throws SQLException {
        return zonaRepo.FindZone(idZona);
    }

    public void createZona(Zona zona) throws SQLException {
        if (zona.getNombre() == null || zona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la zona es obligatorio");
        }

        if (zona.getDescripcion() == null || zona.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la zona es obligatoria");
        }

        zonaRepo.AddZone(zona);
    }

    public void updateZona(Zona zona) throws SQLException {
        if (zonaRepo.FindZone(zona.getId_zona()) == null) {
            throw new IllegalArgumentException("No existe una zona con este ID");
        }

        if (zona.getNombre() == null || zona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la zona es obligatorio");
        }

        if (zona.getDescripcion() == null || zona.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la zona es obligatoria");
        }

        zonaRepo.UpdateZone(zona);
    }

    public void deleteZona(int idZona) throws SQLException {
        if (zonaRepo.FindZone(idZona) == null) {
            throw new IllegalArgumentException("No existe una zona con este ID");
        }
        
        boolean deleted = zonaRepo.DeleteZone(idZona);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la zona");
        }
    }
}
