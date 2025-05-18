package com.example.hamrobank.dao;

import com.example.hamrobank.model.UserPreference;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the UserPreferenceDAO interface
 */
public class UserPreferenceDAOImpl implements UserPreferenceDAO {
    
    private static final Logger LOGGER = Logger.getLogger(UserPreferenceDAOImpl.class.getName());
    
    @Override
    public UserPreference createUserPreference(UserPreference userPreference) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO user_preferences (user_id, theme, dashboard_layout, language) " +
                         "VALUES (?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userPreference.getUserId());
            stmt.setString(2, userPreference.getTheme());
            stmt.setString(3, userPreference.getDashboardLayout());
            stmt.setString(4, userPreference.getLanguage());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user preference failed, no rows affected.");
            }
            
            return userPreference;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public UserPreference getUserPreferenceByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM user_preferences WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUserPreference(rs);
            }
            
            // If no preferences found, create default preferences
            UserPreference defaultPreference = new UserPreference(userId);
            return createUserPreference(defaultPreference);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user preference by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateUserPreference(UserPreference userPreference) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE user_preferences SET theme = ?, dashboard_layout = ?, language = ? " +
                         "WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userPreference.getTheme());
            stmt.setString(2, userPreference.getDashboardLayout());
            stmt.setString(3, userPreference.getLanguage());
            stmt.setInt(4, userPreference.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                // No existing preferences, create new ones
                createUserPreference(userPreference);
                return true;
            }
            
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateTheme(int userId, String theme) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Check if user preferences exist
            UserPreference userPreference = getUserPreferenceByUserId(userId);
            
            String sql = "UPDATE user_preferences SET theme = ? WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, theme);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user theme", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateDashboardLayout(int userId, String dashboardLayout) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Check if user preferences exist
            UserPreference userPreference = getUserPreferenceByUserId(userId);
            
            String sql = "UPDATE user_preferences SET dashboard_layout = ? WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, dashboardLayout);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user dashboard layout", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateLanguage(int userId, String language) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Check if user preferences exist
            UserPreference userPreference = getUserPreferenceByUserId(userId);
            
            String sql = "UPDATE user_preferences SET language = ? WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, language);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user language", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteUserPreference(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM user_preferences WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    /**
     * Map a ResultSet to a UserPreference object
     * 
     * @param rs ResultSet to map
     * @return UserPreference object
     * @throws SQLException if an error occurs during the mapping
     */
    private UserPreference mapResultSetToUserPreference(ResultSet rs) throws SQLException {
        UserPreference userPreference = new UserPreference();
        userPreference.setUserId(rs.getInt("user_id"));
        userPreference.setTheme(rs.getString("theme"));
        userPreference.setDashboardLayout(rs.getString("dashboard_layout"));
        userPreference.setLanguage(rs.getString("language"));
        userPreference.setCreatedAt(rs.getTimestamp("created_at"));
        userPreference.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return userPreference;
    }
}
