package com.example.hamrobank.controller.admin;

import com.example.hamrobank.dao.*;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.ActivityLog;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet for admin dashboard
 */
@WebServlet(name = "adminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get summary data for dashboard
            
            // Total number of customers
            List<User> customers = userDAO.getUsersByRole(User.Role.CUSTOMER);
            request.setAttribute("totalCustomers", customers.size());
            
            // Total number of accounts
            List<Account> accounts = accountDAO.getAllAccounts();
            request.setAttribute("totalAccounts", accounts.size());
            
            // Total balance across all accounts
            BigDecimal totalBalance = BigDecimal.ZERO;
            for (Account account : accounts) {
                totalBalance = totalBalance.add(account.getBalance());
            }
            request.setAttribute("totalBalance", totalBalance);
            
            // Recent transactions
            List<Transaction> recentTransactions = transactionDAO.getRecentTransactions(0, 5);
            request.setAttribute("recentTransactions", recentTransactions);
            
            // Recent activity logs
            List<ActivityLog> recentLogs = activityLogDAO.getRecentActivityLogs(5);
            request.setAttribute("recentLogs", recentLogs);
            
            // Forward to dashboard page
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle any POST requests if needed
        doGet(request, response);
    }
}
