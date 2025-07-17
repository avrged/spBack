package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Comprobante;

public class ComprobanteRepository {
    
    public void AddComprobante(Comprobante c) throws SQLException {
        String query = "INSERT INTO comprobante(codigo_restaurante, ruta_archivo, fecha_subida) VALUES(?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, c.getCodigo_Rest());
            stmt.setString(2, c.getRuta_Archivo());
            stmt.setDate(3, Date.valueOf(c.getFecha()));
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear el comprobante: " + e.getMessage());
        }
    }

    public Comprobante FindComprobante(int id) throws SQLException {
        String query = "SELECT * FROM comprobante WHERE id_comprobante = ?";
        Comprobante comprobante = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    comprobante = mapResultSetToComprobante(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el comprobante: " + e.getMessage());
        }
        
        return comprobante;
    }

    public boolean DeleteComprobante(int id) throws SQLException {
        String query = "DELETE FROM comprobante WHERE id_comprobante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el comprobante: " + e.getMessage());
        }
    }

    public Comprobante UpdateComprobante(Comprobante c) throws SQLException {
        String query = "UPDATE comprobante SET codigo_restaurante = ?, ruta_archivo = ?, fecha_subida = ? WHERE id_comprobante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, c.getCodigo_Rest());
            stmt.setString(2, c.getRuta_Archivo());
            stmt.setDate(3, Date.valueOf(c.getFecha()));
            stmt.setInt(4, c.getIdComprobante());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindComprobante(c.getIdComprobante());
            } else {
                throw new SQLException("No se encontró el comprobante con ID: " + c.getIdComprobante());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el comprobante: " + e.getMessage());
        }
    }

    public List<Comprobante> ListAllComprobantes() throws SQLException {
        List<Comprobante> comprobantes = new ArrayList<>();
        String query = "SELECT * FROM comprobante";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                comprobantes.add(mapResultSetToComprobante(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los comprobantes: " + e.getMessage());
        }
        
        return comprobantes;
    }

    private Comprobante mapResultSetToComprobante(ResultSet rs) throws SQLException {
        return new Comprobante(
            rs.getInt("id_comprobante"),
            rs.getInt("codigo_restaurante"),
            rs.getString("ruta_archivo"),
            rs.getDate("fecha_subida").toLocalDate()
        );
    }
}
