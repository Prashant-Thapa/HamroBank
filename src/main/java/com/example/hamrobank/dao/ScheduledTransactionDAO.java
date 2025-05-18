package com.example.hamrobank.dao;

import com.example.hamrobank.model.ScheduledTransaction;

import java.util.List;

/**
 * Data Access Object interface for scheduled transactions
 */
public interface ScheduledTransactionDAO {
    
    /**
     * Create a new scheduled transaction
     * 
     * @param scheduledTransaction ScheduledTransaction object to create
     * @return created ScheduledTransaction with ID
     * @throws Exception if an error occurs during the operation
     */
    ScheduledTransaction createScheduledTransaction(ScheduledTransaction scheduledTransaction) throws Exception;
    
    /**
     * Get a scheduled transaction by ID
     * 
     * @param scheduledTxId ID of the scheduled transaction to retrieve
     * @return ScheduledTransaction object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    ScheduledTransaction getScheduledTransactionById(int scheduledTxId) throws Exception;
    
    /**
     * Get all scheduled transactions for a user
     * 
     * @param userId ID of the user
     * @return List of ScheduledTransaction objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<ScheduledTransaction> getScheduledTransactionsByUserId(int userId) throws Exception;
    
    /**
     * Get all scheduled transactions for an account
     * 
     * @param accountId ID of the account
     * @return List of ScheduledTransaction objects for the account
     * @throws Exception if an error occurs during the operation
     */
    List<ScheduledTransaction> getScheduledTransactionsByAccountId(int accountId) throws Exception;
    
    /**
     * Get all scheduled transactions that are due for execution
     * 
     * @return List of ScheduledTransaction objects that are due
     * @throws Exception if an error occurs during the operation
     */
    List<ScheduledTransaction> getDueScheduledTransactions() throws Exception;
    
    /**
     * Update a scheduled transaction
     * 
     * @param scheduledTransaction ScheduledTransaction object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateScheduledTransaction(ScheduledTransaction scheduledTransaction) throws Exception;
    
    /**
     * Update the next execution date of a scheduled transaction
     * 
     * @param scheduledTxId ID of the scheduled transaction
     * @param nextExecutionDate Next execution date
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateNextExecutionDate(int scheduledTxId, java.sql.Date nextExecutionDate) throws Exception;
    
    /**
     * Update the status of a scheduled transaction
     * 
     * @param scheduledTxId ID of the scheduled transaction
     * @param status New status
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateStatus(int scheduledTxId, ScheduledTransaction.Status status) throws Exception;
    
    /**
     * Delete a scheduled transaction
     * 
     * @param scheduledTxId ID of the scheduled transaction to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteScheduledTransaction(int scheduledTxId) throws Exception;
}
