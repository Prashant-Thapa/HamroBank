package com.example.hamrobank.dao;

import com.example.hamrobank.model.User;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of UserDAO interface
 */
public class UserDAOImpl implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    @Override
    public User createUser(User user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            // Check if profile_picture column exists
            boolean profilePictureColumnExists = columnExists(conn, "users", "profile_picture");

            String sql;
            if (profilePictureColumnExists) {
                sql = "INSERT INTO users (username, password, email, full_name, role, phone, address, profile_picture) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO users (username, password, email, full_name, role, phone, address) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
            }

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getRole().toString());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getAddress());

            if (profilePictureColumnExists) {
                stmt.setString(8, user.getProfilePicture());
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            return user;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public User getUserById(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public User getUserByEmail(String email) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            // Check if profile_picture column exists
            boolean profilePictureColumnExists = columnExists(conn, "users", "profile_picture");

            String sql;
            if (profilePictureColumnExists) {
                sql = "UPDATE users SET username = ?, email = ?, full_name = ?, " +
                      "role = ?, phone = ?, address = ?, profile_picture = ? WHERE user_id = ?";
            } else {
                sql = "UPDATE users SET username = ?, email = ?, full_name = ?, " +
                      "role = ?, phone = ?, address = ? WHERE user_id = ?";
            }

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getRole().toString());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());

            if (profilePictureColumnExists) {
                stmt.setString(7, user.getProfilePicture());
                stmt.setInt(8, user.getUserId());
            } else {
                stmt.setInt(7, user.getUserId());
            }

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean deleteUser(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM users WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users ORDER BY user_id";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<User> getUsersByRole(User.Role role) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE role = ? ORDER BY user_id";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, role.toString());

            rs = stmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by role", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateLastLogin(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating last login", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean usernameExists(String username) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if username exists", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean emailExists(String email) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if email exists", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean activateUser(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            // Check if active column exists
            boolean activeColumnExists = columnExists(conn, "users", "active");

            if (!activeColumnExists) {
                // If the column doesn't exist, run the alter script
                Statement alterStmt = conn.createStatement();
                alterStmt.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE");
                alterStmt.close();
            }

            String sql = "UPDATE users SET active = TRUE WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error activating user", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean deactivateUser(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            // Check if active column exists
            boolean activeColumnExists = columnExists(conn, "users", "active");

            if (!activeColumnExists) {
                // If the column doesn't exist, run the alter script
                Statement alterStmt = conn.createStatement();
                alterStmt.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE");
                alterStmt.close();
            }

            String sql = "UPDATE users SET active = FALSE WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deactivating user", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean isUserActive(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            // Check if active column exists
            boolean activeColumnExists = columnExists(conn, "users", "active");

            if (!activeColumnExists) {
                // If the column doesn't exist, run the alter script
                Statement alterStmt = conn.createStatement();
                alterStmt.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE");
                alterStmt.close();

                // If the column was just added, all users are active by default
                return true;
            }

            String sql = "SELECT active FROM users WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("active");
            }

            // If user doesn't exist, return false
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user is active", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    /**
     * Check if a column exists in a table
     *
     * @param conn Database connection
     * @param tableName Name of the table
     * @param columnName Name of the column
     * @return true if the column exists, false otherwise
     * @throws SQLException if an error occurs during the check
     */
    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        ResultSet rs = null;
        Statement stmt = null;

        try {
            // Use a direct query to check if the column exists
            // This is more reliable than using DatabaseMetaData which can be case-sensitive
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                         "WHERE TABLE_SCHEMA = DATABASE() AND " +
                         "TABLE_NAME = ? AND COLUMN_NAME = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, columnName);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error checking if column exists", e);

            // Fallback method using try-catch
            try {
                // Try to execute a simple query that uses the column
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT " + columnName + " FROM " + tableName + " LIMIT 1");
                return true;
            } catch (SQLException ex) {
                // If the query fails, the column probably doesn't exist
                return false;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Map a ResultSet to a User object
     *
     * @param rs ResultSet containing user data
     * @return User object
     * @throws SQLException if an error occurs during the mapping
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));

        // Try to get profile_picture, but handle the case where the column doesn't exist yet
        try {
            user.setProfilePicture(rs.getString("profile_picture"));
        } catch (SQLException e) {
            // If the column doesn't exist, just set profile picture to null
            user.setProfilePicture(null);
        }

        // Try to get active status, but handle the case where the column doesn't exist yet
        try {
            user.setActive(rs.getBoolean("active"));
        } catch (SQLException e) {
            // If the column doesn't exist, set active to true by default
            user.setActive(true);
        }

        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        return user;
    }
}
