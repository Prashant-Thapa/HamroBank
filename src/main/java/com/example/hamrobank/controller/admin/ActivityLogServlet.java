package com.example.hamrobank.controller.admin;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.model.ActivityLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for viewing activity logs (admin)
 */
@WebServlet(name = "activityLogServlet", urlPatterns = {"/admin/logs"})
public class ActivityLogServlet extends HttpServlet {
    
    private ActivityLogDAO activityLogDAO;
//    Initialize the Servlet
    @Override
    public void init() throws ServletException {
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String activityType = request.getParameter("type");
        
        try {
            List<ActivityLog> logs;
            
            if (userId != null && !userId.isEmpty()) {
                // Get logs for a specific user
                logs = activityLogDAO.getActivityLogsByUserId(Integer.parseInt(userId));
                request.setAttribute("filterType", "user");
                request.setAttribute("filterId", userId);
            } else if (activityType != null && !activityType.isEmpty()) {
                // Get logs for a specific activity type
                logs = activityLogDAO.getActivityLogsByType(activityType);
                request.setAttribute("filterType", "activity");
                request.setAttribute("filterId", activityType);
            } else {
                // Get all logs
                logs = activityLogDAO.getAllActivityLogs();
            }
            
            request.setAttribute("logs", logs);
            request.getRequestDispatcher("/WEB-INF/views/admin/logs.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading activity logs: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/logs.jsp").forward(request, response);
        }
    }
}
