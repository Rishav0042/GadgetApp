package com.mobileshop.dao;

import com.mobileshop.model.Product;
import com.mobileshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product entity.
 */
public class ProductDAO {

    public int createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category_id, brand, image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getCategoryId());
            ps.setString(6, product.getBrand());
            ps.setString(7, product.getImage());
            ps.setString(8, product.getStatus());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return -1;
    }

    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToProduct(rs);
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return products;
    }

    public List<Product> getActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.status = 'active' ORDER BY p.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) {
            System.err.println("Error getting active products: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return products;
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.status = 'active' ORDER BY p.created_at DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) {
            System.err.println("Error getting featured products: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return products;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.category_id = ? AND p.status = 'active' ORDER BY p.name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return products;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.status = 'active' AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ORDER BY p.name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return products;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category_id = ?, brand = ?, image = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getCategoryId());
            ps.setString(6, product.getBrand());
            ps.setString(7, product.getImage());
            ps.setString(8, product.getStatus());
            ps.setInt(9, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
        return false;
    }

    public boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 'active'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting products: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    public int countProductsByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = ? AND status = 'active'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting products by category: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setBrand(rs.getString("brand"));
        product.setImage(rs.getString("image"));
        product.setStatus(rs.getString("status"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            product.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {}
        return product;
    }
}
