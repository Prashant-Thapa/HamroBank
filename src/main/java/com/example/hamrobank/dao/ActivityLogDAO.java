package com.example.hamrobank.dao;

import com.example.hamrobank.model.ActivityLog;
import java.util.List;

/**
 * Interface for ActivityLog data access operations
 */
public interface ActivityLogDAO {
    
    /**
     * Create a new activity log
     * 
     * @param activityLog ActivityLog object to create
     * @return The created activity log with ID set
     * @throws Exception if an error occurs during the operation
     */
    ActivityLog createActivityLog(ActivityLog activityLog) throws Exception;
    
    /**
     * Get an activity log by ID
     * 
     * @param logId ID of the activity log to retrieve
     * @return ActivityLog object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    ActivityLog getActivityLogById(int logId) throws Exception;
    
    /**
     * Get all activity logs for a user
     * 
     * @param userId ID of the user
     * @return List of activity logs for the user
     * @throws Exception if an error occurs during the operation
     */
    List<ActivityLog> getActivityLogsByUserId(int userId) throws Exception;
    
    /**
     * Get all activity logs
     * 
     * @return List of all activity logs
     * @throws Exception if an error occurs during the operation
     */
    List<ActivityLog> getAllActivityLogs() throws Exception;
    
    /**
     * Get activity logs by activity type
     * 
     * @param activityType Type of activity to filter by
     * @return List of activity logs with the specified activity type
     * @throws Exception if an error occurs during the operation
     */
    List<ActivityLog> getActivityLogsByType(String activityType) throws Exception;
    
    /**
     * Get recent activity logs
     * 
     * @param limit Maximum number of logs to retrieve
     * @return List of recent activity logs
     * @throws Exception if an error occurs during the operation
     */
    List<ActivityLog> getRecentActivityLogs(int limit) throws Exception;
    
    /**
     * Get recent activity logs for a user
     * 
     * @param userId ID of the user
     * @param limit Maximum number of logs to retrieve
     * @return List of recent activity logs for the user
     * @throws Exception if an error occurs during the operation
     */
    List<ActivityLog> getRecentActivityLogsByUserId(int userId, int limit) throws Exception;
}
