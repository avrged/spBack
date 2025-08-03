package org.sazonpt.repository;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepository {

    private Connection getConnection() throws SQLException {
        return DBConfig.getDataSource().getConnection();
    }

    public List<Menu> findAll() throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            ORDER BY m.id_menu DESC
            """;

        List<Menu> menus = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                menus.add(mapRowToMenu(rs));
            }
        }
        return menus;
    }

    public Optional<Menu> findById(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            WHERE m.id_menu = ? AND m.id_restaurante = ? AND m.id_solicitud = ? AND m.id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMenu);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMenu(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Menu> findByRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            WHERE m.id_restaurante = ? AND m.id_solicitud = ? AND m.id_restaurantero = ?
            ORDER BY m.id_menu DESC
            """;

        List<Menu> menus = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapRowToMenu(rs));
                }
            }
        }
        return menus;
    }

    public List<Menu> findByRestaurantero(int idRestaurantero) throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            WHERE m.id_restaurantero = ?
            ORDER BY m.id_menu DESC
            """;

        List<Menu> menus = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapRowToMenu(rs));
                }
            }
        }
        return menus;
    }

    public List<Menu> findByEstado(String estado) throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            WHERE m.estado = ?
            ORDER BY m.id_menu DESC
            """;

        List<Menu> menus = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapRowToMenu(rs));
                }
            }
        }
        return menus;
    }

    public Menu save(Menu menu) throws SQLException {
        String sql = """
            INSERT INTO menu (ruta_archivo, estado, id_restaurante, id_solicitud, id_restaurantero) 
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Validar que el restaurante existe
            if (!restauranteExists(menu.getId_restaurante(), menu.getId_solicitud(), menu.getId_restaurantero())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, menu.getRuta_archivo());
            stmt.setString(2, menu.getEstado());
            stmt.setInt(3, menu.getId_restaurante());
            stmt.setInt(4, menu.getId_solicitud());
            stmt.setInt(5, menu.getId_restaurantero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el menú, no se insertaron filas");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    menu.setId_menu(generatedKeys.getInt(1));
                    return menu;
                } else {
                    throw new SQLException("Error al crear el menú, no se obtuvo el ID generado");
                }
            }
        }
    }

    public boolean update(Menu menu) throws SQLException {
        String sql = """
            UPDATE menu 
            SET ruta_archivo = ?, estado = ?
            WHERE id_menu = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Validar que el restaurante existe
            if (!restauranteExists(menu.getId_restaurante(), menu.getId_solicitud(), menu.getId_restaurantero())) {
                throw new SQLException("El restaurante especificado no existe");
            }

            stmt.setString(1, menu.getRuta_archivo());
            stmt.setString(2, menu.getEstado());
            stmt.setInt(3, menu.getId_menu());
            stmt.setInt(4, menu.getId_restaurante());
            stmt.setInt(5, menu.getId_solicitud());
            stmt.setInt(6, menu.getId_restaurantero());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateEstado(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero, String nuevoEstado) throws SQLException {
        String sql = """
            UPDATE menu 
            SET estado = ?
            WHERE id_menu = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idMenu);
            stmt.setInt(3, idRestaurante);
            stmt.setInt(4, idSolicitud);
            stmt.setInt(5, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            DELETE FROM menu 
            WHERE id_menu = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMenu);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT 1 FROM menu 
            WHERE id_menu = ? AND id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMenu);
            stmt.setInt(2, idRestaurante);
            stmt.setInt(3, idSolicitud);
            stmt.setInt(4, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean restauranteExists(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT 1 FROM restaurante 
            WHERE id_restaurante = ? AND id_solicitud = ? AND id_restaurantero = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Optional<Menu> findMenuActivoByRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) throws SQLException {
        String sql = """
            SELECT m.id_menu, m.ruta_archivo, m.estado, 
                   m.id_restaurante, m.id_solicitud, m.id_restaurantero
            FROM menu m
            INNER JOIN restaurante r ON m.id_restaurante = r.id_restaurante 
                AND m.id_solicitud = r.id_solicitud 
                AND m.id_restaurantero = r.id_restaurantero
            WHERE m.id_restaurante = ? AND m.id_solicitud = ? AND m.id_restaurantero = ? AND m.estado = 'activo'
            LIMIT 1
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            stmt.setInt(2, idSolicitud);
            stmt.setInt(3, idRestaurantero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMenu(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Menu mapRowToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId_menu(rs.getInt("id_menu"));
        menu.setRuta_archivo(rs.getString("ruta_archivo"));
        // Para mantener compatibilidad con el modelo, copiamos ruta_archivo a ruta_menu
        menu.setRuta_menu(rs.getString("ruta_archivo"));
        menu.setEstado(rs.getString("estado"));
        menu.setId_restaurante(rs.getInt("id_restaurante"));
        menu.setId_solicitud(rs.getInt("id_solicitud"));
        menu.setId_restaurantero(rs.getInt("id_restaurantero"));
        
        return menu;
    }
}
