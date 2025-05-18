package com.example.hamrobank.service;

import com.example.hamrobank.dao.SavingsGoalDAO;
import com.example.hamrobank.dao.SavingsGoalDAOImpl;
import com.example.hamrobank.model.SavingsGoal;
import com.example.hamrobank.model.Transaction;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for savings goals
 */
public class SavingsGoalService {
    
    private static final Logger LOGGER = Logger.getLogger(SavingsGoalService.class.getName());
    private final SavingsGoalDAO savingsGoalDAO;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     */
    public SavingsGoalService() {
        this.savingsGoalDAO = new SavingsGoalDAOImpl();
        this.transactionService = new TransactionService();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param savingsGoalDAO DAO to use
     * @param transactionService TransactionService to use
     * @param notificationService NotificationService to use
     */
    public SavingsGoalService(SavingsGoalDAO savingsGoalDAO, 
                             TransactionService transactionService,
                             NotificationService notificationService) {
        this.savingsGoalDAO = savingsGoalDAO;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }
    
    /**
     * Create a new savings goal
     * 
     * @param savingsGoal SavingsGoal object to create
     * @return created SavingsGoal with ID
     */
    public SavingsGoal createSavingsGoal(SavingsGoal savingsGoal) {
        try {
            // Set default values if not set
            if (savingsGoal.getCurrentAmount() == null) {
                savingsGoal.setCurrentAmount(BigDecimal.ZERO);
            }
            
            if (savingsGoal.getStartDate() == null) {
                savingsGoal.setStartDate(Date.valueOf(LocalDate.now()));
            }
            
            if (savingsGoal.getStatus() == null) {
                savingsGoal.setStatus(SavingsGoal.Status.ACTIVE);
            }
            
            SavingsGoal createdGoal = savingsGoalDAO.createSavingsGoal(savingsGoal);
            
            // Send notification
            notificationService.sendBalanceNotification(
                savingsGoal.getUserId(),
                "New Savings Goal Created",
                "You have created a new savings goal: " + savingsGoal.getName()
            );
            
            return createdGoal;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating savings goal", e);
            throw new RuntimeException("Error creating savings goal", e);
        }
    }
    
    /**
     * Get a savings goal by ID
     * 
     * @param goalId ID of the savings goal to retrieve
     * @return SavingsGoal object if found, null otherwise
     */
    public SavingsGoal getSavingsGoalById(int goalId) {
        try {
            return savingsGoalDAO.getSavingsGoalById(goalId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goal by ID", e);
            throw new RuntimeException("Error getting savings goal by ID", e);
        }
    }
    
    /**
     * Get all savings goals for a user
     * 
     * @param userId ID of the user
     * @return List of SavingsGoal objects for the user
     */
    public List<SavingsGoal> getSavingsGoalsByUserId(int userId) {
        try {
            return savingsGoalDAO.getSavingsGoalsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goals by user ID", e);
            throw new RuntimeException("Error getting savings goals by user ID", e);
        }
    }
    
    /**
     * Get all savings goals for an account
     * 
     * @param accountId ID of the account
     * @return List of SavingsGoal objects for the account
     */
    public List<SavingsGoal> getSavingsGoalsByAccountId(int accountId) {
        try {
            return savingsGoalDAO.getSavingsGoalsByAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goals by account ID", e);
            throw new RuntimeException("Error getting savings goals by account ID", e);
        }
    }
    
    /**
     * Update a savings goal
     * 
     * @param savingsGoal SavingsGoal object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateSavingsGoal(SavingsGoal savingsGoal) {
        try {
            return savingsGoalDAO.updateSavingsGoal(savingsGoal);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating savings goal", e);
            throw new RuntimeException("Error updating savings goal", e);
        }
    }
    
    /**
     * Make a deposit to a savings goal
     * 
     * @param goalId ID of the savings goal
     * @param amount Amount to deposit
     * @param userId ID of the user making the deposit
     * @return updated SavingsGoal
     */
    public SavingsGoal makeDeposit(int goalId, BigDecimal amount, int userId) {
        try {
            // Get the savings goal
            SavingsGoal savingsGoal = savingsGoalDAO.getSavingsGoalById(goalId);
            
            if (savingsGoal == null) {
                throw new RuntimeException("Savings goal not found");
            }
            
            // Check if the goal is active
            if (savingsGoal.getStatus() != SavingsGoal.Status.ACTIVE) {
                throw new RuntimeException("Cannot deposit to a non-active savings goal");
            }
            
            // Add to current amount
            boolean success = savingsGoalDAO.addToCurrentAmount(goalId, amount);
            
            if (!success) {
                throw new RuntimeException("Error adding to savings goal");
            }
            
            // Create a transaction record
            Transaction transaction = new Transaction(
                Transaction.TransactionType.TRANSFER,
                amount,
                null,  // Source account is null for a deposit
                savingsGoal.getAccountId(),
                "Deposit to savings goal: " + savingsGoal.getName()
            );
            
            transactionService.createTransaction(transaction);
            
            // Check if goal is completed
            SavingsGoal updatedGoal = savingsGoalDAO.getSavingsGoalById(goalId);
            
            if (updatedGoal.isCompleted()) {
                // Send completion notification
                notificationService.sendBalanceNotification(
                    userId,
                    "Savings Goal Completed",
                    "Congratulations! You have reached your savings goal: " + updatedGoal.getName()
                );
            } else {
                // Send deposit notification
                notificationService.sendBalanceNotification(
                    userId,
                    "Deposit to Savings Goal",
                    "You have deposited " + amount + " to your savings goal: " + updatedGoal.getName()
                );
            }
            
            return updatedGoal;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error making deposit to savings goal", e);
            throw new RuntimeException("Error making deposit to savings goal", e);
        }
    }
    
    /**
     * Update the status of a savings goal
     * 
     * @param goalId ID of the savings goal
     * @param status New status
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStatus(int goalId, SavingsGoal.Status status) {
        try {
            return savingsGoalDAO.updateStatus(goalId, status);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating savings goal status", e);
            throw new RuntimeException("Error updating savings goal status", e);
        }
    }
    
    /**
     * Delete a savings goal
     * 
     * @param goalId ID of the savings goal to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteSavingsGoal(int goalId) {
        try {
            return savingsGoalDAO.deleteSavingsGoal(goalId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting savings goal", e);
            throw new RuntimeException("Error deleting savings goal", e);
        }
    }
    
    /**
     * Calculate progress percentage for a savings goal
     * 
     * @param goalId ID of the savings goal
     * @return progress percentage (0-100)
     */
    public double calculateProgressPercentage(int goalId) {
        try {
            SavingsGoal savingsGoal = savingsGoalDAO.getSavingsGoalById(goalId);
            
            if (savingsGoal == null) {
                throw new RuntimeException("Savings goal not found");
            }
            
            return savingsGoal.getProgressPercentage();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating progress percentage", e);
            throw new RuntimeException("Error calculating progress percentage", e);
        }
    }
    
    /**
     * Calculate remaining amount for a savings goal
     * 
     * @param goalId ID of the savings goal
     * @return remaining amount
     */
    public BigDecimal calculateRemainingAmount(int goalId) {
        try {
            SavingsGoal savingsGoal = savingsGoalDAO.getSavingsGoalById(goalId);
            
            if (savingsGoal == null) {
                throw new RuntimeException("Savings goal not found");
            }
            
            return savingsGoal.getTargetAmount().subtract(savingsGoal.getCurrentAmount());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating remaining amount", e);
            throw new RuntimeException("Error calculating remaining amount", e);
        }
    }
}
