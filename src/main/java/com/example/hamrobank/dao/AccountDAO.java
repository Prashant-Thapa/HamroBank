package com.example.hamrobank.dao;

import com.example.hamrobank.model.Account;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for Account data access operations
 */
public interface AccountDAO {

    /**
     * Create a new account
     *
     * @param account Account object to create
     * @return The created account with ID set
     * @throws Exception if an error occurs during the operation
     */
    Account createAccount(Account account) throws Exception;

    /**
     * Get an account by ID
     *
     * @param accountId ID of the account to retrieve
     * @return Account object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Account getAccountById(int accountId) throws Exception;

    /**
     * Get an account by account number
     *
     * @param accountNumber Account number to retrieve
     * @return Account object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    Account getAccountByAccountNumber(String accountNumber) throws Exception;

    /**
     * Get all accounts for a user
     *
     * @param userId ID of the user
     * @return List of accounts belonging to the user
     * @throws Exception if an error occurs during the operation
     */
    List<Account> getAccountsByUserId(int userId) throws Exception;

    /**
     * Update an existing account
     *
     * @param account Account object with updated information
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateAccount(Account account) throws Exception;

    /**
     * Delete an account by ID
     *
     * @param accountId ID of the account to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteAccount(int accountId) throws Exception;

    /**
     * Close an account (mark as closed)
     *
     * @param accountId ID of the account to close
     * @return true if the closure was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean closeAccount(int accountId) throws Exception;

    /**
     * Get all accounts
     *
     * @return List of all accounts
     * @throws Exception if an error occurs during the operation
     */
    List<Account> getAllAccounts() throws Exception;

    /**
     * Update the balance of an account
     *
     * @param accountId ID of the account
     * @param newBalance New balance for the account
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateBalance(int accountId, BigDecimal newBalance) throws Exception;

    /**
     * Update the status of an account
     *
     * @param accountId ID of the account
     * @param status New status for the account
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateStatus(int accountId, Account.AccountStatus status) throws Exception;

    /**
     * Generate a unique account number
     *
     * @return A unique account number
     * @throws Exception if an error occurs during the operation
     */
    String generateAccountNumber() throws Exception;

    /**
     * Check if an account number already exists
     *
     * @param accountNumber Account number to check
     * @return true if the account number exists, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean accountNumberExists(String accountNumber) throws Exception;

    /**
     * Get accounts by status
     *
     * @param status Status of the accounts to retrieve
     * @return List of Account objects with the specified status
     * @throws Exception if an error occurs during the operation
     */
    List<Account> getAccountsByStatus(String status) throws Exception;

    /**
     * Get accounts by type
     *
     * @param accountType Type of the accounts to retrieve
     * @return List of Account objects with the specified type
     * @throws Exception if an error occurs during the operation
     */
    List<Account> getAccountsByType(String accountType) throws Exception;
}
