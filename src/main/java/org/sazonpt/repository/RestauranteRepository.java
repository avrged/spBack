package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurante.EstadoRestaurante;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestauranteRepository {
    
    // Crear un nuevo restaurante
    public Restaurante save(Restaurante restaurante) {
        String sql = """
            INSERT INTO restaurante (id_restaurantero, nombre, direccion, telefono, horario, menu_url, estado, creado_en, actualizado_en)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, restaurante.getId_restaurantero());
            stmt.setString(2, restaurante.getNombre());
            stmt.setString(3, restaurante.getDireccion());
            stmt.setString(4, restaurante.getTelefono());
            stmt.setString(5, restaurante.getHorario());
            stmt.setString(6, restaurante.getMenu_url());
            stmt.setString(7, restaurante.getEstado().getValor());
            stmt.setTimestamp(8, Timestamp.valueOf(restaurante.getCreado_en()));
            stmt.setTimestamp(9, Timestamp.valueOf(restaurante.getActualizado_en()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear restaurante, no se insertaron filas.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    restaurante.setId_restaurante(generatedKeys.getInt(1));
                    return restaurante;
                } else {
                    throw new SQLException("Error al crear restaurante, no se obtuvo el ID.");
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar restaurante: " + e.getMessage(), e);
        }
    }
    
    // Buscar restaurante por ID
    public Optional<Restaurante> findById(int id) {
        String sql = """
            SELECT * FROM restaurante 
            WHERE id_restaurante = ? AND eliminado_en IS NULL
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRestaurante(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar restaurante por ID: " + e.getMessage(), e);
        }
    }
    
    // Buscar restaurante por ID de restaurantero
    public Optional<Restaurante> findByRestauranteroId(int restauranteroId) {
        String sql = """
            SELECT * FROM restaurante 
            WHERE id_restaurantero = ? AND eliminado_en IS NULL
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteroId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRestaurante(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar restaurante por restaurantero: " + e.getMessage(), e);
        }
    }
    
    // Obtener todos los restaurantes
    public List<Restaurante> findAll() {
        String sql = """
            SELECT * FROM restaurante 
            WHERE eliminado_en IS NULL 
            ORDER BY creado_en DESC
            """;
        
        List<Restaurante> restaurantes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                restaurantes.add(mapResultSetToRestaurante(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los restaurantes: " + e.getMessage(), e);
        }
        
        return restaurantes;
    }
    
    // Obtener restaurantes por estado
    public List<Restaurante> findByEstado(EstadoRestaurante estado) {
        String sql = """
            SELECT * FROM restaurante 
            WHERE estado = ? AND eliminado_en IS NULL 
            ORDER BY creado_en DESC
            """;
        
        List<Restaurante> restaurantes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getValor());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                restaurantes.add(mapResultSetToRestaurante(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar restaurantes por estado: " + e.getMessage(), e);
        }
        
        return restaurantes;
    }
    
    // Actualizar restaurante
    public Restaurante update(Restaurante restaurante) {
        String sql = """
            UPDATE restaurante 
            SET nombre = ?, direccion = ?, telefono = ?, horario = ?, menu_url = ?, 
                estado = ?, aprobado_por = ?, aprobado_en = ?, actualizado_en = ?
            WHERE id_restaurante = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, restaurante.getNombre());
            stmt.setString(2, restaurante.getDireccion());
            stmt.setString(3, restaurante.getTelefono());
            stmt.setString(4, restaurante.getHorario());
            stmt.setString(5, restaurante.getMenu_url());
            stmt.setString(6, restaurante.getEstado().getValor());
            
            if (restaurante.getAprobado_por() != null) {
                stmt.setInt(7, restaurante.getAprobado_por());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            if (restaurante.getAprobado_en() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(restaurante.getAprobado_en()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }
            
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(10, restaurante.getId_restaurante());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar restaurante, no se encontró el registro.");
            }
            
            // Actualizar el timestamp en el objeto
            restaurante.setActualizado_en(LocalDateTime.now());
            return restaurante;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar restaurante: " + e.getMessage(), e);
        }
    }
    
    // Eliminar restaurante (soft delete)
    public boolean delete(int id) {
        String sql = """
            UPDATE restaurante 
            SET eliminado_en = ?, actualizado_en = ?
            WHERE id_restaurante = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setInt(3, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar restaurante: " + e.getMessage(), e);
        }
    }
    
    // Contar restaurantes por estado
    public int countByEstado(EstadoRestaurante estado) {
        String sql = """
            SELECT COUNT(*) FROM restaurante 
            WHERE estado = ? AND eliminado_en IS NULL
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
            throw new RuntimeException("Error al contar restaurantes por estado: " + e.getMessage(), e);
        }
    }
    
    // Buscar restaurantes por nombre (para búsqueda)
    public List<Restaurante> findByNombreContaining(String nombre) {
        String sql = """
            SELECT * FROM restaurante 
            WHERE nombre LIKE ? AND eliminado_en IS NULL 
            ORDER BY nombre
            """;
        
        List<Restaurante> restaurantes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                restaurantes.add(mapResultSetToRestaurante(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar restaurantes por nombre: " + e.getMessage(), e);
        }
        
        return restaurantes;
    }
    
    // Método privado para mapear ResultSet a Restaurante
    private Restaurante mapResultSetToRestaurante(ResultSet rs) throws SQLException {
        Restaurante restaurante = new Restaurante();
        
        restaurante.setId_restaurante(rs.getInt("id_restaurante"));
        restaurante.setId_restaurantero(rs.getInt("id_restaurantero"));
        restaurante.setNombre(rs.getString("nombre"));
        restaurante.setDireccion(rs.getString("direccion"));
        restaurante.setTelefono(rs.getString("telefono"));
        restaurante.setHorario(rs.getString("horario"));
        restaurante.setMenu_url(rs.getString("menu_url"));
        
        // Convertir estado de String a Enum
        String estadoStr = rs.getString("estado");
        if (estadoStr != null) {
            restaurante.setEstado(EstadoRestaurante.fromValor(estadoStr));
        }
        
        // Manejar campos que pueden ser null
        int aprobadoPor = rs.getInt("aprobado_por");
        if (!rs.wasNull()) {
            restaurante.setAprobado_por(aprobadoPor);
        }
        
        Timestamp aprobadoEn = rs.getTimestamp("aprobado_en");
        if (aprobadoEn != null) {
            restaurante.setAprobado_en(aprobadoEn.toLocalDateTime());
        }
        
        restaurante.setCreado_en(rs.getTimestamp("creado_en").toLocalDateTime());
        restaurante.setActualizado_en(rs.getTimestamp("actualizado_en").toLocalDateTime());
        
        Timestamp eliminadoEn = rs.getTimestamp("eliminado_en");
        if (eliminadoEn != null) {
            restaurante.setEliminado_en(eliminadoEn.toLocalDateTime());
        }
        
        return restaurante;
    }
}
