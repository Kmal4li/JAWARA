package app.repository;

import app.config.DatabaseConfig;
import app.model.Produk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukRepository {

    public void insert(Produk produk) throws SQLException {
        String sql = "INSERT INTO products (nama_produk, harga_beli, harga_jual, stok, barcode, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produk.getNamaProduk());
            stmt.setDouble(2, produk.getHargaBeli());
            stmt.setDouble(3, produk.getHargaJual());
            stmt.setInt(4, produk.getStok());
            stmt.setString(5, produk.getBarcode());
            if (produk.getCategoryId() > 0) {
                stmt.setInt(6, produk.getCategoryId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produk.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Produk> findAll() {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Produk(
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga_beli"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok"),
                    rs.getString("barcode"),
                    rs.getInt("category_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Produk produk) throws SQLException {
        String sql = "UPDATE products SET nama_produk = ?, harga_beli = ?, harga_jual = ?, stok = ?, barcode = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produk.getNamaProduk());
            stmt.setDouble(2, produk.getHargaBeli());
            stmt.setDouble(3, produk.getHargaJual());
            stmt.setInt(4, produk.getStok());
            stmt.setString(5, produk.getBarcode());
            if (produk.getCategoryId() > 0) {
                stmt.setInt(6, produk.getCategoryId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setInt(7, produk.getId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

