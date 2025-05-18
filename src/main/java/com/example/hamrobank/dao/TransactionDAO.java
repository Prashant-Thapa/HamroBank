package com.example.hamrobank.dao;

import com.example.hamrobank.model.Transaction;
import java.sql.Date;
import java.util.List;

/**
 * Interface for Transaction data access operations
 */
public interface TransactionDAO {

    /**
     * Create a new transaction
     *
     * @param transaction Transaction object to create
     * @return The created transaction with ID set
     * @throws Exception if an error occurs during the operation
     */
    Transaction createTransaction(Transaction transaction) throws Exception;

    /**
     * Get a transaction by ID
     *
     * @param transactionId ID of the transaction to retrieve
     * @return Transaction object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Transaction getTransactionById(int transactionId) throws Exception;

    /**
     * Get a transaction by reference number
     *
     * @param referenceNumber Reference number of the transaction to retrieve
     * @return Transaction object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Transaction getTransactionByReferenceNumber(String referenceNumber) throws Exception;

    /**
     * Get all transactions for an account (as source or destination)
     *
     * @param accountId ID of the account
     * @return List of transactions involving the account
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getTransactionsByAccountId(int accountId) throws Exception;

    /**
     * Get all transactions where the account is the source
     *
     * @param accountId ID of the source account
     * @return List of transactions where the account is the source
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getTransactionsBySourceAccountId(int accountId) throws Exception;

    /**
     * Get all transactions where the account is the destination
     *
     * @param accountId ID of the destination account
     * @return List of transactions where the account is the destination
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getTransactionsByDestinationAccountId(int accountId) throws Exception;

    /**
     * Update the status of a transaction
     *
     * @param transactionId ID of the transaction
     * @param status New status for the transaction
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateTransactionStatus(int transactionId, Transaction.TransactionStatus status) throws Exception;

    /**
     * Generate a unique reference number for a transaction
     *
     * @return A unique reference number
     * @throws Exception if an error occurs during the operation
     */
    String generateReferenceNumber() throws Exception;

    /**
     * Get all transactions
     *
     * @return List of all transactions
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getAllTransactions() throws Exception;

    /**
     * Get recent transactions for an account or all accounts
     *
     * @param accountId ID of the account, or 0 to get transactions for all accounts
     * @param limit Maximum number of transactions to retrieve
     * @return List of recent transactions involving the account, or all recent transactions if accountId is 0
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getRecentTransactions(int accountId, int limit) throws Exception;

    /**
     * Get transactions for an account within a date range
     *
     * @param accountId ID of the account
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of transactions for the account within the date range
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getTransactionsByDateRange(int accountId, Date startDate, Date endDate) throws Exception;

    /**
     * Get transactions for an account by category
     *
     * @param accountId ID of the account
     * @param categoryId ID of the category
     * @return List of transactions for the account with the specified category
     * @throws Exception if an error occurs during the operation
     */
    List<Transaction> getTransactionsByCategory(int accountId, int categoryId) throws Exception;

    /**
     * Update the category of a transaction
     *
     * @param transactionId ID of the transaction
     * @param categoryId ID of the category, or null to remove the category
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateTransactionCategory(int transactionId, Integer categoryId) throws Exception;
}
