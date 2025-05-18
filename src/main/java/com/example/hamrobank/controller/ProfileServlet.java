package com.example.hamrobank.controller;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.FileUtil;
import com.example.hamrobank.util.PasswordUtil;
import com.example.hamrobank.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

/**
 * Servlet for handling user profile operations
 */
@WebServlet(name = "profileServlet", urlPatterns = {"/profile"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 2,   // 2 MB
    maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;

    // Directory to store uploaded profile pictures
    private static final String UPLOAD_DIRECTORY = "uploads/profile_pictures";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();

        // Create upload directory if it doesn't exist
        String uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // Get fresh user data from database
            User freshUser = userDAO.getUserById(user.getUserId());
            request.setAttribute("user", freshUser);

            // Forward to profile page
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading profile: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");

        try {
            // Get user from database
            User user = userDAO.getUserById(sessionUser.getUserId());

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Get form data
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // Validate input
            StringBuilder errorMessage = new StringBuilder();

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

            // Check if email already exists (for other users)
            if (!email.equals(user.getEmail()) && userDAO.emailExists(email)) {
                errorMessage.append("Email already exists. ");
            }

            // Handle password change if requested
            boolean passwordChanged = false;
            if (currentPassword != null && !currentPassword.isEmpty()) {
                // Verify current password
                if (!PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
                    errorMessage.append("Current password is incorrect. ");
                } else if (newPassword == null || newPassword.isEmpty()) {
                    errorMessage.append("New password is required. ");
                } else if (!ValidationUtil.isValidPassword(newPassword)) {
                    errorMessage.append("New password must be at least 8 characters and include at least one digit, one lowercase letter, one uppercase letter, and one special character. ");
                } else if (confirmPassword == null || !confirmPassword.equals(newPassword)) {
                    errorMessage.append("Passwords do not match. ");
                } else {
                    passwordChanged = true;
                }
            }

            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString());
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
                return;
            }

            // Handle profile picture upload
            String uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
            Part filePart = request.getPart("profilePicture");
            String fileName = null;

            if (filePart != null && filePart.getSize() > 0) {
                // Delete old profile picture if exists
                if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                    FileUtil.deleteFile(user.getProfilePicture(), uploadPath);
                }

                // Upload new profile picture
                fileName = FileUtil.uploadFile(request, "profilePicture", uploadPath);
                if (fileName != null) {
                    user.setProfilePicture(fileName);
                }
            }

            // Update user data
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAddress(address);

            // Update password if changed
            if (passwordChanged) {
                user.setPassword(PasswordUtil.hashPassword(newPassword));
            }

            // Save changes to database
            userDAO.updateUser(user);

            // Log activity
            ActivityLog activityLog = new ActivityLog();
            activityLog.setUserId(user.getUserId());
            activityLog.setActivityType("PROFILE_UPDATE");
            activityLog.setDescription("User updated profile information");
            activityLog.setIpAddress(request.getRemoteAddr());
            activityLog.setUserAgent(request.getHeader("User-Agent"));
            activityLogDAO.createActivityLog(activityLog);

            // Update session user
            session.setAttribute("user", user);

            // Set success message and redirect
            session.setAttribute("successMessage", "Profile updated successfully");
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            request.setAttribute("user", sessionUser);
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }
}
