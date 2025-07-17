package org.sazonpt.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.sazonpt.model.Imagen;
import org.sazonpt.repository.ImagenRepository;

public class ImagenService {
    private final ImagenRepository imagenRepo;

    public ImagenService(ImagenRepository imagenRepo) {
        this.imagenRepo = imagenRepo;
    }

    public List<Imagen> getAllImagenes() throws SQLException {
        return imagenRepo.ListAllImagesRoute();
    }

    public Imagen getById(int idImagen) throws SQLException {
        return imagenRepo.FindImage(idImagen);
    }

    public List<Imagen> getImagesByRestaurant(int codigoRestaurante) throws SQLException {
        return imagenRepo.ListImagesByRestaurant(codigoRestaurante);
    }

    public void createImagen(Imagen imagen) throws SQLException {
        if (imagen.getRuta_archivoI() == null || imagen.getRuta_archivoI().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        if (imagen.getCodigo_restarante() <= 0) {
            throw new IllegalArgumentException("Código de restaurante inválido");
        }

        // Si no se especifica fecha, usar la fecha actual
        if (imagen.getFecha_subida() == null) {
            imagen = new Imagen(
                imagen.getId_imagen(),
                imagen.getCodigo_restarante(),
                LocalDate.now(),
                imagen.getRuta_archivoI(),
                imagen.getEstado()
            );
        }

        imagenRepo.AddImage(imagen);
    }

    public void updateImagen(Imagen imagen) throws SQLException {
        if (imagenRepo.FindImage(imagen.getId_imagen()) == null) {
            throw new IllegalArgumentException("No existe una imagen con este ID");
        }

        if (imagen.getRuta_archivoI() == null || imagen.getRuta_archivoI().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        imagenRepo.UpdateImage(imagen);
    }

    public void deleteImagen(int idImagen) throws SQLException {
        if (imagenRepo.FindImage(idImagen) == null) {
            throw new IllegalArgumentException("No existe una imagen con este ID");
        }
        
        boolean deleted = imagenRepo.DeleteImage(idImagen);
        if (!deleted) {
            throw new SQLException("No se pudo eliminar la imagen");
        }
    }
}
