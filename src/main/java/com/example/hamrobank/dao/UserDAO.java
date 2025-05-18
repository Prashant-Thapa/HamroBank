package com.example.hamrobank.dao;

import com.example.hamrobank.model.User;
import java.util.List;

/**
 * Interface for User data access operations
 */
public interface UserDAO {

    /**
     * Create a new user
     *
     * @param user User object to create
     * @return The created user with ID set
     * @throws Exception if an error occurs during the operation
     */
    User createUser(User user) throws Exception;

    /**
     * Get a user by ID
     *
     * @param userId ID of the user to retrieve
     * @return User object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    User getUserById(int userId) throws Exception;

    /**
     * Get a user by username
     *
     * @param username Username of the user to retrieve
     * @return User object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    User getUserByUsername(String username) throws Exception;

    /**
     * Get a user by email
     *
     * @param email Email of the user to retrieve
     * @return User object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    User getUserByEmail(String email) throws Exception;

    /**
     * Update an existing user
     *
     * @param user User object with updated information
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateUser(User user) throws Exception;

    /**
     * Delete a user by ID
     *
     * @param userId ID of the user to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteUser(int userId) throws Exception;

    /**
     * Get all users
     *
     * @return List of all users
     * @throws Exception if an error occurs during the operation
     */
    List<User> getAllUsers() throws Exception;

    /**
     * Get all users with a specific role
     *
     * @param role Role to filter by
     * @return List of users with the specified role
     * @throws Exception if an error occurs during the operation
     */
    List<User> getUsersByRole(User.Role role) throws Exception;

    /**
     * Update the last login time for a user
     *
     * @param userId ID of the user
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateLastLogin(int userId) throws Exception;

    /**
     * Check if a username already exists
     *
     * @param username Username to check
     * @return true if the username exists, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean usernameExists(String username) throws Exception;

    /**
     * Check if an email already exists
     *
     * @param email Email to check
     * @return true if the email exists, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean emailExists(String email) throws Exception;

    /**
     * Activate a user account
     *
     * @param userId ID of the user to activate
     * @return true if the activation was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean activateUser(int userId) throws Exception;

    /**
     * Deactivate a user account
     *
     * @param userId ID of the user to deactivate
     * @return true if the deactivation was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deactivateUser(int userId) throws Exception;

    /**
     * Check if a user account is active
     *
     * @param userId ID of the user to check
     * @return true if the account is active, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean isUserActive(int userId) throws Exception;
}
