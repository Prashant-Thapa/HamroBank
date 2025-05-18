package com.example.hamrobank.dao;

import com.example.hamrobank.model.Budget;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for budgets
 */
public interface BudgetDAO {
    
    /**
     * Create a new budget
     * 
     * @param budget Budget object to create
     * @return created Budget with ID
     * @throws Exception if an error occurs during the operation
     */
    Budget createBudget(Budget budget) throws Exception;
    
    /**
     * Get a budget by ID
     * 
     * @param budgetId ID of the budget to retrieve
     * @return Budget object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Budget getBudgetById(int budgetId) throws Exception;
    
    /**
     * Get all budgets for a user
     * 
     * @param userId ID of the user
     * @return List of Budget objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<Budget> getBudgetsByUserId(int userId) throws Exception;
    
    /**
     * Get all budgets for a user by category
     * 
     * @param userId ID of the user
     * @param categoryId ID of the category
     * @return List of Budget objects for the user and category
     * @throws Exception if an error occurs during the operation
     */
    List<Budget> getBudgetsByCategory(int userId, int categoryId) throws Exception;
    
    /**
     * Get all active budgets for a user (budgets that are currently in effect)
     * 
     * @param userId ID of the user
     * @return List of active Budget objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<Budget> getActiveBudgets(int userId) throws Exception;
    
    /**
     * Update a budget
     * 
     * @param budget Budget object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateBudget(Budget budget) throws Exception;
    
    /**
     * Delete a budget
     * 
     * @param budgetId ID of the budget to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteBudget(int budgetId) throws Exception;
    
    /**
     * Get spending by category for a user within a date range
     * 
     * @param userId ID of the user
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return Map of category IDs to spending amounts
     * @throws Exception if an error occurs during the operation
     */
    Map<Integer, BigDecimal> getSpendingByCategory(int userId, Date startDate, Date endDate) throws Exception;
    
    /**
     * Get budget vs. actual spending for a user
     * 
     * @param userId ID of the user
     * @return Map of budget IDs to arrays containing [budget amount, actual spending]
     * @throws Exception if an error occurs during the operation
     */
    Map<Integer, BigDecimal[]> getBudgetVsActual(int userId) throws Exception;
}
