package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Imagen;

public class ImagenRepository {
    
    public void AddImage(Imagen i) throws SQLException {
        String query = "INSERT INTO imagen(codigo_restaurante, fecha_subida, ruta_archivo, estado) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, i.getCodigo_restarante());
            stmt.setDate(2, Date.valueOf(i.getFecha_subida()));
            stmt.setString(3, i.getRuta_archivoI());
            stmt.setBoolean(4, i.getEstado());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la imagen: " + e.getMessage());
        }
    }

    public Imagen FindImage(int id) throws SQLException {
        String query = "SELECT * FROM imagen WHERE id_imagen = ?";
        Imagen imagen = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imagen = mapResultSetToImagen(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la imagen: " + e.getMessage());
        }
        
        return imagen;
    }

    public boolean DeleteImage(int id) throws SQLException {
        String query = "DELETE FROM imagen WHERE id_imagen = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    public Imagen UpdateImage(Imagen i) throws SQLException {
        String query = "UPDATE imagen SET codigo_restaurante = ?, fecha_subida = ?, ruta_archivo = ?, estado = ? WHERE id_imagen = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, i.getCodigo_restarante());
            stmt.setDate(2, Date.valueOf(i.getFecha_subida()));
            stmt.setString(3, i.getRuta_archivoI());
            stmt.setBoolean(4, i.getEstado());
            stmt.setInt(5, i.getId_imagen());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindImage(i.getId_imagen());
            } else {
                throw new SQLException("No se encontró la imagen con ID: " + i.getId_imagen());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la imagen: " + e.getMessage());
        }
    }

    public List<Imagen> ListAllImagesRoute() throws SQLException {
        List<Imagen> imagenes = new ArrayList<>();
        String query = "SELECT * FROM imagen";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                imagenes.add(mapResultSetToImagen(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las imágenes: " + e.getMessage());
        }
        
        return imagenes;
    }

    public List<Imagen> ListImagesByRestaurant(int codigoRestaurante) throws SQLException {
        List<Imagen> imagenes = new ArrayList<>();
        String query = "SELECT * FROM imagen WHERE codigo_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, codigoRestaurante);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    imagenes.add(mapResultSetToImagen(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las imágenes del restaurante: " + e.getMessage());
        }
        
        return imagenes;
    }

    private Imagen mapResultSetToImagen(ResultSet rs) throws SQLException {
        return new Imagen(
            rs.getInt("id_imagen"),
            rs.getInt("codigo_restaurante"),
            rs.getDate("fecha_subida").toLocalDate(),
            rs.getString("ruta_archivo"),
            rs.getBoolean("estado")
        );
    }
}
