package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Descarga;
import org.sazonpt.model.dto.EstadisticaDescargaDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DescargaRepository {

    public List<Descarga> findAll() throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String sql = """
            SELECT d.id_descarga, d.cantidad_descargas, d.origen, d.opinion, d.id_restaurantero,
                   u.nombre as restaurantero_nombre
            FROM descarga d
            INNER JOIN restaurantero r ON d.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            ORDER BY d.id_descarga DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                descargas.add(descarga);
            }
        }
        return descargas;
    }

    public Optional<Descarga> findById(int id) throws SQLException {
        String sql = """
            SELECT d.id_descarga, d.cantidad_descargas, d.origen, d.opinion, d.id_restaurantero,
                   u.nombre as restaurantero_nombre
            FROM descarga d
            INNER JOIN restaurantero r ON d.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            WHERE d.id_descarga = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                return Optional.of(descarga);
            }
        }
        return Optional.empty();
    }

    public List<Descarga> findByRestaurantero(int idRestaurantero) throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String sql = """
            SELECT id_descarga, cantidad_descargas, origen, opinion, id_restaurantero
            FROM descarga 
            WHERE id_restaurantero = ?
            ORDER BY id_descarga DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                descargas.add(descarga);
            }
        }
        return descargas;
    }

    public List<Descarga> findByOrigen(String origen) throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String sql = """
            SELECT d.id_descarga, d.cantidad_descargas, d.origen, d.opinion, d.id_restaurantero,
                   u.nombre as restaurantero_nombre
            FROM descarga d
            INNER JOIN restaurantero r ON d.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            WHERE d.origen = ?
            ORDER BY d.cantidad_descargas DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, origen);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                descargas.add(descarga);
            }
        }
        return descargas;
    }

    public List<Descarga> findByOpinion(String opinion) throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String sql = """
            SELECT d.id_descarga, d.cantidad_descargas, d.origen, d.opinion, d.id_restaurantero,
                   u.nombre as restaurantero_nombre
            FROM descarga d
            INNER JOIN restaurantero r ON d.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            WHERE d.opinion = ?
            ORDER BY d.cantidad_descargas DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, opinion);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                descargas.add(descarga);
            }
        }
        return descargas;
    }

    public Descarga save(Descarga descarga) throws SQLException {
        String sql = """
            INSERT INTO descarga (cantidad_descargas, origen, opinion, id_restaurantero)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, descarga.getCantidad_descargas());
            stmt.setString(2, descarga.getOrigen());
            stmt.setString(3, descarga.getOpinion());
            stmt.setInt(4, descarga.getId_restaurantero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la descarga");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                descarga.setId_descarga(generatedKeys.getInt(1));
            }
        }
        return descarga;
    }

    public boolean update(Descarga descarga) throws SQLException {
        String sql = """
            UPDATE descarga 
            SET cantidad_descargas = ?, origen = ?, opinion = ?
            WHERE id_descarga = ? AND id_restaurantero = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, descarga.getCantidad_descargas());
            stmt.setString(2, descarga.getOrigen());
            stmt.setString(3, descarga.getOpinion());
            stmt.setInt(4, descarga.getId_descarga());
            stmt.setInt(5, descarga.getId_restaurantero());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean incrementarDescargas(int idDescarga) throws SQLException {
        String sql = """
            UPDATE descarga 
            SET cantidad_descargas = cantidad_descargas + 1
            WHERE id_descarga = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDescarga);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idDescarga, int idRestaurantero) throws SQLException {
        String sql = """
            DELETE FROM descarga 
            WHERE id_descarga = ? AND id_restaurantero = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDescarga);
            stmt.setInt(2, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM descarga WHERE id_descarga = ?";

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

    // Métodos de estadísticas
    public int getTotalDescargasByRestaurantero(int idRestaurantero) throws SQLException {
        String sql = "SELECT COALESCE(SUM(cantidad_descargas), 0) FROM descarga WHERE id_restaurantero = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Descarga> getTopDescargasByOrigen(String origen, int limit) throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String sql = """
            SELECT d.id_descarga, d.cantidad_descargas, d.origen, d.opinion, d.id_restaurantero,
                   u.nombre as restaurantero_nombre
            FROM descarga d
            INNER JOIN restaurantero r ON d.id_restaurantero = r.id_usuario
            INNER JOIN usuario u ON r.id_usuario = u.id_usuario
            WHERE d.origen = ?
            ORDER BY d.cantidad_descargas DESC
            LIMIT ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, origen);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Descarga descarga = new Descarga();
                descarga.setId_descarga(rs.getInt("id_descarga"));
                descarga.setCantidad_descargas(rs.getInt("cantidad_descargas"));
                descarga.setOrigen(rs.getString("origen"));
                descarga.setOpinion(rs.getString("opinion"));
                descarga.setId_restaurantero(rs.getInt("id_restaurantero"));
                
                descargas.add(descarga);
            }
        }
        return descargas;
    }

    public List<EstadisticaDescargaDTO> getEstadisticasAgrupadasPorOrigenYOpinion() throws SQLException {
        List<EstadisticaDescargaDTO> estadisticas = new ArrayList<>();
        String sql = """
            SELECT 
                origen AS Origen,
                opinion AS Opinion,
                SUM(cantidad_descargas) AS TotalDescargas
            FROM descarga
            GROUP BY origen, opinion
            ORDER BY TotalDescargas DESC
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaDescargaDTO estadistica = new EstadisticaDescargaDTO();
                estadistica.setOrigen(rs.getString("Origen"));
                estadistica.setOpinion(rs.getString("Opinion"));
                estadistica.setTotalDescargas(rs.getInt("TotalDescargas"));
                
                estadisticas.add(estadistica);
            }
        }
        return estadisticas;
    }
}
