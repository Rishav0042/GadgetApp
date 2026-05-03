package com.mobileshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database utility class for managing connections.
 * Uses MySQL JDBC driver with connection parameters.
 */
public class DBUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/gadgetzone?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    /**
     * Gets a database connection.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Closes database resources safely.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { System.err.println("Error closing connection: " + e.getMessage()); }
        }
    }

    public static void close(PreparedStatement ps) {
        if (ps != null) {
            try { ps.close(); } catch (SQLException e) { System.err.println("Error closing statement: " + e.getMessage()); }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { System.err.println("Error closing result set: " + e.getMessage()); }
        }
    }

    /**
     * Closes all resources safely.
     */
    public static void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) {
        close(rs);
        close(ps);
        close(conn);
    }
}
