package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurante;

public class RestauranteRepository {
    
    public void AddRestaurante(Restaurante re) throws SQLException {
        String query = "INSERT INTO restaurante(codigo_solicitud_aprobada, codigo_zona, nombre, direccion, horario, telefono, etiquetas) VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, re.getSolicitud_aprobada());
            stmt.setInt(2, re.getCodigo_zona());
            stmt.setString(3, re.getNombre());
            stmt.setString(4, re.getDireccion());
            stmt.setString(5, re.getHorario());
            stmt.setString(6, re.getTelefono());
            stmt.setString(7, re.getEtiquetas());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear el restaurante: " + e.getMessage());
        }
    }

    public Restaurante FindRestaurante(int id) throws SQLException {
        String query = "SELECT * FROM restaurante WHERE id_restaurante = ?";
        Restaurante restaurante = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    restaurante = mapResultSetToRestaurante(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el restaurante: " + e.getMessage());
        }
        
        return restaurante;
    }

    public boolean DeleteRestaurante(int id) throws SQLException {
        String query = "DELETE FROM restaurante WHERE id_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el restaurante: " + e.getMessage());
        }
    }

    public Restaurante UpdateRestaurante(Restaurante re) throws SQLException {
        String query = "UPDATE restaurante SET codigo_solicitud_aprobada = ?, codigo_zona = ?, nombre = ?, direccion = ?, horario = ?, telefono = ?, etiquetas = ? WHERE id_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, re.getSolicitud_aprobada());
            stmt.setInt(2, re.getCodigo_zona());
            stmt.setString(3, re.getNombre());
            stmt.setString(4, re.getDireccion());
            stmt.setString(5, re.getHorario());
            stmt.setString(6, re.getTelefono());
            stmt.setString(7, re.getEtiquetas());
            stmt.setInt(8, re.getIdRestaurante());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindRestaurante(re.getIdRestaurante());
            } else {
                throw new SQLException("No se encontró el restaurante con ID: " + re.getIdRestaurante());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el restaurante: " + e.getMessage());
        }
    }

    public List<Restaurante> ListAllRestaurantes() throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String query = "SELECT * FROM restaurante";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                restaurantes.add(mapResultSetToRestaurante(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los restaurantes: " + e.getMessage());
        }
        
        return restaurantes;
    }

    private Restaurante mapResultSetToRestaurante(ResultSet rs) throws SQLException {
        return new Restaurante(
            rs.getInt("id_restaurante"),
            rs.getInt("codigo_solicitud_aprobada"),
            rs.getInt("codigo_zona"),
            rs.getString("nombre"),
            rs.getString("direccion"),
            rs.getString("horario"),
            rs.getString("telefono"),
            rs.getString("etiquetas")
        );
    }
}
