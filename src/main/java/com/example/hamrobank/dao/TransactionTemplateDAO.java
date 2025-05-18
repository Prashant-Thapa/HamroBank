package com.example.hamrobank.dao;

import com.example.hamrobank.model.TransactionTemplate;

import java.util.List;

/**
 * Data Access Object interface for transaction templates
 */
public interface TransactionTemplateDAO {
    
    /**
     * Create a new transaction template
     * 
     * @param template TransactionTemplate object to create
     * @return created TransactionTemplate with ID
     * @throws Exception if an error occurs during the operation
     */
    TransactionTemplate createTemplate(TransactionTemplate template) throws Exception;
    
    /**
     * Get a transaction template by ID
     * 
     * @param templateId ID of the template to retrieve
     * @return TransactionTemplate object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    TransactionTemplate getTemplateById(int templateId) throws Exception;
    
    /**
     * Get all transaction templates for a user
     * 
     * @param userId ID of the user
     * @return List of TransactionTemplate objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<TransactionTemplate> getTemplatesByUserId(int userId) throws Exception;
    
    /**
     * Get all transaction templates for a source account
     * 
     * @param accountId ID of the source account
     * @return List of TransactionTemplate objects for the account
     * @throws Exception if an error occurs during the operation
     */
    List<TransactionTemplate> getTemplatesBySourceAccountId(int accountId) throws Exception;
    
    /**
     * Update a transaction template
     * 
     * @param template TransactionTemplate object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateTemplate(TransactionTemplate template) throws Exception;
    
    /**
     * Delete a transaction template
     * 
     * @param templateId ID of the template to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteTemplate(int templateId) throws Exception;
}
