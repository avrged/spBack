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
        String query = "INSERT INTO administrador(id_usuario) VALUES(?)";
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, admin.getId_usuario());
            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al crear el administrador: "+e.getMessage());
        }
    }

    public Administrador findAdminById(int idAdmin) throws SQLException {
        String query = "SELECT u.*, a.id_usuario as admin_id_usuario FROM usuario u " +
                      "INNER JOIN administrador a ON u.id_usuario = a.id_usuario " +
                      "WHERE u.id_usuario = ? AND u.status = 'activo'";
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
        String query = "SELECT u.*, a.id_usuario as admin_id_usuario FROM usuario u " +
                      "INNER JOIN administrador a ON u.id_usuario = a.id_usuario " +
                      "WHERE u.status = 'activo'";
        
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
            
            stmt.setString(1, admin.getNombre());
            stmt.setString(2, admin.getCorreo());
            stmt.setString(3, admin.getContrasena());
            stmt.setString(4, admin.getStatus());
            stmt.setInt(5, admin.getId_usuario());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el administrador: " + e.getMessage());
        }
    }

    public void deleteAdmin(int idAdmin) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Usar transacción para múltiples DELETE
            
            // Primero eliminar de la tabla administrador
            String queryAdmin = "DELETE FROM administrador WHERE id_usuario = ?";
            PreparedStatement stmtAdmin = conn.prepareStatement(queryAdmin);
            stmtAdmin.setInt(1, idAdmin);
            int rowsAffectedAdmin = stmtAdmin.executeUpdate();
            
            System.out.println("Query executed: " + queryAdmin);
            System.out.println("Admin rows affected: " + rowsAffectedAdmin);
            
            // Luego eliminar de la tabla usuario
            String queryUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
            PreparedStatement stmtUsuario = conn.prepareStatement(queryUsuario);
            stmtUsuario.setInt(1, idAdmin);
            int rowsAffectedUsuario = stmtUsuario.executeUpdate();
            
            System.out.println("Query executed: " + queryUsuario);
            System.out.println("Admin ID: " + idAdmin);
            System.out.println("Usuario rows affected: " + rowsAffectedUsuario);
            
            if (rowsAffectedUsuario == 0) {
                throw new SQLException("No se encontró el administrador con ID: " + idAdmin);
            }
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al eliminar el administrador: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private Administrador mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Administrador(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getInt("admin_id_usuario"),
            rs.getString("status")
        );
    }
}
