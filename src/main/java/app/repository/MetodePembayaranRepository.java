package app.repository;

import app.config.DatabaseConfig;
import app.model.MetodePembayaran;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodePembayaranRepository {

    public void insert(MetodePembayaran metode) throws SQLException {
        String sql = "INSERT INTO payment_methods (nama_metode, status) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, metode.getMetode());
            stmt.setBoolean(2, metode.getStatus());
            
            stmt.executeUpdate();
        }
    }

    public void update(String oldName, MetodePembayaran metode) throws SQLException {
        String sql = "UPDATE payment_methods SET nama_metode = ?, status = ? WHERE nama_metode = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, metode.getMetode());
            stmt.setBoolean(2, metode.getStatus());
            stmt.setString(3, oldName);
            
            stmt.executeUpdate();
        }
    }

    public void delete(String namaMetode) throws SQLException {
        String sql = "DELETE FROM payment_methods WHERE nama_metode = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaMetode);
            stmt.executeUpdate();
        }
    }

    public List<MetodePembayaran> findAll() {
        List<MetodePembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new MetodePembayaran(
                    rs.getString("nama_metode"),
                    rs.getBoolean("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
