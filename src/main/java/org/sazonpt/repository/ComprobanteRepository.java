package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Comprobante;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComprobanteRepository {

    private Connection getConnection() throws SQLException {
        return DBConfig.getDataSource().getConnection();
    }

    public List<Comprobante> findAll() throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            ORDER BY c.fecha_subida DESC
            """;

        List<Comprobante> comprobantes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                comprobantes.add(mapRowToComprobante(rs));
            }
        }
        return comprobantes;
    }

    public Optional<Comprobante> findById(int idComprobante, int idRestaurante, int idSolicitud, 
                                         int idRestaurantero, int idZona) throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            WHERE c.id_comprobante = ? AND c.id_restaurante = ? AND c.id_solicitud = ? 
                AND c.id_restaurantero = ? AND c.id_zona = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComprobante);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);
            stmt.setInt(5, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToComprobante(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Comprobante> findByRestaurante(int idRestaurante, int idSolicitud, 
                                              int idRestaurantero, int idZona) throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            WHERE c.id_restaurante = ? AND c.id_solicitud = ? 
                AND c.id_restaurantero = ? AND c.id_zona = ?
            ORDER BY c.fecha_subida DESC
            """;

        List<Comprobante> comprobantes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);
            stmt.setInt(4, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comprobantes.add(mapRowToComprobante(rs));
                }
            }
        }
        return comprobantes;
    }

    public List<Comprobante> findByRestaurantero(int idRestaurantero) throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            WHERE c.id_restaurantero = ?
            ORDER BY c.fecha_subida DESC
            """;

        List<Comprobante> comprobantes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comprobantes.add(mapRowToComprobante(rs));
                }
            }
        }
        return comprobantes;
    }

    public List<Comprobante> findByTipo(String tipo) throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            WHERE c.tipo = ?
            ORDER BY c.fecha_subida DESC
            """;

        List<Comprobante> comprobantes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comprobantes.add(mapRowToComprobante(rs));
                }
            }
        }
        return comprobantes;
    }

    public List<Comprobante> findByZona(int idZona) throws SQLException {
        String sql = """
            SELECT c.id_comprobante, c.tipo, c.ruta_archivo, c.fecha_subida, 
                   c.id_restaurante, c.id_solicitud, c.id_restaurantero, c.id_zona
            FROM comprobante c
            INNER JOIN restaurante r ON c.id_restaurante = r.id_restaurante 
                AND c.id_solicitud = r.id_solicitud 
                AND c.id_restaurantero = r.id_restaurantero 
                AND c.id_zona = r.id_zona
            WHERE c.id_zona = ?
            ORDER BY c.fecha_subida DESC
            """;

        List<Comprobante> comprobantes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comprobantes.add(mapRowToComprobante(rs));
                }
            }
        }
        return comprobantes;
    }

    public Comprobante save(Comprobante comprobante) throws SQLException {
        String sql = """
            INSERT INTO comprobante (tipo, ruta_archivo, fecha_subida, id_restaurante, 
                                   id_solicitud, id_restaurantero, id_zona) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Validar que el restaurante existe
            if (!restauranteExists(comprobante.getId_restaurante(), comprobante.getId_solicitud(), 
                                 comprobante.getId_restaurantero(), comprobante.getId_zona())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, comprobante.getTipo());
            stmt.setString(2, comprobante.getRuta_archivo());
            stmt.setTimestamp(3, comprobante.getFecha_subida() != null ? 
                Timestamp.valueOf(comprobante.getFecha_subida()) : Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, comprobante.getId_restaurante());
            stmt.setInt(5, comprobante.getId_solicitud());
            stmt.setInt(6, comprobante.getId_restaurantero());
            stmt.setInt(7, comprobante.getId_zona());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el comprobante, no se insertaron filas");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comprobante.setId_comprobante(generatedKeys.getInt(1));
                    return comprobante;
                } else {
                    throw new SQLException("Error al crear el comprobante, no se obtuvo el ID generado");
                }
            }
        }
    }

    public boolean update(Comprobante comprobante) throws SQLException {
        String sql = """
            UPDATE comprobante 
            SET tipo = ?, ruta_archivo = ?, fecha_subida = ?
            WHERE id_comprobante = ? AND id_restaurante = ? AND id_solicitud = ? 
                AND id_restaurantero = ? AND id_zona = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Validar que el restaurante existe
            if (!restauranteExists(comprobante.getId_restaurante(), comprobante.getId_solicitud(), 
                                 comprobante.getId_restaurantero(), comprobante.getId_zona())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, comprobante.getTipo());
            stmt.setString(2, comprobante.getRuta_archivo());
            stmt.setTimestamp(3, comprobante.getFecha_subida() != null ? 
                Timestamp.valueOf(comprobante.getFecha_subida()) : Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, comprobante.getId_comprobante());
            stmt.setInt(5, comprobante.getId_restaurante());
            stmt.setInt(6, comprobante.getId_solicitud());
            stmt.setInt(7, comprobante.getId_restaurantero());
            stmt.setInt(8, comprobante.getId_zona());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idComprobante, int idRestaurante, int idSolicitud, 
                         int idRestaurantero, int idZona) throws SQLException {
        String sql = """
            DELETE FROM comprobante 
            WHERE id_comprobante = ? AND id_restaurante = ? AND id_solicitud = ? 
                AND id_restaurantero = ? AND id_zona = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComprobante);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);
            stmt.setInt(5, idZona);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int idComprobante, int idRestaurante, int idSolicitud, 
                             int idRestaurantero, int idZona) throws SQLException {
        String sql = """
            SELECT 1 FROM comprobante 
            WHERE id_comprobante = ? AND id_restaurante = ? AND id_solicitud = ? 
                AND id_restaurantero = ? AND id_zona = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComprobante);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);
            stmt.setInt(5, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean restauranteExists(int idRestaurante, int idSolicitud, int idRestaurantero, int idZona) throws SQLException {
        String sql = """
            SELECT 1 FROM restaurante 
            WHERE id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ? AND id_zona = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);
            stmt.setInt(4, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Comprobante mapRowToComprobante(ResultSet rs) throws SQLException {
        Comprobante comprobante = new Comprobante();
        comprobante.setId_comprobante(rs.getInt("id_comprobante"));
        comprobante.setTipo(rs.getString("tipo"));
        comprobante.setRuta_archivo(rs.getString("ruta_archivo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_subida");
        if (timestamp != null) {
            comprobante.setFecha_subida(timestamp.toLocalDateTime());
        }
        
        comprobante.setId_restaurante(rs.getInt("id_restaurante"));
        comprobante.setId_solicitud(rs.getInt("id_solicitud"));
        comprobante.setId_restaurantero(rs.getInt("id_restaurantero"));
        comprobante.setId_zona(rs.getInt("id_zona"));
        
        return comprobante;
    }
}
