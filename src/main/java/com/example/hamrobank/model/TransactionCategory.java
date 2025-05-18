package com.example.hamrobank.model;

import java.sql.Timestamp;

/**
 * Model class for transaction categories
 */
public class TransactionCategory {
    private int categoryId;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer parentCategoryId;
    private boolean isSystem;
    private Timestamp createdAt;
    
    // Default constructor
    public TransactionCategory() {
    }
    
    // Constructor with essential fields
    public TransactionCategory(String name, String description, String icon, String color) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
        this.isSystem = false;
    }
    
    // Full constructor
    public TransactionCategory(int categoryId, String name, String description, String icon, 
                              String color, Integer parentCategoryId, boolean isSystem, Timestamp createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
        this.parentCategoryId = parentCategoryId;
        this.isSystem = isSystem;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }
    
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
    
    public boolean isSystem() {
        return isSystem;
    }
    
    public void setSystem(boolean system) {
        isSystem = system;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "TransactionCategory{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                ", isSystem=" + isSystem +
                '}';
    }
}
