package com.example.hamrobank.controller;

import com.example.hamrobank.model.TransactionCategory;
import com.example.hamrobank.model.User;
import com.example.hamrobank.service.TransactionCategoryService;
import com.example.hamrobank.util.AuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling transaction category operations
 */
@WebServlet(name = "TransactionCategoryServlet", urlPatterns = {"/categories", "/categories/*"})
public class TransactionCategoryServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TransactionCategoryServlet.class.getName());
    private final TransactionCategoryService transactionCategoryService;
    private final Gson gson;

    /**
     * Constructor
     */
    public TransactionCategoryServlet() {
        this.transactionCategoryService = new TransactionCategoryService();
        this.gson = new Gson();
    }

    /**
     * Handle GET requests
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User user = AuthUtil.getLoggedInUser(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all categories
                List<TransactionCategory> categories = transactionCategoryService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/views/transaction/categories.jsp").forward(request, response);
            } else if (pathInfo.equals("/api")) {
                // API endpoint to get categories as JSON
                List<TransactionCategory> categories = transactionCategoryService.getAllCategories();
                sendJsonResponse(response, categories);
            } else if (pathInfo.startsWith("/api/")) {
                // API endpoint to get a specific category
                String idStr = pathInfo.substring(5);
                try {
                    int categoryId = Integer.parseInt(idStr);
                    TransactionCategory category = transactionCategoryService.getCategoryById(categoryId);

                    if (category != null) {
                        sendJsonResponse(response, category);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Category not found");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing category request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    /**
     * Handle POST requests
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User user = AuthUtil.getLoggedInUser(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Only admin users can create categories
        if (user.getRole() != User.Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only administrators can create categories");
            return;
        }

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/api")) {
                // Create a new category
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String icon = request.getParameter("icon");
                String color = request.getParameter("color");

                if (name == null || name.trim().isEmpty()) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
                    return;
                }

                TransactionCategory category = new TransactionCategory(name, description, icon, color);
                TransactionCategory createdCategory = transactionCategoryService.createCategory(category);

                if (pathInfo != null && pathInfo.equals("/api")) {
                    // API request
                    sendJsonResponse(response, createdCategory);
                } else {
                    // Form submission
                    response.sendRedirect(request.getContextPath() + "/categories");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating category", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating category");
        }
    }

    /**
     * Handle PUT requests
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User user = AuthUtil.getLoggedInUser(session);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Only admin users can update categories
        if (user.getRole() != User.Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only administrators can update categories");
            return;
        }

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.startsWith("/api/")) {
                // Update a category
                String idStr = pathInfo.substring(5);
                try {
                    int categoryId = Integer.parseInt(idStr);

                    // Parse request body
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = request.getReader().readLine()) != null) {
                        sb.append(line);
                    }

                    TransactionCategory category = gson.fromJson(sb.toString(), TransactionCategory.class);
                    category.setCategoryId(categoryId);

                    boolean success = transactionCategoryService.updateCategory(category);

                    if (success) {
                        TransactionCategory updatedCategory = transactionCategoryService.getCategoryById(categoryId);
                        sendJsonResponse(response, updatedCategory);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Category not found or cannot be updated");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating category", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating category");
        }
    }

    /**
     * Handle DELETE requests
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User user = AuthUtil.getLoggedInUser(session);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Only admin users can delete categories
        if (user.getRole() != User.Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only administrators can delete categories");
            return;
        }

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.startsWith("/api/")) {
                // Delete a category
                String idStr = pathInfo.substring(5);
                try {
                    int categoryId = Integer.parseInt(idStr);
                    boolean success = transactionCategoryService.deleteCategory(categoryId);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Category not found or cannot be deleted");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting category", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting category");
        }
    }

    /**
     * Send a JSON response
     *
     * @param response HttpServletResponse
     * @param data Object to convert to JSON
     * @throws IOException if an I/O error occurs
     */
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(data));
        out.flush();
    }

    /**
     * Send an error response in JSON format
     *
     * @param response HttpServletResponse
     * @param status HTTP status code
     * @param message Error message
     * @throws IOException if an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        JsonObject error = new JsonObject();
        error.addProperty("error", message);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(error));
        out.flush();
    }
}
