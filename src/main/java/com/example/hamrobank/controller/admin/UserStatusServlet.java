package com.example.hamrobank.controller.admin;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user account activation/deactivation by admins
 */
@WebServlet(name = "userStatusServlet", urlPatterns = {"/admin/users/status"})
public class UserStatusServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User admin = (User) session.getAttribute("user");
        if (admin.getRole() != User.Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        
        // Get parameters
        String userIdStr = request.getParameter("userId");
        String action = request.getParameter("action");
        
        try {
            // Validate input
            if (userIdStr == null || userIdStr.isEmpty() || action == null || action.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Invalid request parameters");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            int userId = Integer.parseInt(userIdStr);
            
            // Get user
            User user = userDAO.getUserById(userId);
            if (user == null) {
                request.getSession().setAttribute("errorMessage", "User not found");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Prevent admins from deactivating their own account
            if (userId == admin.getUserId()) {
                request.getSession().setAttribute("errorMessage", "You cannot change the status of your own account");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            boolean success;
            String activityType;
            String description;
            
            // Perform action
            if ("activate".equals(action)) {
                success = userDAO.activateUser(userId);
                activityType = "USER_ACTIVATED";
                description = "Admin activated user account: " + user.getUsername();
            } else if ("deactivate".equals(action)) {
                success = userDAO.deactivateUser(userId);
                activityType = "USER_DEACTIVATED";
                description = "Admin deactivated user account: " + user.getUsername();
            } else {
                request.getSession().setAttribute("errorMessage", "Invalid action");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            if (success) {
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(admin.getUserId());
                activityLog.setActivityType(activityType);
                activityLog.setDescription(description);
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Set success message
                request.getSession().setAttribute("successMessage", 
                    "activate".equals(action) ? "User account activated successfully" : "User account deactivated successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update user account status");
            }
            
            // Redirect back to users list
            response.sendRedirect(request.getContextPath() + "/admin/users");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
