package org.sazonpt.service;

import org.sazonpt.model.Descarga;
import org.sazonpt.model.dto.EstadisticaDescargaDTO;
import org.sazonpt.repository.DescargaRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DescargaService {
    public boolean actualizarPrimeraDescargaPorRestaurantero(int idRestaurantero, Descarga datos) {
        try {
            List<Descarga> descargas = descargaRepository.findByRestaurantero(idRestaurantero);
            if (descargas.isEmpty()) {
                return false;
            }
            Descarga descarga = descargas.get(0);
            descarga.setCantidad_descargas(datos.getCantidad_descargas());
            descarga.setOpinion(datos.getOpinion());
            descarga.setOrigen(datos.getOrigen());
            return descargaRepository.update(descarga);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la descarga por restaurantero: " + e.getMessage(), e);
        }
    }
    
    private final DescargaRepository descargaRepository;

    public DescargaService(DescargaRepository descargaRepository) {
        this.descargaRepository = descargaRepository;
    }

    public List<Descarga> obtenerTodasLasDescargas() {
        try {
            return descargaRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las descargas: " + e.getMessage(), e);
        }
    }

    public Optional<Descarga> obtenerDescargaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de descarga inválido");
        }

        try {
            return descargaRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la descarga: " + e.getMessage(), e);
        }
    }

    public List<Descarga> obtenerDescargasPorRestaurantero(int idRestaurantero) {
        if (idRestaurantero <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        try {
            if (!descargaRepository.restauranteroExists(idRestaurantero)) {
                throw new IllegalArgumentException("El restaurantero especificado no existe");
            }

            return descargaRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las descargas del restaurantero: " + e.getMessage(), e);
        }
    }

    public List<Descarga> obtenerDescargasPorOrigen(String origen) {
        if (origen == null || origen.trim().isEmpty()) {
            throw new IllegalArgumentException("El origen es obligatorio");
        }

        if (!Descarga.Origen.esValido(origen)) {
            throw new IllegalArgumentException("Origen inválido. Valores permitidos: Nacional, Extranjero");
        }

        try {
            return descargaRepository.findByOrigen(origen);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las descargas por origen: " + e.getMessage(), e);
        }
    }

    public List<Descarga> obtenerDescargasPorOpinion(String opinion) {
        if (opinion == null || opinion.trim().isEmpty()) {
            throw new IllegalArgumentException("La opinión es obligatoria");
        }

        if (!Descarga.TipoOpinion.esValido(opinion)) {
            throw new IllegalArgumentException("Opinión inválida. Valores permitidos: La comida, La ubicacion, Recomendacion, El horario, La vista");
        }

        try {
            return descargaRepository.findByOpinion(opinion);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las descargas por opinión: " + e.getMessage(), e);
        }
    }

    public Descarga crearDescarga(Descarga descarga) {
        validarDescarga(descarga);

        try {
            if (!descargaRepository.restauranteroExists(descarga.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurantero especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurantero: " + e.getMessage(), e);
        }

        try {
            return descargaRepository.save(descarga);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la descarga: " + e.getMessage(), e);
        }
    }

    public boolean actualizarDescarga(int id, Descarga descargaActualizada) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de descarga inválido");
        }

        try {
            if (!descargaRepository.existsById(id)) {
                throw new IllegalArgumentException("La descarga no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la descarga: " + e.getMessage(), e);
        }

        validarDescargaActualizacion(descargaActualizada);

        descargaActualizada.setId_descarga(id);

        try {
            return descargaRepository.update(descargaActualizada);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la descarga: " + e.getMessage(), e);
        }
    }

    public boolean incrementarDescargas(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de descarga inválido");
        }

        try {
            if (!descargaRepository.existsById(id)) {
                throw new IllegalArgumentException("La descarga no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la descarga: " + e.getMessage(), e);
        }

        try {
            return descargaRepository.incrementarDescargas(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al incrementar las descargas: " + e.getMessage(), e);
        }
    }

    public boolean eliminarDescarga(int idDescarga, int idRestaurantero) {
        if (idDescarga <= 0) {
            throw new IllegalArgumentException("ID de descarga inválido");
        }
        if (idRestaurantero <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        try {
            if (!descargaRepository.existsById(idDescarga)) {
                throw new IllegalArgumentException("La descarga no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la descarga: " + e.getMessage(), e);
        }

        try {
            return descargaRepository.delete(idDescarga, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la descarga: " + e.getMessage(), e);
        }
    }

    public boolean existeDescarga(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de descarga inválido");
        }

        try {
            return descargaRepository.existsById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia de la descarga: " + e.getMessage(), e);
        }
    }

    public int getTotalDescargasByRestaurantero(int idRestaurantero) {
        if (idRestaurantero <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        try {
            if (!descargaRepository.restauranteroExists(idRestaurantero)) {
                throw new IllegalArgumentException("El restaurantero especificado no existe");
            }

            return descargaRepository.getTotalDescargasByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el total de descargas: " + e.getMessage(), e);
        }
    }

    public List<Descarga> getTopDescargasByOrigen(String origen, int limit) {
        if (origen == null || origen.trim().isEmpty()) {
            throw new IllegalArgumentException("El origen es obligatorio");
        }

        if (!Descarga.Origen.esValido(origen)) {
            throw new IllegalArgumentException("Origen inválido. Valores permitidos: Nacional, Extranjero");
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0");
        }

        if (limit > 100) {
            throw new IllegalArgumentException("El límite no puede ser mayor a 100");
        }

        try {
            return descargaRepository.getTopDescargasByOrigen(origen, limit);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el top de descargas: " + e.getMessage(), e);
        }
    }

    public Descarga crearDescargaPorRestaurantero(int idRestaurantero, int cantidadDescargas, 
                                                  String origen, String opinion) {
        Descarga descarga = new Descarga();
        descarga.setId_restaurantero(idRestaurantero);
        descarga.setCantidad_descargas(cantidadDescargas);
        descarga.setOrigen(origen);
        descarga.setOpinion(opinion);

        return crearDescarga(descarga);
    }

    private void validarDescarga(Descarga descarga) {
        if (descarga == null) {
            throw new IllegalArgumentException("La descarga no puede ser nula");
        }

        if (descarga.getCantidad_descargas() < 0) {
            throw new IllegalArgumentException("La cantidad de descargas no puede ser negativa");
        }

        if (descarga.getOrigen() == null || descarga.getOrigen().trim().isEmpty()) {
            throw new IllegalArgumentException("El origen es obligatorio");
        }

        if (!Descarga.Origen.esValido(descarga.getOrigen())) {
            throw new IllegalArgumentException("Origen inválido. Valores permitidos: Nacional, Extranjero");
        }

        if (descarga.getOpinion() == null || descarga.getOpinion().trim().isEmpty()) {
            throw new IllegalArgumentException("La opinión es obligatoria");
        }

        if (!Descarga.TipoOpinion.esValido(descarga.getOpinion())) {
            throw new IllegalArgumentException("Opinión inválida. Valores permitidos: La comida, La ubicacion, Recomendacion, El horario, La vista");
        }

        if (descarga.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }
    }

    private void validarDescargaActualizacion(Descarga descarga) {
        if (descarga == null) {
            throw new IllegalArgumentException("La descarga no puede ser nula");
        }

        if (descarga.getCantidad_descargas() < 0) {
            throw new IllegalArgumentException("La cantidad de descargas no puede ser negativa");
        }

        if (descarga.getOrigen() != null && !descarga.getOrigen().trim().isEmpty()) {
            if (!Descarga.Origen.esValido(descarga.getOrigen())) {
                throw new IllegalArgumentException("Origen inválido. Valores permitidos: Nacional, Extranjero");
            }
        }
 
        if (descarga.getOpinion() != null && !descarga.getOpinion().trim().isEmpty()) {
            if (!Descarga.TipoOpinion.esValido(descarga.getOpinion())) {
                throw new IllegalArgumentException("Opinión inválida. Valores permitidos: La comida, La ubicacion, Recomendacion, El horario, La vista");
            }
        }
    }

    public Map<String, Object> obtenerEstadisticasRestaurantero(int idRestaurantero) {
        if (idRestaurantero <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        try {
            if (!descargaRepository.restauranteroExists(idRestaurantero)) {
                throw new IllegalArgumentException("El restaurantero especificado no existe");
            }

            List<Descarga> descargas = descargaRepository.findByRestaurantero(idRestaurantero);
            int totalDescargas = descargaRepository.getTotalDescargasByRestaurantero(idRestaurantero);

            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("id_restaurantero", idRestaurantero);
            estadisticas.put("total_registros", descargas.size());
            estadisticas.put("total_descargas", totalDescargas);
            estadisticas.put("descargas", descargas);

            return estadisticas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las estadísticas: " + e.getMessage(), e);
        }
    }

    public List<EstadisticaDescargaDTO> obtenerEstadisticasAgrupadasPorOrigenYOpinion() {
        try {
            return descargaRepository.getEstadisticasAgrupadasPorOrigenYOpinion();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estadísticas agrupadas: " + e.getMessage(), e);
        }
    }
}
