package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Usuario;
import org.sazonpt.model.Administrador;
import org.sazonpt.model.Restaurantero;

public class UsuarioRepository {

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> users = new ArrayList<>();
        String query = """
            SELECT 
                u.id_usuario, u.nombre, u.email AS correo, u.password_hash AS contrasena,
                IFNULL(u.activo, TRUE) AS status,
                a.nivel_permiso,
                r.rfc, r.verificado
            FROM usuario u
            LEFT JOIN administrador a ON u.id_usuario = a.id_administrador
            LEFT JOIN restaurantero r ON u.id_usuario = r.id_restaurantero
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error al listar los usuarios: " + e.getMessage(), e);
        }

        return users;
    }

    private Usuario mapResultSetToUser(ResultSet rs) throws SQLException {
        Usuario user = new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getBoolean("status") ? "activo" : "inactivo"
        );

        String nivelPermiso = rs.getString("nivel_permiso");
        if (nivelPermiso != null) {
            user.setAdministrador(new Administrador(nivelPermiso));
        }

        String rfc = rs.getString("rfc");
        if (rfc != null) {
            boolean verificado = rs.getBoolean("verificado");
            user.setRestaurantero(new Restaurantero(rfc, verificado));
        }

        return user;
    }
}
