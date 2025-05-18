package com.example.hamrobank.controller;

import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.ScheduledTransaction;
import com.example.hamrobank.model.User;
import com.example.hamrobank.service.AccountService;
import com.example.hamrobank.service.ScheduledTransactionService;
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
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling scheduled transaction operations
 */
@WebServlet(name = "ScheduledTransactionServlet", urlPatterns = {"/scheduled-transactions", "/scheduled-transactions/*"})
public class ScheduledTransactionServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ScheduledTransactionServlet.class.getName());
    private final ScheduledTransactionService scheduledTransactionService;
    private final AccountService accountService;
    private final Gson gson;

    /**
     * Constructor
     */
    public ScheduledTransactionServlet() {
        this.scheduledTransactionService = new ScheduledTransactionService();
        this.accountService = new AccountService();
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
                // Get all scheduled transactions for the user
                List<ScheduledTransaction> scheduledTransactions = scheduledTransactionService.getScheduledTransactionsByUserId(user.getUserId());
                List<Account> userAccounts = accountService.getAccountsByUserId(user.getUserId());

                request.setAttribute("scheduledTransactions", scheduledTransactions);
                request.setAttribute("userAccounts", userAccounts);
                request.getRequestDispatcher("/WEB-INF/views/transaction/scheduled-transactions.jsp").forward(request, response);
            } else if (pathInfo.equals("/api")) {
                // API endpoint to get scheduled transactions as JSON
                List<ScheduledTransaction> scheduledTransactions = scheduledTransactionService.getScheduledTransactionsByUserId(user.getUserId());
                sendJsonResponse(response, scheduledTransactions);
            } else if (pathInfo.startsWith("/api/")) {
                // API endpoint to get a specific scheduled transaction
                String idStr = pathInfo.substring(5);
                try {
                    int scheduledTxId = Integer.parseInt(idStr);
                    ScheduledTransaction scheduledTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);

                    if (scheduledTransaction != null) {
                        // Check if the user owns this scheduled transaction
                        if (scheduledTransaction.getUserId() == user.getUserId()) {
                            sendJsonResponse(response, scheduledTransaction);
                        } else {
                            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        }
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Scheduled transaction not found");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid scheduled transaction ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing scheduled transaction request", e);
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

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/api")) {
                // Create a new scheduled transaction
                int sourceAccountId = Integer.parseInt(request.getParameter("sourceAccountId"));
                int destinationAccountId = Integer.parseInt(request.getParameter("destinationAccountId"));
                BigDecimal amount = new BigDecimal(request.getParameter("amount"));
                String description = request.getParameter("description");
                String frequency = request.getParameter("frequency");
                Date startDate = Date.valueOf(request.getParameter("startDate"));

                // End date is optional
                String endDateStr = request.getParameter("endDate");
                Date endDate = null;
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    endDate = Date.valueOf(endDateStr);
                }

                // Validate that the user owns the source account
                Account sourceAccount = accountService.getAccountById(sourceAccountId);
                if (sourceAccount == null || sourceAccount.getUserId() != user.getUserId()) {
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "You do not own the source account");
                    return;
                }

                // Create the scheduled transaction
                ScheduledTransaction scheduledTransaction = new ScheduledTransaction(
                    user.getUserId(),
                    sourceAccountId,
                    destinationAccountId,
                    amount,
                    description,
                    ScheduledTransaction.Frequency.fromString(frequency),
                    startDate,
                    startDate // Next execution date is initially the start date
                );

                scheduledTransaction.setEndDate(endDate);

                ScheduledTransaction createdTransaction = scheduledTransactionService.createScheduledTransaction(scheduledTransaction);

                if (pathInfo != null && pathInfo.equals("/api")) {
                    // API request
                    sendJsonResponse(response, createdTransaction);
                } else {
                    // Form submission
                    response.sendRedirect(request.getContextPath() + "/scheduled-transactions");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating scheduled transaction", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating scheduled transaction");
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

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.startsWith("/api/")) {
                // Update a scheduled transaction
                String idStr = pathInfo.substring(5);
                try {
                    int scheduledTxId = Integer.parseInt(idStr);

                    // Check if the user owns this scheduled transaction
                    ScheduledTransaction existingTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);
                    if (existingTransaction == null || existingTransaction.getUserId() != user.getUserId()) {
                        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        return;
                    }

                    // Parse request body
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = request.getReader().readLine()) != null) {
                        sb.append(line);
                    }

                    ScheduledTransaction scheduledTransaction = gson.fromJson(sb.toString(), ScheduledTransaction.class);
                    scheduledTransaction.setScheduledTxId(scheduledTxId);
                    scheduledTransaction.setUserId(user.getUserId()); // Ensure user ID is set correctly

                    boolean success = scheduledTransactionService.updateScheduledTransaction(scheduledTransaction);

                    if (success) {
                        ScheduledTransaction updatedTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);
                        sendJsonResponse(response, updatedTransaction);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Scheduled transaction not found or cannot be updated");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid scheduled transaction ID");
                }
            } else if (pathInfo != null && pathInfo.startsWith("/api/status/")) {
                // Update the status of a scheduled transaction
                String idStr = pathInfo.substring(12);
                try {
                    int scheduledTxId = Integer.parseInt(idStr);

                    // Check if the user owns this scheduled transaction
                    ScheduledTransaction existingTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);
                    if (existingTransaction == null || existingTransaction.getUserId() != user.getUserId()) {
                        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        return;
                    }

                    // Parse request body
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = request.getReader().readLine()) != null) {
                        sb.append(line);
                    }

                    JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
                    String status = jsonObject.get("status").getAsString();

                    boolean success = scheduledTransactionService.updateStatus(
                        scheduledTxId,
                        ScheduledTransaction.Status.fromString(status)
                    );

                    if (success) {
                        ScheduledTransaction updatedTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);
                        sendJsonResponse(response, updatedTransaction);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Scheduled transaction not found or cannot be updated");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid scheduled transaction ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating scheduled transaction", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating scheduled transaction");
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

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.startsWith("/api/")) {
                // Delete a scheduled transaction
                String idStr = pathInfo.substring(5);
                try {
                    int scheduledTxId = Integer.parseInt(idStr);

                    // Check if the user owns this scheduled transaction
                    ScheduledTransaction existingTransaction = scheduledTransactionService.getScheduledTransactionById(scheduledTxId);
                    if (existingTransaction == null || existingTransaction.getUserId() != user.getUserId()) {
                        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        return;
                    }

                    boolean success = scheduledTransactionService.deleteScheduledTransaction(scheduledTxId);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Scheduled transaction not found or cannot be deleted");
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid scheduled transaction ID");
                }
            } else {
                // Invalid path
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting scheduled transaction", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting scheduled transaction");
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
