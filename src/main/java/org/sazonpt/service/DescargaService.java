package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Descarga;
import org.sazonpt.repository.DescargaRepository;

public class DescargaService {
    private final DescargaRepository descargaRepo;

    public DescargaService(DescargaRepository descargaRepo) {
        this.descargaRepo = descargaRepo;
    }

    public List<Descarga> getAllDescargas() throws SQLException {
        return descargaRepo.ListAllDescargas();
    }

    public Descarga getById(int idDescarga) throws SQLException {
        return descargaRepo.FindDescarga(idDescarga);
    }

    public void createDescarga(Descarga descarga) throws SQLException {
        if (descarga.getLugar_origen() == null || descarga.getLugar_origen().trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar de origen es obligatorio");
        }

        if (descarga.getCantidad_descargas() < 0) {
            throw new IllegalArgumentException("La cantidad de descargas no puede ser negativa");
        }

        if (descarga.getId_adquisicion() <= 0) {
            throw new IllegalArgumentException("Código de adquisición inválido");
        }

        descargaRepo.AddDescarga(descarga);
    }

    public void updateDescarga(Descarga descarga) throws SQLException {
        if (descargaRepo.FindDescarga(descarga.getId_descarga()) == null) {
            throw new IllegalArgumentException("No existe una descarga con este ID");
        }

        if (descarga.getLugar_origen() == null || descarga.getLugar_origen().trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar de origen es obligatorio");
        }

        if (descarga.getCantidad_descargas() < 0) {
            throw new IllegalArgumentException("La cantidad de descargas no puede ser negativa");
        }

        descargaRepo.UpdateDescarga(descarga);
    }

    public void deleteDescarga(int idDescarga) throws SQLException {
        if (descargaRepo.FindDescarga(idDescarga) == null) {
            throw new IllegalArgumentException("No existe una descarga con este ID");
        }
        
        boolean deleted = descargaRepo.DeleteDescarga(idDescarga);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la descarga");
        }
    }
}
