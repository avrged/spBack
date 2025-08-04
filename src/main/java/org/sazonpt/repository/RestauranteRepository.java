package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestauranteRepository {

    public List<Restaurante> findAll() throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = """
            SELECT r.id_restaurante, r.nombre, r.horario, r.telefono, r.etiquetas,
                   r.id_solicitud, r.id_restaurantero, r.id_zona, r.direccion, r.facebook, r.instagram,
                   sr.nombre_propuesto_restaurante, u.nombre as restaurantero_nombre
            FROM restaurante r
            INNER JOIN solicitud_registro sr ON r.id_solicitud = sr.id_solicitud 
                AND r.id_restaurantero = sr.id_restaurantero
            INNER JOIN restaurantero rt ON r.id_restaurantero = rt.id_usuario
            INNER JOIN usuario u ON rt.id_usuario = u.id_usuario
            ORDER BY r.nombre
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setNombre(rs.getString("nombre"));
                restaurante.setHorario(rs.getString("horario"));
                restaurante.setTelefono(rs.getString("telefono"));
                restaurante.setEtiquetas(rs.getString("etiquetas"));
                restaurante.setId_solicitud(rs.getInt("id_solicitud"));
                restaurante.setId_restaurantero(rs.getInt("id_restaurantero"));
                restaurante.setId_zona(rs.getInt("id_zona"));
                restaurante.setDireccion(rs.getString("direccion"));
                restaurante.setFacebook(rs.getString("facebook"));
                restaurante.setInstagram(rs.getString("instagram"));
                restaurantes.add(restaurante);
            }
        }
        return restaurantes;
    }

    public Optional<Restaurante> findById(int id) throws SQLException {
        String sql = """
            SELECT r.id_restaurante, r.nombre, r.horario, r.telefono, r.etiquetas,
                   r.id_solicitud, r.id_restaurantero, r.id_zona,
                   sr.nombre_propuesto_restaurante, u.nombre as restaurantero_nombre
            FROM restaurante r
            INNER JOIN solicitud_registro sr ON r.id_solicitud = sr.id_solicitud 
                AND r.id_restaurantero = sr.id_restaurantero
            INNER JOIN restaurantero rt ON r.id_restaurantero = rt.id_usuario
            INNER JOIN usuario u ON rt.id_usuario = u.id_usuario
            WHERE r.id_restaurante = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setNombre(rs.getString("nombre"));
                restaurante.setHorario(rs.getString("horario"));
                restaurante.setTelefono(rs.getString("telefono"));
                restaurante.setEtiquetas(rs.getString("etiquetas"));
                restaurante.setId_solicitud(rs.getInt("id_solicitud"));
                restaurante.setId_restaurantero(rs.getInt("id_restaurantero"));
                restaurante.setId_zona(rs.getInt("id_zona"));
                
                return Optional.of(restaurante);
            }
        }
        return Optional.empty();
    }

    public List<Restaurante> findByRestaurantero(int idRestaurantero) throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = """
            SELECT r.id_restaurante, r.nombre, r.horario, r.telefono, r.etiquetas,
                   r.id_solicitud, r.id_restaurantero, r.id_zona, r.direccion, r.facebook, r.instagram
            FROM restaurante r
            WHERE r.id_restaurantero = ?
            ORDER BY r.nombre
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setNombre(rs.getString("nombre"));
                restaurante.setHorario(rs.getString("horario"));
                restaurante.setTelefono(rs.getString("telefono"));
                restaurante.setEtiquetas(rs.getString("etiquetas"));
                restaurante.setId_solicitud(rs.getInt("id_solicitud"));
                restaurante.setId_restaurantero(rs.getInt("id_restaurantero"));
                restaurante.setId_zona(rs.getInt("id_zona"));
                restaurante.setDireccion(rs.getString("direccion"));
                restaurante.setFacebook(rs.getString("facebook"));
                restaurante.setInstagram(rs.getString("instagram"));
                restaurantes.add(restaurante);
            }
        }
        return restaurantes;
    }

    public List<Restaurante> findByZona(int idZona) throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = """
            SELECT r.id_restaurante, r.nombre, r.horario, r.telefono, r.etiquetas,
                   r.id_solicitud, r.id_restaurantero, r.id_zona
            FROM restaurante r
            WHERE r.id_zona = ?
            ORDER BY r.nombre
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setNombre(rs.getString("nombre"));
                restaurante.setHorario(rs.getString("horario"));
                restaurante.setTelefono(rs.getString("telefono"));
                restaurante.setEtiquetas(rs.getString("etiquetas"));
                restaurante.setId_solicitud(rs.getInt("id_solicitud"));
                restaurante.setId_restaurantero(rs.getInt("id_restaurantero"));
                restaurante.setId_zona(rs.getInt("id_zona"));
                
                restaurantes.add(restaurante);
            }
        }
        return restaurantes;
    }

    public Restaurante save(Restaurante restaurante) throws SQLException {
        String sql = """
            INSERT INTO restaurante (nombre, horario, telefono, etiquetas, 
                                   id_solicitud, id_restaurantero, id_zona, direccion, facebook, instagram)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, restaurante.getNombre());
            stmt.setString(2, restaurante.getHorario());
            stmt.setString(3, restaurante.getTelefono());
            stmt.setString(4, restaurante.getEtiquetas());
            stmt.setInt(5, restaurante.getId_solicitud());
            stmt.setInt(6, restaurante.getId_restaurantero());
            stmt.setInt(7, restaurante.getId_zona());
            stmt.setString(8, restaurante.getDireccion());
            stmt.setString(9, restaurante.getFacebook());
            stmt.setString(10, restaurante.getInstagram());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el restaurante");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                restaurante.setId_restaurante(generatedKeys.getInt(1));
            }
        }
        return restaurante;
    }

    public boolean update(Restaurante restaurante) throws SQLException {
        String sql = """
            UPDATE restaurante 
            SET nombre = ?, horario = ?, telefono = ?, etiquetas = ?, id_zona = ?, direccion = ?, facebook = ?, instagram = ?
            WHERE id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, restaurante.getNombre());
            stmt.setString(2, restaurante.getHorario());
            stmt.setString(3, restaurante.getTelefono());
            stmt.setString(4, restaurante.getEtiquetas());
            stmt.setInt(5, restaurante.getId_zona());
            stmt.setString(6, restaurante.getDireccion());
            stmt.setString(7, restaurante.getFacebook());
            stmt.setString(8, restaurante.getInstagram());
            stmt.setInt(9, restaurante.getId_restaurante());
            stmt.setInt(10, restaurante.getId_solicitud());
            stmt.setInt(11, restaurante.getId_restaurantero());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateByRestaurantero(int idRestaurantero, Restaurante restaurante) throws SQLException {
        String sql = "UPDATE restaurante SET nombre=?, horario=?, telefono=?, etiquetas=?, direccion=?, facebook=?, instagram=? WHERE id_restaurantero=?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurante.getNombre());
            stmt.setString(2, restaurante.getHorario());
            stmt.setString(3, restaurante.getTelefono());
            stmt.setString(4, restaurante.getEtiquetas());
            stmt.setString(5, restaurante.getDireccion());
            stmt.setString(6, restaurante.getFacebook());
            stmt.setString(7, restaurante.getInstagram());
            stmt.setInt(8, idRestaurantero);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            DELETE FROM restaurante 
            WHERE id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM restaurante WHERE id_restaurante = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean existsBySolicitud(int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = "SELECT 1 FROM restaurante WHERE id_solicitud = ? AND id_restaurantero = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            stmt.setInt(2, idRestaurantero);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean solicitudExists(int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = "SELECT 1 FROM solicitud_registro WHERE id_solicitud = ? AND id_restaurantero = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitud);
            stmt.setInt(2, idRestaurantero);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean zonaExists(int idZona) throws SQLException {
        String sql = "SELECT 1 FROM zona WHERE id_zona = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
