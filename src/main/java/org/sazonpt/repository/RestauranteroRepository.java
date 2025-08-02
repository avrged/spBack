package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurantero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la gestión de restauranteros
 * Mapea la tabla 'restaurantero' de la base de datos
 */
public class RestauranteroRepository {
    
    /**
     * Guardar un nuevo restaurantero
     * @param restaurantero Datos del restaurantero
     * @return ID del usuario asociado
     */
    public int save(Restaurantero restaurantero) throws SQLException {
        String sql = """
            INSERT INTO restaurantero (id_restaurantero, rfc, verificado)
            VALUES (?, ?, ?)
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restaurantero.getId_restaurantero());
            stmt.setString(2, restaurantero.getRfc());
            stmt.setBoolean(3, restaurantero.isVerificado());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return restaurantero.getId_restaurantero();
            }
            throw new SQLException("Error al guardar restaurantero");
        }
    }
    
    /**
     * Buscar restaurantero por ID de usuario
     * @param id_restaurantero ID del restaurantero
     * @return Restaurantero encontrado o null
     */
    public Restaurantero findById(int id_restaurantero) throws SQLException {
        String sql = """
            SELECT id_restaurantero, rfc, verificado
            FROM restaurantero 
            WHERE id_restaurantero = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id_restaurantero);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToRestaurantero(rs);
            }
            return null;
        }
    }
    
    /**
     * Buscar restaurantero por RFC
     * @param rfc RFC del restaurantero
     * @return Restaurantero encontrado o null
     */
    public Restaurantero findByRfc(String rfc) throws SQLException {
        String sql = """
            SELECT id_restaurantero, rfc, verificado
            FROM restaurantero 
            WHERE rfc = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, rfc);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToRestaurantero(rs);
            }
            return null;
        }
    }
    
    /**
     * Actualizar restaurantero
     * @param restaurantero Datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Restaurantero restaurantero) throws SQLException {
        String sql = """
            UPDATE restaurantero 
            SET rfc = ?, verificado = ?
            WHERE id_restaurantero = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, restaurantero.getRfc());
            stmt.setBoolean(2, restaurantero.isVerificado());
            stmt.setInt(3, restaurantero.getId_restaurantero());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Obtener todos los restauranteros
     * @return Lista de restauranteros
     */
    public List<Restaurantero> findAll() throws SQLException {
        String sql = """
            SELECT id_restaurantero, rfc, verificado
            FROM restaurantero
            ORDER BY id_restaurantero
            """;
        
        List<Restaurantero> restauranteros = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                restauranteros.add(mapRowToRestaurantero(rs));
            }
        }
        
        return restauranteros;
    }
    
    /**
     * Verificar si existe un restaurantero por ID de usuario
     * @param id_restaurantero ID del restaurantero
     * @return true si existe
     */
    public boolean existsById(int id_restaurantero) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM restaurantero 
            WHERE id_restaurantero = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id_restaurantero);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Mapear ResultSet a objeto Restaurantero
     * @param rs ResultSet
     * @return Restaurantero
     */
    private Restaurantero mapRowToRestaurantero(ResultSet rs) throws SQLException {
        Restaurantero restaurantero = new Restaurantero();
        restaurantero.setId_restaurantero(rs.getInt("id_restaurantero"));
        restaurantero.setRfc(rs.getString("rfc"));
        restaurantero.setVerificado(rs.getBoolean("verificado"));
        return restaurantero;
    }
}
