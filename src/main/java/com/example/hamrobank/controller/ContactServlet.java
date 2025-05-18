package com.example.hamrobank.controller;

import com.example.hamrobank.dao.ActivityLogDAO;
import com.example.hamrobank.dao.ActivityLogDAOImpl;
import com.example.hamrobank.model.ActivityLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling the contact page and contact form submissions
 */
@WebServlet(name = "contactServlet", urlPatterns = {"/contact"})
public class ContactServlet extends HttpServlet {
    
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to contact page
        request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        
        // Validate form data
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            subject == null || subject.trim().isEmpty() || 
            message == null || message.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Please fill in all required fields.");
            request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
            return;
        }
        
        try {
            // In a real application, you would save the contact form data to a database
            // and/or send an email notification
            
            // For now, we'll just log the activity
            ActivityLog activityLog = new ActivityLog();
            activityLog.setActivityType("CONTACT_FORM");
            activityLog.setDescription("Contact form submitted by: " + name + " (" + email + ")");
            activityLog.setIpAddress(request.getRemoteAddr());
            activityLog.setUserAgent(request.getHeader("User-Agent"));
            activityLogDAO.createActivityLog(activityLog);
            
            // Set success message
            request.setAttribute("successMessage", "Thank you for your message! We will get back to you soon.");
            
            // Forward back to contact page
            request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Set error message
            request.setAttribute("errorMessage", "An error occurred while processing your request. Please try again later.");
            
            // Forward back to contact page
            request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
        }
    }
}
