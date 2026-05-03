package com.mobileshop.dao;

import com.mobileshop.model.Order;
import com.mobileshop.model.OrderItem;
import com.mobileshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order and OrderItem entities.
 */
public class OrderDAO {

    /**
     * Creates a new order with its items in a transaction.
     * @param order the order to create
     * @param items the order items
     * @return the generated order ID, or -1 on failure
     */
    public int createOrder(Order order, List<OrderItem> items) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, status, shipping_address, phone) VALUES (?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        String stockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            // Insert order
            ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.setString(4, order.getShippingAddress());
            ps.setString(5, order.getPhone());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) { conn.rollback(); return -1; }
            rs = ps.getGeneratedKeys();
            if (!rs.next()) { conn.rollback(); return -1; }
            int orderId = rs.getInt(1);
            rs.close();
            ps.close();
            // Insert order items and update stock
            for (OrderItem item : items) {
                // Update stock
                ps = conn.prepareStatement(stockSql);
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                int stockUpdated = ps.executeUpdate();
                if (stockUpdated == 0) { conn.rollback(); return -1; }
                ps.close();
                // Insert item
                ps = conn.prepareStatement(itemSql);
                ps.setInt(1, orderId);
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
                ps.executeUpdate();
                ps.close();
            }
            conn.commit();
            return orderId;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { System.err.println("Error rolling back: " + ex.getMessage()); }
            System.err.println("Error creating order: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { System.err.println("Error resetting autoCommit: " + e.getMessage()); }
            DBUtil.closeAll(rs, ps, conn);
        }
        return -1;
    }

    public Order getOrderById(int id) {
        String sql = "SELECT o.*, u.full_name AS user_name, u.email AS user_email FROM orders o LEFT JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToOrder(rs);
        } catch (SQLException e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS user_name, u.email AS user_email FROM orders o LEFT JOIN users u ON o.user_id = u.id WHERE o.user_id = ? ORDER BY o.order_date DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) orders.add(mapResultSetToOrder(rs));
        } catch (SQLException e) {
            System.err.println("Error getting orders by user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS user_name, u.email AS user_email FROM orders o LEFT JOIN users u ON o.user_id = u.id ORDER BY o.order_date DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) orders.add(mapResultSetToOrder(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.image AS product_image FROM order_items oi LEFT JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setProductName(rs.getString("product_name"));
                item.setProductImage(rs.getString("product_image"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return items;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
        } finally {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        return false;
    }

    public int countOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting orders: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    public int countOrdersByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting orders by status: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    public java.math.BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status IN ('confirmed', 'shipped', 'delivered')";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return java.math.BigDecimal.ZERO;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setPhone(rs.getString("phone"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            order.setUserName(rs.getString("user_name"));
            order.setUserEmail(rs.getString("user_email"));
        } catch (SQLException ignored) {}
        return order;
    }
}
