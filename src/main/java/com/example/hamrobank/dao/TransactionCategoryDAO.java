package com.example.hamrobank.dao;

import com.example.hamrobank.model.TransactionCategory;

import java.util.List;

/**
 * Data Access Object interface for transaction categories
 */
public interface TransactionCategoryDAO {
    
    /**
     * Create a new transaction category
     * 
     * @param category TransactionCategory object to create
     * @return created TransactionCategory with ID
     * @throws Exception if an error occurs during the operation
     */
    TransactionCategory createCategory(TransactionCategory category) throws Exception;
    
    /**
     * Get a transaction category by ID
     * 
     * @param categoryId ID of the category to retrieve
     * @return TransactionCategory object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    TransactionCategory getCategoryById(int categoryId) throws Exception;
    
    /**
     * Get all transaction categories
     * 
     * @return List of all TransactionCategory objects
     * @throws Exception if an error occurs during the operation
     */
    List<TransactionCategory> getAllCategories() throws Exception;
    
    /**
     * Get all system-defined transaction categories
     * 
     * @return List of system-defined TransactionCategory objects
     * @throws Exception if an error occurs during the operation
     */
    List<TransactionCategory> getSystemCategories() throws Exception;
    
    /**
     * Get all user-defined transaction categories
     * 
     * @return List of user-defined TransactionCategory objects
     * @throws Exception if an error occurs during the operation
     */
    List<TransactionCategory> getUserCategories() throws Exception;
    
    /**
     * Update a transaction category
     * 
     * @param category TransactionCategory object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateCategory(TransactionCategory category) throws Exception;
    
    /**
     * Delete a transaction category
     * 
     * @param categoryId ID of the category to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteCategory(int categoryId) throws Exception;
}
