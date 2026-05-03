package com.mobileshop.dao;

import com.mobileshop.model.User;
import com.mobileshop.util.DBUtil;
import com.mobileshop.util.EncryptionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity.
 * Handles all database operations related to users.
 */
public class UserDAO {

    /**
     * Creates a new user in the database.
     * @param user the user to create
     * @return the generated user ID, or -1 on failure
     */
    public int createUser(User user) {
        String sql = "INSERT INTO users (username, email, password, full_name, phone, address, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, EncryptionUtil.hashPassword(user.getPassword()));
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());
            ps.setString(8, user.getStatus());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return -1;
    }

    /**
     * Authenticates a user by username and password.
     * @param username the username
     * @param password the plain text password
     * @return User object if authenticated, null otherwise
     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND status = 'active'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (EncryptionUtil.verifyPassword(password, hashedPassword)) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    /**
     * Gets a user by ID.
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToUser(rs);
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    /**
     * Gets a user by username.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToUser(rs);
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    /**
     * Gets a user by email.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToUser(rs);
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    /**
     * Gets a user by phone number.
     */
    public User getUserByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToUser(rs);
        } catch (SQLException e) {
            System.err.println("Error getting user by phone: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return null;
    }

    /**
     * Gets all users.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) users.add(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return users;
    }

    /**
     * Gets users by role.
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, role);
            rs = ps.executeQuery();
            while (rs.next()) users.add(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error getting users by role: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return users;
    }

    /**
     * Gets pending users (awaiting admin approval).
     */
    public List<User> getPendingUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = 'pending' ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) users.add(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error getting pending users: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return users;
    }

    /**
     * Updates a user's profile information.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, full_name = ?, phone = ?, address = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
        return false;
    }

    /**
     * Updates a user's password.
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, EncryptionUtil.hashPassword(newPassword));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
        return false;
    }

    /**
     * Updates a user's status (approve/reject).
     */
    public boolean updateUserStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user status: " + e.getMessage());
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
        return false;
    }

    /**
     * Deletes a user by ID.
     */
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
        return false;
    }

    /**
     * Counts total users.
     */
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return 0;
    }

    /**
     * Maps a ResultSet row to a User object.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
