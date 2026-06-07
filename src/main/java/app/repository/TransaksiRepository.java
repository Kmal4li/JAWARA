package app.repository;

import app.config.DatabaseConfig;
import app.model.DetailTransaksi;
import app.model.ItemCart;
import app.model.Transaksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TransaksiRepository {

    public void simpanTransaksi(Transaksi transaksi) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Mulai Transaction

            // 1. Insert ke tabel transactions
            String sqlTrans = "INSERT INTO transactions (kasir_id, total_harga, diskon, total_bayar, kembalian, payment_method, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlTrans, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, transaksi.getKasirId());
                stmt.setDouble(2, transaksi.getTotalHarga());
                stmt.setDouble(3, transaksi.getDiskon());
                stmt.setDouble(4, transaksi.getTotalBayar());
                stmt.setDouble(5, transaksi.getKembalian());
                stmt.setString(6, transaksi.getPaymentMethod());
                stmt.setString(7, "selesai");
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaksi.setId(rs.getInt(1));
                    } else {
                        throw new Exception("Gagal mendapatkan ID transaksi");
                    }
                }
            }

            // 2. Insert ke transaction_details dan Update tabel products (kurangi stok)
            String sqlDetail = "INSERT INTO transaction_details (transaction_id, product_id, qty, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE products SET stok = stok - ? WHERE id = ? AND stok >= ?";
            
            try (PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock)) {
                
                for (ItemCart item : transaksi.getItems()) {
                    // Detail Transaksi
                    stmtDetail.setInt(1, transaksi.getId());
                    stmtDetail.setInt(2, item.getProduk().getId());
                    stmtDetail.setInt(3, item.getQty());
                    stmtDetail.setDouble(4, item.getHargaSatuan());
                    stmtDetail.setDouble(5, item.getSubtotal());
                    stmtDetail.addBatch();

                    // Kurangi Stok Produk
                    stmtStock.setInt(1, item.getQty());
                    stmtStock.setInt(2, item.getProduk().getId());
                    stmtStock.setInt(3, item.getQty()); // Validasi stok harus cukup di level DB
                    int affected = stmtStock.executeUpdate();
                    if (affected == 0) {
                        throw new Exception("Stok tidak mencukupi untuk produk: " + item.getProduk().getNamaProduk());
                    }
                }
                stmtDetail.executeBatch();
            }

            conn.commit(); // Jika semua sukses, commit!
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Jika ada error stok / lainnya, batalkan semua (rollback)!
            }
            throw e; // Lemparkan error ke Controller/View
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Kembalikan state default
            }
        }
    }
}
