package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Administrador;

public class AdminRI implements AdminRepository {
    
    @Override
    public void AddAdmin(Administrador ad) {
        String query = "INSERT INTO administrador(nombre, correo, contrasena, tipo, codigo_usuario) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, ad.getNombreU());
            ps.setString(2, ad.getCorreo());
            ps.setString(3, ad.getContrasena());
            ps.setString(4, ad.getTipo());
            ps.setInt(5, ad.getCodigo_admin());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al agregar administrador: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos", e);
        }
    }

    @Override
    public boolean FindAdmin(int id) {
        String query = "SELECT * FROM administrador WHERE codigo_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Retorna true si encuentra al menos un registro
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean DeleteAdmin(int id) {
        String query = "DELETE FROM administrador WHERE codigo_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0; // Retorna true si se eliminó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Administrador UpdateAdmin(Administrador ad) {
        String query = "UPDATE administrador SET nombre = ?, correo = ?, contrasena = ?, tipo = ? WHERE codigo_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, ad.getNombreU());
            ps.setString(2, ad.getCorreo());
            ps.setString(3, ad.getContrasena());
            ps.setString(4, ad.getTipo());
            ps.setInt(5, ad.getCodigo_admin());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                return ad; // Retorna el administrador actualizado
            } else {
                return null; // No se encontró el administrador
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar administrador: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos", e);
        }
    }

    @Override
    public void ListAllAdmins() {
        String query = "SELECT * FROM administrador";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("=== LISTA DE ADMINISTRADORES ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_usuario") + 
                                 ", Nombre: " + rs.getString("nombre") + 
                                 ", Correo: " + rs.getString("correo") + 
                                 ", Tipo: " + rs.getString("tipo") + 
                                 ", Código: " + rs.getInt("codigo_usuario"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar administradores: " + e.getMessage());
        }
    }
}
