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
import java.util.List;

/**
 * Servlet for customer dashboard
 */
@WebServlet(name = "customerDashboardServlet", urlPatterns = {"/customer/dashboard"})
public class CustomerDashboardServlet extends HttpServlet {
    
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
            
            // Get recent transactions for the first account (if any)
            if (!accounts.isEmpty()) {
                Account primaryAccount = accounts.get(0);
                List<Transaction> recentTransactions = transactionDAO.getRecentTransactions(primaryAccount.getAccountId(), 5);
                request.setAttribute("recentTransactions", recentTransactions);
                request.setAttribute("primaryAccount", primaryAccount);
            }
            
            // Forward to dashboard page
            request.getRequestDispatcher("/WEB-INF/views/customer/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customer/dashboard.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle any POST requests if needed
        doGet(request, response);
    }
}
