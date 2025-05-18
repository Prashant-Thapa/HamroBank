package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * Model class for notifications
 */
public class Notification {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String notificationType;
    private boolean isRead;
    private Timestamp createdAt;
    
    // Notification types
    public static final String TYPE_TRANSACTION = "TRANSACTION";
    public static final String TYPE_BALANCE = "BALANCE";
    public static final String TYPE_SECURITY = "SECURITY";
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_MARKETING = "MARKETING";
    
    // Default constructor
    public Notification() {
    }
    
    // Constructor with essential fields
    public Notification(int userId, String title, String message, String notificationType) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = false;
    }
    
    // Full constructor
    public Notification(int notificationId, int userId, String title, String message, 
                       String notificationType, boolean isRead, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
