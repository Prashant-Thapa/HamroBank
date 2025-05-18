package com.example.hamrobank.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    // Regular expressions for validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";
    private static final String ACCOUNT_NUMBER_REGEX = "^[0-9]{10}$";
    
    // Compiled patterns for better performance
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(ACCOUNT_NUMBER_REGEX);
    
    /**
     * Validate an email address
     * 
     * @param email Email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate a username
     * 
     * @param username Username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validate a password
     * 
     * @param password Password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validate a phone number
     * 
     * @param phone Phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate an account number
     * 
     * @param accountNumber Account number to validate
     * @return true if the account number is valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return false;
        }
        return ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }
    
    /**
     * Validate a money amount
     * 
     * @param amount Amount to validate
     * @return true if the amount is valid, false otherwise
     */
    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Validate a transfer amount against an account balance
     * 
     * @param amount Amount to transfer
     * @param balance Current account balance
     * @return true if the amount is valid for transfer, false otherwise
     */
    public static boolean isValidTransferAmount(BigDecimal amount, BigDecimal balance) {
        if (amount == null || balance == null) {
            return false;
        }
        return amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(balance) <= 0;
    }
    
    /**
     * Sanitize a string for display
     * 
     * @param input String to sanitize
     * @return Sanitized string
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
