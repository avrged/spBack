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
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);
            
            // Primero insertar en tabla usuario
            String queryUsuario = "INSERT INTO usuario(nombre, correo, contrasena, tipo, status) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(queryUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtUsuario.setString(1, reo.getNombreU());
            stmtUsuario.setString(2, reo.getCorreo());
            stmtUsuario.setString(3, reo.getContrasena());
            stmtUsuario.setString(4, "restaurantero");
            stmtUsuario.setString(5, reo.getStatus());
            stmtUsuario.executeUpdate();
            
            // Obtener el ID generado
            ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
            int idUsuario = 0;
            if (generatedKeys.next()) {
                idUsuario = generatedKeys.getInt(1);
            }
            
            // Luego insertar en tabla restaurantero
            String queryRestaurantero = "INSERT INTO restaurantero(id_usuario) VALUES(?)";
            PreparedStatement stmtRestaurantero = conn.prepareStatement(queryRestaurantero);
            stmtRestaurantero.setInt(1, idUsuario);
            stmtRestaurantero.executeUpdate();
            
            conn.commit();
            
        } catch(SQLException e){
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al crear el restaurantero: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Restaurantero> findAllRestauranteros() throws SQLException{
        List<Restaurantero> restauranteros = new ArrayList<>();
        String query = "SELECT u.*, r.id_usuario FROM usuario u " +
                      "INNER JOIN restaurantero r ON u.id_usuario = r.id_usuario";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    restauranteros.add(mapResultSetToRestaurantero(rs));
                }
        } catch(SQLException e){
            System.out.println("Error al intentar listar los restauranteros: " + e.getMessage());
        }
        return restauranteros;
    }

    public List<Restaurantero> getAllRestauranteros() throws SQLException{
        List<Restaurantero> restauranteros = new ArrayList<>();
        // Cambiado temporalmente para debugging - solo buscar usuarios de tipo 'restaurantero'
        String query = "SELECT u.*, u.id_usuario as id_usuario FROM usuario u " +
                      "WHERE u.tipo = 'restaurantero' AND u.status = 'activo'";

        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    restauranteros.add(mapResultSetToRestaurantero(rs));
                }
        } catch(SQLException e){
            throw new SQLException("Error al intentar listar los restauranteros: " + e.getMessage());
        }
        return restauranteros;
    }

    public void UpdateRestaurantero(Restaurantero reo) throws SQLException{
        String query = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, status = ? WHERE id_usuario = ?";
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, reo.getNombreU());
            stmt.setString(2, reo.getCorreo());
            stmt.setString(3, reo.getContrasena());
            stmt.setString(4, reo.getStatus());
            stmt.setInt(5, reo.getId_usuario());
            stmt.executeUpdate();
        } catch(SQLException e){
            throw new SQLException("Error al actualizar el restaurantero: " + e.getMessage());
        }
    }

    public void DeleteUser(int idReo) throws SQLException{
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Usar transacción para múltiples DELETE
            
            // Primero eliminar de la tabla restaurantero
            String queryRestaurantero = "DELETE FROM restaurantero WHERE id_usuario = ?";
            PreparedStatement stmtRestaurantero = conn.prepareStatement(queryRestaurantero);
            stmtRestaurantero.setInt(1, idReo);
            int rowsAffectedRestaurantero = stmtRestaurantero.executeUpdate();
            
            System.out.println("Query executed: " + queryRestaurantero);
            System.out.println("Restaurantero rows affected: " + rowsAffectedRestaurantero);
            
            // Luego eliminar de la tabla usuario
            String queryUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
            PreparedStatement stmtUsuario = conn.prepareStatement(queryUsuario);
            stmtUsuario.setInt(1, idReo);
            int rowsAffectedUsuario = stmtUsuario.executeUpdate();
            
            System.out.println("Query executed: " + queryUsuario);
            System.out.println("Restaurantero ID: " + idReo);
            System.out.println("Usuario rows affected: " + rowsAffectedUsuario);
            
            if (rowsAffectedUsuario == 0) {
                throw new SQLException("No se encontró el restaurantero con ID: " + idReo);
            }
            
            conn.commit();
            
        } catch(SQLException e){
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al intentar eliminar al restaurantero: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public Restaurantero findRestauranteroById(int idReo) throws SQLException{
        String query = "SELECT u.*, r.id_usuario FROM usuario u " +
                      "INNER JOIN restaurantero r ON u.id_usuario = r.id_usuario " +
                      "WHERE u.id_usuario = ? AND u.status = 'activo'";
        
        try(Connection conn = DBConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, idReo);
            
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToRestaurantero(rs);
                }
            }
        } catch(SQLException e){
            throw new SQLException("Error al buscar restaurantero por ID: " + e.getMessage());
        }
        return null;
    }

    private Restaurantero mapResultSetToRestaurantero(ResultSet rs) throws SQLException{
        return new Restaurantero(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getString("contrasena"),
            rs.getString("tipo"),
            rs.getInt("id_usuario"),
            rs.getString("status")
        );
    }

    // Método temporal para migrar usuarios tipo 'restaurantero' a la tabla restaurantero
    public void migrateUsersToRestaurantero() throws SQLException {
        String query = "INSERT INTO restaurantero (id_usuario) " +
                      "SELECT id_usuario FROM usuario " +
                      "WHERE tipo = 'restaurantero' AND status = 'activo' " +
                      "AND id_usuario NOT IN (SELECT id_usuario FROM restaurantero)";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            int rowsInserted = stmt.executeUpdate();
            System.out.println("Migrados " + rowsInserted + " usuarios restauranteros a la tabla restaurantero");
            
        } catch (SQLException e) {
            throw new SQLException("Error al migrar usuarios restauranteros: " + e.getMessage());
        }
    }
}