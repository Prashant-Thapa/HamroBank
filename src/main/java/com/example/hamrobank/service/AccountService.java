package com.example.hamrobank.service;

import com.example.hamrobank.dao.AccountDAO;
import com.example.hamrobank.dao.AccountDAOImpl;
import com.example.hamrobank.dao.AccountOwnerDAO;
import com.example.hamrobank.dao.AccountOwnerDAOImpl;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.AccountOwner;
import com.example.hamrobank.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for account operations
 */
public class AccountService {
    
    private static final Logger LOGGER = Logger.getLogger(AccountService.class.getName());
    private final AccountDAO accountDAO;
    private final AccountOwnerDAO accountOwnerDAO;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     */
    public AccountService() {
        this.accountDAO = new AccountDAOImpl();
        this.accountOwnerDAO = new AccountOwnerDAOImpl();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param accountDAO DAO to use
     * @param accountOwnerDAO AccountOwnerDAO to use
     * @param notificationService NotificationService to use
     */
    public AccountService(AccountDAO accountDAO, AccountOwnerDAO accountOwnerDAO, NotificationService notificationService) {
        this.accountDAO = accountDAO;
        this.accountOwnerDAO = accountOwnerDAO;
        this.notificationService = notificationService;
    }
    
    /**
     * Create a new account
     * 
     * @param account Account object to create
     * @return created Account with ID
     */
    public Account createAccount(Account account) {
        try {
            Account createdAccount = accountDAO.createAccount(account);
            
            // Add the user as the primary owner of the account
            AccountOwner primaryOwner = new AccountOwner(createdAccount.getAccountId(), account.getUserId(), 
                                                        AccountOwner.PERMISSION_FULL, true);
            accountOwnerDAO.addAccountOwner(primaryOwner);
            
            // Send notification
            notificationService.sendBalanceNotification(
                account.getUserId(),
                "Account Created",
                "Your new " + account.getAccountType() + " account has been created successfully."
            );
            
            return createdAccount;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating account", e);
            throw new RuntimeException("Error creating account", e);
        }
    }
    
    /**
     * Get an account by ID
     * 
     * @param accountId ID of the account to retrieve
     * @return Account object if found, null otherwise
     */
    public Account getAccountById(int accountId) {
        try {
            return accountDAO.getAccountById(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account by ID", e);
            throw new RuntimeException("Error getting account by ID", e);
        }
    }
    
    /**
     * Get an account by account number
     * 
     * @param accountNumber Account number to retrieve
     * @return Account object if found, null otherwise
     */
    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountDAO.getAccountByAccountNumber(accountNumber);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account by account number", e);
            throw new RuntimeException("Error getting account by account number", e);
        }
    }
    
    /**
     * Get all accounts for a user
     * 
     * @param userId ID of the user
     * @return List of Account objects for the user
     */
    public List<Account> getAccountsByUserId(int userId) {
        try {
            return accountDAO.getAccountsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting accounts by user ID", e);
            throw new RuntimeException("Error getting accounts by user ID", e);
        }
    }
    
    /**
     * Update an account
     * 
     * @param account Account object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateAccount(Account account) {
        try {
            return accountDAO.updateAccount(account);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating account", e);
            throw new RuntimeException("Error updating account", e);
        }
    }
    
    /**
     * Update account balance
     * 
     * @param accountId ID of the account
     * @param newBalance New balance
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBalance(int accountId, BigDecimal newBalance) {
        try {
            Account account = accountDAO.getAccountById(accountId);
            
            if (account == null) {
                throw new RuntimeException("Account not found");
            }
            
            // Check if balance is going below minimum balance
            if (newBalance.compareTo(account.getMinimumBalance()) < 0) {
                // Send low balance notification
                notificationService.sendBalanceNotification(
                    account.getUserId(),
                    "Low Balance Alert",
                    "Your account balance is below the minimum required balance."
                );
            }
            
            return accountDAO.updateBalance(accountId, newBalance);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating account balance", e);
            throw new RuntimeException("Error updating account balance", e);
        }
    }
    
    /**
     * Close an account
     * 
     * @param accountId ID of the account to close
     * @return true if the closure was successful, false otherwise
     */
    public boolean closeAccount(int accountId) {
        try {
            Account account = accountDAO.getAccountById(accountId);
            
            if (account == null) {
                throw new RuntimeException("Account not found");
            }
            
            // Check if account has zero balance
            if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new RuntimeException("Account must have zero balance to close");
            }
            
            boolean success = accountDAO.closeAccount(accountId);
            
            if (success) {
                // Send notification
                notificationService.sendBalanceNotification(
                    account.getUserId(),
                    "Account Closed",
                    "Your " + account.getAccountType() + " account has been closed successfully."
                );
            }
            
            return success;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error closing account", e);
            throw new RuntimeException("Error closing account", e);
        }
    }
    
    /**
     * Create a joint account
     * 
     * @param account Account object to create
     * @param primaryUserId ID of the primary user
     * @param secondaryUserIds List of IDs of secondary users
     * @return created Account with ID
     */
    public Account createJointAccount(Account account, int primaryUserId, List<Integer> secondaryUserIds) {
        try {
            // Set the primary user ID
            account.setUserId(primaryUserId);
            
            // Create the account
            Account createdAccount = accountDAO.createAccount(account);
            
            // Set up joint account owners
            AccountOwnerService accountOwnerService = new AccountOwnerService();
            accountOwnerService.createJointAccount(createdAccount.getAccountId(), primaryUserId, secondaryUserIds);
            
            return createdAccount;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating joint account", e);
            throw new RuntimeException("Error creating joint account", e);
        }
    }
    
    /**
     * Get all users who own an account
     * 
     * @param accountId ID of the account
     * @return List of User objects who own the account
     */
    public List<User> getAccountOwners(int accountId) {
        try {
            return accountOwnerDAO.getUsersByAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account owners", e);
            throw new RuntimeException("Error getting account owners", e);
        }
    }
    
    /**
     * Check if a user is an owner of an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user
     * @return true if the user is an owner of the account, false otherwise
     */
    public boolean isAccountOwner(int accountId, int userId) {
        try {
            return accountOwnerDAO.isAccountOwner(accountId, userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if user is account owner", e);
            throw new RuntimeException("Error checking if user is account owner", e);
        }
    }
    
    /**
     * Get the total balance of all accounts for a user
     * 
     * @param userId ID of the user
     * @return total balance
     */
    public BigDecimal getTotalBalance(int userId) {
        try {
            List<Account> accounts = accountDAO.getAccountsByUserId(userId);
            
            BigDecimal totalBalance = BigDecimal.ZERO;
            for (Account account : accounts) {
                totalBalance = totalBalance.add(account.getBalance());
            }
            
            return totalBalance;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting total balance", e);
            throw new RuntimeException("Error getting total balance", e);
        }
    }
}
