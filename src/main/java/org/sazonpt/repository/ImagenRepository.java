package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Imagen;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImagenRepository {

    private Connection getConnection() throws SQLException {
        return DBConfig.getDataSource().getConnection();
    }

    public List<Imagen> findAll() throws SQLException {
        String sql = """
            SELECT i.id_imagen, i.ruta_imagen, i.fecha_subida, i.id_restaurante, i.id_solicitud, i.id_restaurantero
            FROM imagen i
            INNER JOIN restaurante r ON i.id_restaurante = r.id_restaurante 
                AND i.id_solicitud = r.id_solicitud 
                AND i.id_restaurantero = r.id_restaurantero
            ORDER BY i.fecha_subida DESC
            """;

        List<Imagen> imagenes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                imagenes.add(mapRowToImagen(rs));
            }
        }
        return imagenes;
    }

    public Optional<Imagen> findById(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT i.id_imagen, i.ruta_imagen, i.fecha_subida, i.id_restaurante, i.id_solicitud, i.id_restaurantero
            FROM imagen i
            INNER JOIN restaurante r ON i.id_restaurante = r.id_restaurante 
                AND i.id_solicitud = r.id_solicitud 
                AND i.id_restaurantero = r.id_restaurantero
            WHERE i.id_imagen = ? AND i.id_restaurante = ? AND i.id_solicitud = ? AND i.id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idImagen);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToImagen(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Imagen> findByRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT i.id_imagen, i.ruta_imagen, i.fecha_subida, i.id_restaurante, i.id_solicitud, i.id_restaurantero
            FROM imagen i
            INNER JOIN restaurante r ON i.id_restaurante = r.id_restaurante 
                AND i.id_solicitud = r.id_solicitud 
                AND i.id_restaurantero = r.id_restaurantero
            WHERE i.id_restaurante = ? AND i.id_solicitud = ? AND i.id_restaurantero = ?
            ORDER BY i.fecha_subida DESC
            """;

        List<Imagen> imagenes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    imagenes.add(mapRowToImagen(rs));
                }
            }
        }
        return imagenes;
    }

    public List<Imagen> findByRestaurantero(int idRestaurantero) throws SQLException {
        String sql = """
            SELECT i.id_imagen, i.ruta_imagen, i.fecha_subida, i.id_restaurante, i.id_solicitud, i.id_restaurantero
            FROM imagen i
            INNER JOIN restaurante r ON i.id_restaurante = r.id_restaurante 
                AND i.id_solicitud = r.id_solicitud 
                AND i.id_restaurantero = r.id_restaurantero
            WHERE i.id_restaurantero = ?
            ORDER BY i.fecha_subida DESC
            """;

        List<Imagen> imagenes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    imagenes.add(mapRowToImagen(rs));
                }
            }
        }
        return imagenes;
    }

    public Imagen save(Imagen imagen) throws SQLException {
        String sql = """
            INSERT INTO imagen (ruta_imagen, fecha_subida, id_restaurante, id_solicitud, id_restaurantero) 
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Validar que el restaurante existe
            if (!restauranteExists(imagen.getId_restaurante(), imagen.getId_solicitud(), imagen.getId_restaurantero())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, imagen.getRuta_imagen());
            stmt.setTimestamp(2, imagen.getFecha_subida() != null ? 
                Timestamp.valueOf(imagen.getFecha_subida()) : Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, imagen.getId_restaurante());
            stmt.setInt(4, imagen.getId_solicitud());
            stmt.setInt(5, imagen.getId_restaurantero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la imagen, no se insertaron filas");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    imagen.setId_imagen(generatedKeys.getInt(1));
                    return imagen;
                } else {
                    throw new SQLException("Error al crear la imagen, no se obtuvo el ID generado");
                }
            }
        }
    }

    public boolean update(Imagen imagen) throws SQLException {
        String sql = """
            UPDATE imagen 
            SET ruta_imagen = ?, fecha_subida = ?
            WHERE id_imagen = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Validar que el restaurante existe
            if (!restauranteExists(imagen.getId_restaurante(), imagen.getId_solicitud(), imagen.getId_restaurantero())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, imagen.getRuta_imagen());
            stmt.setTimestamp(2, imagen.getFecha_subida() != null ? 
                Timestamp.valueOf(imagen.getFecha_subida()) : Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, imagen.getId_imagen());
            stmt.setInt(4, imagen.getId_restaurante());
            stmt.setInt(5, imagen.getId_solicitud());
            stmt.setInt(6, imagen.getId_restaurantero());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            DELETE FROM imagen 
            WHERE id_imagen = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idImagen);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int idImagen, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT 1 FROM imagen 
            WHERE id_imagen = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idImagen);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean restauranteExists(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT 1 FROM restaurante 
            WHERE id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Imagen mapRowToImagen(ResultSet rs) throws SQLException {
        Imagen imagen = new Imagen();
        imagen.setId_imagen(rs.getInt("id_imagen"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_subida");
        if (timestamp != null) {
            imagen.setFecha_subida(timestamp.toLocalDateTime());
        }
        
        imagen.setId_restaurante(rs.getInt("id_restaurante"));
        imagen.setId_solicitud(rs.getInt("id_solicitud"));
        imagen.setId_restaurantero(rs.getInt("id_restaurantero"));
        
        return imagen;
    }
}
