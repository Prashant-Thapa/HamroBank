package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * Model class for account owners (for joint accounts)
 */
public class AccountOwner {
    private int accountId;
    private int userId;
    private String permissionLevel;
    private boolean isPrimary;
    private Timestamp createdAt;
    
    // Permission levels
    public static final String PERMISSION_FULL = "FULL";
    public static final String PERMISSION_VIEW_ONLY = "VIEW_ONLY";
    public static final String PERMISSION_TRANSACT = "TRANSACT";
    
    // Default constructor
    public AccountOwner() {
    }
    
    // Constructor with essential fields
    public AccountOwner(int accountId, int userId, String permissionLevel, boolean isPrimary) {
        this.accountId = accountId;
        this.userId = userId;
        this.permissionLevel = permissionLevel;
        this.isPrimary = isPrimary;
    }
    
    // Full constructor
    public AccountOwner(int accountId, int userId, String permissionLevel, boolean isPrimary, Timestamp createdAt) {
        this.accountId = accountId;
        this.userId = userId;
        this.permissionLevel = permissionLevel;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getPermissionLevel() {
        return permissionLevel;
    }
    
    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
    
    public boolean isPrimary() {
        return isPrimary;
    }
    
    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "AccountOwner{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", permissionLevel='" + permissionLevel + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
