package com.example.hamrobank.service;

import com.example.hamrobank.dao.ScheduledTransactionDAO;
import com.example.hamrobank.dao.ScheduledTransactionDAOImpl;
import com.example.hamrobank.model.ScheduledTransaction;
import com.example.hamrobank.model.Transaction;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for scheduled transactions
 */
public class ScheduledTransactionService {
    
    private static final Logger LOGGER = Logger.getLogger(ScheduledTransactionService.class.getName());
    private final ScheduledTransactionDAO scheduledTransactionDAO;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     */
    public ScheduledTransactionService() {
        this.scheduledTransactionDAO = new ScheduledTransactionDAOImpl();
        this.transactionService = new TransactionService();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param scheduledTransactionDAO DAO to use
     * @param transactionService TransactionService to use
     * @param notificationService NotificationService to use
     */
    public ScheduledTransactionService(ScheduledTransactionDAO scheduledTransactionDAO, 
                                      TransactionService transactionService,
                                      NotificationService notificationService) {
        this.scheduledTransactionDAO = scheduledTransactionDAO;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }
    
    /**
     * Create a new scheduled transaction
     * 
     * @param scheduledTransaction ScheduledTransaction object to create
     * @return created ScheduledTransaction with ID
     */
    public ScheduledTransaction createScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        try {
            // Set next execution date if not set
            if (scheduledTransaction.getNextExecutionDate() == null) {
                scheduledTransaction.setNextExecutionDate(scheduledTransaction.getStartDate());
            }
            
            return scheduledTransactionDAO.createScheduledTransaction(scheduledTransaction);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating scheduled transaction", e);
            throw new RuntimeException("Error creating scheduled transaction", e);
        }
    }
    
