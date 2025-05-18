package com.example.hamrobank.controller.admin;

import com.example.hamrobank.util.SampleDataUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for creating a sample transaction for testing
 */
@WebServlet(name = "createSampleTransactionServlet", urlPatterns = {"/admin/create-sample-transaction"})
public class CreateSampleTransactionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = SampleDataUtil.createSampleTransaction();
        
        if (success) {
            request.setAttribute("successMessage", "Sample transaction created successfully");
        } else {
            request.setAttribute("errorMessage", "Error creating sample transaction");
        }
        
        // Redirect to the admin dashboard
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
