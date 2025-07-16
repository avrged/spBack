package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Usuario;

public class UsuarioRepository {
    public void save(Usuario user) throws SQLException {
        String query = "INSERT INTO usuario(nombre, correo, contrasena, tipo, status) VALUES (?, ?, ?, ?, ?);";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, user.getNombreU());
            stmt.setString(2, user.getCorreo());
            stmt.setString(3, user.getContrasena());
            stmt.setString(4, user.getTipo());
            stmt.setInt(5, 1);
            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al crear el usuario: "+e.getMessage());
        }
    }

    public Usuario findByIdUser(int idUser) throws SQLException {
        String query = "SELECT * FROM usuario WHERE id_usuario= ?;";
        Usuario user = null;

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, idUser);

                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()){
                        user = mapResulsetToUser(rs);
                    }
                }
        } catch(SQLException e){
            throw new SQLException("Error al buscar el usuario: "+e.getMessage());
        } 
        return user;
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> users = new ArrayList<>();
        String query = "SELECT * FROM usuario";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResulsetToUser(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los usuarios: " + e.getMessage());
        }
        
        return users;
    }

    public void UpdateUser(Usuario user) throws SQLException{
        String query = "UPDATE usuario SET nombre= ?, correo= ?, contrasena= ?, tipo= ?, status= ? WHERE id_usuario= ?;";
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, user.getNombreU());
            stmt.setString(2, user.getCorreo());
            stmt.setString(3, user.getContrasena());
            stmt.setString(4, user.getTipo());
            stmt.setInt(5, user.getStatus());
            stmt.setInt(6, user.getId_usuario());

            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al actualizar el usuario: "+e.getMessage());
        }
    }

    public void deleteUser(int idUser) throws SQLException{
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Usar transacción
            
            // Primero verificar el tipo de usuario
            String queryTipo = "SELECT tipo FROM usuario WHERE id_usuario = ?";
            PreparedStatement stmtTipo = conn.prepareStatement(queryTipo);
            stmtTipo.setInt(1, idUser);
            ResultSet rs = stmtTipo.executeQuery();
            
            String tipoUsuario = null;
            if (rs.next()) {
                tipoUsuario = rs.getString("tipo");
            } else {
                throw new SQLException("No se encontró el usuario con ID: " + idUser);
            }
            
            System.out.println("Eliminando usuario ID: " + idUser + ", Tipo: " + tipoUsuario);
            
            // Eliminar de tablas hijas primero según el tipo
            if ("administrador".equals(tipoUsuario)) {
                String queryAdmin = "DELETE FROM administrador WHERE codigo_usuario = ?";
                PreparedStatement stmtAdmin = conn.prepareStatement(queryAdmin);
                stmtAdmin.setInt(1, idUser);
                int adminRows = stmtAdmin.executeUpdate();
                System.out.println("Filas eliminadas de administrador: " + adminRows);
            } else if ("restaurantero".equals(tipoUsuario)) {
                String queryRest = "DELETE FROM restaurantero WHERE codigo_usuario = ?";
                PreparedStatement stmtRest = conn.prepareStatement(queryRest);
                stmtRest.setInt(1, idUser);
                int restRows = stmtRest.executeUpdate();
                System.out.println("Filas eliminadas de restaurantero: " + restRows);
            }
            
            // Finalmente eliminar de la tabla usuario
            String queryUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
            PreparedStatement stmtUsuario = conn.prepareStatement(queryUsuario);
            stmtUsuario.setInt(1, idUser);
            int rowsAffected = stmtUsuario.executeUpdate();
            
            System.out.println("Query executed: " + queryUsuario);
            System.out.println("User ID: " + idUser);
            System.out.println("Rows affected by delete: " + rowsAffected);
            
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo eliminar el usuario con ID: " + idUser);
            }
            
            conn.commit();
            
        } catch(SQLException e){
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al eliminar el usuario: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private Usuario mapResulsetToUser(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getInt("status")
        );
    }
}
