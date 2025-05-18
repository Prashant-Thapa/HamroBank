package com.example.hamrobank.controller.admin;

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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing users (admin)
 */
@WebServlet(name = "userManagementServlet", urlPatterns = {"/admin/users"})
public class UserManagementServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.equals("list")) {
                // List all users
                List<User> users = userDAO.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Show edit form for a user
                String userId = request.getParameter("id");
                if (userId != null && !userId.isEmpty()) {
                    User user = userDAO.getUserById(Integer.parseInt(userId));
                    if (user != null) {
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/WEB-INF/views/admin/user-edit.jsp").forward(request, response);
                        return;
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else if (action.equals("add")) {
                // Show add user form
                request.getRequestDispatcher("/WEB-INF/views/admin/user-add.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User adminUser = (User) session.getAttribute("user");
        
        try {
            if (action == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            if (action.equals("add")) {
                // Add a new user
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String fullName = request.getParameter("fullName");
                String roleStr = request.getParameter("role");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                
                // Validate input
                StringBuilder errorMessage = new StringBuilder();
                
                if (username == null || username.isEmpty()) {
                    errorMessage.append("Username is required. ");
                } else if (!ValidationUtil.isValidUsername(username)) {
                    errorMessage.append("Username must be 3-20 characters and can only contain letters, numbers, and underscores. ");
                }
                
                if (password == null || password.isEmpty()) {
                    errorMessage.append("Password is required. ");
                }
                
                if (email == null || email.isEmpty()) {
                    errorMessage.append("Email is required. ");
                } else if (!ValidationUtil.isValidEmail(email)) {
                    errorMessage.append("Invalid email format. ");
                }
                
                if (fullName == null || fullName.isEmpty()) {
                    errorMessage.append("Full name is required. ");
                }
                
                if (roleStr == null || roleStr.isEmpty()) {
                    errorMessage.append("Role is required. ");
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
                    request.setAttribute("role", roleStr);
                    request.setAttribute("phone", phone);
                    request.setAttribute("address", address);
                    request.getRequestDispatcher("/WEB-INF/views/admin/user-add.jsp").forward(request, response);
                    return;
                }
                
                // Create new user
                User user = new User();
                user.setUsername(username);
                user.setPassword(PasswordUtil.hashPassword(password));
                user.setEmail(email);
                user.setFullName(fullName);
                user.setRole(User.Role.valueOf(roleStr));
                user.setPhone(phone);
                user.setAddress(address);
                
                user = userDAO.createUser(user);
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("USER_CREATED");
                activityLog.setDescription("Admin created new user: " + username);
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "User created successfully");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                
            } else if (action.equals("update")) {
                // Update an existing user
                String userId = request.getParameter("userId");
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String fullName = request.getParameter("fullName");
                String roleStr = request.getParameter("role");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String password = request.getParameter("password");
                
                if (userId == null || userId.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
                }
                
                // Get existing user
                User user = userDAO.getUserById(Integer.parseInt(userId));
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
                }
                
                // Validate input
                StringBuilder errorMessage = new StringBuilder();
                
                if (username == null || username.isEmpty()) {
                    errorMessage.append("Username is required. ");
                } else if (!ValidationUtil.isValidUsername(username)) {
                    errorMessage.append("Username must be 3-20 characters and can only contain letters, numbers, and underscores. ");
                }
                
                if (email == null || email.isEmpty()) {
                    errorMessage.append("Email is required. ");
                } else if (!ValidationUtil.isValidEmail(email)) {
                    errorMessage.append("Invalid email format. ");
                }
                
                if (fullName == null || fullName.isEmpty()) {
                    errorMessage.append("Full name is required. ");
                }
                
                if (roleStr == null || roleStr.isEmpty()) {
                    errorMessage.append("Role is required. ");
                }
                
                if (phone != null && !phone.isEmpty() && !ValidationUtil.isValidPhone(phone)) {
                    errorMessage.append("Invalid phone number format. ");
                }
                
                // Check if username or email already exists (for other users)
                if (!username.equals(user.getUsername()) && userDAO.usernameExists(username)) {
                    errorMessage.append("Username already exists. ");
                }
                
                if (!email.equals(user.getEmail()) && userDAO.emailExists(email)) {
                    errorMessage.append("Email already exists. ");
                }
                
                if (errorMessage.length() > 0) {
                    request.setAttribute("errorMessage", errorMessage.toString());
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/admin/user-edit.jsp").forward(request, response);
                    return;
                }
                
                // Update user
                user.setUsername(username);
                user.setEmail(email);
                user.setFullName(fullName);
                user.setRole(User.Role.valueOf(roleStr));
                user.setPhone(phone);
                user.setAddress(address);
                
                // Update password if provided
                if (password != null && !password.isEmpty()) {
                    user.setPassword(PasswordUtil.hashPassword(password));
                }
                
                userDAO.updateUser(user);
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("USER_UPDATED");
                activityLog.setDescription("Admin updated user: " + username);
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "User updated successfully");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                
            } else if (action.equals("delete")) {
                // Delete a user
                String userId = request.getParameter("id");
                
                if (userId == null || userId.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
                }
                
                // Get user to be deleted
                User user = userDAO.getUserById(Integer.parseInt(userId));
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
                }
                
                // Prevent deleting the current admin user
                if (user.getUserId() == adminUser.getUserId()) {
                    session.setAttribute("errorMessage", "You cannot delete your own account");
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
                }
                
                // Delete user
                userDAO.deleteUser(user.getUserId());
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("USER_DELETED");
                activityLog.setDescription("Admin deleted user: " + user.getUsername());
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "User deleted successfully");
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            try {
                List<User> users = userDAO.getAllUsers();
                request.setAttribute("users", users);
            } catch (Exception ex) {
                // Ignore
            }
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        }
    }
}
