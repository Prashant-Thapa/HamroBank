package com.example.hamrobank.service;

import com.example.hamrobank.dao.AccountOwnerDAO;
import com.example.hamrobank.dao.AccountOwnerDAOImpl;
import com.example.hamrobank.model.AccountOwner;
import com.example.hamrobank.model.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for account owners (joint accounts)
 */
public class AccountOwnerService {
    
    private static final Logger LOGGER = Logger.getLogger(AccountOwnerService.class.getName());
    private final AccountOwnerDAO accountOwnerDAO;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     */
    public AccountOwnerService() {
        this.accountOwnerDAO = new AccountOwnerDAOImpl();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param accountOwnerDAO DAO to use
     * @param notificationService NotificationService to use
     */
    public AccountOwnerService(AccountOwnerDAO accountOwnerDAO, NotificationService notificationService) {
        this.accountOwnerDAO = accountOwnerDAO;
        this.notificationService = notificationService;
    }
    
    /**
     * Add an owner to an account
     * 
     * @param accountOwner AccountOwner object to add
     * @return true if the addition was successful, false otherwise
     */
    public boolean addAccountOwner(AccountOwner accountOwner) {
        try {
            boolean result = accountOwnerDAO.addAccountOwner(accountOwner);
            
            if (result) {
                // Send notification to the new owner
                notificationService.sendBalanceNotification(
                    accountOwner.getUserId(),
                    "Account Access Granted",
                    "You have been granted access to account #" + accountOwner.getAccountId()
                );
                
                // Send notification to the primary owner
                AccountOwner primaryOwner = accountOwnerDAO.getPrimaryOwner(accountOwner.getAccountId());
                if (primaryOwner != null && primaryOwner.getUserId() != accountOwner.getUserId()) {
                    notificationService.sendBalanceNotification(
                        primaryOwner.getUserId(),
                        "Account Owner Added",
                        "A new owner has been added to account #" + accountOwner.getAccountId()
                    );
                }
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding account owner", e);
            throw new RuntimeException("Error adding account owner", e);
        }
    }
    
    /**
     * Get all owners of an account
     * 
     * @param accountId ID of the account
     * @return List of AccountOwner objects for the account
     */
    public List<AccountOwner> getAccountOwnersByAccountId(int accountId) {
        try {
            return accountOwnerDAO.getAccountOwnersByAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account owners by account ID", e);
            throw new RuntimeException("Error getting account owners by account ID", e);
        }
    }
    
    /**
     * Get all accounts owned by a user
     * 
     * @param userId ID of the user
     * @return List of AccountOwner objects for the user
     */
    public List<AccountOwner> getAccountOwnersByUserId(int userId) {
        try {
            return accountOwnerDAO.getAccountOwnersByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account owners by user ID", e);
            throw new RuntimeException("Error getting account owners by user ID", e);
        }
    }
    
    /**
     * Get the primary owner of an account
     * 
     * @param accountId ID of the account
     * @return AccountOwner object for the primary owner, null if not found
     */
    public AccountOwner getPrimaryOwner(int accountId) {
        try {
            return accountOwnerDAO.getPrimaryOwner(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting primary owner", e);
            throw new RuntimeException("Error getting primary owner", e);
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
     * Update the permission level of an account owner
     * 
     * @param accountId ID of the account
     * @param userId ID of the user
     * @param permissionLevel New permission level
     * @return true if the update was successful, false otherwise
     */
    public boolean updatePermissionLevel(int accountId, int userId, String permissionLevel) {
        try {
            // Validate permission level
            if (!permissionLevel.equals(AccountOwner.PERMISSION_FULL) && 
                !permissionLevel.equals(AccountOwner.PERMISSION_VIEW_ONLY) && 
                !permissionLevel.equals(AccountOwner.PERMISSION_TRANSACT)) {
                throw new IllegalArgumentException("Invalid permission level: " + permissionLevel);
            }
            
            boolean result = accountOwnerDAO.updatePermissionLevel(accountId, userId, permissionLevel);
            
            if (result) {
                // Send notification to the affected user
                notificationService.sendBalanceNotification(
                    userId,
                    "Account Permission Updated",
                    "Your permission level for account #" + accountId + " has been updated to " + permissionLevel
                );
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating permission level", e);
            throw new RuntimeException("Error updating permission level", e);
        }
    }
    
    /**
     * Set the primary owner of an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user to set as primary owner
     * @return true if the update was successful, false otherwise
     */
    public boolean setPrimaryOwner(int accountId, int userId) {
        try {
            boolean result = accountOwnerDAO.setPrimaryOwner(accountId, userId);
            
            if (result) {
                // Send notification to the new primary owner
                notificationService.sendBalanceNotification(
                    userId,
                    "Primary Owner Status",
                    "You are now the primary owner of account #" + accountId
                );
                
                // Send notifications to other owners
                List<AccountOwner> owners = accountOwnerDAO.getAccountOwnersByAccountId(accountId);
                for (AccountOwner owner : owners) {
                    if (owner.getUserId() != userId) {
                        notificationService.sendBalanceNotification(
                            owner.getUserId(),
                            "Primary Owner Changed",
                            "The primary owner of account #" + accountId + " has been changed"
                        );
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting primary owner", e);
            throw new RuntimeException("Error setting primary owner", e);
        }
    }
    
    /**
     * Remove an owner from an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user to remove
     * @return true if the removal was successful, false otherwise
     */
    public boolean removeAccountOwner(int accountId, int userId) {
        try {
            // Check if the user is the primary owner
            AccountOwner primaryOwner = accountOwnerDAO.getPrimaryOwner(accountId);
            if (primaryOwner != null && primaryOwner.getUserId() == userId) {
                // Cannot remove the primary owner
                return false;
            }
            
            boolean result = accountOwnerDAO.removeAccountOwner(accountId, userId);
            
            if (result) {
                // Send notification to the removed user
                notificationService.sendBalanceNotification(
                    userId,
                    "Account Access Removed",
                    "Your access to account #" + accountId + " has been removed"
                );
                
                // Send notification to the primary owner
                if (primaryOwner != null) {
                    notificationService.sendBalanceNotification(
                        primaryOwner.getUserId(),
                        "Account Owner Removed",
                        "An owner has been removed from account #" + accountId
                    );
                }
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing account owner", e);
            throw new RuntimeException("Error removing account owner", e);
        }
    }
    
    /**
     * Get all users who own an account
     * 
     * @param accountId ID of the account
     * @return List of User objects who own the account
     */
    public List<User> getUsersByAccountId(int accountId) {
        try {
            return accountOwnerDAO.getUsersByAccountId(accountId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting users by account ID", e);
            throw new RuntimeException("Error getting users by account ID", e);
        }
    }
    
    /**
     * Create a joint account
     * 
     * @param accountId ID of the account
     * @param primaryUserId ID of the primary user
     * @param secondaryUserIds List of IDs of secondary users
     * @return true if the creation was successful, false otherwise
     */
    public boolean createJointAccount(int accountId, int primaryUserId, List<Integer> secondaryUserIds) {
        try {
            // Add primary owner
            AccountOwner primaryOwner = new AccountOwner(accountId, primaryUserId, AccountOwner.PERMISSION_FULL, true);
            boolean primaryResult = accountOwnerDAO.addAccountOwner(primaryOwner);
            
            if (!primaryResult) {
                return false;
            }
            
            // Add secondary owners
            boolean allSuccess = true;
            for (int userId : secondaryUserIds) {
                AccountOwner secondaryOwner = new AccountOwner(accountId, userId, AccountOwner.PERMISSION_FULL, false);
                boolean result = accountOwnerDAO.addAccountOwner(secondaryOwner);
                
                if (result) {
                    // Send notification to the secondary owner
                    notificationService.sendBalanceNotification(
                        userId,
                        "Joint Account Access",
                        "You have been added to joint account #" + accountId
                    );
                } else {
                    allSuccess = false;
                }
            }
            
            // Send notification to the primary owner
            notificationService.sendBalanceNotification(
                primaryUserId,
                "Joint Account Created",
                "Joint account #" + accountId + " has been created successfully"
            );
            
            return allSuccess;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating joint account", e);
            throw new RuntimeException("Error creating joint account", e);
        }
    }
}
