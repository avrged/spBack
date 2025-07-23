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
    public Usuario findByCorreo(String correo) throws SQLException {
        String query = "SELECT * FROM usuario WHERE correo = ?;";
        Usuario user = null;
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, correo);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    user = mapResulsetToUser(rs);
                }
            }
        } catch(SQLException e){
            throw new SQLException("Error al buscar usuario por correo: "+e.getMessage());
        }
        return user;
    }
    public int save(Usuario user) throws SQLException {
        if (findByCorreo(user.getCorreo()) != null) {
            throw new SQLException("Ya existe un usuario con ese correo");
        }
        String query = "INSERT INTO usuario(nombre, correo, contrasena, tipo, status) VALUES (?, ?, ?, ?, ?);";
        int id_usuario = -1;
        System.out.println("[UsuarioRepository.save] Recibido:");
        System.out.println("nombre: " + user.getNombre());
        System.out.println("correo: " + user.getCorreo());
        System.out.println("contrasena: " + user.getContrasena());
        System.out.println("tipo: " + user.getTipo());
        System.out.println("status: " + user.getStatus());
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getCorreo());
            stmt.setString(3, user.getContrasena());
            stmt.setString(4, user.getTipo());
            stmt.setString(5, user.getStatus());
            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    id_usuario = generatedKeys.getInt(1);
                }
            } catch(SQLException e){
                System.out.println("[UsuarioRepository.save] Error al obtener el ID generado: " + e.getMessage());
                throw new SQLException("Error al crear el ID del usuario creado: " + e.getMessage());
            }

        } catch(SQLException e){
            System.out.println("[UsuarioRepository.save] Error SQL: " + e.getMessage());
            throw new SQLException("Error al crear el usuario: "+e.getMessage());
        }
        System.out.println("[UsuarioRepository.save] id_usuario generado: " + id_usuario);
        return id_usuario;
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

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getCorreo());
            stmt.setString(3, user.getContrasena());
            stmt.setString(4, user.getTipo());
            stmt.setString(5, user.getStatus());
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
            conn.setAutoCommit(false);
            
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

            if ("administrador".equals(tipoUsuario)) {
                String queryAdmin = "DELETE FROM administrador WHERE id_usuario = ?";
                PreparedStatement stmtAdmin = conn.prepareStatement(queryAdmin);
                stmtAdmin.setInt(1, idUser);
                int adminRows = stmtAdmin.executeUpdate();
                System.out.println("Filas eliminadas de administrador: " + adminRows);
            } else if ("restaurantero".equals(tipoUsuario)) {
                String queryRest = "DELETE FROM restaurantero WHERE id_usuario = ?";
                PreparedStatement stmtRest = conn.prepareStatement(queryRest);
                stmtRest.setInt(1, idUser);
                int restRows = stmtRest.executeUpdate();
                System.out.println("Filas eliminadas de restaurantero: " + restRows);
            }

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

    public Usuario findByCorreoAndContrasenaAndRol(String correo, String contrasena, String rol) throws SQLException {
        String query = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ? AND tipo = ?;";
        Usuario user = null;
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            stmt.setString(3, rol);
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

    private Usuario mapResulsetToUser(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getString("status")
        );
    }
}
