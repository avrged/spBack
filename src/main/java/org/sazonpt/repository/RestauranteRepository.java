package org.sazonpt.repository;

import org.sazonpt.model.Restaurante;

public interface RestauranteRepository {
    void AddRestaurante(Restaurante re);
    boolean FindRestaurante(int id);
    boolean DeleteRestaurante(int id);
    Restaurante UpdateRestaurante(Restaurante re);
    void ListAllRestaurantes();
}
