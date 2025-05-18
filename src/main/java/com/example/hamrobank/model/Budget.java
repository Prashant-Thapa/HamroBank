package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class for budgets
 */
public class Budget {
    private int budgetId;
    private int userId;
    private String name;
    private BigDecimal amount;
    private String period;
    private Integer categoryId;
    private Date startDate;
    private Date endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Budget periods
    public static final String PERIOD_MONTHLY = "MONTHLY";
    public static final String PERIOD_QUARTERLY = "QUARTERLY";
    public static final String PERIOD_YEARLY = "YEARLY";
    
    // Default constructor
    public Budget() {
    }
    
    // Constructor with essential fields
    public Budget(int userId, String name, BigDecimal amount, String period, Integer categoryId, Date startDate) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.period = period;
        this.categoryId = categoryId;
        this.startDate = startDate;
    }
    
    // Full constructor
    public Budget(int budgetId, int userId, String name, BigDecimal amount, String period, 
                 Integer categoryId, Date startDate, Date endDate, Timestamp createdAt, 
                 Timestamp updatedAt) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.period = period;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getBudgetId() {
        return budgetId;
    }
    
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
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
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        return "Budget{" +
                "budgetId=" + budgetId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", period='" + period + '\'' +
                ", categoryId=" + categoryId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
