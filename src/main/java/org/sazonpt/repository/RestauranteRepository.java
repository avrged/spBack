package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurante;
import org.sazonpt.model.Restaurantero;

public class RestauranteRepository {
    
    public void AddRestaurante(Restaurante re) throws SQLException {
        String query = "INSERT INTO restaurante(id_solicitud_aprobada, id_zona, nombre, direccion, horario, telefono, etiquetas, imagen1, imagen2, imagen3, facebook, instagram) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, re.getId_solicitud_aprobada());
            stmt.setInt(2, re.getId_zona());
            stmt.setString(3, re.getNombre());
            stmt.setString(4, re.getDireccion());
            stmt.setString(5, re.getHorario());
            stmt.setString(6, re.getTelefono());
            stmt.setString(7, re.getEtiquetas());
            stmt.setString(8, re.getImagen1());
            stmt.setString(9, re.getImagen2());
            stmt.setString(10, re.getImagen3());
            stmt.setString(11, re.getFacebook());
            stmt.setString(12, re.getInstagram());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear el restaurante: " + e.getMessage());
        }
    }

    public Restaurante FindRestaurante(int id) throws SQLException {
        String query = "SELECT * FROM restaurante WHERE id_restaurante = ?";
        Restaurante restaurante = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    restaurante = mapResultSetToRestaurante(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el restaurante: " + e.getMessage());
        }
        
        return restaurante;
    }

    public boolean DeleteRestaurante(int id) throws SQLException {
        String query = "DELETE FROM restaurante WHERE id_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el restaurante: " + e.getMessage());
        }
    }

    public Restaurante UpdateRestaurante(Restaurante re) throws SQLException {
        String query = "UPDATE restaurante SET id_solicitud_aprobada = ?, id_zona = ?, nombre = ?, direccion = ?, horario = ?, telefono = ?, etiquetas = ?, imagen1 = ?, imagen2 = ?, imagen3 = ?, facebook = ?, instagram = ? WHERE id_restaurante = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, re.getId_solicitud_aprobada());
            stmt.setInt(2, re.getId_zona());
            stmt.setString(3, re.getNombre());
            stmt.setString(4, re.getDireccion());
            stmt.setString(5, re.getHorario());
            stmt.setString(6, re.getTelefono());
            stmt.setString(7, re.getEtiquetas());
            stmt.setString(8, re.getImagen1());
            stmt.setString(9, re.getImagen2());
            stmt.setString(10, re.getImagen3());
            stmt.setString(11, re.getFacebook());
            stmt.setString(12, re.getInstagram());
            stmt.setInt(13, re.getIdRestaurante());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return FindRestaurante(re.getIdRestaurante());
            } else {
                throw new SQLException("No se encontró el restaurante con ID: " + re.getIdRestaurante());
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el restaurante: " + e.getMessage());
        }
    }

    public List<Restaurante> ListAllRestaurantes() throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String query = "SELECT * FROM restaurante";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                restaurantes.add(mapResultSetToRestaurante(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los restaurantes: " + e.getMessage());
        }
        
        return restaurantes;
    }

    // Método para obtener el dueño (restaurantero) de un restaurante
    public Restaurantero findDuenoByRestauranteId(int idRestaurante) throws SQLException {
        String query = """
            SELECT u.*, r.id_usuario as restaurantero_id_usuario 
            FROM usuario u 
            INNER JOIN restaurantero r ON u.id_usuario = r.id_usuario 
            INNER JOIN solicitud_registro sr ON r.id_usuario = sr.id_restaurantero 
            INNER JOIN restaurante rest ON sr.id_solicitud = rest.id_solicitud_aprobada 
            WHERE rest.id_restaurante = ? AND u.status = 'activo'
            """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idRestaurante);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Restaurantero(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getInt("restaurantero_id_usuario"),
                        rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el dueño del restaurante: " + e.getMessage());
        }
        return null;
    }

    // Método para obtener todos los restaurantes de un restaurantero específico
    public List<Restaurante> findRestaurantesByDueno(int idRestaurantero) throws SQLException {
        String query = """
            SELECT rest.* 
            FROM restaurante rest 
            INNER JOIN solicitud_registro sr ON rest.id_solicitud_aprobada = sr.id_solicitud 
            WHERE sr.id_restaurantero = ?
            """;
        
        List<Restaurante> restaurantes = new ArrayList<>();
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    restaurantes.add(mapResultSetToRestaurante(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar restaurantes por dueño: " + e.getMessage());
        }
        return restaurantes;
    }

    // Método para obtener todos los restaurantes de un usuario específico (por ID de usuario)
    public List<Restaurante> findRestaurantesByUsuario(int idUsuario) throws SQLException {
        String query = """
            SELECT rest.* 
            FROM restaurante rest 
            INNER JOIN solicitud_registro sr ON rest.id_solicitud_aprobada = sr.id_solicitud 
            INNER JOIN restaurantero r ON sr.id_restaurantero = r.id_usuario 
            WHERE r.id_usuario = ?
            """;

        List<Restaurante> restaurantes = new ArrayList<>();

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    restaurantes.add(mapResultSetToRestaurante(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar restaurantes por usuario: " + e.getMessage());
        }
        return restaurantes;
    }

    // Método para obtener información completa del restaurante con su dueño
    public class RestauranteConDueno {
        private Restaurante restaurante;
        private Restaurantero dueno;
        
        public RestauranteConDueno(Restaurante restaurante, Restaurantero dueno) {
            this.restaurante = restaurante;
            this.dueno = dueno;
        }
        
        public Restaurante getRestaurante() { return restaurante; }
        public Restaurantero getDueno() { return dueno; }
        public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
        public void setDueno(Restaurantero dueno) { this.dueno = dueno; }
    }

    public RestauranteConDueno findRestauranteConDueno(int idRestaurante) throws SQLException {
        Restaurante restaurante = FindRestaurante(idRestaurante);
        if (restaurante != null) {
            Restaurantero dueno = findDuenoByRestauranteId(idRestaurante);
            return new RestauranteConDueno(restaurante, dueno);
        }
        return null;
    }

    private Restaurante mapResultSetToRestaurante(ResultSet rs) throws SQLException {
        return new Restaurante(
            rs.getInt("id_restaurante"),
            rs.getInt("id_solicitud_aprobada"),
            rs.getInt("id_zona"),
            rs.getString("nombre"),
            rs.getString("direccion"),
            rs.getString("horario"),
            rs.getString("telefono"),
            rs.getString("etiquetas"),
            rs.getString("imagen1"),
            rs.getString("imagen2"),
            rs.getString("imagen3"),
            rs.getString("facebook"),
            rs.getString("instagram")
        );
    }
}
