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
        
        String query = "INSERT INTO solicitud_registro(" +
                "id_restaurantero, propietario, correo, numero, direccion, horario, " +
                "imagen1, imagen2, imagen3, comprobante, fecha, estado, restaurante) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, soliR.getId_restaurantero());
            stmt.setString(2, soliR.getPropietario());
            stmt.setString(3, soliR.getCorreo());
            stmt.setString(4, soliR.getNumero());
            stmt.setString(5, soliR.getDireccion());
            stmt.setString(6, soliR.getHorario());
            stmt.setString(7, soliR.getImagen1());
            stmt.setString(8, soliR.getImagen2());
            stmt.setString(9, soliR.getImagen3());
            stmt.setString(10, soliR.getComprobante());
            stmt.setDate(11, Date.valueOf(soliR.getFecha()));
            stmt.setString(12, soliR.getEstado());
            stmt.setString(13, soliR.getRestaurante());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear la solicitud de registro: " + e.getMessage());
        }
    }
    
    private boolean existeRestaurantero(int idRestaurantero) throws SQLException {
        String query = "SELECT COUNT(*) FROM restaurantero WHERE id_usuario = ?";
        
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
        String query = "UPDATE solicitud_registro SET " +
                "id_restaurantero = ?, propietario = ?, correo = ?, numero = ?, direccion = ?, horario = ?, " +
                "imagen1 = ?, imagen2 = ?, imagen3 = ?, comprobante = ?, fecha = ?, estado = ?, restaurante = ? " +
                "WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, soliR.getId_restaurantero());
            stmt.setString(2, soliR.getPropietario());
            stmt.setString(3, soliR.getCorreo());
            stmt.setString(4, soliR.getNumero());
            stmt.setString(5, soliR.getDireccion());
            stmt.setString(6, soliR.getHorario());
            stmt.setString(7, soliR.getImagen1());
            stmt.setString(8, soliR.getImagen2());
            stmt.setString(9, soliR.getImagen3());
            stmt.setString(10, soliR.getComprobante());
            stmt.setDate(11, Date.valueOf(soliR.getFecha()));
            stmt.setString(12, soliR.getEstado());
            stmt.setString(13, soliR.getRestaurante());
            stmt.setInt(14, soliR.getId_solicitud());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return FindSolicitudR(soliR.getId_solicitud());
            } else {
                throw new SQLException("No se encontró la solicitud con ID: " + soliR.getId_solicitud());
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
        Solicitud_registro solicitud = new Solicitud_registro(
            rs.getInt("id_solicitud"),
            rs.getInt("id_restaurantero"),
            rs.getDate("fecha").toLocalDate(),
            rs.getString("estado"),
            rs.getString("restaurante"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("imagen1"),
            rs.getString("imagen2"),
            rs.getString("imagen3"),
            rs.getString("comprobante"),
            rs.getString("propietario"),
            rs.getString("numero"),
            rs.getString("horario")
        );
        return solicitud;
    }
}
