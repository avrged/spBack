package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Solicitud_registro;
import org.sazonpt.model.Solicitud_registro.EstadoSolicitud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de solicitudes de registro
 * Mapea la tabla 'solicitud_registro' de la base de datos
 */
public class Solicitud_registroRepository {
    
    // Crear una nueva solicitud de registro
    public Solicitud_registro save(Solicitud_registro solicitud) {
        String sql = """
            INSERT INTO solicitud_registro (id_restaurantero, estado, datos_restaurante, creado_en)
            VALUES (?, ?, ?, ?)
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, solicitud.getId_restaurantero());
            stmt.setString(2, solicitud.getEstado().getValor());
            stmt.setString(3, solicitud.getDatos_restaurante());
            stmt.setTimestamp(4, Timestamp.valueOf(solicitud.getCreado_en()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear solicitud, no se insertaron filas.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    solicitud.setId_solicitud(generatedKeys.getInt(1));
                    return solicitud;
                } else {
                    throw new SQLException("Error al crear solicitud, no se obtuvo el ID.");
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar solicitud: " + e.getMessage(), e);
        }
    }
    
    // Buscar solicitud por ID
    public Optional<Solicitud_registro> findById(int id) {
        String sql = """
            SELECT * FROM solicitud_registro 
            WHERE id_solicitud = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSolicitud(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitud por ID: " + e.getMessage(), e);
        }
    }
    
    // Buscar solicitud por ID de restaurantero
    public Optional<Solicitud_registro> findByRestauranteroId(int restauranteroId) {
        String sql = """
            SELECT * FROM solicitud_registro 
            WHERE id_restaurantero = ? 
            ORDER BY creado_en DESC 
            LIMIT 1
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteroId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSolicitud(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitud por restaurantero: " + e.getMessage(), e);
        }
    }
    
    // Obtener todas las solicitudes
    public List<Solicitud_registro> findAll() {
        String sql = """
            SELECT * FROM solicitud_registro 
            ORDER BY creado_en DESC
            """;
        
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las solicitudes: " + e.getMessage(), e);
        }
        
        return solicitudes;
    }
    
    // Obtener solicitudes por estado
    public List<Solicitud_registro> findByEstado(EstadoSolicitud estado) {
        String sql = """
            SELECT * FROM solicitud_registro 
            WHERE estado = ? 
            ORDER BY creado_en DESC
            """;
        
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getValor());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitudes por estado: " + e.getMessage(), e);
        }
        
        return solicitudes;
    }
    
    // Obtener solicitudes pendientes
    public List<Solicitud_registro> findPendientes() {
        return findByEstado(EstadoSolicitud.PENDIENTE);
    }
    
    // Obtener solicitudes aprobadas
    public List<Solicitud_registro> findAprobadas() {
        return findByEstado(EstadoSolicitud.APROBADO);
    }
    
    // Obtener solicitudes rechazadas
    public List<Solicitud_registro> findRechazadas() {
        return findByEstado(EstadoSolicitud.RECHAZADO);
    }
    
    // Actualizar solicitud
    public Solicitud_registro update(Solicitud_registro solicitud) {
        String sql = """
            UPDATE solicitud_registro 
            SET estado = ?, datos_restaurante = ?, revisado_por = ?, revisado_en = ?
            WHERE id_solicitud = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, solicitud.getEstado().getValor());
            stmt.setString(2, solicitud.getDatos_restaurante());
            
            if (solicitud.getRevisado_por() != null) {
                stmt.setInt(3, solicitud.getRevisado_por());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (solicitud.getRevisado_en() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(solicitud.getRevisado_en()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            
            stmt.setInt(5, solicitud.getId_solicitud());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar solicitud, no se encontró el registro.");
            }
            
            return solicitud;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar solicitud: " + e.getMessage(), e);
        }
    }
    
