package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Administrador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    public int save(Administrador administrador) throws SQLException {
        if (administrador.getNivel_permiso() == Administrador.NivelPermiso.SUPER && existeAdminSuper()) {
            throw new IllegalArgumentException("Ya existe un administrador con nivel SUPER. Solo puede haber uno.");
        }
        
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdmin = null;
        ResultSet rs = null;
        
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);
            
            String queryUsuario = "INSERT INTO usuario (email, password_hash, nombre, telefono, activo) VALUES (?, ?, ?, ?, TRUE)";
            stmtUsuario = conn.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS);
            stmtUsuario.setString(1, administrador.getEmail());
            stmtUsuario.setString(2, administrador.getPassword_hash());
            stmtUsuario.setString(3, administrador.getNombre());
            stmtUsuario.setString(4, administrador.getTelefono());
            
            int rowsAffected = stmtUsuario.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Error al crear el usuario");
            }
            
            rs = stmtUsuario.getGeneratedKeys();
            int idUsuario = 0;
            if (rs.next()) {
                idUsuario = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del usuario creado");
            }
            
            String queryAdmin = "INSERT INTO administrador (id_administrador, nivel_permiso) VALUES (?, ?)";
            stmtAdmin = conn.prepareStatement(queryAdmin);
            stmtAdmin.setInt(1, idUsuario);
            stmtAdmin.setString(2, administrador.getNivel_permiso().getValor());
            
            stmtAdmin.executeUpdate();
            
            conn.commit();
            return idUsuario;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error al crear el administrador: " + e.getMessage());
        } finally {
            closeResources(rs, stmtUsuario, stmtAdmin, conn);
        }
    }

    public Administrador findById(int id) throws SQLException {
        String query = """
            SELECT u.id_usuario, u.email, u.nombre, u.telefono, u.password_hash, 
                   u.avatar_url, u.activo, u.creado_en, u.actualizado_en, u.eliminado_en,
                   a.nivel_permiso
            FROM administrador a
            JOIN usuario u ON u.id_usuario = a.id_administrador
            WHERE u.id_usuario = ? AND u.activo = TRUE
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el administrador: " + e.getMessage());
        }
        
        return null;
    }

    public Administrador findByEmail(String email) throws SQLException {
        String query = """
            SELECT u.id_usuario, u.email, u.nombre, u.telefono, u.password_hash, 
                   u.avatar_url, u.activo, u.creado_en, u.actualizado_en, u.eliminado_en,
                   a.nivel_permiso
            FROM administrador a
            JOIN usuario u ON u.id_usuario = a.id_administrador
            WHERE u.email = ? AND u.activo = TRUE
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el administrador por email: " + e.getMessage());
        }
        
        return null;
    }

    public List<Administrador> findAll() throws SQLException {
        List<Administrador> administradores = new ArrayList<>();
        
        String query = """
            SELECT u.id_usuario, u.email, u.nombre, u.telefono, u.password_hash, 
                   u.avatar_url, u.activo, u.creado_en, u.actualizado_en, u.eliminado_en,
                   a.nivel_permiso
            FROM administrador a
            JOIN usuario u ON u.id_usuario = a.id_administrador
            WHERE u.activo = TRUE
            ORDER BY a.nivel_permiso DESC, u.nombre ASC
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                administradores.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los administradores: " + e.getMessage());
        }
        
        return administradores;
    }

    public boolean update(Administrador administrador) throws SQLException {
        // Validar que no se cambie a SUPER si ya existe otro
        if (administrador.getNivel_permiso() == Administrador.NivelPermiso.SUPER) {
            Administrador adminActual = findById(administrador.getId_usuario());
            if (adminActual != null && adminActual.getNivel_permiso() != Administrador.NivelPermiso.SUPER) {
                if (existeAdminSuper()) {
                    throw new IllegalArgumentException("Ya existe un administrador con nivel SUPER. Solo puede haber uno.");
                }
            }
        }
        
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdmin = null;
        
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);

            String queryUsuario = "UPDATE usuario SET email = ?, nombre = ?, telefono = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id_usuario = ?";
            stmtUsuario = conn.prepareStatement(queryUsuario);
            stmtUsuario.setString(1, administrador.getEmail());
            stmtUsuario.setString(2, administrador.getNombre());
            stmtUsuario.setString(3, administrador.getTelefono());
            stmtUsuario.setInt(4, administrador.getId_usuario());
            
            int rowsUsuario = stmtUsuario.executeUpdate();

            String queryAdmin = "UPDATE administrador SET nivel_permiso = ? WHERE id_administrador = ?";
            stmtAdmin = conn.prepareStatement(queryAdmin);
            stmtAdmin.setString(1, administrador.getNivel_permiso().getValor());
            stmtAdmin.setInt(2, administrador.getId_usuario());
            
            int rowsAdmin = stmtAdmin.executeUpdate();
            
            conn.commit();
            return (rowsUsuario > 0 && rowsAdmin > 0);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error al actualizar el administrador: " + e.getMessage());
        } finally {
            closeResources(null, stmtUsuario, stmtAdmin, conn);
        }
    }

    public boolean delete(int id) throws SQLException {
        // Verificar que no sea el administrador SUPER
        Administrador admin = findById(id);
        if (admin != null && admin.esSuper()) {
            throw new IllegalArgumentException("No se puede eliminar al administrador SUPER");
        }
        
        String query = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el administrador: " + e.getMessage());
        }
    }
    
    public int count() throws SQLException {
        String query = """
            SELECT COUNT(*) AS total
            FROM administrador a
            JOIN usuario u ON u.id_usuario = a.id_administrador
            WHERE u.activo = TRUE
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al contar administradores: " + e.getMessage());
        }
        
        return 0;
    }
    
    public boolean existeAdminSuper() throws SQLException {
        String query = "SELECT COUNT(*) FROM administrador WHERE nivel_permiso = 'super'";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al verificar administrador SUPER: " + e.getMessage());
        }
        
        return false;
    }

    private Administrador mapResultSetToEntity(ResultSet rs) throws SQLException {
        Administrador admin = new Administrador();

        admin.setId_usuario(rs.getInt("id_usuario"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword_hash(rs.getString("password_hash"));
        admin.setNombre(rs.getString("nombre"));
        admin.setTelefono(rs.getString("telefono"));
        admin.setAvatar_url(rs.getString("avatar_url"));
        admin.setActivo(rs.getBoolean("activo"));

        Timestamp creadoEn = rs.getTimestamp("creado_en");
        if (creadoEn != null) {
            admin.setCreado_en(creadoEn.toLocalDateTime());
        }
        
        Timestamp actualizadoEn = rs.getTimestamp("actualizado_en");
        if (actualizadoEn != null) {
            admin.setActualizado_en(actualizadoEn.toLocalDateTime());
        }
        
        Timestamp eliminadoEn = rs.getTimestamp("eliminado_en");
        if (eliminadoEn != null) {
            admin.setEliminado_en(eliminadoEn.toLocalDateTime());
        }

        String nivelPermiso = rs.getString("nivel_permiso");
        if (nivelPermiso != null) {
            admin.setNivel_permiso(Administrador.NivelPermiso.fromValor(nivelPermiso));
        }
        
        return admin;
    }
    
    private void closeResources(ResultSet rs, PreparedStatement... statements) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        for (PreparedStatement stmt : statements) {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    private void closeResources(ResultSet rs, PreparedStatement stmt1, PreparedStatement stmt2, Connection conn) {
        closeResources(rs, stmt1, stmt2);
        
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
