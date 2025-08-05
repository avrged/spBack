package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Revision_solicitud;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Revision_solicitudRepository {

    public List<Revision_solicitud> findAll() throws SQLException {
        List<Revision_solicitud> revisiones = new ArrayList<>();
        String sql = """
            SELECT rs.id_revision, rs.fecha, 
                   rs.id_solicitud, rs.id_restaurantero, rs.id_administrador,
                   sr.nombre_propuesto_restaurante, u1.nombre as admin_nombre
            FROM revision_solicitud rs
            INNER JOIN solicitud_registro sr ON rs.id_solicitud = sr.id_solicitud 
            INNER JOIN administrador a ON rs.id_administrador = a.id_usuario
            INNER JOIN usuario u1 ON a.id_usuario = u1.id_usuario
            ORDER BY rs.fecha DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Revision_solicitud revision = new Revision_solicitud();
                revision.setId_revision(rs.getInt("id_revision"));
                revision.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                revision.setId_solicitud(rs.getInt("id_solicitud"));
                revision.setId_restaurantero(rs.getInt("id_restaurantero"));
                revision.setId_administrador(rs.getInt("id_administrador"));
                
                revisiones.add(revision);
            }
        }
        return revisiones;
    }

    public Optional<Revision_solicitud> findById(int id) throws SQLException {
        String sql = """
            SELECT rs.id_revision, rs.fecha, 
                   rs.id_solicitud, rs.id_restaurantero, rs.id_administrador,
                   sr.nombre_propuesto_restaurante, u1.nombre as admin_nombre
            FROM revision_solicitud rs
            INNER JOIN solicitud_registro sr ON rs.id_solicitud = sr.id_solicitud 
            INNER JOIN administrador a ON rs.id_administrador = a.id_usuario
            INNER JOIN usuario u1 ON a.id_usuario = u1.id_usuario
            WHERE rs.id_revision = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Revision_solicitud revision = new Revision_solicitud();
                revision.setId_revision(rs.getInt("id_revision"));
                revision.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                revision.setId_solicitud(rs.getInt("id_solicitud"));
                revision.setId_restaurantero(rs.getInt("id_restaurantero"));
                revision.setId_administrador(rs.getInt("id_administrador"));
                
                return Optional.of(revision);
            }
        }
        return Optional.empty();
    }

    public List<Revision_solicitud> findBySolicitud(int idSolicitud) throws SQLException {
        List<Revision_solicitud> revisiones = new ArrayList<>();
        String sql = """
            SELECT rs.id_revision, rs.fecha, 
                   rs.id_solicitud, rs.id_restaurantero, rs.id_administrador
            FROM revision_solicitud rs
            WHERE rs.id_solicitud = ?
            ORDER BY rs.fecha DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Revision_solicitud revision = new Revision_solicitud();
                revision.setId_revision(rs.getInt("id_revision"));
                revision.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                revision.setId_solicitud(rs.getInt("id_solicitud"));
                revision.setId_restaurantero(rs.getInt("id_restaurantero"));
                revision.setId_administrador(rs.getInt("id_administrador"));
                
                revisiones.add(revision);
            }
        }
        return revisiones;
    }

    public List<Revision_solicitud> findByAdministrador(int idAdministrador) throws SQLException {
        List<Revision_solicitud> revisiones = new ArrayList<>();
        String sql = """
            SELECT rs.id_revision, rs.fecha, 
                   rs.id_solicitud, rs.id_restaurantero, rs.id_administrador,
                   sr.nombre_propuesto_restaurante
            FROM revision_solicitud rs
            INNER JOIN solicitud_registro sr ON rs.id_solicitud = sr.id_solicitud 
            WHERE rs.id_administrador = ?
            ORDER BY rs.fecha DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdministrador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Revision_solicitud revision = new Revision_solicitud();
                revision.setId_revision(rs.getInt("id_revision"));
                revision.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                revision.setId_solicitud(rs.getInt("id_solicitud"));
                revision.setId_restaurantero(rs.getInt("id_restaurantero"));
                revision.setId_administrador(rs.getInt("id_administrador"));
                
                revisiones.add(revision);
            }
        }
        return revisiones;
    }

    public Revision_solicitud save(Revision_solicitud revision) throws SQLException {
        String sql = """
            INSERT INTO revision_solicitud (fecha, id_solicitud, id_restaurantero, id_administrador)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(revision.getFecha()));
            stmt.setInt(2, revision.getId_solicitud());
            stmt.setInt(3, revision.getId_restaurantero());
            stmt.setInt(4, revision.getId_administrador());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la revisión de solicitud");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                revision.setId_revision(generatedKeys.getInt(1));
            }
        }
        return revision;
    }

    // Método utilitario para crear una revisión con fecha actual
    public Revision_solicitud crearRevision(int idSolicitud, int idRestaurantero, int idAdministrador) throws SQLException {
        Revision_solicitud nuevaRevision = new Revision_solicitud();
        nuevaRevision.setFecha(LocalDateTime.now());
        nuevaRevision.setId_solicitud(idSolicitud);
        nuevaRevision.setId_restaurantero(idRestaurantero);
        nuevaRevision.setId_administrador(idAdministrador);
        
        return save(nuevaRevision);
    }

    public boolean update(Revision_solicitud revision) throws SQLException {
        String sql = """
            UPDATE revision_solicitud 
            SET fecha = ?
            WHERE id_revision = ? AND id_solicitud = ? AND id_restaurantero = ? AND id_administrador = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(revision.getFecha()));
            stmt.setInt(2, revision.getId_revision());
            stmt.setInt(3, revision.getId_solicitud());
            stmt.setInt(4, revision.getId_restaurantero());
            stmt.setInt(5, revision.getId_administrador());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idRevision, int idSolicitud, int idAdministrador) throws SQLException {
        String sql = """
            DELETE FROM revision_solicitud 
            WHERE id_revision = ? AND id_solicitud = ? AND id_administrador = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRevision);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idAdministrador);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM revision_solicitud WHERE id_revision = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean solicitudExists(int idSolicitud) throws SQLException {
        String sql = "SELECT 1 FROM solicitud_registro WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean administradorExists(int idAdministrador) throws SQLException {
        String sql = "SELECT 1 FROM administrador WHERE id_usuario = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdministrador);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Nuevo método para eliminar todas las revisiones de una solicitud
    public boolean deleteByIdSolicitud(int idSolicitud) throws SQLException {
        String sql = "DELETE FROM revision_solicitud WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            return stmt.executeUpdate() >= 0; // Puede ser 0 si no hay revisiones
        }
    }

    public boolean actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado) throws SQLException {
        String sql = "UPDATE solicitud_registro SET estado = ? WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idSolicitud);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
