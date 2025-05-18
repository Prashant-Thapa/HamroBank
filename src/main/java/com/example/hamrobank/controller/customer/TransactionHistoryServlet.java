package com.example.hamrobank.controller.customer;

import com.example.hamrobank.dao.AccountDAO;
import com.example.hamrobank.dao.AccountDAOImpl;
import com.example.hamrobank.dao.TransactionDAO;
import com.example.hamrobank.dao.TransactionDAOImpl;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for viewing transaction history
 */
@WebServlet(name = "transactionHistoryServlet", urlPatterns = {"/customer/transactions"})
public class TransactionHistoryServlet extends HttpServlet {
    
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        try {
            // Get user's accounts
            List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
            request.setAttribute("accounts", accounts);
            
            // Get selected account ID from request parameter
            String accountIdStr = request.getParameter("accountId");
            int accountId = 0;
            
            if (accountIdStr != null && !accountIdStr.isEmpty()) {
                accountId = Integer.parseInt(accountIdStr);
            } else if (!accounts.isEmpty()) {
                // Default to first account
                accountId = accounts.get(0).getAccountId();
            }
            
            // Get transactions for the selected account
            List<Transaction> transactions = new ArrayList<>();
            Account selectedAccount = null;
            
            if (accountId > 0) {
                selectedAccount = accountDAO.getAccountById(accountId);
                
                // Verify that the account belongs to the user
                if (selectedAccount != null && selectedAccount.getUserId() == user.getUserId()) {
                    transactions = transactionDAO.getTransactionsByAccountId(accountId);
                    request.setAttribute("selectedAccount", selectedAccount);
                }
            }
            
            request.setAttribute("transactions", transactions);
            
            // Check for success message in session
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            
            // Forward to transactions page
            request.getRequestDispatcher("/WEB-INF/views/customer/transactions.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading transactions: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customer/transactions.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle any POST requests if needed
        doGet(request, response);
    }
}
