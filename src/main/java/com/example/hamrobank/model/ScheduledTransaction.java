package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class for scheduled transactions
 */
public class ScheduledTransaction {
    private int scheduledTxId;
    private int userId;
    private int sourceAccountId;
    private int destinationAccountId;
    private BigDecimal amount;
    private String description;
    private Frequency frequency;
    private Date startDate;
    private Date endDate;
    private Date nextExecutionDate;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Frequency enum
    public enum Frequency {
        ONCE, DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY;
        
        public static Frequency fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return ONCE;
            }
        }
    }
    
    // Status enum
    public enum Status {
        ACTIVE, PAUSED, COMPLETED, FAILED;
        
        public static Status fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return ACTIVE;
            }
        }
    }
    
    // Default constructor
    public ScheduledTransaction() {
    }
    
    // Constructor with essential fields
    public ScheduledTransaction(int userId, int sourceAccountId, int destinationAccountId, 
                               BigDecimal amount, String description, Frequency frequency, 
                               Date startDate, Date nextExecutionDate) {
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.nextExecutionDate = nextExecutionDate;
        this.status = Status.ACTIVE;
    }
    
    // Full constructor
    public ScheduledTransaction(int scheduledTxId, int userId, int sourceAccountId, int destinationAccountId, 
                               BigDecimal amount, String description, Frequency frequency, Date startDate, 
                               Date endDate, Date nextExecutionDate, Status status, Timestamp createdAt, 
                               Timestamp updatedAt) {
        this.scheduledTxId = scheduledTxId;
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextExecutionDate = nextExecutionDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getScheduledTxId() {
        return scheduledTxId;
    }
    
    public void setScheduledTxId(int scheduledTxId) {
        this.scheduledTxId = scheduledTxId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
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
    
    public Frequency getFrequency() {
        return frequency;
    }
    
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
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
    
    public Date getNextExecutionDate() {
        return nextExecutionDate;
    }
    
    public void setNextExecutionDate(Date nextExecutionDate) {
        this.nextExecutionDate = nextExecutionDate;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
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
        return "ScheduledTransaction{" +
                "scheduledTxId=" + scheduledTxId +
                ", userId=" + userId +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", frequency=" + frequency +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", nextExecutionDate=" + nextExecutionDate +
                ", status=" + status +
                '}';
    }
}
