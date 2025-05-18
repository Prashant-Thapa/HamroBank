package com.example.hamrobank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling the about page
 */
@WebServlet(name = "aboutServlet", urlPatterns = {"/about"})
public class AboutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to about page
        request.getRequestDispatcher("/WEB-INF/views/about.jsp").forward(request, response);
    }
}
