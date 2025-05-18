package com.example.hamrobank.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {
    
    // Work factor for BCrypt (higher is more secure but slower)
    private static final int BCRYPT_WORKLOAD = 12;
    
    /**
     * Hash a password using BCrypt
     * 
     * @param plainTextPassword The plain text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt(BCRYPT_WORKLOAD);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
    
    /**
     * Verify a password against a hashed password
     * 
     * @param plainTextPassword The plain text password to check
     * @param hashedPassword The hashed password to check against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
    
    /**
     * Generate a random password
     * 
     * @param length Length of the password to generate
     * @return A random password
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            length = 8; // Minimum password length
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }
}
