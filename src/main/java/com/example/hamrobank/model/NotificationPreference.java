package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * Model class for notification preferences
 */
public class NotificationPreference {
    private int preferenceId;
    private int userId;
    private String notificationType;
    private String channel;
    private boolean isEnabled;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Notification channels
    public static final String CHANNEL_EMAIL = "EMAIL";
    public static final String CHANNEL_SMS = "SMS";
    public static final String CHANNEL_PUSH = "PUSH";
    public static final String CHANNEL_IN_APP = "IN_APP";
    
    // Default constructor
    public NotificationPreference() {
    }
    
    // Constructor with essential fields
    public NotificationPreference(int userId, String notificationType, String channel, boolean isEnabled) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.channel = channel;
        this.isEnabled = isEnabled;
    }
    
    // Full constructor
    public NotificationPreference(int preferenceId, int userId, String notificationType, 
                                 String channel, boolean isEnabled, Timestamp createdAt, 
                                 Timestamp updatedAt) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.notificationType = notificationType;
        this.channel = channel;
        this.isEnabled = isEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getPreferenceId() {
        return preferenceId;
    }
    
    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "NotificationPreference{" +
                "preferenceId=" + preferenceId +
                ", userId=" + userId +
                ", notificationType='" + notificationType + '\'' +
                ", channel='" + channel + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
