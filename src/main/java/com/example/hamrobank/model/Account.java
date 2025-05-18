package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Account model class representing a bank account
 */
public class Account {
    private int accountId;
    private int userId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Enum for account types
    public enum AccountType {
        SAVINGS, CHECKING, FIXED_DEPOSIT
    }

    // Enum for account status
    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED, CLOSED
    }

    // Default constructor
    public Account() {
    }

    // Constructor with essential fields
    public Account(int userId, String accountNumber, AccountType accountType, BigDecimal balance) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = AccountStatus.ACTIVE;
    }

    // Full constructor
    public Account(int accountId, int userId, String accountNumber, AccountType accountType,
                  BigDecimal balance, AccountStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
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

    /**
     * Get the minimum balance for this account type
     *
     * @return minimum balance
     */
    public BigDecimal getMinimumBalance() {
        // Default minimum balances based on account type
        switch (accountType) {
            case SAVINGS:
                return new BigDecimal("100.00");
            case CHECKING:
                return new BigDecimal("50.00");
            case FIXED_DEPOSIT:
                return new BigDecimal("1000.00");
            default:
                return BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
