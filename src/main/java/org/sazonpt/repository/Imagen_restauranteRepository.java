package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Imagen_restaurante;
import org.sazonpt.model.Imagen_restaurante.TipoImagen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Imagen_restauranteRepository {
    
    // Crear una nueva imagen de restaurante
    public Imagen_restaurante save(Imagen_restaurante imagen) {
        String sql = """
            INSERT INTO imagen_restaurante (id_restaurante, url, tipo, creado_en)
            VALUES (?, ?, ?, ?)
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, imagen.getId_restaurante());
            stmt.setString(2, imagen.getUrl());
            stmt.setString(3, imagen.getTipo().getValor());
            stmt.setTimestamp(4, Timestamp.valueOf(imagen.getCreado_en()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear imagen, no se insertaron filas.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    imagen.setId_imagen(generatedKeys.getInt(1));
                    return imagen;
                } else {
                    throw new SQLException("Error al crear imagen, no se obtuvo el ID.");
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage(), e);
        }
    }
    
    // Buscar imagen por ID
    public Optional<Imagen_restaurante> findById(int id) {
        String sql = """
            SELECT * FROM imagen_restaurante 
            WHERE id_imagen = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToImagen(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar imagen por ID: " + e.getMessage(), e);
        }
    }
    
    // Buscar todas las imágenes de un restaurante específico
    public List<Imagen_restaurante> findByRestauranteId(int restauranteId) {
        String sql = """
            SELECT * FROM imagen_restaurante 
            WHERE id_restaurante = ? 
            ORDER BY tipo, creado_en ASC
            """;
        
        List<Imagen_restaurante> imagenes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                imagenes.add(mapResultSetToImagen(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar imágenes por restaurante: " + e.getMessage(), e);
        }
        
        return imagenes;
    }
    
    // Buscar imágenes por tipo
    public List<Imagen_restaurante> findByTipo(TipoImagen tipo) {
        String sql = """
            SELECT * FROM imagen_restaurante 
            WHERE tipo = ? 
            ORDER BY creado_en DESC
            """;
        
        List<Imagen_restaurante> imagenes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo.getValor());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                imagenes.add(mapResultSetToImagen(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar imágenes por tipo: " + e.getMessage(), e);
        }
        
        return imagenes;
    }
    
    // Buscar imágenes por restaurante y tipo específico
    public List<Imagen_restaurante> findByRestauranteIdAndTipo(int restauranteId, TipoImagen tipo) {
        String sql = """
            SELECT * FROM imagen_restaurante 
            WHERE id_restaurante = ? AND tipo = ? 
            ORDER BY creado_en ASC
            """;
        
        List<Imagen_restaurante> imagenes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteId);
            stmt.setString(2, tipo.getValor());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                imagenes.add(mapResultSetToImagen(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar imágenes por restaurante y tipo: " + e.getMessage(), e);
        }
        
        return imagenes;
    }
    
    // Obtener imagen principal de un restaurante
    public Optional<Imagen_restaurante> findImagenPrincipal(int restauranteId) {
        List<Imagen_restaurante> imagenes = findByRestauranteIdAndTipo(restauranteId, TipoImagen.PRINCIPAL);
        return imagenes.isEmpty() ? Optional.empty() : Optional.of(imagenes.get(0));
    }
    
    // Obtener todas las imágenes
    public List<Imagen_restaurante> findAll() {
        String sql = """
            SELECT * FROM imagen_restaurante 
            ORDER BY id_restaurante, tipo, creado_en ASC
            """;
        
        List<Imagen_restaurante> imagenes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                imagenes.add(mapResultSetToImagen(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las imágenes: " + e.getMessage(), e);
        }
        
        return imagenes;
    }
    
    // Actualizar imagen
    public Imagen_restaurante update(Imagen_restaurante imagen) {
        String sql = """
            UPDATE imagen_restaurante 
            SET id_restaurante = ?, url = ?, tipo = ?
            WHERE id_imagen = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, imagen.getId_restaurante());
            stmt.setString(2, imagen.getUrl());
            stmt.setString(3, imagen.getTipo().getValor());
            stmt.setInt(4, imagen.getId_imagen());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar imagen, no se encontró el registro.");
            }
            
            return imagen;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar imagen: " + e.getMessage(), e);
        }
    }
    
    // Eliminar imagen por ID
    public boolean deleteById(int id) {
        String sql = """
            DELETE FROM imagen_restaurante 
            WHERE id_imagen = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage(), e);
        }
    }
    
    // Eliminar todas las imágenes de un restaurante
    public int deleteByRestauranteId(int restauranteId) {
        String sql = """
            DELETE FROM imagen_restaurante 
            WHERE id_restaurante = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteId);
            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar imágenes del restaurante: " + e.getMessage(), e);
        }
    }
    
    // Contar imágenes por restaurante
    public int countByRestauranteId(int restauranteId) {
        String sql = """
            SELECT COUNT(*) FROM imagen_restaurante 
            WHERE id_restaurante = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, restauranteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar imágenes: " + e.getMessage(), e);
        }
    }
    
    // Contar imágenes por tipo
    public int countByTipo(TipoImagen tipo) {
        String sql = """
            SELECT COUNT(*) FROM imagen_restaurante 
            WHERE tipo = ?
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo.getValor());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar imágenes por tipo: " + e.getMessage(), e);
        }
    }
    
    // Verificar si un restaurante tiene imagen principal
    public boolean tieneImagenPrincipal(int restauranteId) {
        return !findByRestauranteIdAndTipo(restauranteId, TipoImagen.PRINCIPAL).isEmpty();
    }
    
    // Método privado para mapear ResultSet a Imagen_restaurante
    private Imagen_restaurante mapResultSetToImagen(ResultSet rs) throws SQLException {
        Imagen_restaurante imagen = new Imagen_restaurante();
        
        imagen.setId_imagen(rs.getInt("id_imagen"));
        imagen.setId_restaurante(rs.getInt("id_restaurante"));
        imagen.setUrl(rs.getString("url"));
        
        // Convertir tipo de String a Enum
        String tipoStr = rs.getString("tipo");
        if (tipoStr != null) {
            imagen.setTipo(TipoImagen.fromValor(tipoStr));
        }
        
        imagen.setCreado_en(rs.getTimestamp("creado_en").toLocalDateTime());
        
        return imagen;
    }
}
