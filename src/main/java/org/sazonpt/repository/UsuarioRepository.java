package org.sazonpt.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Usuario;

public class UsuarioRepository {

    public int save(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (email, password_hash, nombre, telefono, avatar_url, activo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getPassword_hash());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getTelefono());
            stmt.setString(5, usuario.getAvatar_url());
            // Asegurar que siempre sea true para nuevos usuarios
            stmt.setBoolean(6, true);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Error al crear el usuario");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del usuario creado");
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public Usuario findById(int id) throws SQLException {
        String query = """
            SELECT id_usuario, email, password_hash, nombre, telefono, avatar_url, 
                   activo, creado_en, actualizado_en, eliminado_en
            FROM usuario 
            WHERE id_usuario = ? AND eliminado_en IS NULL
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el usuario: " + e.getMessage());
        }
        
        return null;
    }

    public Usuario findByEmail(String email) throws SQLException {
        String query = """
            SELECT id_usuario, email, password_hash, nombre, telefono, avatar_url, 
                   activo, creado_en, actualizado_en, eliminado_en
            FROM usuario 
            WHERE email = ? AND eliminado_en IS NULL
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el usuario por email: " + e.getMessage());
        }
        
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = """
            SELECT id_usuario, email, password_hash, nombre, telefono, avatar_url, 
                   activo, creado_en, actualizado_en, eliminado_en
            FROM usuario 
            WHERE activo = TRUE AND eliminado_en IS NULL
            ORDER BY nombre ASC
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error al listar los usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public boolean update(Usuario usuario) throws SQLException {
        String query = """
            UPDATE usuario 
            SET email = ?, nombre = ?, telefono = ?, avatar_url = ?, actualizado_en = CURRENT_TIMESTAMP 
            WHERE id_usuario = ? AND activo = TRUE
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getAvatar_url());
            stmt.setInt(5, usuario.getId_usuario());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public boolean softDelete(int id) throws SQLException {
        String query = """
            UPDATE usuario 
            SET activo = FALSE, eliminado_en = CURRENT_TIMESTAMP, actualizado_en = CURRENT_TIMESTAMP 
            WHERE id_usuario = ?
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al marcar usuario como eliminado: " + e.getMessage());
        }
    }

    public int count() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM usuario WHERE activo = TRUE AND eliminado_en IS NULL";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al contar usuarios: " + e.getMessage());
        }
        
        return 0;
    }

    public List<Object[]> findAllWithRoles() throws SQLException {
        List<Object[]> usuarios = new ArrayList<>();
        String query = """
            SELECT 
                u.id_usuario, u.email, u.nombre, u.telefono, u.activo,
                a.nivel_permiso,
                r.rfc, r.verificado,
                CASE 
                    WHEN a.id_administrador IS NOT NULL THEN 'Administrador'
                    WHEN r.id_restaurantero IS NOT NULL THEN 'Restaurantero'
                    ELSE 'Usuario'
                END AS tipo_usuario
            FROM usuario u
            LEFT JOIN administrador a ON u.id_usuario = a.id_administrador
            LEFT JOIN restaurantero r ON u.id_usuario = r.id_restaurantero
            WHERE u.activo = TRUE AND u.eliminado_en IS NULL
            ORDER BY u.nombre ASC
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] userData = {
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getBoolean("activo"),
                    rs.getString("nivel_permiso"),
                    rs.getString("rfc"),
                    rs.getBoolean("verificado"),
                    rs.getString("tipo_usuario")
                };
                usuarios.add(userData);
            }

        } catch (SQLException e) {
            throw new SQLException("Error al listar usuarios con roles: " + e.getMessage());
        }

        return usuarios;
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword_hash(rs.getString("password_hash"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setAvatar_url(rs.getString("avatar_url"));
        usuario.setActivo(rs.getBoolean("activo"));
        
        // Mapear timestamps
        Timestamp creadoEn = rs.getTimestamp("creado_en");
        if (creadoEn != null) {
            usuario.setCreado_en(creadoEn.toLocalDateTime());
        }
        
        Timestamp actualizadoEn = rs.getTimestamp("actualizado_en");
        if (actualizadoEn != null) {
            usuario.setActualizado_en(actualizadoEn.toLocalDateTime());
        }
        
        Timestamp eliminadoEn = rs.getTimestamp("eliminado_en");
        if (eliminadoEn != null) {
            usuario.setEliminado_en(eliminadoEn.toLocalDateTime());
        }
        
        return usuario;
    }
    
    /**
     * Obtiene la información completa de un usuario incluyendo su tipo
     * @param email Email del usuario
     * @return Información completa del usuario o null si no existe
     * @throws SQLException Si hay error en la base de datos
     */
    public Object[] findUserWithRoleByEmail(String email) throws SQLException {
        String query = """
            SELECT 
                u.id_usuario,
                u.email,
                u.nombre,
                u.telefono,
                u.avatar_url,
                u.activo,
                u.creado_en,
                u.actualizado_en,
                a.nivel_permiso,
                r.rfc,
                r.verificado,
                CASE 
                    WHEN a.id_administrador IS NOT NULL THEN 'Administrador'
                    WHEN r.id_restaurantero IS NOT NULL THEN 'Restaurantero'
                    ELSE 'Usuario'
                END AS tipo_usuario
            FROM usuario u
            LEFT JOIN administrador a ON u.id_usuario = a.id_administrador
            LEFT JOIN restaurantero r ON u.id_usuario = r.id_restaurantero
            WHERE u.email = ? AND u.activo = TRUE AND u.eliminado_en IS NULL
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email.toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("avatar_url"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creado_en"),
                        rs.getTimestamp("actualizado_en"),
                        rs.getString("nivel_permiso"),
                        rs.getString("rfc"),
                        rs.getBoolean("verificado"),
                        rs.getString("tipo_usuario")
                    };
                }
            }
        }

        return null;
    }
}
