package com.example.hamrobank.util;

import com.example.hamrobank.dao.*;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for creating sample data for testing
 */
public class SampleDataUtil {
    private static final Logger LOGGER = Logger.getLogger(SampleDataUtil.class.getName());
    
    /**
     * Create a sample transaction between two accounts
     * 
     * @return true if the transaction was created successfully, false otherwise
     */
    public static boolean createSampleTransaction() {
        try {
            // Get DAOs
            AccountDAO accountDAO = new AccountDAOImpl();
            TransactionDAO transactionDAO = new TransactionDAOImpl();
            
            // Get all accounts
            List<Account> accounts = accountDAO.getAllAccounts();
            
            // Check if we have at least 2 accounts
            if (accounts.size() < 2) {
                LOGGER.log(Level.WARNING, "Not enough accounts to create a sample transaction");
                return false;
            }
            
            // Get the first two accounts
            Account sourceAccount = accounts.get(0);
            Account destinationAccount = accounts.get(1);
            
            // Create a transaction
            Transaction transaction = new Transaction();
            transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
            transaction.setAmount(new BigDecimal("100.00"));
            transaction.setSourceAccountId(sourceAccount.getAccountId());
            transaction.setDestinationAccountId(destinationAccount.getAccountId());
            transaction.setDescription("Sample transaction for testing");
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            
            // Save the transaction
            transactionDAO.createTransaction(transaction);
            
            LOGGER.log(Level.INFO, "Sample transaction created successfully");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating sample transaction", e);
            return false;
        }
    }
}
