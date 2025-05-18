package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class for savings goals
 */
public class SavingsGoal {
    private int goalId;
    private int userId;
    private int accountId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private Date startDate;
    private Date targetDate;
    private Status status;
    private String icon;
    private String color;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Status enum
    public enum Status {
        ACTIVE, COMPLETED, CANCELLED;

        public static Status fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return ACTIVE;
            }
        }
    }

    // Default constructor
    public SavingsGoal() {
    }

    // Constructor with essential fields
    public SavingsGoal(int userId, int accountId, String name, BigDecimal targetAmount,
                      Date startDate, Date targetDate) {
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = BigDecimal.ZERO;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.status = Status.ACTIVE;
    }

    // Full constructor
    public SavingsGoal(int goalId, int userId, int accountId, String name, BigDecimal targetAmount,
                      BigDecimal currentAmount, Date startDate, Date targetDate, Status status,
                      String icon, String color, Timestamp createdAt, Timestamp updatedAt) {
        this.goalId = goalId;
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.status = status;
        this.icon = icon;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    /**
     * Calculate the progress percentage of the savings goal
     * @return percentage as a double between 0 and 100
     */
    public double getProgressPercentage() {
        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        return currentAmount.multiply(new BigDecimal("100"))
                .divide(targetAmount, 2, java.math.RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Check if the goal is completed
     * @return true if the current amount is greater than or equal to the target amount
     */
    public boolean isCompleted() {
        return currentAmount.compareTo(targetAmount) >= 0;
    }

    @Override
    public String toString() {
        return "SavingsGoal{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", accountId=" + accountId +
                ", name='" + name + '\'' +
                ", targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", startDate=" + startDate +
                ", targetDate=" + targetDate +
                ", status=" + status +
                ", progress=" + getProgressPercentage() + "%" +
                '}';
    }
}
