package com.example.hamrobank.controller.auth;

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
 * Servlet for handling user login
 */
@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");

            // Redirect based on user role
            if (user.getRole() == User.Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            }
            return;
        }

        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMessage = null;

        try {
            // Validate input
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                errorMessage = "Username and password are required";
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // Get user by username
            User user = userDAO.getUserByUsername(username);

            // Check if user exists and password is correct
            if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
                errorMessage = "Invalid username or password";
                request.setAttribute("errorMessage", errorMessage);

                // Log failed login attempt
                ActivityLog activityLog = new ActivityLog();
                activityLog.setActivityType("LOGIN_FAILED");
                activityLog.setDescription("Failed login attempt for username: " + username);
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);

                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // Check if user account is active
            if (!user.isActive()) {
                errorMessage = "Your account has been deactivated. Please contact support for assistance.";
                request.setAttribute("errorMessage", errorMessage);

                // Log login attempt on deactivated account
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(user.getUserId());
                activityLog.setActivityType("LOGIN_FAILED");
                activityLog.setDescription("Login attempt on deactivated account");
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);

                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // Update last login time
            userDAO.updateLastLogin(user.getUserId());

            // Create session and set user attribute
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Log successful login
            ActivityLog activityLog = new ActivityLog();
            activityLog.setUserId(user.getUserId());
            activityLog.setActivityType("LOGIN");
            activityLog.setDescription("User logged in");
            activityLog.setIpAddress(request.getRemoteAddr());
            activityLog.setUserAgent(request.getHeader("User-Agent"));
            activityLogDAO.createActivityLog(activityLog);

            // Redirect based on user role
            if (user.getRole() == User.Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            }

        } catch (Exception e) {
            errorMessage = "An error occurred during login: " + e.getMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
