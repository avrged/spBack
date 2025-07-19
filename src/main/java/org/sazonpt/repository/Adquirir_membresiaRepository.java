package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Adquirir_membresia;

public class Adquirir_membresiaRepository {
    
    public void save(Adquirir_membresia membresia) throws SQLException {
        String query = "INSERT INTO adquirir_membresia(id_restaurantero, fecha_adquisicion, costo, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, membresia.getId_restaurantero());
            stmt.setDate(2, Date.valueOf(membresia.getFecha_adquisicion()));
            stmt.setDouble(3, membresia.getCosto());
            stmt.setString(4, membresia.getEstado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear la membresía: " + e.getMessage());
        }
    }

    public Adquirir_membresia findById(int idAdquisicion) throws SQLException {
        String query = "SELECT * FROM adquirir_membresia WHERE id_adquisicion = ?";
        Adquirir_membresia membresia = null;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idAdquisicion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    membresia = mapResultSetToMembresia(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la membresía: " + e.getMessage());
        }
        
        return membresia;
    }

    public List<Adquirir_membresia> findAll() throws SQLException {
        List<Adquirir_membresia> membresias = new ArrayList<>();
        String query = "SELECT * FROM adquirir_membresia";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membresias.add(mapResultSetToMembresia(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las membresías: " + e.getMessage());
        }
        
        return membresias;
    }

    public List<Adquirir_membresia> findByRestaurantero(int codigoRestaurantero) throws SQLException {
        List<Adquirir_membresia> membresias = new ArrayList<>();
        String query = "SELECT * FROM adquirir_membresia WHERE id_restaurantero = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, codigoRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membresias.add(mapResultSetToMembresia(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las membresías del restaurantero: " + e.getMessage());
        }
        
        return membresias;
    }

    public void update(Adquirir_membresia membresia) throws SQLException {
        String query = "UPDATE adquirir_membresia SET id_restaurantero = ?, fecha_adquisicion = ?, costo = ?, estado = ? WHERE id_adquisicion = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, membresia.getId_restaurantero());
            stmt.setDate(2, Date.valueOf(membresia.getFecha_adquisicion()));
            stmt.setDouble(3, membresia.getCosto());
            stmt.setString(4, membresia.getEstado());
            stmt.setInt(5, membresia.getId_adquisicion());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la membresía: " + e.getMessage());
        }
    }

    public void delete(int idAdquisicion) throws SQLException {
        String query = "DELETE FROM adquirir_membresia WHERE id_adquisicion = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idAdquisicion);
            int rowsAffected = stmt.executeUpdate();
            
            System.out.println("Query executed: " + query);
            System.out.println("Adquisicion ID: " + idAdquisicion);
            System.out.println("Rows affected by delete: " + rowsAffected);
            
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo eliminar la membresía con ID: " + idAdquisicion);
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la membresía: " + e.getMessage());
        }
    }

    private Adquirir_membresia mapResultSetToMembresia(ResultSet rs) throws SQLException {
        return new Adquirir_membresia(
            rs.getInt("id_adquisicion"),
            rs.getInt("id_restaurantero"),
            rs.getDate("fecha_adquisicion").toLocalDate(),
            rs.getDouble("costo"),
            rs.getString("estado")
        );
    }
}
