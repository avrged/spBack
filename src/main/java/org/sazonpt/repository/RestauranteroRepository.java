package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Restaurantero;

public class RestauranteroRepository {
    public void CreateRestaurantero(Restaurantero reo) throws SQLException{
        String query = "INSERT INTO restaurantero(nombre, correo, contrasena, tipo, status) VALUES(?, ?, ?, ?, ?);";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, reo.getNombreU());
            stmt.setString(2, reo.getCorreo());
            stmt.setString(3, reo.getContrasena());
            stmt.setString(4, reo.getTipo());
            stmt.setInt(5, 1);

            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al crear el restaurantero: "+e.getMessage());
        }
    }

    public Restaurantero findRestauranteroById(int idReo) throws SQLException{
        String query = "SELECT * FROM restaurantero WHERE codigo_usuario= ?;";
        Restaurantero reo = null;

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, idReo);

                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()){
                        reo = mapResulsetToRestaurantero(rs);
                    }
                }
            } catch(SQLException e){
                throw new SQLException("Error al encontrar el restaurantero: "+e.getMessage());
            }
        return reo;
    }

    public List<Restaurantero> getAllRestauranteros() throws SQLException{
        List<Restaurantero> restauranteros = new ArrayList<>();
        String query = "SELECT * FROM restaurantero WHERE status= 1;";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    restauranteros.add(mapResulsetToRestaurantero(rs));
                }
        } catch(SQLException e){
            throw new SQLException("Error al intentar listar los restauranteros: "+e.getMessage());
        }
        return restauranteros;
    }


    public void UpdateRestaurantero(Restaurantero reo) throws SQLException{
        String query = "UPDATE restaurantero SET nombre= ?, correo= ?, contrasena= ?, tipo= ?, status= ? WHERE codigo_usuario= ?";
        try(Connection conn =DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, reo.getNombreU());
            stmt.setString(2, reo.getCorreo());
            stmt.setString(3, reo.getContrasena());
            stmt.setString(4, reo.getTipo());
            stmt.setInt(5, reo.getStatus());
                stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al actualizar el restaurantero: "+e.getMessage());
        }
    }


    public void DeleteUser(int idReo) throws SQLException{
        String query = "UPDATE status= 0 WHERE codigo_usuario= ?;";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, idReo);
            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al intentar eliminar al restaurantero: "+e.getMessage());
        }
    }

    private Restaurantero mapResulsetToRestaurantero(ResultSet rs) throws SQLException{
        return new Restaurantero(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getInt("codigo_usuario"),
            rs.getString("rfc"),
            rs.getInt("status")
        );
    }
}