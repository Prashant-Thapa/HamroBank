package com.example.hamrobank.service;

import com.example.hamrobank.dao.AccountDAO;
import com.example.hamrobank.dao.AccountDAOImpl;
import com.example.hamrobank.dao.TransactionCategoryDAO;
import com.example.hamrobank.dao.TransactionCategoryDAOImpl;
import com.example.hamrobank.dao.TransactionDAO;
import com.example.hamrobank.dao.TransactionDAOImpl;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.TransactionCategory;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for transactions
 */
public class TransactionService {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    private final TransactionDAO transactionDAO;
    private final AccountDAO accountDAO;
    private final TransactionCategoryDAO transactionCategoryDAO;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     */
    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.accountDAO = new AccountDAOImpl();
        this.transactionCategoryDAO = new TransactionCategoryDAOImpl();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param transactionDAO DAO to use
     * @param accountDAO AccountDAO to use
     * @param transactionCategoryDAO TransactionCategoryDAO to use
     * @param notificationService NotificationService to use
     */
    public TransactionService(TransactionDAO transactionDAO, AccountDAO accountDAO, 
                             TransactionCategoryDAO transactionCategoryDAO,
                             NotificationService notificationService) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
        this.transactionCategoryDAO = transactionCategoryDAO;
        this.notificationService = notificationService;
    }
    
    /**
     * Create a new transaction
     * 
     * @param transaction Transaction object to create
     * @return created Transaction with ID
     */
    public Transaction createTransaction(Transaction transaction) {
        try {
            // Validate transaction
            validateTransaction(transaction);
            
            // Process transaction based on type
            switch (transaction.getTransactionType()) {
                case DEPOSIT:
                    processDeposit(transaction);
                    break;
                case WITHDRAWAL:
                    processWithdrawal(transaction);
                    break;
                case TRANSFER:
                    processTransfer(transaction);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid transaction type");
            }
            
            // Create transaction record
            Transaction createdTransaction = transactionDAO.createTransaction(transaction);
            
            // Load category if available
            if (createdTransaction.getCategoryId() != null) {
                try {
                    TransactionCategory category = transactionCategoryDAO.getCategoryById(createdTransaction.getCategoryId());
                    createdTransaction.setCategory(category);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error loading transaction category", e);
                }
            }
            
            // Send notifications
            sendTransactionNotifications(createdTransaction);
            
            return createdTransaction;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction", e);
            throw new RuntimeException("Error creating transaction: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get a transaction by ID
     * 
     * @param transactionId ID of the transaction to retrieve
     * @return Transaction object if found, null otherwise
     */
    public Transaction getTransactionById(int transactionId) {
        try {
            Transaction transaction = transactionDAO.getTransactionById(transactionId);
            
            // Load category if available
            if (transaction != null && transaction.getCategoryId() != null) {
                try {
                    TransactionCategory category = transactionCategoryDAO.getCategoryById(transaction.getCategoryId());
                    transaction.setCategory(category);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error loading transaction category", e);
                }
            }
            
            return transaction;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction by ID", e);
            throw new RuntimeException("Error getting transaction by ID", e);
        }
    }
    
    /**
     * Get all transactions for an account
     * 
     * @param accountId ID of the account
     * @return List of Transaction objects for the account
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        try {
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);
            
            // Load categories for all transactions
            for (Transaction transaction : transactions) {
                if (transaction.getCategoryId() != null) {
                    try {
                        TransactionCategory category = transactionCategoryDAO.getCategoryById(transaction.getCategoryId());
                        transaction.setCategory(category);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading transaction category", e);
                    }
                }
            }
            
            return transactions;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by account ID", e);
            throw new RuntimeException("Error getting transactions by account ID", e);
        }
    }
    
    /**
     * Get transactions for an account within a date range
     * 
     * @param accountId ID of the account
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of Transaction objects for the account within the date range
     */
    public List<Transaction> getTransactionsByDateRange(int accountId, Date startDate, Date endDate) {
        try {
            List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(accountId, startDate, endDate);
            
            // Load categories for all transactions
            for (Transaction transaction : transactions) {
                if (transaction.getCategoryId() != null) {
                    try {
                        TransactionCategory category = transactionCategoryDAO.getCategoryById(transaction.getCategoryId());
                        transaction.setCategory(category);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading transaction category", e);
                    }
                }
            }
            
            return transactions;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by date range", e);
            throw new RuntimeException("Error getting transactions by date range", e);
        }
    }
    
    /**
     * Get transactions for an account by category
     * 
     * @param accountId ID of the account
     * @param categoryId ID of the category
     * @return List of Transaction objects for the account and category
     */
    public List<Transaction> getTransactionsByCategory(int accountId, int categoryId) {
        try {
            List<Transaction> transactions = transactionDAO.getTransactionsByCategory(accountId, categoryId);
            
            // Load category for all transactions
            TransactionCategory category = transactionCategoryDAO.getCategoryById(categoryId);
            for (Transaction transaction : transactions) {
                transaction.setCategory(category);
            }
            
            return transactions;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by category", e);
            throw new RuntimeException("Error getting transactions by category", e);
        }
    }
    
    /**
     * Update the category of a transaction
     * 
     * @param transactionId ID of the transaction
     * @param categoryId ID of the category
     * @return true if the update was successful, false otherwise
     */
    public boolean updateTransactionCategory(int transactionId, Integer categoryId) {
        try {
            return transactionDAO.updateTransactionCategory(transactionId, categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction category", e);
            throw new RuntimeException("Error updating transaction category", e);
        }
    }
    
    /**
     * Validate a transaction
     * 
     * @param transaction Transaction to validate
     * @throws Exception if validation fails
     */
    private void validateTransaction(Transaction transaction) throws Exception {
        // Check amount
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        
        // Check transaction type
        switch (transaction.getTransactionType()) {
            case DEPOSIT:
                // For deposits, only destination account is required
                if (transaction.getDestinationAccountId() == null) {
                    throw new IllegalArgumentException("Destination account is required for deposits");
                }
                
                // Verify destination account exists
                Account destinationAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
                if (destinationAccount == null) {
                    throw new IllegalArgumentException("Destination account not found");
                }
                break;
                
            case WITHDRAWAL:
                // For withdrawals, only source account is required
                if (transaction.getSourceAccountId() == null) {
                    throw new IllegalArgumentException("Source account is required for withdrawals");
                }
                
                // Verify source account exists
                Account sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
                if (sourceAccount == null) {
                    throw new IllegalArgumentException("Source account not found");
                }
                
                // Check if source account has sufficient balance
                if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient balance in source account");
                }
                break;
                
            case TRANSFER:
                // For transfers, both source and destination accounts are required
                if (transaction.getSourceAccountId() == null || transaction.getDestinationAccountId() == null) {
                    throw new IllegalArgumentException("Source and destination accounts are required for transfers");
                }
                
                // Verify source and destination accounts exist
                sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
                destinationAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
                
                if (sourceAccount == null) {
                    throw new IllegalArgumentException("Source account not found");
                }
                
                if (destinationAccount == null) {
                    throw new IllegalArgumentException("Destination account not found");
                }
                
                // Check if source account has sufficient balance
                if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient balance in source account");
                }
                break;
                
            default:
                throw new IllegalArgumentException("Invalid transaction type");
        }
        
        // Validate category if provided
        if (transaction.getCategoryId() != null) {
            TransactionCategory category = transactionCategoryDAO.getCategoryById(transaction.getCategoryId());
            if (category == null) {
                throw new IllegalArgumentException("Invalid category ID");
            }
        }
    }
    
    /**
     * Process a deposit transaction
     * 
     * @param transaction Transaction to process
     * @throws Exception if processing fails
     */
    private void processDeposit(Transaction transaction) throws Exception {
        // Update destination account balance
        Account destinationAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
        BigDecimal newBalance = destinationAccount.getBalance().add(transaction.getAmount());
        accountDAO.updateBalance(transaction.getDestinationAccountId(), newBalance);
    }
    
    /**
     * Process a withdrawal transaction
     * 
     * @param transaction Transaction to process
     * @throws Exception if processing fails
     */
    private void processWithdrawal(Transaction transaction) throws Exception {
        // Update source account balance
        Account sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
        BigDecimal newBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
        accountDAO.updateBalance(transaction.getSourceAccountId(), newBalance);
    }
    
    /**
     * Process a transfer transaction
     * 
     * @param transaction Transaction to process
     * @throws Exception if processing fails
     */
    private void processTransfer(Transaction transaction) throws Exception {
        // Update source account balance
        Account sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
        accountDAO.updateBalance(transaction.getSourceAccountId(), newSourceBalance);
        
        // Update destination account balance
        Account destinationAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
        BigDecimal newDestinationBalance = destinationAccount.getBalance().add(transaction.getAmount());
        accountDAO.updateBalance(transaction.getDestinationAccountId(), newDestinationBalance);
    }
    
    /**
     * Send notifications for a transaction
     * 
     * @param transaction Transaction to send notifications for
     */
    private void sendTransactionNotifications(Transaction transaction) {
        try {
            // Get account details
            Account sourceAccount = null;
            Account destinationAccount = null;
            
            if (transaction.getSourceAccountId() != null) {
                sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
            }
            
            if (transaction.getDestinationAccountId() != null) {
                destinationAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
            }
            
            // Send notifications based on transaction type
            switch (transaction.getTransactionType()) {
                case DEPOSIT:
                    if (destinationAccount != null) {
                        notificationService.sendTransactionNotification(
                            destinationAccount.getUserId(),
                            "Deposit Completed",
                            "A deposit of " + transaction.getAmount() + " has been made to your account."
                        );
                    }
                    break;
                    
                case WITHDRAWAL:
                    if (sourceAccount != null) {
                        notificationService.sendTransactionNotification(
                            sourceAccount.getUserId(),
                            "Withdrawal Completed",
                            "A withdrawal of " + transaction.getAmount() + " has been made from your account."
                        );
                    }
                    break;
                    
                case TRANSFER:
                    if (sourceAccount != null) {
                        notificationService.sendTransactionNotification(
                            sourceAccount.getUserId(),
                            "Transfer Sent",
                            "A transfer of " + transaction.getAmount() + " has been sent from your account."
                        );
                    }
                    
                    if (destinationAccount != null && 
                        (sourceAccount == null || sourceAccount.getUserId() != destinationAccount.getUserId())) {
                        notificationService.sendTransactionNotification(
                            destinationAccount.getUserId(),
                            "Transfer Received",
                            "A transfer of " + transaction.getAmount() + " has been received in your account."
                        );
                    }
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error sending transaction notifications", e);
        }
    }
}
