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
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);
            
            // Primero insertar en tabla usuario
            String queryUsuario = "INSERT INTO usuario(nombre, correo, contrasena, tipo, status) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(queryUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtUsuario.setString(1, admin.getNombreU());
            stmtUsuario.setString(2, admin.getCorreo());
            stmtUsuario.setString(3, admin.getContrasena());
            stmtUsuario.setString(4, "administrador");
            stmtUsuario.setInt(5, 1);
            stmtUsuario.executeUpdate();
            
            // Obtener el ID generado
            ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
            int idUsuario = 0;
            if (generatedKeys.next()) {
                idUsuario = generatedKeys.getInt(1);
            }
            
            // Luego insertar en tabla administrador
            String queryAdmin = "INSERT INTO administrador(codigo_usuario) VALUES(?)";
            PreparedStatement stmtAdmin = conn.prepareStatement(queryAdmin);
            stmtAdmin.setInt(1, idUsuario);
            stmtAdmin.executeUpdate();
            
            conn.commit();
            
        } catch(SQLException e){
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al crear el administrador: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public Administrador findAdminById(int idAdmin) throws SQLException {
        String query = "SELECT u.*, a.codigo_usuario FROM usuario u " +
                      "INNER JOIN administrador a ON u.id_usuario = a.codigo_usuario " +
                      "WHERE u.id_usuario = ? AND u.status = 1";
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
        String query = "SELECT u.*, a.codigo_usuario FROM usuario u " +
                      "INNER JOIN administrador a ON u.id_usuario = a.codigo_usuario " +
                      "WHERE u.status = 1";
        
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
        String query = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, status = ? WHERE id_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, admin.getNombreU());
            stmt.setString(2, admin.getCorreo());
            stmt.setString(3, admin.getContrasena());
            stmt.setInt(4, admin.getStatus());
            stmt.setInt(5, admin.getId_usuario());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el administrador: " + e.getMessage());
        }
    }

    public void deleteAdmin(int idAdmin) throws SQLException {
        String query = "UPDATE usuario SET status = 0 WHERE id_usuario = ?";
        
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
