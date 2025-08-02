package org.sazonpt.service;

import org.sazonpt.model.Imagen_restaurante;
import org.sazonpt.model.Imagen_restaurante.TipoImagen;
import org.sazonpt.repository.Imagen_restauranteRepository;

import java.util.List;
import java.util.Optional;

public class Imagen_restauranteService {
    
    private final Imagen_restauranteRepository imagenRepository;
    
    public Imagen_restauranteService(Imagen_restauranteRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Imagen_restaurante crearImagen(Imagen_restaurante imagen) {
        // Validar datos básicos
        imagen.validar();
        
        return imagenRepository.save(imagen);
    }

    public Optional<Imagen_restaurante> obtenerImagenPorId(int id) {
        return imagenRepository.findById(id);
    }

    public List<Imagen_restaurante> obtenerImagenesDeRestaurante(int restauranteId) {
        return imagenRepository.findByRestauranteId(restauranteId);
    }

    public List<Imagen_restaurante> obtenerTodasLasImagenes() {
        return imagenRepository.findAll();
    }

    public Imagen_restaurante actualizarImagen(Imagen_restaurante imagen) {
        // Verificar que la imagen existe
        if (!imagenRepository.findById(imagen.getId_imagen()).isPresent()) {
            throw new IllegalArgumentException("La imagen con ID " + imagen.getId_imagen() + " no existe");
        }
        imagen.validar();
        
        return imagenRepository.update(imagen);
    }

    public boolean eliminarImagen(int id) {
        if (!imagenRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("La imagen con ID " + id + " no existe");
        }
        
        return imagenRepository.deleteById(id);
    }

    public List<Imagen_restaurante> obtenerImagenesPorTipo(int restauranteId, TipoImagen tipo) {
        return imagenRepository.findByRestauranteIdAndTipo(restauranteId, tipo);
    }

    public Optional<Imagen_restaurante> obtenerImagenPrincipal(int restauranteId) {
        return imagenRepository.findImagenPrincipal(restauranteId);
    }
}
