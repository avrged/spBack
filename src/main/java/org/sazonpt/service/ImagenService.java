package org.sazonpt.service;

import org.sazonpt.model.Imagen;
import org.sazonpt.repository.ImagenRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ImagenService {
    
    private final ImagenRepository imagenRepository;

    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public List<Imagen> obtenerTodasLasImagenes() {
        try {
            return imagenRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las imágenes: " + e.getMessage(), e);
        }
    }

    public Optional<Imagen> obtenerImagenPorId(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return imagenRepository.findById(idImagen, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la imagen: " + e.getMessage(), e);
        }
    }

    public List<Imagen> obtenerImagenesPorRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return imagenRepository.findByRestaurante(idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las imágenes del restaurante: " + e.getMessage(), e);
        }
    }

    public List<Imagen> obtenerImagenesPorRestaurantero(int idRestaurantero) {
        try {
            return imagenRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las imágenes del restaurantero: " + e.getMessage(), e);
        }
    }

    public Imagen crearImagen(Imagen imagen) {
        // Validaciones
        if (imagen.getId_restaurante() <= 0) {
            throw new IllegalArgumentException("ID de restaurante inválido");
        }
        if (imagen.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido"); 
        }
        if (imagen.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        // Verificar que el restaurante existe
        try {
            if (!imagenRepository.restauranteExists(imagen.getId_restaurante(), 
                    imagen.getId_solicitud(), imagen.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurante: " + e.getMessage(), e);
        }

        // Establecer fecha actual si no se proporciona
        if (imagen.getFecha_subida() == null) {
            imagen.setFecha_subida(LocalDateTime.now());
        }

        try {
            return imagenRepository.save(imagen);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la imagen: " + e.getMessage(), e);
        }
    }

    public boolean actualizarImagen(int idImagen, int idRestaurante, int idSolicitud, 
                                   int idRestaurantero, Imagen imagenActualizada) {
        try {
            // Verificar que la imagen existe
            if (!imagenRepository.existsById(idImagen, idRestaurante, idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("La imagen no existe");
            }

            // Validaciones
            if (imagenActualizada.getId_restaurante() <= 0) {
                throw new IllegalArgumentException("ID de restaurante inválido");
            }
            if (imagenActualizada.getId_solicitud() <= 0) {
                throw new IllegalArgumentException("ID de solicitud inválido");
            }
            if (imagenActualizada.getId_restaurantero() <= 0) {
                throw new IllegalArgumentException("ID de restaurantero inválido");
            }

            // Verificar que el restaurante existe
            if (!imagenRepository.restauranteExists(imagenActualizada.getId_restaurante(), 
                    imagenActualizada.getId_solicitud(), imagenActualizada.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }

            // Establecer los IDs de la clave primaria
            imagenActualizada.setId_imagen(idImagen);
            imagenActualizada.setId_restaurante(idRestaurante);
            imagenActualizada.setId_solicitud(idSolicitud);
            imagenActualizada.setId_restaurantero(idRestaurantero);

            return imagenRepository.update(imagenActualizada);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la imagen: " + e.getMessage(), e);
        }
    }

    public boolean eliminarImagen(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            if (!imagenRepository.existsById(idImagen, idRestaurante, idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("La imagen no existe");
            }

            return imagenRepository.delete(idImagen, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage(), e);
        }
    }

    public boolean existeImagen(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return imagenRepository.existsById(idImagen, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia de la imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Método utilitario para crear una imagen asociada a un restaurante específico
     */
    public Imagen crearImagenParaRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) {
        Imagen nuevaImagen = new Imagen();
        nuevaImagen.setId_restaurante(idRestaurante);
        nuevaImagen.setId_solicitud(idSolicitud);
        nuevaImagen.setId_restaurantero(idRestaurantero);
        nuevaImagen.setFecha_subida(LocalDateTime.now());
        
        return crearImagen(nuevaImagen);
    }
}
