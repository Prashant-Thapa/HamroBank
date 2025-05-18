package com.example.hamrobank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Transaction model class representing a bank transaction
 */
public class Transaction {
    private int transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Integer sourceAccountId;
    private Integer destinationAccountId;
    private String description;
    private TransactionStatus status;
    private Timestamp transactionDate;
    private String referenceNumber;
    private Integer categoryId;

    // Additional fields for display purposes (not in database)
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private TransactionCategory category;

    // Enum for transaction types
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    // Enum for transaction status
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }

    // Default constructor
    public Transaction() {
    }

    // Constructor with essential fields
    public Transaction(TransactionType transactionType, BigDecimal amount,
                      Integer sourceAccountId, Integer destinationAccountId,
                      String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.description = description;
        this.status = TransactionStatus.COMPLETED;
    }

    // Constructor with category
    public Transaction(TransactionType transactionType, BigDecimal amount,
                      Integer sourceAccountId, Integer destinationAccountId,
                      String description, Integer categoryId) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.description = description;
        this.categoryId = categoryId;
        this.status = TransactionStatus.COMPLETED;
    }

    // Full constructor
    public Transaction(int transactionId, TransactionType transactionType, BigDecimal amount,
                      Integer sourceAccountId, Integer destinationAccountId, String description,
                      TransactionStatus status, Timestamp transactionDate, String referenceNumber,
                      Integer categoryId) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.description = description;
        this.status = status;
        this.transactionDate = transactionDate;
        this.referenceNumber = referenceNumber;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Integer sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Integer destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", status=" + status +
                ", transactionDate=" + transactionDate +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
