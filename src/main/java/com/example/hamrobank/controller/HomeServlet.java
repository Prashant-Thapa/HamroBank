package com.example.hamrobank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling the home page
 */
@WebServlet(name = "homeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to home page
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
