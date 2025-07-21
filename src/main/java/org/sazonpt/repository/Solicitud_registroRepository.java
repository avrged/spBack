package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Solicitud_registro;

public class Solicitud_registroRepository {
    
    public void AddSolicitudR(Solicitud_registro soliR) throws SQLException {
        // Modificamos para permitir solicitudes sin id_restaurantero válido
        // Si el id_restaurantero no existe, lo establecemos como NULL
        Integer idRestauranteroFinal = null;
        if (soliR.getId_restaurantero() > 0 && existeRestaurantero(soliR.getId_restaurantero())) {
            idRestauranteroFinal = soliR.getId_restaurantero();
        }

        String query = "INSERT INTO solicitud_registro(" +
                "id_restaurantero, propietario, correo, numero, direccion, horario, " +
                "imagen1, imagen2, imagen3, comprobante, fecha, estado, restaurante) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Usar setObject para permitir NULL
            if (idRestauranteroFinal != null) {
                stmt.setInt(1, idRestauranteroFinal);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }

            stmt.setString(2, soliR.getPropietario());
            stmt.setString(3, soliR.getCorreo());
            stmt.setString(4, soliR.getNumero());
            stmt.setString(5, soliR.getDireccion());
            stmt.setString(6, soliR.getHorario());
            stmt.setString(7, soliR.getImagen1());
            stmt.setString(8, soliR.getImagen2());
            stmt.setString(9, soliR.getImagen3());
            stmt.setString(10, soliR.getComprobante());
            stmt.setDate(11, Date.valueOf(soliR.getFecha()));
            stmt.setString(12, soliR.getEstado());
            stmt.setString(13, soliR.getRestaurante());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear la solicitud de registro: " + e.getMessage());
        }
    }
    
    private boolean existeRestaurantero(int idRestaurantero) throws SQLException {
        String query = "SELECT COUNT(*) FROM restaurantero WHERE id_usuario = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idRestaurantero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Solicitud_registro FindSolicitudR(int id) throws SQLException {
        String query = "SELECT * FROM solicitud_registro WHERE id_solicitud = ?";
        Solicitud_registro solicitud = null;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    solicitud = mapResultSetToSolicitud(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar la solicitud de registro: " + e.getMessage());
        }
        
        return solicitud;
    }

    public boolean DeleteSolicitudR(int id) throws SQLException {
        String query = "DELETE FROM solicitud_registro WHERE id_solicitud = ?";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la solicitud de registro: " + e.getMessage());
        }
    }

    public Solicitud_registro UpdateSolicitud(Solicitud_registro soliR) throws SQLException {
        String query = "UPDATE solicitud_registro SET " +
                "id_restaurantero = ?, propietario = ?, correo = ?, numero = ?, direccion = ?, horario = ?, " +
                "imagen1 = ?, imagen2 = ?, imagen3 = ?, comprobante = ?, fecha = ?, estado = ?, restaurante = ? " +
                "WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, soliR.getId_restaurantero());
            stmt.setString(2, soliR.getPropietario());
            stmt.setString(3, soliR.getCorreo());
            stmt.setString(4, soliR.getNumero());
            stmt.setString(5, soliR.getDireccion());
            stmt.setString(6, soliR.getHorario());
            stmt.setString(7, soliR.getImagen1());
            stmt.setString(8, soliR.getImagen2());
            stmt.setString(9, soliR.getImagen3());
            stmt.setString(10, soliR.getComprobante());
            stmt.setDate(11, Date.valueOf(soliR.getFecha()));
            stmt.setString(12, soliR.getEstado());
            stmt.setString(13, soliR.getRestaurante());
            stmt.setInt(14, soliR.getId_solicitud());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return FindSolicitudR(soliR.getId_solicitud());
            } else {
                throw new SQLException("No se encontró la solicitud con ID: " + soliR.getId_solicitud());
            }
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la solicitud de registro: " + e.getMessage());
        }
    }

    public List<Solicitud_registro> ListAllSolicitudes() throws SQLException {
        List<Solicitud_registro> solicitudes = new ArrayList<>();
        String query = "SELECT * FROM solicitud_registro";
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las solicitudes de registro: " + e.getMessage());
        }
        
        return solicitudes;
    }

    private Solicitud_registro mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        Solicitud_registro solicitud = new Solicitud_registro(
            rs.getInt("id_solicitud"),
            rs.getInt("id_restaurantero"),
            rs.getDate("fecha").toLocalDate(),
            rs.getString("estado"),
            rs.getString("restaurante"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("imagen1"),
            rs.getString("imagen2"),
            rs.getString("imagen3"),
            rs.getString("comprobante"),
            rs.getString("propietario"),
            rs.getString("numero"),
            rs.getString("horario")
        );
        return solicitud;
    }

    public void aprobarSolicitud(int idSolicitud) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener datos de la solicitud
            Solicitud_registro solicitud = FindSolicitudR(idSolicitud);
            if (solicitud == null) {
                throw new SQLException("No se encontró la solicitud con ID: " + idSolicitud);
            }

            if ("aprobado".equals(solicitud.getEstado())) {
                throw new SQLException("La solicitud ya está aprobada");
            }

            // 2. Actualizar el estado de la solicitud a 'aprobado'
            String updateQuery = "UPDATE solicitud_registro SET estado = 'aprobado' WHERE id_solicitud = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, idSolicitud);
                updateStmt.executeUpdate();
            }

            // 3. Crear el restaurante incluyendo todos los campos obligatorios con etiquetas por defecto
            String insertRestauranteQuery = "INSERT INTO restaurante (nombre, direccion, horario, telefono, etiquetas, id_zona) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertRestauranteQuery)) {
                insertStmt.setString(1, solicitud.getRestaurante()); // nombre
                insertStmt.setString(2, solicitud.getDireccion());   // direccion
                insertStmt.setString(3, solicitud.getHorario() != null ? solicitud.getHorario() : "No especificado"); // horario
                insertStmt.setString(4, solicitud.getNumero() != null ? solicitud.getNumero() : ""); // telefono

                // Asignar etiquetas por defecto basadas en el tipo de restaurante
                String etiquetasPorDefecto = "Comida Rápida, Familiar";
                insertStmt.setString(5, etiquetasPorDefecto); // etiquetas por defecto

                insertStmt.setInt(6, 1); // ID de zona por defecto

                System.out.println("Creando restaurante con todos los campos requeridos:");
                System.out.println("- Nombre: " + solicitud.getRestaurante());
                System.out.println("- Dirección: " + solicitud.getDireccion());
                System.out.println("- Horario: " + solicitud.getHorario());
                System.out.println("- Teléfono: " + solicitud.getNumero());
                System.out.println("- Etiquetas: " + etiquetasPorDefecto);

                insertStmt.executeUpdate();
                System.out.println("Restaurante creado exitosamente con etiquetas por defecto");
            }

            conn.commit(); // Confirmar transacción
            System.out.println("Transacción completada exitosamente");

        } catch (SQLException e) {
            System.err.println("Error SQL en aprobarSolicitud: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir cambios en caso de error
                    System.out.println("Rollback ejecutado");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error en rollback: " + rollbackEx.getMessage());
                }
            }
            throw new SQLException("Error al aprobar la solicitud: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    public void rechazarSolicitud(int idSolicitud) throws SQLException {
        String query = "UPDATE solicitud_registro SET estado = 'rechazado' WHERE id_solicitud = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idSolicitud);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la solicitud con ID: " + idSolicitud);
            }

        } catch (SQLException e) {
            throw new SQLException("Error al rechazar la solicitud: " + e.getMessage());
        }
    }
}
