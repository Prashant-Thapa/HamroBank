package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * Model class for user preferences
 */
public class UserPreference {
    private int userId;
    private String theme;
    private String dashboardLayout;
    private String language;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Theme constants
    public static final String THEME_LIGHT = "LIGHT";
    public static final String THEME_DARK = "DARK";
    public static final String THEME_SYSTEM = "SYSTEM";
    
    // Default constructor
    public UserPreference() {
    }
    
    // Constructor with essential fields
    public UserPreference(int userId) {
        this.userId = userId;
        this.theme = THEME_LIGHT;
        this.language = "en";
    }
    
    // Full constructor
    public UserPreference(int userId, String theme, String dashboardLayout, String language, 
                         Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.theme = theme;
        this.dashboardLayout = dashboardLayout;
        this.language = language;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getDashboardLayout() {
        return dashboardLayout;
    }
    
    public void setDashboardLayout(String dashboardLayout) {
        this.dashboardLayout = dashboardLayout;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
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
        return "UserPreference{" +
                "userId=" + userId +
                ", theme='" + theme + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
