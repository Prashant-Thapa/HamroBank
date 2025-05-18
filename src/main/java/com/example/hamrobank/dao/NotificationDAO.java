package com.example.hamrobank.dao;

import com.example.hamrobank.model.Notification;

import java.util.List;

/**
 * Data Access Object interface for notifications
 */
public interface NotificationDAO {
    
    /**
     * Create a new notification
     * 
     * @param notification Notification object to create
     * @return created Notification with ID
     * @throws Exception if an error occurs during the operation
     */
    Notification createNotification(Notification notification) throws Exception;
    
    /**
     * Get a notification by ID
     * 
     * @param notificationId ID of the notification to retrieve
     * @return Notification object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Notification getNotificationById(int notificationId) throws Exception;
    
    /**
     * Get all notifications for a user
     * 
     * @param userId ID of the user
     * @return List of Notification objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<Notification> getNotificationsByUserId(int userId) throws Exception;
    
    /**
     * Get all unread notifications for a user
     * 
     * @param userId ID of the user
     * @return List of unread Notification objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<Notification> getUnreadNotificationsByUserId(int userId) throws Exception;
    
    /**
     * Get notifications for a user by type
     * 
     * @param userId ID of the user
     * @param notificationType Type of notifications to retrieve
     * @return List of Notification objects of the specified type for the user
     * @throws Exception if an error occurs during the operation
     */
    List<Notification> getNotificationsByType(int userId, String notificationType) throws Exception;
    
    /**
     * Mark a notification as read
     * 
     * @param notificationId ID of the notification to mark as read
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean markAsRead(int notificationId) throws Exception;
    
    /**
     * Mark all notifications for a user as read
     * 
     * @param userId ID of the user
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean markAllAsRead(int userId) throws Exception;
    
    /**
     * Delete a notification
     * 
     * @param notificationId ID of the notification to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteNotification(int notificationId) throws Exception;
    
    /**
     * Delete all notifications for a user
     * 
     * @param userId ID of the user
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteAllNotifications(int userId) throws Exception;
    
    /**
     * Get the count of unread notifications for a user
     * 
     * @param userId ID of the user
     * @return Count of unread notifications
     * @throws Exception if an error occurs during the operation
     */
    int getUnreadNotificationCount(int userId) throws Exception;
}
