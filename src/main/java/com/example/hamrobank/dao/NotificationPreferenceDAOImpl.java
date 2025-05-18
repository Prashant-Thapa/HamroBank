package com.example.hamrobank.dao;

import com.example.hamrobank.model.Notification;
import com.example.hamrobank.model.NotificationPreference;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the NotificationPreferenceDAO interface
 */
public class NotificationPreferenceDAOImpl implements NotificationPreferenceDAO {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationPreferenceDAOImpl.class.getName());
    
    @Override
    public NotificationPreference createNotificationPreference(NotificationPreference preference) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Check if preference already exists
            NotificationPreference existingPreference = getNotificationPreference(
                    preference.getUserId(), preference.getNotificationType(), preference.getChannel());
            
            if (existingPreference != null) {
                // Update existing preference
                existingPreference.setEnabled(preference.isEnabled());
                updateNotificationPreference(existingPreference);
                return existingPreference;
            }
            
            String sql = "INSERT INTO notification_preferences (user_id, notification_type, channel, is_enabled) " +
                         "VALUES (?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, preference.getUserId());
            stmt.setString(2, preference.getNotificationType());
            stmt.setString(3, preference.getChannel());
            stmt.setBoolean(4, preference.isEnabled());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating notification preference failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                preference.setPreferenceId(rs.getInt(1));
            } else {
                throw new SQLException("Creating notification preference failed, no ID obtained.");
            }
            
            return preference;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating notification preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public NotificationPreference getNotificationPreferenceById(int preferenceId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notification_preferences WHERE preference_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, preferenceId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNotificationPreference(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preference by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<NotificationPreference> getNotificationPreferencesByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notification_preferences WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<NotificationPreference> preferences = new ArrayList<>();
            while (rs.next()) {
                preferences.add(mapResultSetToNotificationPreference(rs));
            }
            
            return preferences;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preferences by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<NotificationPreference> getNotificationPreferencesByType(int userId, String notificationType) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notification_preferences WHERE user_id = ? AND notification_type = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, notificationType);
            
            rs = stmt.executeQuery();
            
            List<NotificationPreference> preferences = new ArrayList<>();
            while (rs.next()) {
                preferences.add(mapResultSetToNotificationPreference(rs));
            }
            
            return preferences;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preferences by type", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<NotificationPreference> getNotificationPreferencesByChannel(int userId, String channel) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notification_preferences WHERE user_id = ? AND channel = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, channel);
            
            rs = stmt.executeQuery();
            
            List<NotificationPreference> preferences = new ArrayList<>();
            while (rs.next()) {
                preferences.add(mapResultSetToNotificationPreference(rs));
            }
            
            return preferences;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preferences by channel", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public NotificationPreference getNotificationPreference(int userId, String notificationType, String channel) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notification_preferences WHERE user_id = ? AND notification_type = ? AND channel = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, notificationType);
            stmt.setString(3, channel);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNotificationPreference(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateNotificationPreference(NotificationPreference preference) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE notification_preferences SET notification_type = ?, channel = ?, is_enabled = ? " +
                         "WHERE preference_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, preference.getNotificationType());
            stmt.setString(2, preference.getChannel());
            stmt.setBoolean(3, preference.isEnabled());
            stmt.setInt(4, preference.getPreferenceId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating notification preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateEnabledStatus(int preferenceId, boolean isEnabled) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE notification_preferences SET is_enabled = ? WHERE preference_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, isEnabled);
            stmt.setInt(2, preferenceId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating notification preference enabled status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteNotificationPreference(int preferenceId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM notification_preferences WHERE preference_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, preferenceId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting notification preference", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public void initializeDefaultPreferences(int userId) throws Exception {
        // Define default notification types and channels
        String[] notificationTypes = {
                Notification.TYPE_TRANSACTION,
                Notification.TYPE_BALANCE,
                Notification.TYPE_SECURITY,
                Notification.TYPE_SYSTEM,
                Notification.TYPE_MARKETING
        };
        
        String[] channels = {
                NotificationPreference.CHANNEL_EMAIL,
                NotificationPreference.CHANNEL_IN_APP
        };
        
        // Create default preferences
        for (String type : notificationTypes) {
            for (String channel : channels) {
                // Enable all by default except marketing emails
                boolean isEnabled = !(type.equals(Notification.TYPE_MARKETING) && 
                                     channel.equals(NotificationPreference.CHANNEL_EMAIL));
                
                NotificationPreference preference = new NotificationPreference(userId, type, channel, isEnabled);
                createNotificationPreference(preference);
            }
        }
    }
    
    /**
     * Map a ResultSet to a NotificationPreference object
     * 
     * @param rs ResultSet to map
     * @return NotificationPreference object
     * @throws SQLException if an error occurs during the mapping
     */
    private NotificationPreference mapResultSetToNotificationPreference(ResultSet rs) throws SQLException {
        NotificationPreference preference = new NotificationPreference();
        preference.setPreferenceId(rs.getInt("preference_id"));
        preference.setUserId(rs.getInt("user_id"));
        preference.setNotificationType(rs.getString("notification_type"));
        preference.setChannel(rs.getString("channel"));
        preference.setEnabled(rs.getBoolean("is_enabled"));
        preference.setCreatedAt(rs.getTimestamp("created_at"));
        preference.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return preference;
    }
}
