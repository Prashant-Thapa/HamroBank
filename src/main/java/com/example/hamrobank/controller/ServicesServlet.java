package com.example.hamrobank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling the services page
 */
@WebServlet(name = "servicesServlet", urlPatterns = {"/services"})
public class ServicesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to services page
        request.getRequestDispatcher("/WEB-INF/views/services.jsp").forward(request, response);
    }
}
