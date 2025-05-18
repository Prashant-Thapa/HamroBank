package com.example.hamrobank.controller.customer;

import com.example.hamrobank.dao.*;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.hamrobank.util.DatabaseUtil;

/**
 * Servlet for transferring funds
 */
@WebServlet(name = "transferFundsServlet", urlPatterns = {"/customer/transfer"})
public class TransferFundsServlet extends HttpServlet {
    
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        try {
            // Get user's accounts
            List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
            request.setAttribute("accounts", accounts);
            
            // Forward to transfer page
            request.getRequestDispatcher("/WEB-INF/views/customer/transfer.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading transfer page: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customer/transfer.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String sourceAccountId = request.getParameter("sourceAccountId");
        String destinationAccountNumber = request.getParameter("destinationAccountNumber");
        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");
        
        Connection conn = null;
        
        try {
            // Validate input
            StringBuilder errorMessage = new StringBuilder();
            
            if (sourceAccountId == null || sourceAccountId.isEmpty()) {
                errorMessage.append("Source account is required. ");
            }
            
            if (destinationAccountNumber == null || destinationAccountNumber.isEmpty()) {
                errorMessage.append("Destination account number is required. ");
            } else if (!ValidationUtil.isValidAccountNumber(destinationAccountNumber)) {
                errorMessage.append("Invalid destination account number. ");
            }
            
            if (amountStr == null || amountStr.isEmpty()) {
                errorMessage.append("Amount is required. ");
            }
            
            BigDecimal amount = null;
            try {
                if (amountStr != null && !amountStr.isEmpty()) {
                    amount = new BigDecimal(amountStr);
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        errorMessage.append("Amount must be greater than zero. ");
                    }
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Invalid amount format. ");
            }
            
            // Get source account
            Account sourceAccount = null;
            if (sourceAccountId != null && !sourceAccountId.isEmpty()) {
                sourceAccount = accountDAO.getAccountById(Integer.parseInt(sourceAccountId));
                
                // Check if source account belongs to the user
                if (sourceAccount == null || sourceAccount.getUserId() != user.getUserId()) {
                    errorMessage.append("Invalid source account. ");
                } else if (sourceAccount.getStatus() != Account.AccountStatus.ACTIVE) {
                    errorMessage.append("Source account is not active. ");
                } else if (amount != null && amount.compareTo(sourceAccount.getBalance()) > 0) {
                    errorMessage.append("Insufficient funds. ");
                }
            }
            
            // Get destination account
            Account destinationAccount = null;
            if (destinationAccountNumber != null && !destinationAccountNumber.isEmpty()) {
                destinationAccount = accountDAO.getAccountByAccountNumber(destinationAccountNumber);
                
                if (destinationAccount == null) {
                    errorMessage.append("Destination account not found. ");
                } else if (destinationAccount.getStatus() != Account.AccountStatus.ACTIVE) {
                    errorMessage.append("Destination account is not active. ");
                }
            }
            
            // Check if source and destination are the same
            if (sourceAccount != null && destinationAccount != null && 
                sourceAccount.getAccountId() == destinationAccount.getAccountId()) {
                errorMessage.append("Source and destination accounts cannot be the same. ");
            }
            
            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString());
                List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
                request.setAttribute("accounts", accounts);
                request.setAttribute("sourceAccountId", sourceAccountId);
                request.setAttribute("destinationAccountNumber", destinationAccountNumber);
                request.setAttribute("amount", amountStr);
                request.setAttribute("description", description);
                request.getRequestDispatcher("/WEB-INF/views/customer/transfer.jsp").forward(request, response);
                return;
            }
            
            // Start transaction
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            
            // Update source account balance
            BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(amount);
            accountDAO.updateBalance(sourceAccount.getAccountId(), newSourceBalance);
            
            // Update destination account balance
            BigDecimal newDestinationBalance = destinationAccount.getBalance().add(amount);
            accountDAO.updateBalance(destinationAccount.getAccountId(), newDestinationBalance);
            
            // Create transaction record
            Transaction transaction = new Transaction();
            transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
            transaction.setAmount(amount);
            transaction.setSourceAccountId(sourceAccount.getAccountId());
            transaction.setDestinationAccountId(destinationAccount.getAccountId());
            transaction.setDescription(description);
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transaction.setReferenceNumber(transactionDAO.generateReferenceNumber());
            
            transaction = transactionDAO.createTransaction(transaction);
            
            // Log activity
            ActivityLog activityLog = new ActivityLog();
            activityLog.setUserId(user.getUserId());
            activityLog.setActivityType("TRANSFER");
            activityLog.setDescription("Transfer of " + amount + " from account " + 
                                      sourceAccount.getAccountNumber() + " to account " + 
                                      destinationAccount.getAccountNumber());
            activityLog.setIpAddress(request.getRemoteAddr());
            activityLog.setUserAgent(request.getHeader("User-Agent"));
            
            activityLogDAO.createActivityLog(activityLog);
            
            // Commit transaction
            conn.commit();
            
            // Set success message and redirect
            session.setAttribute("successMessage", "Transfer successful! Reference number: " + transaction.getReferenceNumber());
            response.sendRedirect(request.getContextPath() + "/customer/transactions");
            
        } catch (Exception e) {
            // Rollback transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    getServletContext().log("Error rolling back transaction", ex);
                }
            }
            
            request.setAttribute("errorMessage", "Error processing transfer: " + e.getMessage());
            try {
                List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
                request.setAttribute("accounts", accounts);
            } catch (Exception ex) {
                // Ignore
            }
            request.setAttribute("sourceAccountId", sourceAccountId);
            request.setAttribute("destinationAccountNumber", destinationAccountNumber);
            request.setAttribute("amount", amountStr);
            request.setAttribute("description", description);
            request.getRequestDispatcher("/WEB-INF/views/customer/transfer.jsp").forward(request, response);
        } finally {
            // Close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    getServletContext().log("Error closing connection", e);
                }
            }
        }
    }
}
