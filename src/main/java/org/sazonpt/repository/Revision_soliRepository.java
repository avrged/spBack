package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Revision_solicitud;
import org.sazonpt.model.Solicitud_registro;

public class Revision_soliRepository {
    
    public void AddSoli(Solicitud_registro soli) throws SQLException {
        String query = "INSERT INTO solicitud_registro(id_restaurantero, fecha, estado, restaurante, correo, direccion, imagen1, imagen2, imagen3, comprobante) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, soli.getId_restaurantero());
            stmt.setDate(2, Date.valueOf(soli.getFecha()));
            stmt.setString(3, soli.getEstado());
            stmt.setString(4, soli.getRestaurante());
            stmt.setString(5, soli.getCorreo());
            stmt.setString(6, soli.getDireccion());
            stmt.setString(7, soli.getImagen1());
            stmt.setString(8, soli.getImagen2());
            stmt.setString(9, soli.getImagen3());
            stmt.setString(10, soli.getComprobante());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la solicitud: " + e.getMessage());
        }
    }

    public Solicitud_registro FindSoli(int id) throws SQLException {
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
            throw new SQLException("Error al buscar la solicitud: " + e.getMessage());
        }
        
        return solicitud;
    }

    public boolean DeleteSoli(int id) throws SQLException {
        String query = "DELETE FROM solicitud_registro WHERE id_solicitud = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la solicitud: " + e.getMessage());
        }
    }

    public Revision_solicitud UpdateRevision(Revision_solicitud revision) throws SQLException {
        String query = "UPDATE revision_solicitud SET id_solicitud = ?, id_admin = ?, fecha = ?, status = ? WHERE id_revision = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, revision.getId_solicitud());
            stmt.setInt(2, revision.getId_admin());
            stmt.setDate(3, Date.valueOf(revision.getFecha()));
            stmt.setString(4, revision.getStatus());
            stmt.setInt(5, revision.getId_revision());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return findRevisionById(revision.getId_revision());
            } else {
                throw new SQLException("No se encontró la revisión con ID: " + revision.getId_revision());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la revisión: " + e.getMessage());
        }
    }

    // Métodos adicionales para gestión completa

    // Método para crear una nueva revisión
    public void AddRevision(Revision_solicitud revision) throws SQLException {
        // Verificar que la solicitud existe
        if (!existeSolicitud(revision.getId_solicitud())) {
            throw new SQLException("Error: No existe una solicitud con id: " + revision.getId_solicitud());
        }
        
        // Verificar que el admin existe
        if (!existeAdmin(revision.getId_admin())) {
            throw new SQLException("Error: No existe un administrador con id: " + revision.getId_admin());
        }
        
        String query = "INSERT INTO revision_solicitud(id_solicitud, id_admin, fecha, status) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, revision.getId_solicitud());
            stmt.setInt(2, revision.getId_admin());
            stmt.setDate(3, Date.valueOf(revision.getFecha()));
            stmt.setString(4, revision.getStatus());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la revisión: " + e.getMessage());
        }
    }
    
    private boolean existeSolicitud(int idSolicitud) throws SQLException {
        String query = "SELECT COUNT(*) FROM solicitud_registro WHERE id_solicitud = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idSolicitud);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private boolean existeAdmin(int codigoAdmin) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuario WHERE id_usuario = ? AND tipo = 'administrador'";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, codigoAdmin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Revision_solicitud findRevisionById(int id) throws SQLException {
        String query = "SELECT * FROM revision_solicitud WHERE id_revision = ?";
        Revision_solicitud revision = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    revision = mapResultSetToRevision(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la revisión: " + e.getMessage());
        }
        
        return revision;
    }

    public List<Revision_solicitud> findAllRevisiones() throws SQLException {
        List<Revision_solicitud> revisiones = new ArrayList<>();
        String query = "SELECT * FROM revision_solicitud";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                revisiones.add(mapResultSetToRevision(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las revisiones: " + e.getMessage());
        }
        
        return revisiones;
    }

    public List<Solicitud_registro> findAllSolicitudes() throws SQLException {
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        String query = "SELECT * FROM solicitud_registro";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las solicitudes: " + e.getMessage());
        }
        
        return solicitudes;
    }

    private Solicitud_registro mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        return new Solicitud_registro(
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
    }

    private Revision_solicitud mapResultSetToRevision(ResultSet rs) throws SQLException {
        return new Revision_solicitud(
            rs.getInt("id_revision"),
            rs.getInt("id_solicitud"),
            rs.getInt("id_admin"),
            rs.getDate("fecha").toLocalDate(),
            rs.getString("status")
        );
    }
}
