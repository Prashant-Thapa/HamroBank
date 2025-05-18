package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class for transaction templates
 */
public class TransactionTemplate {
    private int templateId;
    private int userId;
    private String name;
    private int sourceAccountId;
    private int destinationAccountId;
    private BigDecimal amount;
    private String description;
    private Integer categoryId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public TransactionTemplate() {
    }
    
    // Constructor with essential fields
    public TransactionTemplate(int userId, String name, int sourceAccountId, int destinationAccountId, 
                              BigDecimal amount, String description) {
        this.userId = userId;
        this.name = name;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
    }
    
    // Full constructor
    public TransactionTemplate(int templateId, int userId, String name, int sourceAccountId, 
                              int destinationAccountId, BigDecimal amount, String description, 
                              Integer categoryId, Timestamp createdAt, Timestamp updatedAt) {
        this.templateId = templateId;
        this.userId = userId;
        this.name = name;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getSourceAccountId() {
        return sourceAccountId;
    }
    
    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }
    
    public int getDestinationAccountId() {
        return destinationAccountId;
    }
    
    public void setDestinationAccountId(int destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
        return "TransactionTemplate{" +
                "templateId=" + templateId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
