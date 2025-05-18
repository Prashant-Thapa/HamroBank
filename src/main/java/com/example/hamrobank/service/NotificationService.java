package com.example.hamrobank.service;

import com.example.hamrobank.dao.NotificationDAO;
import com.example.hamrobank.dao.NotificationDAOImpl;
import com.example.hamrobank.dao.NotificationPreferenceDAO;
import com.example.hamrobank.dao.NotificationPreferenceDAOImpl;
import com.example.hamrobank.model.Notification;
import com.example.hamrobank.model.NotificationPreference;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for notifications
 */
public class NotificationService {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    private final NotificationDAO notificationDAO;
    private final NotificationPreferenceDAO notificationPreferenceDAO;
    
    /**
     * Constructor
     */
    public NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
        this.notificationPreferenceDAO = new NotificationPreferenceDAOImpl();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param notificationDAO DAO to use
     * @param notificationPreferenceDAO NotificationPreferenceDAO to use
     */
    public NotificationService(NotificationDAO notificationDAO, NotificationPreferenceDAO notificationPreferenceDAO) {
        this.notificationDAO = notificationDAO;
        this.notificationPreferenceDAO = notificationPreferenceDAO;
    }
    
    /**
     * Create a new notification
     * 
     * @param notification Notification object to create
     * @return created Notification with ID
     */
    public Notification createNotification(Notification notification) {
        try {
            return notificationDAO.createNotification(notification);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating notification", e);
            throw new RuntimeException("Error creating notification", e);
        }
    }
    
    /**
     * Get a notification by ID
     * 
     * @param notificationId ID of the notification to retrieve
     * @return Notification object if found, null otherwise
     */
    public Notification getNotificationById(int notificationId) {
        try {
            return notificationDAO.getNotificationById(notificationId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting notification by ID", e);
            throw new RuntimeException("Error getting notification by ID", e);
        }
    }
    
    /**
     * Get all notifications for a user
     * 
     * @param userId ID of the user
     * @return List of Notification objects for the user
     */
    public List<Notification> getNotificationsByUserId(int userId) {
        try {
            return notificationDAO.getNotificationsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting notifications by user ID", e);
            throw new RuntimeException("Error getting notifications by user ID", e);
        }
    }
    
    /**
     * Get all unread notifications for a user
     * 
     * @param userId ID of the user
     * @return List of unread Notification objects for the user
     */
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        try {
            return notificationDAO.getUnreadNotificationsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting unread notifications by user ID", e);
            throw new RuntimeException("Error getting unread notifications by user ID", e);
        }
    }
    
    /**
     * Mark a notification as read
     * 
     * @param notificationId ID of the notification to mark as read
     * @return true if the update was successful, false otherwise
     */
    public boolean markAsRead(int notificationId) {
        try {
            return notificationDAO.markAsRead(notificationId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error marking notification as read", e);
            throw new RuntimeException("Error marking notification as read", e);
        }
    }
    
    /**
     * Mark all notifications for a user as read
     * 
     * @param userId ID of the user
     * @return true if the update was successful, false otherwise
     */
    public boolean markAllAsRead(int userId) {
        try {
            return notificationDAO.markAllAsRead(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error marking all notifications as read", e);
            throw new RuntimeException("Error marking all notifications as read", e);
        }
    }
    
    /**
     * Delete a notification
     * 
     * @param notificationId ID of the notification to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteNotification(int notificationId) {
        try {
            return notificationDAO.deleteNotification(notificationId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting notification", e);
            throw new RuntimeException("Error deleting notification", e);
        }
    }
    
    /**
     * Get the count of unread notifications for a user
     * 
     * @param userId ID of the user
     * @return Count of unread notifications
     */
    public int getUnreadNotificationCount(int userId) {
        try {
            return notificationDAO.getUnreadNotificationCount(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting unread notification count", e);
            throw new RuntimeException("Error getting unread notification count", e);
        }
    }
    
    /**
     * Send a transaction notification to a user
     * 
     * @param userId ID of the user
     * @param title Title of the notification
     * @param message Message of the notification
     * @return created Notification with ID
     */
    public Notification sendTransactionNotification(int userId, String title, String message) {
        try {
            // Check if user has enabled transaction notifications
            NotificationPreference preference = notificationPreferenceDAO.getNotificationPreference(
                    userId, Notification.TYPE_TRANSACTION, NotificationPreference.CHANNEL_IN_APP);
            
            if (preference == null || preference.isEnabled()) {
                Notification notification = new Notification(userId, title, message, Notification.TYPE_TRANSACTION);
                return notificationDAO.createNotification(notification);
            }
            
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending transaction notification", e);
            throw new RuntimeException("Error sending transaction notification", e);
        }
    }
    
    /**
     * Send a balance notification to a user
     * 
     * @param userId ID of the user
     * @param title Title of the notification
     * @param message Message of the notification
     * @return created Notification with ID
     */
    public Notification sendBalanceNotification(int userId, String title, String message) {
        try {
            // Check if user has enabled balance notifications
            NotificationPreference preference = notificationPreferenceDAO.getNotificationPreference(
                    userId, Notification.TYPE_BALANCE, NotificationPreference.CHANNEL_IN_APP);
            
            if (preference == null || preference.isEnabled()) {
                Notification notification = new Notification(userId, title, message, Notification.TYPE_BALANCE);
                return notificationDAO.createNotification(notification);
            }
            
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending balance notification", e);
            throw new RuntimeException("Error sending balance notification", e);
        }
    }
    
    /**
     * Send a security notification to a user
     * 
     * @param userId ID of the user
     * @param title Title of the notification
     * @param message Message of the notification
     * @return created Notification with ID
     */
    public Notification sendSecurityNotification(int userId, String title, String message) {
        try {
            // Security notifications are always sent regardless of preferences
            Notification notification = new Notification(userId, title, message, Notification.TYPE_SECURITY);
            return notificationDAO.createNotification(notification);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending security notification", e);
            throw new RuntimeException("Error sending security notification", e);
        }
    }
    
    /**
     * Initialize notification preferences for a new user
     * 
     * @param userId ID of the user
     */
    public void initializeNotificationPreferences(int userId) {
        try {
            notificationPreferenceDAO.initializeDefaultPreferences(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing notification preferences", e);
            throw new RuntimeException("Error initializing notification preferences", e);
        }
    }
    
    /**
     * Get all notification preferences for a user
     * 
     * @param userId ID of the user
     * @return List of NotificationPreference objects for the user
     */
    public List<NotificationPreference> getNotificationPreferencesByUserId(int userId) {
        try {
            return notificationPreferenceDAO.getNotificationPreferencesByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting notification preferences by user ID", e);
            throw new RuntimeException("Error getting notification preferences by user ID", e);
        }
    }
    
    /**
     * Update a notification preference
     * 
     * @param preference NotificationPreference object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateNotificationPreference(NotificationPreference preference) {
        try {
            return notificationPreferenceDAO.updateNotificationPreference(preference);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating notification preference", e);
            throw new RuntimeException("Error updating notification preference", e);
        }
    }
    
    /**
     * Update the enabled status of a notification preference
     * 
     * @param preferenceId ID of the preference
     * @param isEnabled New enabled status
     * @return true if the update was successful, false otherwise
     */
    public boolean updateEnabledStatus(int preferenceId, boolean isEnabled) {
        try {
            return notificationPreferenceDAO.updateEnabledStatus(preferenceId, isEnabled);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating notification preference enabled status", e);
            throw new RuntimeException("Error updating notification preference enabled status", e);
        }
    }
}
