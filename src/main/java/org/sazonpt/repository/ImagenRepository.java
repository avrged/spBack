package org.sazonpt.repository;

import org.sazonpt.model.Imagen;

public interface ImagenRepository {
    void AddImage(Imagen i);
    boolean FindImage(int id);
    boolean DeleteImage(int id);
    Imagen UpdateImage(Imagen i);
    void ListAllImagesRoute(Imagen i);
}
