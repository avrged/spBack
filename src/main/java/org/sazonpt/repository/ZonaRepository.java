package org.sazonpt.repository;

import org.sazonpt.model.Zona;

public interface ZonaRepository {
    void AddZone(Zona z);
    boolean FindZone(int id);
    boolean DeleteZone(int id);
    Zona UpdateZone(Zona z);
    void listAllZones();
}
