package com.example.hamrobank.dao;

import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of ActivityLogDAO interface
 */
public class ActivityLogDAOImpl implements ActivityLogDAO {
    private static final Logger LOGGER = Logger.getLogger(ActivityLogDAOImpl.class.getName());
    
    @Override
    public ActivityLog createActivityLog(ActivityLog activityLog) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO activity_logs (user_id, activity_type, description, ip_address, user_agent) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            if (activityLog.getUserId() != null) {
                stmt.setInt(1, activityLog.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            stmt.setString(2, activityLog.getActivityType());
            stmt.setString(3, activityLog.getDescription());
            stmt.setString(4, activityLog.getIpAddress());
            stmt.setString(5, activityLog.getUserAgent());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating activity log failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                activityLog.setLogId(rs.getInt(1));
            } else {
                throw new SQLException("Creating activity log failed, no ID obtained.");
            }
            
            return activityLog;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating activity log", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public ActivityLog getActivityLogById(int logId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "WHERE al.log_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, logId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToActivityLog(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting activity log by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ActivityLog> getActivityLogsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "WHERE al.user_id = ? " +
                         "ORDER BY al.created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<ActivityLog> activityLogs = new ArrayList<>();
            while (rs.next()) {
                activityLogs.add(mapResultSetToActivityLog(rs));
            }
            
            return activityLogs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting activity logs by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ActivityLog> getAllActivityLogs() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "ORDER BY al.created_at DESC";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<ActivityLog> activityLogs = new ArrayList<>();
            while (rs.next()) {
                activityLogs.add(mapResultSetToActivityLog(rs));
            }
            
            return activityLogs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all activity logs", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ActivityLog> getActivityLogsByType(String activityType) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "WHERE al.activity_type = ? " +
                         "ORDER BY al.created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, activityType);
            
            rs = stmt.executeQuery();
            
            List<ActivityLog> activityLogs = new ArrayList<>();
            while (rs.next()) {
                activityLogs.add(mapResultSetToActivityLog(rs));
            }
            
            return activityLogs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting activity logs by type", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ActivityLog> getRecentActivityLogs(int limit) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "ORDER BY al.created_at DESC LIMIT ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            
            rs = stmt.executeQuery();
            
            List<ActivityLog> activityLogs = new ArrayList<>();
            while (rs.next()) {
                activityLogs.add(mapResultSetToActivityLog(rs));
            }
            
            return activityLogs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent activity logs", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ActivityLog> getRecentActivityLogsByUserId(int userId, int limit) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT al.*, u.username " +
                         "FROM activity_logs al " +
                         "LEFT JOIN users u ON al.user_id = u.user_id " +
                         "WHERE al.user_id = ? " +
                         "ORDER BY al.created_at DESC LIMIT ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            
            rs = stmt.executeQuery();
            
            List<ActivityLog> activityLogs = new ArrayList<>();
            while (rs.next()) {
                activityLogs.add(mapResultSetToActivityLog(rs));
            }
            
            return activityLogs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent activity logs by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Map a ResultSet to an ActivityLog object
     * 
     * @param rs ResultSet containing activity log data
     * @return ActivityLog object
     * @throws SQLException if an error occurs during the mapping
     */
    private ActivityLog mapResultSetToActivityLog(ResultSet rs) throws SQLException {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setLogId(rs.getInt("log_id"));
        
        int userId = rs.getInt("user_id");
        if (!rs.wasNull()) {
            activityLog.setUserId(userId);
        }
        
        activityLog.setActivityType(rs.getString("activity_type"));
        activityLog.setDescription(rs.getString("description"));
        activityLog.setIpAddress(rs.getString("ip_address"));
        activityLog.setUserAgent(rs.getString("user_agent"));
        activityLog.setCreatedAt(rs.getTimestamp("created_at"));
        
        try {
            activityLog.setUsername(rs.getString("username"));
        } catch (SQLException e) {
            // Ignore if this column is not in the result set
        }
        
        return activityLog;
    }
}
