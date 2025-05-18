package com.example.hamrobank.controller;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling account deactivation by users
 */
@WebServlet(name = "deactivateAccountServlet", urlPatterns = {"/profile/deactivate"})
public class DeactivateAccountServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Forward to confirmation page
        request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String password = request.getParameter("password");
        String confirmation = request.getParameter("confirmation");
        
        try {
            // Validate input
            if (password == null || password.isEmpty()) {
                request.setAttribute("errorMessage", "Password is required");
                request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
                return;
            }
            
            if (!"DEACTIVATE".equals(confirmation)) {
                request.setAttribute("errorMessage", "Please type DEACTIVATE to confirm");
                request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
                return;
            }
            
            // Verify password
            User currentUser = userDAO.getUserById(user.getUserId());
            if (!PasswordUtil.verifyPassword(password, currentUser.getPassword())) {
                request.setAttribute("errorMessage", "Incorrect password");
                request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
                return;
            }
            
            // Deactivate account
            boolean success = userDAO.deactivateUser(user.getUserId());
            
            if (success) {
                // Log account deactivation
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(user.getUserId());
                activityLog.setActivityType("ACCOUNT_DEACTIVATED");
                activityLog.setDescription("User deactivated their account");
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Invalidate session
                session.invalidate();
                
                // Redirect to login page with success message
                request.getSession().setAttribute("successMessage", "Your account has been deactivated successfully");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("errorMessage", "Failed to deactivate account. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/profile/deactivate.jsp").forward(request, response);
        }
    }
}
