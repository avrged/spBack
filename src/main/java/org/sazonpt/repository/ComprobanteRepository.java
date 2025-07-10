package org.sazonpt.repository;

import org.sazonpt.model.Comprobante;

public interface ComprobanteRepository {
    void AddComprobante(Comprobante c);
    boolean FindComprobante(int id);
    boolean DeleteComprobante(int id);
    Comprobante UpdateComprobante(Comprobante c);
    void ListAllComprobantes();
}
