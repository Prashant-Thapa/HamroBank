package com.example.hamrobank.service;

import com.example.hamrobank.dao.TransactionTemplateDAO;
import com.example.hamrobank.dao.TransactionTemplateDAOImpl;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.TransactionTemplate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for transaction templates
 */
public class TransactionTemplateService {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionTemplateService.class.getName());
    private final TransactionTemplateDAO transactionTemplateDAO;
    private final TransactionService transactionService;
    
    /**
     * Constructor
     */
    public TransactionTemplateService() {
        this.transactionTemplateDAO = new TransactionTemplateDAOImpl();
        this.transactionService = new TransactionService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param transactionTemplateDAO DAO to use
     * @param transactionService TransactionService to use
     */
    public TransactionTemplateService(TransactionTemplateDAO transactionTemplateDAO, TransactionService transactionService) {
        this.transactionTemplateDAO = transactionTemplateDAO;
        this.transactionService = transactionService;
    }
    
    /**
     * Create a new transaction template
     * 
     * @param template TransactionTemplate object to create
     * @return created TransactionTemplate with ID
     */
    public TransactionTemplate createTemplate(TransactionTemplate template) {
        try {
            return transactionTemplateDAO.createTemplate(template);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction template", e);
            throw new RuntimeException("Error creating transaction template", e);
        }
    }
    
    /**
     * Get a transaction template by ID
     * 
     * @param templateId ID of the template to retrieve
     * @return TransactionTemplate object if found, null otherwise
     */
    public TransactionTemplate getTemplateById(int templateId) {
        try {
            return transactionTemplateDAO.getTemplateById(templateId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction template by ID", e);
            throw new RuntimeException("Error getting transaction template by ID", e);
        }
    }
    
    /**
     * Get all transaction templates for a user
     * 
     * @param userId ID of the user
     * @return List of TransactionTemplate objects for the user
     */
    public List<TransactionTemplate> getTemplatesByUserId(int userId) {
        try {
            return transactionTemplateDAO.getTemplatesByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction templates by user ID", e);
            throw new RuntimeException("Error getting transaction templates by user ID", e);
        }
    }
    
    /**
     * Get all transaction templates for a source account
     * 
     * @param accountId ID of the source account
     * @return List of TransactionTemplate objects for the account
     */
    public List<TransactionTemplate> getTemplatesBySourceAccountId(int accountId) {
        try {
            return transactionTemplateDAO.getTemplatesBySourceAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction templates by source account ID", e);
            throw new RuntimeException("Error getting transaction templates by source account ID", e);
        }
    }
    
    /**
     * Update a transaction template
     * 
     * @param template TransactionTemplate object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateTemplate(TransactionTemplate template) {
        try {
            return transactionTemplateDAO.updateTemplate(template);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction template", e);
            throw new RuntimeException("Error updating transaction template", e);
        }
    }
    
    /**
     * Delete a transaction template
     * 
     * @param templateId ID of the template to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteTemplate(int templateId) {
        try {
            return transactionTemplateDAO.deleteTemplate(templateId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction template", e);
            throw new RuntimeException("Error deleting transaction template", e);
        }
    }
    
    /**
     * Execute a transaction from a template
     * 
     * @param templateId ID of the template to execute
     * @return created Transaction
     */
    public Transaction executeTemplate(int templateId) {
        try {
            // Get the template
            TransactionTemplate template = transactionTemplateDAO.getTemplateById(templateId);
            
            if (template == null) {
                throw new RuntimeException("Transaction template not found");
            }
            
            // Create a transaction from the template
            Transaction transaction = new Transaction(
                Transaction.TransactionType.TRANSFER,
                template.getAmount(),
                template.getSourceAccountId(),
                template.getDestinationAccountId(),
                template.getDescription() + " (From Template)"
            );
            
            // Set category if available
            if (template.getCategoryId() != null) {
                transaction.setCategoryId(template.getCategoryId());
            }
            
            // Execute the transaction
            return transactionService.createTransaction(transaction);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing transaction template", e);
            throw new RuntimeException("Error executing transaction template", e);
        }
    }
    
    /**
     * Create a scheduled transaction from a template
     * 
     * @param templateId ID of the template
     * @param frequency Frequency of the scheduled transaction
     * @param startDate Start date of the scheduled transaction
     * @param endDate End date of the scheduled transaction (can be null)
     * @return ID of the created scheduled transaction
     */
    public int createScheduledTransactionFromTemplate(int templateId, String frequency, java.sql.Date startDate, java.sql.Date endDate) {
        try {
            // Get the template
            TransactionTemplate template = transactionTemplateDAO.getTemplateById(templateId);
            
            if (template == null) {
                throw new RuntimeException("Transaction template not found");
            }
            
            // Create a scheduled transaction from the template
            ScheduledTransactionService scheduledTransactionService = new ScheduledTransactionService();
            
            com.example.hamrobank.model.ScheduledTransaction scheduledTransaction = new com.example.hamrobank.model.ScheduledTransaction(
                template.getUserId(),
                template.getSourceAccountId(),
                template.getDestinationAccountId(),
                template.getAmount(),
                template.getDescription() + " (Scheduled)",
                com.example.hamrobank.model.ScheduledTransaction.Frequency.fromString(frequency),
                startDate,
                startDate  // Next execution date is initially the start date
            );
            
            scheduledTransaction.setEndDate(endDate);
            
            // Create the scheduled transaction
            com.example.hamrobank.model.ScheduledTransaction createdScheduledTransaction = 
                scheduledTransactionService.createScheduledTransaction(scheduledTransaction);
            
            return createdScheduledTransaction.getScheduledTxId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating scheduled transaction from template", e);
            throw new RuntimeException("Error creating scheduled transaction from template", e);
        }
    }
}
