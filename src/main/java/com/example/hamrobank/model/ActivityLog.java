package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * ActivityLog model class representing a user activity log
 */
public class ActivityLog {
    private int logId;
    private Integer userId;
    private String activityType;
    private String description;
    private String ipAddress;
    private String userAgent;
    private Timestamp createdAt;
    
    // Additional field for display purposes (not in database)
    private String username;
    
    // Default constructor
    public ActivityLog() {
    }
    
    // Constructor with essential fields
    public ActivityLog(Integer userId, String activityType, String description) {
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
    }
    
    // Full constructor
    public ActivityLog(int logId, Integer userId, String activityType, String description,
                      String ipAddress, String userAgent, Timestamp createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return "ActivityLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", activityType='" + activityType + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
