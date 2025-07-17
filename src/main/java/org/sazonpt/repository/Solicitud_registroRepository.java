package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Solicitud_registro;

public class Solicitud_registroRepository {
    
    public void AddSolicitudR(Solicitud_registro soliR) throws SQLException {
        // Primero verificar si el restaurantero existe
        if (!existeRestaurantero(soliR.getId_restaurantero())) {
            throw new SQLException("Error: No existe un restaurantero con id: " + soliR.getId_restaurantero() + 
                                 ". Debe ejecutar la migración primero o verificar que el restaurantero exista.");
        }
        
        String query = "INSERT INTO solicitud_registro(id_restaurantero, fecha, estado, nombre_propuesto_restaurante, correo, direccion_propuesta) VALUES(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, soliR.getId_restaurantero());
            stmt.setDate(2, Date.valueOf(soliR.getFecha()));
            stmt.setBoolean(3, soliR.getEstado());
            stmt.setString(4, soliR.getNombrePropuesto());
            stmt.setString(5, soliR.getCorreo());
            stmt.setString(6, soliR.getDireccionPropuesta());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la solicitud de registro: " + e.getMessage());
        }
    }
    
    private boolean existeRestaurantero(int idRestaurantero) throws SQLException {
        String query = "SELECT COUNT(*) FROM restaurantero WHERE codigo_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Solicitud_registro FindSolicitudR(int id) throws SQLException {
        String query = "SELECT * FROM solicitud_registro WHERE id_solicitud = ?";
        Solicitud_registro solicitud = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    solicitud = mapResultSetToSolicitud(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la solicitud de registro: " + e.getMessage());
        }
        
        return solicitud;
    }

    public boolean DeleteSolicitudR(int id) throws SQLException {
        String query = "DELETE FROM solicitud_registro WHERE id_solicitud = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la solicitud de registro: " + e.getMessage());
        }
    }

    public Solicitud_registro UpdateSolicitud(Solicitud_registro soliR) throws SQLException {
        String query = "UPDATE solicitud_registro SET id_restaurantero = ?, fecha = ?, estado = ?, nombre_propuesto_restaurante = ?, correo = ?, direccion_propuesta = ? WHERE id_solicitud = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, soliR.getId_restaurantero());
            stmt.setDate(2, Date.valueOf(soliR.getFecha()));
            stmt.setBoolean(3, soliR.getEstado());
            stmt.setString(4, soliR.getNombrePropuesto());
            stmt.setString(5, soliR.getCorreo());
            stmt.setString(6, soliR.getDireccionPropuesta());
            stmt.setInt(7, soliR.getSolicitudR());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindSolicitudR(soliR.getSolicitudR());
            } else {
                throw new SQLException("No se encontró la solicitud de registro con ID: " + soliR.getSolicitudR());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la solicitud de registro: " + e.getMessage());
        }
    }

    public List<Solicitud_registro> ListAllSolicitudes() throws SQLException {
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        String query = "SELECT * FROM solicitud_registro";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las solicitudes de registro: " + e.getMessage());
        }
        
        return solicitudes;
    }

    private Solicitud_registro mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        return new Solicitud_registro(
            rs.getInt("id_solicitud"),
            rs.getInt("id_restaurantero"),
            rs.getDate("fecha").toLocalDate(),
            rs.getBoolean("estado"),
            rs.getString("nombre_propuesto_restaurante"),
            rs.getString("correo"),
            rs.getString("direccion_propuesta")
        );
    }
}
