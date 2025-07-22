package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Menu;

public class MenuRepository {
    
    public void save(Menu menu) throws SQLException {
        String query = "INSERT INTO menu(telefono, ruta_archivo) VALUES(?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, menu.getTelefono());
            stmt.setString(2, menu.getRuta_archivo());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear el menú: " + e.getMessage());
        }
    }

    public Menu findById(int id) throws SQLException {
        String query = "SELECT * FROM menu WHERE id_menu = ?";
        Menu menu = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    menu = mapResultSetToMenu(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el menú: " + e.getMessage());
        }
        
        return menu;
    }

    public List<Menu> findAll() throws SQLException {
        List<Menu> menus = new ArrayList<>();
        String query = "SELECT * FROM menu";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los menus: " + e.getMessage());
        }
        
        return menus;
    }

    public List<Menu> findByRestaurant(int codigoRestaurante) throws SQLException {
        List<Menu> menus = new ArrayList<>();
        String query = "SELECT * FROM menu WHERE id_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, codigoRestaurante);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToMenu(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los menús del restaurante: " + e.getMessage());
        }
        
        return menus;
    }

    public void update(Menu menu) throws SQLException {
        String query = "UPDATE menu SET telefono = ?, ruta_archivo = ? WHERE id_menu = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, menu.getTelefono());
            stmt.setString(2, menu.getRuta_archivo());
            stmt.setInt(3, menu.getId_menu());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el menú: " + e.getMessage());
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM menu WHERE id_menu = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el menú con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el menú: " + e.getMessage());
        }
    }

    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        return new Menu(
            rs.getInt("id_menu"),
            rs.getString("telefono"),
            rs.getString("ruta_archivo")
        );
    }
}
