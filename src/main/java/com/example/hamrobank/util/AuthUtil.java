package com.example.hamrobank.util;

import com.example.hamrobank.model.User;
import jakarta.servlet.http.HttpSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for authentication operations
 */
public class AuthUtil {

    private static final Logger LOGGER = Logger.getLogger(AuthUtil.class.getName());
    private static final int SALT_LENGTH = 16;

    /**
     * Get the logged-in user from the session
     *
     * @param session HttpSession object
     * @return User object if logged in, null otherwise
     */
    public static User getLoggedInUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        return (User) session.getAttribute("user");
    }

    /**
     * Check if a user is logged in
     *
     * @param session HttpSession object
     * @return true if logged in, false otherwise
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getLoggedInUser(session) != null;
    }

    /**
     * Check if a user is an admin
     *
     * @param session HttpSession object
     * @return true if admin, false otherwise
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && User.Role.ADMIN == user.getRole();
    }

    /**
     * Check if a user is a customer
     *
     * @param session HttpSession object
     * @return true if customer, false otherwise
     */
    public static boolean isCustomer(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && User.Role.CUSTOMER == user.getRole();
    }

    /**
     * Generate a random salt
     *
     * @return salt as a Base64 encoded string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a password with a salt using SHA-256
     *
     * @param password Password to hash
     * @param salt Salt to use
     * @return Hashed password
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a hashed password and salt
     *
     * @param password Password to verify
     * @param hashedPassword Hashed password to verify against
     * @param salt Salt used for hashing
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hashedPassword, String salt) {
        String hashedInput = hashPassword(password, salt);
        return hashedInput.equals(hashedPassword);
    }

    /**
     * Generate a random token for password reset
     *
     * @return Random token
     */
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
