package com.mobileshop.dao;

import com.mobileshop.model.Category;
import com.mobileshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category entity.
 */
public class CategoryDAO {

    public int createCategory(Category category) {
        String sql = "INSERT INTO categories (name, description, image, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getImage());
            ps.setString(4, category.getStatus());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating category: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return -1;
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.status = 'active') AS product_count FROM categories c WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToCategory(rs);
        } catch (SQLException e) {
            System.err.println("Error getting category by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToCategory(rs);
        } catch (SQLException e) {
            System.err.println("Error getting category by name: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.status = 'active') AS product_count FROM categories c ORDER BY c.name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) categories.add(mapResultSetToCategory(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return categories;
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.status = 'active') AS product_count FROM categories c WHERE c.status = 'active' ORDER BY c.name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) categories.add(mapResultSetToCategory(rs));
        } catch (SQLException e) {
            System.err.println("Error getting active categories: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return categories;
    }

    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, image = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getImage());
            ps.setString(4, category.getStatus());
            ps.setInt(5, category.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public int countCategories() {
        String sql = "SELECT COUNT(*) FROM categories";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting categories: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setImage(rs.getString("image"));
        category.setStatus(rs.getString("status"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            category.setProductCount(rs.getInt("product_count"));
        } catch (SQLException ignored) {}
        return category;
    }
}
