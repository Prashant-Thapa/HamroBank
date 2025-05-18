package com.example.hamrobank.controller.auth;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.PasswordUtil;
import com.example.hamrobank.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling user registration
 */
@WebServlet(name = "registerServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        try {
            // Validate input
            StringBuilder errorMessage = new StringBuilder();
            
            if (username == null || username.isEmpty()) {
                errorMessage.append("Username is required. ");
            } else if (!ValidationUtil.isValidUsername(username)) {
                errorMessage.append("Username must be 3-20 characters and can only contain letters, numbers, and underscores. ");
            }
            
            if (password == null || password.isEmpty()) {
                errorMessage.append("Password is required. ");
            } else if (!ValidationUtil.isValidPassword(password)) {
                errorMessage.append("Password must be at least 8 characters and include at least one digit, one lowercase letter, one uppercase letter, and one special character. ");
            }
            
            if (confirmPassword == null || !confirmPassword.equals(password)) {
                errorMessage.append("Passwords do not match. ");
            }
            
            if (email == null || email.isEmpty()) {
                errorMessage.append("Email is required. ");
            } else if (!ValidationUtil.isValidEmail(email)) {
                errorMessage.append("Invalid email format. ");
            }
            
            if (fullName == null || fullName.isEmpty()) {
                errorMessage.append("Full name is required. ");
            }
            
            if (phone != null && !phone.isEmpty() && !ValidationUtil.isValidPhone(phone)) {
                errorMessage.append("Invalid phone number format. ");
            }
            
            // Check if username or email already exists
            if (username != null && !username.isEmpty() && userDAO.usernameExists(username)) {
                errorMessage.append("Username already exists. ");
            }
            
            if (email != null && !email.isEmpty() && userDAO.emailExists(email)) {
                errorMessage.append("Email already exists. ");
            }
            
            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString());
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                return;
            }
            
            // Create new user
            User user = new User();
            user.setUsername(username);
            user.setPassword(PasswordUtil.hashPassword(password));
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole(User.Role.CUSTOMER); // Default role is CUSTOMER
            user.setPhone(phone);
            user.setAddress(address);
            
            user = userDAO.createUser(user);
            
            // Log registration
            ActivityLog activityLog = new ActivityLog();
            activityLog.setUserId(user.getUserId());
            activityLog.setActivityType("REGISTRATION");
            activityLog.setDescription("New user registered");
            activityLog.setIpAddress(request.getRemoteAddr());
            activityLog.setUserAgent(request.getHeader("User-Agent"));
            activityLogDAO.createActivityLog(activityLog);
            
            // Set success message and redirect to login page
            request.getSession().setAttribute("successMessage", "Registration successful! You can now log in.");
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred during registration: " + e.getMessage());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
