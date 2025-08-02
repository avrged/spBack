package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    
    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuario";
        try (
                Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTipo(rs.getString("tipo"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public Usuario findByIdUsuario(int idUsuario) throws SQLException {
        Usuario usuario = null;
        String query = "SELECT * FROM usuario WHERE id_usuario = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setTipo(rs.getString("tipo"));
                }
            }
        }

        return usuario;
    }

    public Usuario findByCorreo(String correo) throws SQLException {
        Usuario usuario = null;
        String query = "SELECT * FROM usuario WHERE correo = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setTipo(rs.getString("tipo"));
                }
            }
        }

        return usuario;
    }

    public Usuario save(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (nombre, correo, contrasena, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setString(4, usuario.getTipo());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el usuario, no se insertaron filas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId_usuario(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Error al crear el usuario, no se obtuvo el ID.");
                }
            }
        }
        return usuario;
    }

    public void update(Usuario usuario) throws SQLException {
        String query = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, tipo = ? WHERE id_usuario = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setString(4, usuario.getTipo());
            stmt.setInt(5, usuario.getId_usuario());
            stmt.executeUpdate();
        }
    }

    public boolean delete(int idUsuario) throws SQLException {
        String query = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
