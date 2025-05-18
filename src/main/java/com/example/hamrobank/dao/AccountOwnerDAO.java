package com.example.hamrobank.dao;

import com.example.hamrobank.model.AccountOwner;
import com.example.hamrobank.model.User;

import java.util.List;

/**
 * Data Access Object interface for account owners (joint accounts)
 */
public interface AccountOwnerDAO {
    
    /**
     * Add an owner to an account
     * 
     * @param accountOwner AccountOwner object to add
     * @return true if the addition was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean addAccountOwner(AccountOwner accountOwner) throws Exception;
    
    /**
     * Get all owners of an account
     * 
     * @param accountId ID of the account
     * @return List of AccountOwner objects for the account
     * @throws Exception if an error occurs during the operation
     */
    List<AccountOwner> getAccountOwnersByAccountId(int accountId) throws Exception;
    
    /**
     * Get all accounts owned by a user
     * 
     * @param userId ID of the user
     * @return List of AccountOwner objects for the user
     * @throws Exception if an error occurs during the operation
     */
    List<AccountOwner> getAccountOwnersByUserId(int userId) throws Exception;
    
    /**
     * Get the primary owner of an account
     * 
     * @param accountId ID of the account
     * @return AccountOwner object for the primary owner, null if not found
     * @throws Exception if an error occurs during the operation
     */
    AccountOwner getPrimaryOwner(int accountId) throws Exception;
    
    /**
     * Check if a user is an owner of an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user
     * @return true if the user is an owner of the account, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean isAccountOwner(int accountId, int userId) throws Exception;
    
    /**
     * Update the permission level of an account owner
     * 
     * @param accountId ID of the account
     * @param userId ID of the user
     * @param permissionLevel New permission level
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updatePermissionLevel(int accountId, int userId, String permissionLevel) throws Exception;
    
    /**
     * Set the primary owner of an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user to set as primary owner
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean setPrimaryOwner(int accountId, int userId) throws Exception;
    
    /**
     * Remove an owner from an account
     * 
     * @param accountId ID of the account
     * @param userId ID of the user to remove
     * @return true if the removal was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean removeAccountOwner(int accountId, int userId) throws Exception;
    
    /**
     * Get all users who own an account
     * 
     * @param accountId ID of the account
     * @return List of User objects who own the account
     * @throws Exception if an error occurs during the operation
     */
    List<User> getUsersByAccountId(int accountId) throws Exception;
}
