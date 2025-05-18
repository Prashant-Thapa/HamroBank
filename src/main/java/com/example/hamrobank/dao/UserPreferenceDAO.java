package com.example.hamrobank.dao;

import com.example.hamrobank.model.UserPreference;

/**
 * Data Access Object interface for user preferences
 */
public interface UserPreferenceDAO {
    
    /**
     * Create user preferences
     * 
     * @param userPreference UserPreference object to create
     * @return created UserPreference
     * @throws Exception if an error occurs during the operation
     */
    UserPreference createUserPreference(UserPreference userPreference) throws Exception;
    
    /**
     * Get user preferences by user ID
     * 
     * @param userId ID of the user
     * @return UserPreference object if found, null otherwise
     * @throws Exception if an error occurs during the operation
     */
    UserPreference getUserPreferenceByUserId(int userId) throws Exception;
    
    /**
     * Update user preferences
     * 
     * @param userPreference UserPreference object to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateUserPreference(UserPreference userPreference) throws Exception;
    
    /**
     * Update user theme preference
     * 
     * @param userId ID of the user
     * @param theme Theme preference
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateTheme(int userId, String theme) throws Exception;
    
    /**
     * Update user dashboard layout preference
     * 
     * @param userId ID of the user
     * @param dashboardLayout Dashboard layout preference
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateDashboardLayout(int userId, String dashboardLayout) throws Exception;
    
    /**
     * Update user language preference
     * 
     * @param userId ID of the user
     * @param language Language preference
     * @return true if the update was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean updateLanguage(int userId, String language) throws Exception;
    
    /**
     * Delete user preferences
     * 
     * @param userId ID of the user
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if an error occurs during the operation
     */
    boolean deleteUserPreference(int userId) throws Exception;
}
