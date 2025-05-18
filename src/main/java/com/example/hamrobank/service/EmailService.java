package com.example.hamrobank.service;

import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Service class for email operations
 */
public class EmailService {
    
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    private static final String PROPERTIES_FILE = "/email.properties";
    private static Properties properties = null;
    private final UserDAO userDAO;
    
    static {
        try {
            // Load email properties
            properties = new Properties();
            InputStream inputStream = EmailService.class.getResourceAsStream(PROPERTIES_FILE);
            
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                LOGGER.log(Level.WARNING, "Unable to find " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading email properties", e);
        }
    }
    
    /**
     * Constructor
     */
    public EmailService() {
        this.userDAO = new UserDAOImpl();
    }
    
    /**
     * Constructor with DAO injection (for testing)
     * 
     * @param userDAO UserDAO to use
     */
    public EmailService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    /**
     * Send an email
     * 
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String subject, String body) {
        if (properties == null) {
            LOGGER.log(Level.SEVERE, "Email properties not loaded");
            return false;
        }
        
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");
        String username = properties.getProperty("mail.smtp.username");
        String password = properties.getProperty("mail.smtp.password");
        String from = properties.getProperty("mail.from");
        
        // Set mail properties
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.host", host);
        mailProperties.put("mail.smtp.port", port);
        
        try {
            // Create session
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            
            // Send message
            Transport.send(message);
            
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending email", e);
            return false;
        }
    }
    
    /**
     * Send a notification email to a user
     * 
     * @param userId ID of the user
     * @param subject Email subject
     * @param body Email body
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendNotificationEmail(int userId, String subject, String body) {
        try {
            // Get the user
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                LOGGER.log(Level.WARNING, "User not found: " + userId);
                return false;
            }
            
            // Send email
            return sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending notification email", e);
            return false;
        }
    }
    
    /**
     * Send a password reset email
     * 
     * @param email Email address
     * @param resetToken Password reset token
     * @param resetUrl Password reset URL
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendPasswordResetEmail(String email, String resetToken, String resetUrl) {
        String subject = "Password Reset Request";
        String body = "Dear User,\n\n"
                    + "You have requested to reset your password. Please click the link below to reset your password:\n\n"
                    + resetUrl + "?token=" + resetToken + "\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Regards,\n"
                    + "Hamro Bank Team";
        
        return sendEmail(email, subject, body);
    }
    
    /**
     * Send a welcome email
     * 
     * @param email Email address
     * @param firstName First name of the user
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendWelcomeEmail(String email, String firstName) {
        String subject = "Welcome to Hamro Bank";
        String body = "Dear " + firstName + ",\n\n"
                    + "Welcome to Hamro Bank! We are excited to have you as a customer.\n\n"
                    + "You can now log in to your account and start using our services.\n\n"
                    + "If you have any questions, please don't hesitate to contact us.\n\n"
                    + "Regards,\n"
                    + "Hamro Bank Team";
        
        return sendEmail(email, subject, body);
    }
    
    /**
     * Send a transaction confirmation email
     * 
     * @param email Email address
     * @param transactionType Type of the transaction
     * @param amount Amount of the transaction
     * @param accountNumber Account number
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendTransactionConfirmationEmail(String email, String transactionType, String amount, String accountNumber) {
        String subject = "Transaction Confirmation";
        String body = "Dear Customer,\n\n"
                    + "A " + transactionType + " transaction of " + amount + " has been processed on your account "
                    + accountNumber + ".\n\n"
                    + "If you did not authorize this transaction, please contact us immediately.\n\n"
                    + "Regards,\n"
                    + "Hamro Bank Team";
        
        return sendEmail(email, subject, body);
    }
}
