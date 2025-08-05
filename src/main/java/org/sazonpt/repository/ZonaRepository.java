package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Zona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZonaRepository {

    public List<Zona> findAll() throws SQLException {
        List<Zona> zonas = new ArrayList<>();
        String sql = "SELECT id_zona, nombre, id_restaurantero FROM zona ORDER BY nombre";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Zona zona = new Zona(
                    rs.getInt("id_zona"),
                    rs.getString("nombre"),
                    rs.getInt("id_restaurantero")
                );
                zonas.add(zona);
            }
        }
        
        return zonas;
    }

    public Optional<Zona> findById(int idZona) throws SQLException {
        String sql = "SELECT id_zona, nombre, id_restaurantero FROM zona WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idZona);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Zona zona = new Zona(
                        rs.getInt("id_zona"),
                        rs.getString("nombre"),
                        rs.getInt("id_restaurantero")
                    );
                    return Optional.of(zona);
                }
            }
        }
        
        return Optional.empty();
    }

    public List<Zona> findByRestaurantero(int idRestaurantero) throws SQLException {
        List<Zona> zonas = new ArrayList<>();
        String sql = "SELECT id_zona, nombre, id_restaurantero FROM zona WHERE id_restaurantero = ? ORDER BY nombre";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Zona zona = new Zona(
                        rs.getInt("id_zona"),
                        rs.getString("nombre"),
                        rs.getInt("id_restaurantero")
                    );
                    zonas.add(zona);
                }
            }
        }
        
        return zonas;
    }

    public Zona save(Zona zona) throws SQLException {
        String sql = "INSERT INTO zona (nombre, id_restaurantero) VALUES (?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, zona.getNombre());
            stmt.setInt(2, zona.getId_restaurantero());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la zona, no se insertaron filas");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    zona.setId_zona(generatedKeys.getInt(1));
                    return zona;
                } else {
                    throw new SQLException("Error al crear la zona, no se obtuvo el ID generado");
                }
            }
        }
    }

    /**
     * Actualiza una zona existente
     */
    public boolean update(int idZona, Zona zona) throws SQLException {
        String sql = "UPDATE zona SET nombre = ?, id_restaurantero = ? WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, zona.getNombre());
            stmt.setInt(2, zona.getId_restaurantero());
            stmt.setInt(3, idZona);
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int idZona) throws SQLException {
        String sql = "DELETE FROM zona WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idZona);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int idZona) throws SQLException {
        String sql = "SELECT 1 FROM zona WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idZona);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByNombreAndRestaurantero(String nombre, int idRestaurantero) throws SQLException {
        String sql = "SELECT 1 FROM zona WHERE nombre = ? AND id_restaurantero = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByNombreAndRestauranteroExcludingId(String nombre, int idRestaurantero, int idZonaExcluir) throws SQLException {
        String sql = "SELECT 1 FROM zona WHERE nombre = ? AND id_restaurantero = ? AND id_zona != ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idRestaurantero);
            stmt.setInt(3, idZonaExcluir);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM zona";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public int countByRestaurantero(int idRestaurantero) throws SQLException {
        String sql = "SELECT COUNT(*) FROM zona WHERE id_restaurantero = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
}
