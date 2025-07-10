package org.sazonpt.repository;

import org.sazonpt.model.Adquirir_membresia;

public interface Adquirir_memRepository {
    void AddMembresia(Adquirir_membresia mem);
    boolean FindMembresia(int id);
    boolean DeleteMembresia(int id);
    Adquirir_membresia UpdateMembresia(Adquirir_membresia mem);
    void ListAllMembresias();
}
