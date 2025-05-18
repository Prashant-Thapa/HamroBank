package com.example.hamrobank.dao;

import com.example.hamrobank.model.SavingsGoal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object interface for savings goals
 */
public interface SavingsGoalDAO {
    
    /**
     * Create a new savings goal
     * 
     * @param savingsGoal SavingsGoal object to create
     * @return created SavingsGoal with ID
     * @throws Exception if an error occurs during the operation
     */
    SavingsGoal createSavingsGoal(SavingsGoal savingsGoal) throws Exception;
    
    /**
     * Get a savings goal by ID
     * 
     * @param goalId ID of the savings goal to retrieve
     * @return SavingsGoal object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    SavingsGoal getSavingsGoalById(int goalId) throws Exception;
    
    /**
     * Get all savings goals for a user
     * 
     * @param userId ID of the user
     * @return List of SavingsGoal objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<SavingsGoal> getSavingsGoalsByUserId(int userId) throws Exception;
    
    /**
     * Get all savings goals for an account
     * 
     * @param accountId ID of the account
     * @return List of SavingsGoal objects for the account
     * @throws Exception if an error occurs during the operation
     */
    List<SavingsGoal> getSavingsGoalsByAccountId(int accountId) throws Exception;
    
    /**
     * Update a savings goal
     * 
     * @param savingsGoal SavingsGoal object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateSavingsGoal(SavingsGoal savingsGoal) throws Exception;
    
    /**
     * Update the current amount of a savings goal
     * 
     * @param goalId ID of the savings goal
     * @param amount New current amount
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateCurrentAmount(int goalId, BigDecimal amount) throws Exception;
    
    /**
     * Add to the current amount of a savings goal
     * 
     * @param goalId ID of the savings goal
     * @param amount Amount to add
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean addToCurrentAmount(int goalId, BigDecimal amount) throws Exception;
    
    /**
     * Update the status of a savings goal
     * 
     * @param goalId ID of the savings goal
     * @param status New status
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateStatus(int goalId, SavingsGoal.Status status) throws Exception;
    
    /**
     * Delete a savings goal
     * 
     * @param goalId ID of the savings goal to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteSavingsGoal(int goalId) throws Exception;
}
