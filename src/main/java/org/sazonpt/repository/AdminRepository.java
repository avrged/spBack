package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Administrador;

public class AdminRepository {
    
    public void createAdmin(Administrador admin) throws SQLException {
        String query = "INSERT INTO administrador (id_usuario, nombreU, correo, contrasena, tipo, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, admin.getId_usuario());
            stmt.setString(2, admin.getNombreU());
            stmt.setString(3, admin.getCorreo());
            stmt.setString(4, admin.getContrasena());
            stmt.setString(5, admin.getTipo());
            stmt.setInt(6, 1); // Status activo por defecto
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear el administrador: " + e.getMessage());
        }
    }

    public Administrador findAdminById(int idAdmin) throws SQLException {
        String query = "SELECT * FROM administrador WHERE codigo_usuario = ?";
        Administrador admin = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idAdmin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el administrador: " + e.getMessage());
        }
        
        return admin;
    }

    public List<Administrador> getAllAdmins() throws SQLException {
        List<Administrador> admins = new ArrayList<>();
        String query = "SELECT * FROM administrador WHERE status= 1";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los administradores: " + e.getMessage());
        }
        
        return admins;
    }

    public void updateAdmin(Administrador admin) throws SQLException {
        String query = "UPDATE administrador SET nombre = ?, correo = ?, contrasena = ?, tipo = ?, status= ? WHERE id_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, admin.getNombreU());
            stmt.setString(2, admin.getCorreo());
            stmt.setString(3, admin.getContrasena());
            stmt.setString(4, admin.getTipo());
            stmt.setInt(5, admin.getId_usuario());
            stmt.setInt(6, admin.getStatus());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el administrador: " + e.getMessage());
        }
    }

    public void deleteAdmin(int idAdmin) throws SQLException {
        String query = "UPDATE administrador SET status = 0 WHERE codigo_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idAdmin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el administrador: " + e.getMessage());
        }
    }

    private Administrador mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Administrador(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getInt("codigo_usuario"),
            rs.getInt("status")
        );
    }
}
