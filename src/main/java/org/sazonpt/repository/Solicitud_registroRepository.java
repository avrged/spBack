package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Solicitud_registro;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Solicitud_registroRepository {

    public List<Solicitud_registro> findAll() throws SQLException {
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        String sql = """
            SELECT sr.id_solicitud, sr.fecha, sr.estado, sr.nombre_propuesto_restaurante, 
                   sr.correo, sr.nombre_propietario, sr.horario_atencion, sr.id_restaurantero,
                   u.nombre, u.correo as restaurantero_correo
            FROM solicitud_registro sr
            INNER JOIN restaurantero r ON sr.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            ORDER BY sr.fecha DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Solicitud_registro solicitud = new Solicitud_registro();
                solicitud.setId_solicitud(rs.getInt("id_solicitud"));
                
                Timestamp timestamp = rs.getTimestamp("fecha");
                if (timestamp != null) {
                    solicitud.setFecha(timestamp.toLocalDateTime());
                }
                
                solicitud.setEstado(rs.getString("estado"));
                solicitud.setNombre_propuesto_restaurante(rs.getString("nombre_propuesto_restaurante"));
                solicitud.setCorreo(rs.getString("correo"));
                solicitud.setNombre_propietario(rs.getString("nombre_propietario"));
                solicitud.setHorario_atencion(rs.getString("horario_atencion"));
                solicitud.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                solicitudes.add(solicitud);
            }
        }
        return solicitudes;
    }

    public Optional<Solicitud_registro> findById(int id) throws SQLException {
        String sql = """
            SELECT sr.id_solicitud, sr.fecha, sr.estado, sr.nombre_propuesto_restaurante, 
                   sr.correo, sr.nombre_propietario, sr.horario_atencion, sr.id_restaurantero,
                   u.nombre, u.correo as restaurantero_correo
            FROM solicitud_registro sr
            INNER JOIN restaurantero r ON sr.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            WHERE sr.id_solicitud = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Solicitud_registro solicitud = new Solicitud_registro();
                solicitud.setId_solicitud(rs.getInt("id_solicitud"));
                
                Timestamp timestamp = rs.getTimestamp("fecha");
                if (timestamp != null) {
                    solicitud.setFecha(timestamp.toLocalDateTime());
                }
                
                solicitud.setEstado(rs.getString("estado"));
                solicitud.setNombre_propuesto_restaurante(rs.getString("nombre_propuesto_restaurante"));
                solicitud.setCorreo(rs.getString("correo"));
                solicitud.setNombre_propietario(rs.getString("nombre_propietario"));
                solicitud.setHorario_atencion(rs.getString("horario_atencion"));
                solicitud.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                return Optional.of(solicitud);
            }
        }
        return Optional.empty();
    }

    public List<Solicitud_registro> findByRestaurantero(int idRestaurantero) throws SQLException {
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        String sql = """
            SELECT sr.id_solicitud, sr.fecha, sr.estado, sr.nombre_propuesto_restaurante, 
                   sr.correo, sr.nombre_propietario, sr.horario_atencion, sr.id_restaurantero
            FROM solicitud_registro sr
            WHERE sr.id_restaurantero = ?
            ORDER BY sr.fecha DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Solicitud_registro solicitud = new Solicitud_registro();
                solicitud.setId_solicitud(rs.getInt("id_solicitud"));
                
                Timestamp timestamp = rs.getTimestamp("fecha");
                if (timestamp != null) {
                    solicitud.setFecha(timestamp.toLocalDateTime());
                }
                
                solicitud.setEstado(rs.getString("estado"));
                solicitud.setNombre_propuesto_restaurante(rs.getString("nombre_propuesto_restaurante"));
                solicitud.setCorreo(rs.getString("correo"));
                solicitud.setNombre_propietario(rs.getString("nombre_propietario"));
                solicitud.setHorario_atencion(rs.getString("horario_atencion"));
                solicitud.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                solicitudes.add(solicitud);
            }
        }
        return solicitudes;
    }

    public Solicitud_registro save(Solicitud_registro solicitud) throws SQLException {
        String sql = """
            INSERT INTO solicitud_registro (fecha, estado, nombre_propuesto_restaurante, 
                                          correo, nombre_propietario, horario_atencion, id_restaurantero)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, solicitud.getFecha() != null ? 
                Timestamp.valueOf(solicitud.getFecha()) : Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, solicitud.getEstado() != null ? 
                solicitud.getEstado() : "pendiente");
            stmt.setString(3, solicitud.getNombre_propuesto_restaurante());
            stmt.setString(4, solicitud.getCorreo());
            stmt.setString(5, solicitud.getNombre_propietario());
            stmt.setString(6, solicitud.getHorario_atencion());
            stmt.setInt(7, solicitud.getId_restaurantero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la solicitud de registro");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                solicitud.setId_solicitud(generatedKeys.getInt(1));
            }
        }
        return solicitud;
    }

    public boolean update(Solicitud_registro solicitud) throws SQLException {
        String sql = """
            UPDATE solicitud_registro 
            SET estado = ?, nombre_propuesto_restaurante = ?, correo = ?, 
                nombre_propietario = ?, horario_atencion = ?
            WHERE id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solicitud.getEstado());
            stmt.setString(2, solicitud.getNombre_propuesto_restaurante());
            stmt.setString(3, solicitud.getCorreo());
            stmt.setString(4, solicitud.getNombre_propietario());
            stmt.setString(5, solicitud.getHorario_atencion());
            stmt.setInt(6, solicitud.getId_solicitud());
            stmt.setInt(7, solicitud.getId_restaurantero());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateEstado(int idSolicitud, String nuevoEstado) throws SQLException {
        String sql = "UPDATE solicitud_registro SET estado = ? WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idSolicitud);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = "DELETE FROM solicitud_registro WHERE id_solicitud = ? AND id_restaurantero = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            stmt.setInt(2, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM solicitud_registro WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean restauranteroExists(int idRestaurantero) throws SQLException {
        String sql = "SELECT 1 FROM restaurantero WHERE id_usuario = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