    // Verificar si el restaurantero ya tiene una solicitud pendiente
    public boolean hasPendingSolicitud(int restauranteroId) {
        String sql = """
            SELECT COUNT(*) FROM solicitud_registro 
            WHERE id_restaurantero = ? AND estado = 'pendiente'
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteroId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar solicitud pendiente: " + e.getMessage(), e);
        }
    }
    
    // Verificar si el restaurantero tiene una solicitud aprobada
    public boolean hasApprovedSolicitud(int restauranteroId) {
        String sql = """
            SELECT COUNT(*) FROM solicitud_registro 
            WHERE id_restaurantero = ? AND estado = 'aprobado'
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteroId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar solicitud aprobada: " + e.getMessage(), e);
        }
    }
    
    // Contar solicitudes por estado
    public int countByEstado(EstadoSolicitud estado) {
        String sql = """
            SELECT COUNT(*) FROM solicitud_registro 
            WHERE estado = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getValor());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar solicitudes por estado: " + e.getMessage(), e);
        }
    }
    
    // Obtener solicitudes con información del restaurantero (JOIN)
    public List<SolicitudConRestaurantero> findAllWithRestauranteroInfo() {
        String sql = """
            SELECT s.*, u.nombre, u.email, u.telefono, r.rfc, r.verificado
            FROM solicitud_registro s
            INNER JOIN restaurantero r ON s.id_restaurantero = r.id_restaurantero
            INNER JOIN usuario u ON r.id_restaurantero = u.id_usuario
            ORDER BY s.creado_en DESC
            """;
        
        List<SolicitudConRestaurantero> solicitudes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SolicitudConRestaurantero solicitud = new SolicitudConRestaurantero();
                
                // Datos de la solicitud
                solicitud.setSolicitud(mapResultSetToSolicitud(rs));
                
                // Datos del usuario/restaurantero
                solicitud.setNombreRestaurantero(rs.getString("nombre"));
                solicitud.setEmailRestaurantero(rs.getString("email"));
                solicitud.setTelefonoRestaurantero(rs.getString("telefono"));
                solicitud.setRfcRestaurantero(rs.getString("rfc"));
                solicitud.setVerificado(rs.getBoolean("verificado"));
                
                solicitudes.add(solicitud);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener solicitudes con info de restaurantero: " + e.getMessage(), e);
        }
        
        return solicitudes;
    }
    
    // Método privado para mapear ResultSet a Solicitud_registro
    private Solicitud_registro mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        Solicitud_registro solicitud = new Solicitud_registro();
        
        solicitud.setId_solicitud(rs.getInt("id_solicitud"));
        solicitud.setId_restaurantero(rs.getInt("id_restaurantero"));
        
        // Convertir estado de String a Enum
        String estadoStr = rs.getString("estado");
        if (estadoStr != null) {
            solicitud.setEstado(EstadoSolicitud.fromValor(estadoStr));
        }
        
        solicitud.setDatos_restaurante(rs.getString("datos_restaurante"));
        
        // Manejar campos que pueden ser null
        int revisadoPor = rs.getInt("revisado_por");
        if (!rs.wasNull()) {
            solicitud.setRevisado_por(revisadoPor);
        }
        
        Timestamp revisadoEn = rs.getTimestamp("revisado_en");
        if (revisadoEn != null) {
            solicitud.setRevisado_en(revisadoEn.toLocalDateTime());
        }
        
        solicitud.setCreado_en(rs.getTimestamp("creado_en").toLocalDateTime());
        
        return solicitud;
    }
    
    // Clase auxiliar para solicitudes con información del restaurantero
    public static class SolicitudConRestaurantero {
        private Solicitud_registro solicitud;
        private String nombreRestaurantero;
        private String emailRestaurantero;
        private String telefonoRestaurantero;
        private String rfcRestaurantero;
        private boolean verificado;
        
        // Getters y Setters
        public Solicitud_registro getSolicitud() { return solicitud; }
        public void setSolicitud(Solicitud_registro solicitud) { this.solicitud = solicitud; }
        
        public String getNombreRestaurantero() { return nombreRestaurantero; }
        public void setNombreRestaurantero(String nombreRestaurantero) { this.nombreRestaurantero = nombreRestaurantero; }
        
        public String getEmailRestaurantero() { return emailRestaurantero; }
        public void setEmailRestaurantero(String emailRestaurantero) { this.emailRestaurantero = emailRestaurantero; }
        
        public String getTelefonoRestaurantero() { return telefonoRestaurantero; }
        public void setTelefonoRestaurantero(String telefonoRestaurantero) { this.telefonoRestaurantero = telefonoRestaurantero; }
        
        public String getRfcRestaurantero() { return rfcRestaurantero; }
        public void setRfcRestaurantero(String rfcRestaurantero) { this.rfcRestaurantero = rfcRestaurantero; }
        
        public boolean isVerificado() { return verificado; }
        public void setVerificado(boolean verificado) { this.verificado = verificado; }
    }
}
