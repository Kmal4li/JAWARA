package app.repository;

import app.config.DatabaseConfig;
import app.model.Voucher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherRepository {

    public void insert(Voucher voucher) throws SQLException {
        String sql = "INSERT INTO vouchers (kode_voucher, tipe_promo, nilai_diskon, tanggal_kadaluarsa, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, voucher.getKodeVoucher());
            stmt.setString(2, voucher.getTipePromo());
            stmt.setInt(3, voucher.getNilaiDiskon());
            stmt.setString(4, voucher.getTanggalKadaluarsa());
            stmt.setBoolean(5, voucher.getStatus());
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    voucher.setIdVoucher(rs.getInt(1));
                }
            }
        }
    }

    public void update(Voucher voucher) throws SQLException {
        String sql = "UPDATE vouchers SET kode_voucher = ?, tipe_promo = ?, nilai_diskon = ?, tanggal_kadaluarsa = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, voucher.getKodeVoucher());
            stmt.setString(2, voucher.getTipePromo());
            stmt.setInt(3, voucher.getNilaiDiskon());
            stmt.setString(4, voucher.getTanggalKadaluarsa());
            stmt.setBoolean(5, voucher.getStatus());
            stmt.setInt(6, voucher.getIdVoucher());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vouchers WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Voucher> findAll() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Voucher(
                    rs.getInt("id"),
                    rs.getString("kode_voucher"),
                    rs.getString("tipe_promo"),
                    (int) rs.getDouble("nilai_diskon"),
                    rs.getDate("tanggal_kadaluarsa").toString(),
                    rs.getBoolean("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
