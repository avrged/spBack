package org.sazonpt.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sazonpt.config.DBConfig;
import org.sazonpt.model.Estadistica;

public class EstadisticaRepository {
    

    public void addEstadistica(Estadistica e) throws SQLException {
        String query = "INSERT INTO estadistica(nacional, extranjero, correo, descargas) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, e.getNacional());
            stmt.setObject(2, e.getExtranjero());
            stmt.setString(3, e.getCorreo());
            stmt.setInt(4, e.getDescargas());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear la estadística: " + ex.getMessage());
        }
    }


    public Estadistica findEstadistica(int id) throws SQLException {
        String query = "SELECT * FROM estadistica WHERE id_estadistica = ?";
        Estadistica estadistica = null;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estadistica = mapResultSetToEstadistica(rs);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar la estadística: " + ex.getMessage());
        }
        return estadistica;
    }


    public boolean deleteEstadistica(int id) throws SQLException {
        String query = "DELETE FROM estadistica WHERE id_estadistica = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar la estadística: " + ex.getMessage());
        }
    }


    public Estadistica updateEstadistica(Estadistica e) throws SQLException {
        String query = "UPDATE estadistica SET nacional = ?, extranjero = ?, correo = ?, descargas = ? WHERE id_estadistica = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, e.getNacional());
            stmt.setObject(2, e.getExtranjero());
            stmt.setString(3, e.getCorreo());
            stmt.setInt(4, e.getDescargas());
            stmt.setInt(5, e.getId_estadistica());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return findEstadistica(e.getId_estadistica());
            } else {
                throw new SQLException("No se encontró la estadística con ID: " + e.getId_estadistica());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar la estadística: " + ex.getMessage());
        }
    }


    public List<Estadistica> listAllEstadisticas() throws SQLException {
        List<Estadistica> estadisticas = new ArrayList<>();
        String query = "SELECT * FROM estadistica";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                estadisticas.add(mapResultSetToEstadistica(rs));
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las estadísticas: " + ex.getMessage());
        }
        return estadisticas;
    }

    private Estadistica mapResultSetToEstadistica(ResultSet rs) throws SQLException {
        return new Estadistica(
            rs.getInt("id_estadistica"),
            (Integer) rs.getObject("nacional"),
            (Integer) rs.getObject("extranjero"),
            rs.getString("correo"),
            rs.getInt("descargas")
        );
    }
}
