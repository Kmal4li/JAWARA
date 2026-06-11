package app.repository;

import app.config.DatabaseConfig;
import app.model.CategoryProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public void insert(CategoryProduct category) throws SQLException {
        String sql = "INSERT INTO categories (nama_kategori, deskripsi) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getNamaKategori());
            stmt.setString(2, category.getDeskripsi());
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(CategoryProduct category) throws SQLException {
        String sql = "UPDATE categories SET nama_kategori = ?, deskripsi = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getNamaKategori());
            stmt.setString(2, category.getDeskripsi());
            stmt.setInt(3, category.getId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<CategoryProduct> findAll() {
        List<CategoryProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new CategoryProduct(
                    rs.getInt("id"),
                    rs.getString("nama_kategori"),
                    rs.getString("deskripsi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
