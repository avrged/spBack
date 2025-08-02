package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Administrador;
import org.sazonpt.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorRepository {
    
    public List<Administrador> findAll() throws SQLException {
        List<Administrador> administradores = new ArrayList<>();
        String query = """
            SELECT a.id_usuario, u.nombre, u.correo, u.contrasena, u.tipo 
            FROM administrador a 
            INNER JOIN usuario u ON a.id_usuario = u.id_usuario
            """;
            
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTipo(rs.getString("tipo"));
                
                Administrador administrador = new Administrador(usuario);
                administradores.add(administrador);
            }
        }
        return administradores;
    }

    public Administrador findByIdUsuario(int idUsuario) throws SQLException {
        String query = """
            SELECT a.id_usuario, u.nombre, u.correo, u.contrasena, u.tipo 
            FROM administrador a 
            INNER JOIN usuario u ON a.id_usuario = u.id_usuario 
            WHERE a.id_usuario = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setTipo(rs.getString("tipo"));
                    
                    return new Administrador(usuario);
                }
            }
        }
        return null;
    }

    public Administrador findByCorreo(String correo) throws SQLException {
        String query = """
            SELECT a.id_usuario, u.nombre, u.correo, u.contrasena, u.tipo 
            FROM administrador a 
            INNER JOIN usuario u ON a.id_usuario = u.id_usuario 
            WHERE u.correo = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setTipo(rs.getString("tipo"));
                    
                    return new Administrador(usuario);
                }
            }
        }
        return null;
    }

    public void save(Administrador administrador) throws SQLException {
        String query = "INSERT INTO administrador (id_usuario) VALUES (?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, administrador.getId_usuario());
            stmt.executeUpdate();
        }
    }

    public boolean delete(int idUsuario) throws SQLException {
        String query = "DELETE FROM administrador WHERE id_usuario = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean existsById(int idUsuario) throws SQLException {
        String query = "SELECT 1 FROM administrador WHERE id_usuario = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Administrador> findAllWithFullUserData() throws SQLException {
        List<Administrador> administradores = new ArrayList<>();
        String query = """
            SELECT a.id_usuario, u.nombre, u.correo, u.contrasena, u.tipo 
            FROM administrador a 
            INNER JOIN usuario u ON a.id_usuario = u.id_usuario
            ORDER BY u.nombre
            """;
            
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTipo(rs.getString("tipo"));
                
                Administrador administrador = new Administrador(usuario);
                administradores.add(administrador);
            }
        }
        return administradores;
    }

    public void insertAdministrador(int idUsuario) throws SQLException {
        String query = "INSERT INTO administrador (id_usuario) VALUES (?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }
}
