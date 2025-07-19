package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Descarga;

public class DescargaRepository {
    
    public void AddDescarga(Descarga d) throws SQLException {
        String query = "INSERT INTO descarga(cantidad_descargas, id_adquisicion, lugar_origen, opiniones) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, d.getCantidad_descargas());
            stmt.setInt(2, d.getId_adquisicion());
            stmt.setString(3, d.getLugar_origen());
            stmt.setString(4, d.getOpiniones());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la descarga: " + e.getMessage());
        }
    }

    public Descarga FindDescarga(int id) throws SQLException {
        String query = "SELECT * FROM descarga WHERE id_descarga = ?";
        Descarga descarga = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    descarga = mapResultSetToDescarga(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la descarga: " + e.getMessage());
        }
        
        return descarga;
    }

    public boolean DeleteDescarga(int id) throws SQLException {
        String query = "DELETE FROM descarga WHERE id_descarga = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la descarga: " + e.getMessage());
        }
    }

    public Descarga UpdateDescarga(Descarga d) throws SQLException {
        String query = "UPDATE descarga SET cantidad_descargas = ?, id_adquisicion = ?, lugar_origen = ?, opiniones = ? WHERE id_descarga = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, d.getCantidad_descargas());
            stmt.setInt(2, d.getId_adquisicion());
            stmt.setString(3, d.getLugar_origen());
            stmt.setString(4, d.getOpiniones());
            stmt.setInt(5, d.getId_descarga());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindDescarga(d.getId_descarga());
            } else {
                throw new SQLException("No se encontró la descarga con ID: " + d.getId_descarga());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la descarga: " + e.getMessage());
        }
    }

    public List<Descarga> ListAllDescargas() throws SQLException {
        List<Descarga> descargas = new ArrayList<>();
        String query = "SELECT * FROM descarga";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                descargas.add(mapResultSetToDescarga(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las descargas: " + e.getMessage());
        }
        
        return descargas;
    }

    private Descarga mapResultSetToDescarga(ResultSet rs) throws SQLException {
        return new Descarga(
            rs.getInt("id_descarga"),
            rs.getInt("cantidad_descargas"),
            rs.getInt("id_adquisicion"),
            rs.getString("lugar_origen"),
            rs.getString("opiniones")
        );
    }
}
