package com.mobileshop.dao;

import com.mobileshop.model.Review;
import com.mobileshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Review entity.
 */
public class ReviewDAO {

    public int createReview(Review review) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE rating = ?, comment = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setInt(5, review.getRating());
            ps.setString(6, review.getComment());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating review: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return -1;
    }

    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS user_name FROM reviews r LEFT JOIN users u ON r.user_id = u.id WHERE r.product_id = ? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) reviews.add(mapResultSetToReview(rs));
        } catch (SQLException e) {
            System.err.println("Error getting reviews by product: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return reviews;
    }

    public double getAverageRating(int productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0) FROM reviews WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error getting average rating: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0.0;
    }

    public boolean deleteReview(int id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            review.setUserName(rs.getString("user_name"));
        } catch (SQLException ignored) {}
        return review;
    }
}
