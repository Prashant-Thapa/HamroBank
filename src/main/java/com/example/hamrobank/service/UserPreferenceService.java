package com.example.hamrobank.service;

import com.example.hamrobank.dao.UserPreferenceDAO;
import com.example.hamrobank.dao.UserPreferenceDAOImpl;
import com.example.hamrobank.model.UserPreference;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for user preferences
 */
public class UserPreferenceService {
    
    private static final Logger LOGGER = Logger.getLogger(UserPreferenceService.class.getName());
    private final UserPreferenceDAO userPreferenceDAO;
    
    /**
     * Constructor
     */
    public UserPreferenceService() {
        this.userPreferenceDAO = new UserPreferenceDAOImpl();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param userPreferenceDAO DAO to use
     */
    public UserPreferenceService(UserPreferenceDAO userPreferenceDAO) {
        this.userPreferenceDAO = userPreferenceDAO;
    }
    
    /**
     * Get user preferences by user ID
     * 
     * @param userId ID of the user
     * @return UserPreference object if found, default preferences otherwise
     */
    public UserPreference getUserPreferenceByUserId(int userId) {
        try {
            return userPreferenceDAO.getUserPreferenceByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user preference by user ID", e);
            throw new RuntimeException("Error getting user preference by user ID", e);
        }
    }
    
    /**
     * Update user preferences
     * 
     * @param userPreference UserPreference object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateUserPreference(UserPreference userPreference) {
        try {
            return userPreferenceDAO.updateUserPreference(userPreference);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user preference", e);
            throw new RuntimeException("Error updating user preference", e);
        }
    }
    
    /**
     * Update user theme preference
     * 
     * @param userId ID of the user
     * @param theme Theme preference
     * @return true if the update was successful, false otherwise
     */
    public boolean updateTheme(int userId, String theme) {
        try {
            // Validate theme
            if (!theme.equals(UserPreference.THEME_LIGHT) && 
                !theme.equals(UserPreference.THEME_DARK) && 
                !theme.equals(UserPreference.THEME_SYSTEM)) {
                throw new IllegalArgumentException("Invalid theme: " + theme);
            }
            
            return userPreferenceDAO.updateTheme(userId, theme);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user theme", e);
            throw new RuntimeException("Error updating user theme", e);
        }
    }
    
    /**
     * Update user dashboard layout preference
     * 
     * @param userId ID of the user
     * @param dashboardLayout Dashboard layout preference
     * @return true if the update was successful, false otherwise
     */
    public boolean updateDashboardLayout(int userId, String dashboardLayout) {
        try {
            // Validate dashboard layout (in a real implementation, you would validate the JSON structure)
            if (dashboardLayout == null || dashboardLayout.isEmpty()) {
                throw new IllegalArgumentException("Invalid dashboard layout");
            }
            
            return userPreferenceDAO.updateDashboardLayout(userId, dashboardLayout);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user dashboard layout", e);
            throw new RuntimeException("Error updating user dashboard layout", e);
        }
    }
    
    /**
     * Update user language preference
     * 
     * @param userId ID of the user
     * @param language Language preference
     * @return true if the update was successful, false otherwise
     */
    public boolean updateLanguage(int userId, String language) {
        try {
            // Validate language (in a real implementation, you would validate against supported languages)
            if (language == null || language.isEmpty()) {
                throw new IllegalArgumentException("Invalid language");
            }
            
            return userPreferenceDAO.updateLanguage(userId, language);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user language", e);
            throw new RuntimeException("Error updating user language", e);
        }
    }
    
    /**
     * Initialize default preferences for a new user
     * 
     * @param userId ID of the user
     * @return created UserPreference
     */
    public UserPreference initializeDefaultPreferences(int userId) {
        try {
            UserPreference defaultPreference = new UserPreference(userId);
            return userPreferenceDAO.createUserPreference(defaultPreference);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing default preferences", e);
            throw new RuntimeException("Error initializing default preferences", e);
        }
    }
    
    /**
     * Get the current theme for a user
     * 
     * @param userId ID of the user
     * @return theme preference
     */
    public String getCurrentTheme(int userId) {
        try {
            UserPreference userPreference = userPreferenceDAO.getUserPreferenceByUserId(userId);
            return userPreference.getTheme();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting current theme", e);
            throw new RuntimeException("Error getting current theme", e);
        }
    }
    
    /**
     * Get the current dashboard layout for a user
     * 
     * @param userId ID of the user
     * @return dashboard layout preference
     */
    public String getCurrentDashboardLayout(int userId) {
        try {
            UserPreference userPreference = userPreferenceDAO.getUserPreferenceByUserId(userId);
            return userPreference.getDashboardLayout();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting current dashboard layout", e);
            throw new RuntimeException("Error getting current dashboard layout", e);
        }
    }
    
    /**
     * Get the current language for a user
     * 
     * @param userId ID of the user
     * @return language preference
     */
    public String getCurrentLanguage(int userId) {
        try {
            UserPreference userPreference = userPreferenceDAO.getUserPreferenceByUserId(userId);
            return userPreference.getLanguage();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting current language", e);
            throw new RuntimeException("Error getting current language", e);
        }
    }
}
