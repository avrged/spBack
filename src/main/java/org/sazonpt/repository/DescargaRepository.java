package org.sazonpt.repository;

import org.sazonpt.model.Descarga;

public interface DescargaRepository {
    void AddDescarga(Descarga d);
    boolean FindDescarga(int id);
    boolean DeleteDescarga(int id);
    Descarga UpdateDescarga(Descarga d);
    void ListAllDescargas();
}
