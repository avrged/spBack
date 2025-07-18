package org.sazonpt.service;

import org.sazonpt.repository.RestauranteroRepository;
import java.sql.SQLException;

public class DebugService {
    private final RestauranteroRepository restauranteroRepository;
    
    public DebugService(RestauranteroRepository restauranteroRepository) {
        this.restauranteroRepository = restauranteroRepository;
    }
    
    public String checkDatabaseIntegrity() throws SQLException {
        StringBuilder result = new StringBuilder();
        
        // Verificar cuántos usuarios tipo restaurantero existen
        String usuariosQuery = "SELECT COUNT(*) as count FROM usuario WHERE tipo = 'restaurantero'";
        // Verificar cuántos registros hay en tabla restaurantero
        String restauranterosQuery = "SELECT COUNT(*) as count FROM restaurantero";
        
        result.append("Diagnóstico de Base de Datos:\n");
        result.append("- Usuarios tipo restaurantero: ").append(getCount(usuariosQuery)).append("\n");
        result.append("- Registros en tabla restaurantero: ").append(getCount(restauranterosQuery)).append("\n");
        
        // Verificar usuarios sin registro en restaurantero
        String orphanedUsersQuery = """
            SELECT u.id_usuario, u.nombre, u.correo 
            FROM usuario u 
            LEFT JOIN restaurantero r ON u.id_usuario = r.id_usuario 
            WHERE u.tipo = 'restaurantero' AND r.id_usuario IS NULL
        """;
        
        result.append("- Usuarios restaurantero sin registro: ").append(getOrphanedUsers(orphanedUsersQuery));
        
        return result.toString();
    }
    
    public String fixForeignKeyIssues() throws SQLException {
        try {
            restauranteroRepository.migrateUsersToRestaurantero();
            return "Migración completada. Usuarios migrados a tabla restaurantero.";
        } catch (Exception e) {
            throw new SQLException("Error durante la migración: " + e.getMessage());
        }
    }
    
    private int getCount(String query) throws SQLException {
        // Implementación simple para obtener conteo
        try (var conn = org.sazonpt.config.DBConfig.getDataSource().getConnection();
             var stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
    
    private String getOrphanedUsers(String query) throws SQLException {
        StringBuilder users = new StringBuilder();
        try (var conn = org.sazonpt.config.DBConfig.getDataSource().getConnection();
             var stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.append("\n  * ID: ").append(rs.getInt("id_usuario"))
                     .append(", Nombre: ").append(rs.getString("nombre"))
                     .append(", Correo: ").append(rs.getString("correo"));
            }
        }
        return users.length() > 0 ? users.toString() : "\n  (ninguno)";
    }
}
