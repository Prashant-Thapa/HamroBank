package com.example.hamrobank.controller.auth;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
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
 * Servlet for handling user logout
 */
@WebServlet(name = "logoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Log logout activity
            User user = (User) session.getAttribute("user");
            if (user != null) {
                try {
                    ActivityLog activityLog = new ActivityLog();
                    activityLog.setUserId(user.getUserId());
                    activityLog.setActivityType("LOGOUT");
                    activityLog.setDescription("User logged out");
                    activityLog.setIpAddress(request.getRemoteAddr());
                    activityLog.setUserAgent(request.getHeader("User-Agent"));
                    activityLogDAO.createActivityLog(activityLog);
                } catch (Exception e) {
                    // Log error but continue with logout
                    getServletContext().log("Error logging logout activity", e);
                }
            }
            
            // Invalidate session
            session.invalidate();
        }
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
