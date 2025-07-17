package org.sazonpt.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Comprobante;
import org.sazonpt.repository.ComprobanteRepository;

public class ComprobanteService {
    private final ComprobanteRepository comprobanteRepo;

    public ComprobanteService(ComprobanteRepository comprobanteRepo) {
        this.comprobanteRepo = comprobanteRepo;
    }

    public List<Comprobante> getAllComprobantes() throws SQLException {
        return comprobanteRepo.ListAllComprobantes();
    }

    public Comprobante getById(int idComprobante) throws SQLException {
        return comprobanteRepo.FindComprobante(idComprobante);
    }

    public void createComprobante(Comprobante comprobante) throws SQLException {
        if (comprobante.getRuta_Archivo() == null || comprobante.getRuta_Archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        if (comprobante.getCodigo_Rest() <= 0) {
            throw new IllegalArgumentException("Código de restaurante inválido");
        }

        // Si no se especifica fecha, usar la fecha actual
        if (comprobante.getFecha() == null) {
            comprobante = new Comprobante(
                comprobante.getIdComprobante(),
                comprobante.getCodigo_Rest(),
                comprobante.getRuta_Archivo(),
                LocalDate.now()
            );
        }

        comprobanteRepo.AddComprobante(comprobante);
    }

    public void updateComprobante(Comprobante comprobante) throws SQLException {
        if (comprobanteRepo.FindComprobante(comprobante.getIdComprobante()) == null) {
            throw new IllegalArgumentException("No existe un comprobante con este ID");
        }

        if (comprobante.getRuta_Archivo() == null || comprobante.getRuta_Archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        comprobanteRepo.UpdateComprobante(comprobante);
    }

    public void deleteComprobante(int idComprobante) throws SQLException {
        if (comprobanteRepo.FindComprobante(idComprobante) == null) {
            throw new IllegalArgumentException("No existe un comprobante con este ID");
        }
        
        boolean deleted = comprobanteRepo.DeleteComprobante(idComprobante);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar el comprobante");
        }
    }
}
