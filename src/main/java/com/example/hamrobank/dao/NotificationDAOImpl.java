package com.example.hamrobank.dao;

import com.example.hamrobank.model.Notification;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the NotificationDAO interface
 */
public class NotificationDAOImpl implements NotificationDAO {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationDAOImpl.class.getName());
    
    @Override
    public Notification createNotification(Notification notification) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO notifications (user_id, title, message, notification_type, is_read) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getTitle());
            stmt.setString(3, notification.getMessage());
            stmt.setString(4, notification.getNotificationType());
            stmt.setBoolean(5, notification.isRead());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating notification failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                notification.setNotificationId(rs.getInt(1));
            } else {
                throw new SQLException("Creating notification failed, no ID obtained.");
            }
            
            return notification;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating notification", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public Notification getNotificationById(int notificationId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notifications WHERE notification_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, notificationId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNotification(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notification by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Notification> getNotificationsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
            return notifications;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notifications by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Notification> getUnreadNotificationsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
            return notifications;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unread notifications by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Notification> getNotificationsByType(int userId, String notificationType) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM notifications WHERE user_id = ? AND notification_type = ? ORDER BY created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, notificationType);
            
            rs = stmt.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
            return notifications;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting notifications by type", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean markAsRead(int notificationId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, notificationId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking notification as read", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean markAllAsRead(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking all notifications as read", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteNotification(int notificationId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM notifications WHERE notification_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, notificationId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting notification", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteAllNotifications(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM notifications WHERE user_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting all notifications", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public int getUnreadNotificationCount(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            return 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unread notification count", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Map a ResultSet to a Notification object
     * 
     * @param rs ResultSet to map
     * @return Notification object
     * @throws SQLException if an error occurs during the mapping
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        
        return notification;
    }
}
