package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Zona;

public class ZonaRepository {
    
    public void AddZone(Zona z) throws SQLException {
        String query = "INSERT INTO zona(nombre, descripcion) VALUES(?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, z.getNombre());
            stmt.setString(2, z.getDescripcion());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la zona: " + e.getMessage());
        }
    }

    public Zona FindZone(int id) throws SQLException {
        String query = "SELECT * FROM zona WHERE id_zona = ?";
        Zona zona = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    zona = mapResultSetToZona(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la zona: " + e.getMessage());
        }
        
        return zona;
    }

    public boolean DeleteZone(int id) throws SQLException {
        String query = "DELETE FROM zona WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la zona: " + e.getMessage());
        }
    }

    public Zona UpdateZone(Zona z) throws SQLException {
        String query = "UPDATE zona SET nombre = ?, descripcion = ? WHERE id_zona = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, z.getNombre());
            stmt.setString(2, z.getDescripcion());
            stmt.setInt(3, z.getId_zona());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindZone(z.getId_zona());
            } else {
                throw new SQLException("No se encontró la zona con ID: " + z.getId_zona());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la zona: " + e.getMessage());
        }
    }

    public List<Zona> listAllZones() throws SQLException {
        List<Zona> zonas = new ArrayList<>();
        String query = "SELECT * FROM zona";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                zonas.add(mapResultSetToZona(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las zonas: " + e.getMessage());
        }
        
        return zonas;
    }

    private Zona mapResultSetToZona(ResultSet rs) throws SQLException {
        return new Zona(
            rs.getInt("id_zona"),
            rs.getString("nombre"),
            rs.getString("descripcion")
        );
    }
}
