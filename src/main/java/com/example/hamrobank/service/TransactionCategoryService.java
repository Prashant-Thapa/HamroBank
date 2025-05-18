package com.example.hamrobank.service;

import com.example.hamrobank.dao.TransactionCategoryDAO;
import com.example.hamrobank.dao.TransactionCategoryDAOImpl;
import com.example.hamrobank.model.TransactionCategory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for transaction categories
 */
public class TransactionCategoryService {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionCategoryService.class.getName());
    private final TransactionCategoryDAO transactionCategoryDAO;
    
    /**
     * Constructor
     */
    public TransactionCategoryService() {
        this.transactionCategoryDAO = new TransactionCategoryDAOImpl();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param transactionCategoryDAO DAO to use
     */
    public TransactionCategoryService(TransactionCategoryDAO transactionCategoryDAO) {
        this.transactionCategoryDAO = transactionCategoryDAO;
    }
    
    /**
     * Create a new transaction category
     * 
     * @param category TransactionCategory object to create
     * @return created TransactionCategory with ID
     */
    public TransactionCategory createCategory(TransactionCategory category) {
        try {
            return transactionCategoryDAO.createCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction category", e);
            throw new RuntimeException("Error creating transaction category", e);
        }
    }
    
    /**
     * Get a transaction category by ID
     * 
     * @param categoryId ID of the category to retrieve
     * @return TransactionCategory object if found, null otherwise
     */
    public TransactionCategory getCategoryById(int categoryId) {
        try {
            return transactionCategoryDAO.getCategoryById(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction category by ID", e);
            throw new RuntimeException("Error getting transaction category by ID", e);
        }
    }
    
    /**
     * Get all transaction categories
     * 
     * @return List of all TransactionCategory objects
     */
    public List<TransactionCategory> getAllCategories() {
        try {
            return transactionCategoryDAO.getAllCategories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all transaction categories", e);
            throw new RuntimeException("Error getting all transaction categories", e);
        }
    }
    
    /**
     * Get all system-defined transaction categories
     * 
     * @return List of system-defined TransactionCategory objects
     */
    public List<TransactionCategory> getSystemCategories() {
        try {
            return transactionCategoryDAO.getSystemCategories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting system transaction categories", e);
            throw new RuntimeException("Error getting system transaction categories", e);
        }
    }
    
    /**
     * Get all user-defined transaction categories
     * 
     * @return List of user-defined TransactionCategory objects
     */
    public List<TransactionCategory> getUserCategories() {
        try {
            return transactionCategoryDAO.getUserCategories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user transaction categories", e);
            throw new RuntimeException("Error getting user transaction categories", e);
        }
    }
    
    /**
     * Update a transaction category
     * 
     * @param category TransactionCategory object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateCategory(TransactionCategory category) {
        try {
            // Check if this is a system category
            TransactionCategory existingCategory = transactionCategoryDAO.getCategoryById(category.getCategoryId());
            if (existingCategory != null && existingCategory.isSystem()) {
                // Cannot modify system categories
                LOGGER.log(Level.WARNING, "Cannot modify system category with ID: " + category.getCategoryId());
                return false;
            }
            
            return transactionCategoryDAO.updateCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction category", e);
            throw new RuntimeException("Error updating transaction category", e);
        }
    }
    
    /**
     * Delete a transaction category
     * 
     * @param categoryId ID of the category to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        try {
            // Check if this is a system category
            TransactionCategory existingCategory = transactionCategoryDAO.getCategoryById(categoryId);
            if (existingCategory != null && existingCategory.isSystem()) {
                // Cannot delete system categories
                LOGGER.log(Level.WARNING, "Cannot delete system category with ID: " + categoryId);
                return false;
            }
            
            return transactionCategoryDAO.deleteCategory(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction category", e);
            throw new RuntimeException("Error deleting transaction category", e);
        }
    }
}
