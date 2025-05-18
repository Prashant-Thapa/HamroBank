package com.example.hamrobank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for database connection management
 */
public class DatabaseUtil {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());
    
    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hamro_bank";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "hello"; // Set your MySQL password here
    
    // Static block to load the JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get a connection to the database
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to database", e);
            throw e;
        }
    }
    
    /**
     * Close the database connection
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
    
    /**
     * Close all database resources
     * 
     * @param connection Connection to close
     * @param autoCloseable Additional resources to close (Statement, PreparedStatement, ResultSet)
     */
    public static void closeResources(Connection connection, AutoCloseable... autoCloseable) {
        for (AutoCloseable resource : autoCloseable) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error closing resource", e);
                }
            }
        }
        closeConnection(connection);
    }
}
