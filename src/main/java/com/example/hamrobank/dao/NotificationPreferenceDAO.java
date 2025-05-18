package com.example.hamrobank.dao;

import com.example.hamrobank.model.NotificationPreference;

import java.util.List;

/**
 * Data Access Object interface for notification preferences
 */
public interface NotificationPreferenceDAO {
    
    /**
     * Create a new notification preference
     * 
     * @param preference NotificationPreference object to create
     * @return created NotificationPreference with ID
     * @throws Exception if an error occurs during the operation
     */
    NotificationPreference createNotificationPreference(NotificationPreference preference) throws Exception;
    
    /**
     * Get a notification preference by ID
     * 
     * @param preferenceId ID of the preference to retrieve
     * @return NotificationPreference object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    NotificationPreference getNotificationPreferenceById(int preferenceId) throws Exception;
    
    /**
     * Get all notification preferences for a user
     * 
     * @param userId ID of the user
     * @return List of NotificationPreference objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<NotificationPreference> getNotificationPreferencesByUserId(int userId) throws Exception;
    
    /**
     * Get notification preferences for a user by type
     * 
     * @param userId ID of the user
     * @param notificationType Type of notifications
     * @return List of NotificationPreference objects for the user and type
     * @throws Exception if an error occurs during the operation
     */
    List<NotificationPreference> getNotificationPreferencesByType(int userId, String notificationType) throws Exception;
    
    /**
     * Get notification preferences for a user by channel
     * 
     * @param userId ID of the user
     * @param channel Channel for notifications
     * @return List of NotificationPreference objects for the user and channel
     * @throws Exception if an error occurs during the operation
     */
    List<NotificationPreference> getNotificationPreferencesByChannel(int userId, String channel) throws Exception;
    
    /**
     * Get a specific notification preference for a user
     * 
     * @param userId ID of the user
     * @param notificationType Type of notifications
     * @param channel Channel for notifications
     * @return NotificationPreference object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    NotificationPreference getNotificationPreference(int userId, String notificationType, String channel) throws Exception;
    
    /**
     * Update a notification preference
     * 
     * @param preference NotificationPreference object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateNotificationPreference(NotificationPreference preference) throws Exception;
    
    /**
     * Update the enabled status of a notification preference
     * 
     * @param preferenceId ID of the preference
     * @param isEnabled New enabled status
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateEnabledStatus(int preferenceId, boolean isEnabled) throws Exception;
    
    /**
     * Delete a notification preference
     * 
     * @param preferenceId ID of the preference to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteNotificationPreference(int preferenceId) throws Exception;
    
    /**
     * Initialize default notification preferences for a user
     * 
     * @param userId ID of the user
     * @throws Exception if an error occurs during the operation
     */
    void initializeDefaultPreferences(int userId) throws Exception;
}
