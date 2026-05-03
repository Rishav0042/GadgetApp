package com.mobileshop.dao;

import com.mobileshop.model.WishlistItem;
import com.mobileshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Wishlist entity.
 */
public class WishlistDAO {

    public boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() != 1062) { // Ignore duplicate key error
                System.err.println("Error adding to wishlist: " + e.getMessage());
            }
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public boolean removeFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing from wishlist: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking wishlist: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return false;
    }

    public List<WishlistItem> getWishlistByUserId(int userId) {
        List<WishlistItem> items = new ArrayList<>();
        String sql = "SELECT w.*, p.name AS product_name, p.price AS product_price, p.image AS product_image, p.brand AS product_brand, p.stock AS product_stock FROM wishlist w LEFT JOIN products p ON w.product_id = p.id WHERE w.user_id = ? ORDER BY w.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) items.add(mapResultSetToWishlistItem(rs));
        } catch (SQLException e) {
            System.err.println("Error getting wishlist: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return items;
    }

    public int countWishlistItems(int userId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting wishlist items: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    private WishlistItem mapResultSetToWishlistItem(ResultSet rs) throws SQLException {
        WishlistItem item = new WishlistItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            item.setProductName(rs.getString("product_name"));
            item.setProductPrice(rs.getBigDecimal("product_price"));
            item.setProductImage(rs.getString("product_image"));
            item.setProductBrand(rs.getString("product_brand"));
            item.setProductStock(rs.getInt("product_stock"));
        } catch (SQLException ignored) {}
        return item;
    }
}