    /**
     * Get a scheduled transaction by ID
     * 
     * @param scheduledTxId ID of the scheduled transaction to retrieve
     * @return ScheduledTransaction object if found, null otherwise
     */
    public ScheduledTransaction getScheduledTransactionById(int scheduledTxId) {
        try {
            return scheduledTransactionDAO.getScheduledTransactionById(scheduledTxId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transaction by ID", e);
            throw new RuntimeException("Error getting scheduled transaction by ID", e);
        }
    }
    
    /**
     * Get all scheduled transactions for a user
     * 
     * @param userId ID of the user
     * @return List of ScheduledTransaction objects for the user
     */
    public List<ScheduledTransaction> getScheduledTransactionsByUserId(int userId) {
        try {
            return scheduledTransactionDAO.getScheduledTransactionsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transactions by user ID", e);
            throw new RuntimeException("Error getting scheduled transactions by user ID", e);
        }
    }
    
    /**
     * Get all scheduled transactions for an account
     * 
     * @param accountId ID of the account
     * @return List of ScheduledTransaction objects for the account
     */
    public List<ScheduledTransaction> getScheduledTransactionsByAccountId(int accountId) {
        try {
            return scheduledTransactionDAO.getScheduledTransactionsByAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transactions by account ID", e);
            throw new RuntimeException("Error getting scheduled transactions by account ID", e);
        }
    }
    
    /**
     * Update a scheduled transaction
     * 
     * @param scheduledTransaction ScheduledTransaction object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        try {
            return scheduledTransactionDAO.updateScheduledTransaction(scheduledTransaction);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating scheduled transaction", e);
            throw new RuntimeException("Error updating scheduled transaction", e);
        }
    }
    
    /**
     * Update the status of a scheduled transaction
     * 
     * @param scheduledTxId ID of the scheduled transaction
     * @param status New status
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStatus(int scheduledTxId, ScheduledTransaction.Status status) {
        try {
            return scheduledTransactionDAO.updateStatus(scheduledTxId, status);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating scheduled transaction status", e);
            throw new RuntimeException("Error updating scheduled transaction status", e);
        }
    }
    
    /**
     * Delete a scheduled transaction
     * 
     * @param scheduledTxId ID of the scheduled transaction to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteScheduledTransaction(int scheduledTxId) {
        try {
            return scheduledTransactionDAO.deleteScheduledTransaction(scheduledTxId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting scheduled transaction", e);
            throw new RuntimeException("Error deleting scheduled transaction", e);
        }
    }
    
    /**
     * Execute due scheduled transactions
     * This method should be called by a scheduler
     */
    public void executeDueTransactions() {
        try {
            List<ScheduledTransaction> dueTransactions = scheduledTransactionDAO.getDueScheduledTransactions();
            
            for (ScheduledTransaction scheduledTx : dueTransactions) {
                try {
                    // Create and execute the actual transaction
                    Transaction transaction = createTransactionFromScheduled(scheduledTx);
                    Transaction executedTransaction = transactionService.createTransaction(transaction);
                    
                    // Update next execution date
                    Date nextExecutionDate = calculateNextExecutionDate(scheduledTx);
                    
                    // Check if this was the last execution
                    if (scheduledTx.getEndDate() != null && nextExecutionDate.after(scheduledTx.getEndDate())) {
                        // Mark as completed
                        scheduledTransactionDAO.updateStatus(scheduledTx.getScheduledTxId(), ScheduledTransaction.Status.COMPLETED);
                    } else {
                        // Update next execution date
                        scheduledTransactionDAO.updateNextExecutionDate(scheduledTx.getScheduledTxId(), nextExecutionDate);
                    }
                    
                    // Notify user
                    notificationService.sendTransactionNotification(
                        scheduledTx.getUserId(),
                        "Scheduled transaction executed",
                        "Your scheduled payment of " + scheduledTx.getAmount() + " has been processed."
                    );
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error executing scheduled transaction", e);
                    
                    // Mark as failed
                    scheduledTransactionDAO.updateStatus(scheduledTx.getScheduledTxId(), ScheduledTransaction.Status.FAILED);
                    
                    // Notify user of failure
                    notificationService.sendTransactionNotification(
                        scheduledTx.getUserId(),
                        "Scheduled transaction failed",
                        "Your scheduled payment could not be processed. Please check your account."
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing due transactions", e);
            throw new RuntimeException("Error executing due transactions", e);
        }
    }
    
    /**
     * Create a Transaction object from a ScheduledTransaction
     * 
     * @param scheduledTx ScheduledTransaction to convert
     * @return Transaction object
     */
    private Transaction createTransactionFromScheduled(ScheduledTransaction scheduledTx) {
        Transaction transaction = new Transaction(
            Transaction.TransactionType.TRANSFER,
            scheduledTx.getAmount(),
            scheduledTx.getSourceAccountId(),
            scheduledTx.getDestinationAccountId(),
            scheduledTx.getDescription() + " (Scheduled)"
        );
        
        return transaction;
    }
    
    /**
     * Calculate the next execution date for a scheduled transaction
     * 
     * @param scheduledTx ScheduledTransaction to calculate for
     * @return next execution date
     */
    private Date calculateNextExecutionDate(ScheduledTransaction scheduledTx) {
        LocalDate currentDate = scheduledTx.getNextExecutionDate().toLocalDate();
        LocalDate nextDate;
        
        switch (scheduledTx.getFrequency()) {
            case ONCE:
                // For one-time transactions, set a far future date
                nextDate = currentDate.plusYears(100);
                break;
            case DAILY:
                nextDate = currentDate.plusDays(1);
                break;
            case WEEKLY:
                nextDate = currentDate.plusWeeks(1);
                break;
            case MONTHLY:
                nextDate = currentDate.plusMonths(1);
                break;
            case QUARTERLY:
                nextDate = currentDate.plusMonths(3);
                break;
            case YEARLY:
                nextDate = currentDate.plusYears(1);
                break;
            default:
                nextDate = currentDate.plusMonths(1);
        }
        
        return Date.valueOf(nextDate);
    }
}
