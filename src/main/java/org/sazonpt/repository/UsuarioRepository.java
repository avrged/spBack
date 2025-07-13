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
    public void CreateUser(Usuario user) throws SQLException{
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

    public Usuario findUserById(int idUser) throws SQLException{
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

    public List<Usuario> getAllUsers()throws SQLException{
        List<Usuario> users = new ArrayList<>();
        String query = "SELECT * FROM usuario WHERE status= 1;";
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    users.add(mapResulsetToUser(rs));
                }
        } catch(SQLException e){
            throw new SQLException("Error al listar los usuarios: "+e.getMessage());
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

            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al actualizar el usuario: "+e.getMessage());
        }
    }

    public void deleteUser(int idUser) throws SQLException{
        String query = "UPDATE usuario SET status= 0 WHERE id_usuario= ?;";
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, idUser);
                stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al eliminar el usuario: "+e.getMessage());
        }
    }

    private Usuario mapResulsetToUser(ResultSet rs) throws SQLException{
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
