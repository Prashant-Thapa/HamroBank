package com.example.hamrobank.controller.admin;

import com.example.hamrobank.dao.*;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.ActivityLog;
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
import java.util.List;

/**
 * Servlet for managing accounts (admin)
 */
@WebServlet(name = "accountManagementServlet", urlPatterns = {"/admin/accounts"})
public class AccountManagementServlet extends HttpServlet {
    
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    
    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAOImpl();
        userDAO = new UserDAOImpl();
        activityLogDAO = new ActivityLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.equals("list")) {
                // List all accounts
                List<Account> accounts = accountDAO.getAllAccounts();
                request.setAttribute("accounts", accounts);
                request.getRequestDispatcher("/WEB-INF/views/admin/accounts.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Show edit form for an account
                String accountId = request.getParameter("id");
                if (accountId != null && !accountId.isEmpty()) {
                    Account account = accountDAO.getAccountById(Integer.parseInt(accountId));
                    if (account != null) {
                        User user = userDAO.getUserById(account.getUserId());
                        request.setAttribute("account", account);
                        request.setAttribute("accountUser", user);
                        request.getRequestDispatcher("/WEB-INF/views/admin/account-edit.jsp").forward(request, response);
                        return;
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
            } else if (action.equals("add")) {
                // Show add account form
                List<User> customers = userDAO.getUsersByRole(User.Role.CUSTOMER);
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/WEB-INF/views/admin/account-add.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/accounts.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User adminUser = (User) session.getAttribute("user");
        
        try {
            if (action == null) {
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
                return;
            }
            
            if (action.equals("add")) {
                // Add a new account
                String userIdStr = request.getParameter("userId");
                String accountTypeStr = request.getParameter("accountType");
                String initialBalanceStr = request.getParameter("initialBalance");
                
                // Validate input
                StringBuilder errorMessage = new StringBuilder();
                
                if (userIdStr == null || userIdStr.isEmpty()) {
                    errorMessage.append("User is required. ");
                }
                
                if (accountTypeStr == null || accountTypeStr.isEmpty()) {
                    errorMessage.append("Account type is required. ");
                }
                
                BigDecimal initialBalance = BigDecimal.ZERO;
                try {
                    if (initialBalanceStr != null && !initialBalanceStr.isEmpty()) {
                        initialBalance = new BigDecimal(initialBalanceStr);
                        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                            errorMessage.append("Initial balance cannot be negative. ");
                        }
                    }
                } catch (NumberFormatException e) {
                    errorMessage.append("Invalid initial balance format. ");
                }
                
                if (errorMessage.length() > 0) {
                    List<User> customers = userDAO.getUsersByRole(User.Role.CUSTOMER);
                    request.setAttribute("customers", customers);
                    request.setAttribute("errorMessage", errorMessage.toString());
                    request.setAttribute("userId", userIdStr);
                    request.setAttribute("accountType", accountTypeStr);
                    request.setAttribute("initialBalance", initialBalanceStr);
                    request.getRequestDispatcher("/WEB-INF/views/admin/account-add.jsp").forward(request, response);
                    return;
                }
                
                // Create new account
                Account account = new Account();
                account.setUserId(Integer.parseInt(userIdStr));
                account.setAccountType(Account.AccountType.valueOf(accountTypeStr));
                account.setBalance(initialBalance);
                account.setStatus(Account.AccountStatus.ACTIVE);
                account.setAccountNumber(accountDAO.generateAccountNumber());
                
                account = accountDAO.createAccount(account);
                
                // Get user for logging
                User accountUser = userDAO.getUserById(account.getUserId());
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("ACCOUNT_CREATED");
                activityLog.setDescription("Admin created new account for user: " + accountUser.getUsername());
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "Account created successfully");
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
                
            } else if (action.equals("update")) {
                // Update an existing account
                String accountId = request.getParameter("accountId");
                String accountTypeStr = request.getParameter("accountType");
                String balanceStr = request.getParameter("balance");
                String statusStr = request.getParameter("status");
                
                if (accountId == null || accountId.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/admin/accounts");
                    return;
                }
                
                // Get existing account
                Account account = accountDAO.getAccountById(Integer.parseInt(accountId));
                if (account == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/accounts");
                    return;
                }
                
                // Validate input
                StringBuilder errorMessage = new StringBuilder();
                
                if (accountTypeStr == null || accountTypeStr.isEmpty()) {
                    errorMessage.append("Account type is required. ");
                }
                
                if (statusStr == null || statusStr.isEmpty()) {
                    errorMessage.append("Status is required. ");
                }
                
                BigDecimal balance = null;
                try {
                    if (balanceStr != null && !balanceStr.isEmpty()) {
                        balance = new BigDecimal(balanceStr);
                        if (balance.compareTo(BigDecimal.ZERO) < 0) {
                            errorMessage.append("Balance cannot be negative. ");
                        }
                    } else {
                        errorMessage.append("Balance is required. ");
                    }
                } catch (NumberFormatException e) {
                    errorMessage.append("Invalid balance format. ");
                }
                
                if (errorMessage.length() > 0) {
                    User user = userDAO.getUserById(account.getUserId());
                    request.setAttribute("errorMessage", errorMessage.toString());
                    request.setAttribute("account", account);
                    request.setAttribute("accountUser", user);
                    request.getRequestDispatcher("/WEB-INF/views/admin/account-edit.jsp").forward(request, response);
                    return;
                }
                
                // Update account
                account.setAccountType(Account.AccountType.valueOf(accountTypeStr));
                account.setBalance(balance);
                account.setStatus(Account.AccountStatus.valueOf(statusStr));
                
                accountDAO.updateAccount(account);
                
                // Get user for logging
                User accountUser = userDAO.getUserById(account.getUserId());
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("ACCOUNT_UPDATED");
                activityLog.setDescription("Admin updated account for user: " + accountUser.getUsername());
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "Account updated successfully");
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
                
            } else if (action.equals("delete")) {
                // Delete an account
                String accountId = request.getParameter("id");
                
                if (accountId == null || accountId.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/admin/accounts");
                    return;
                }
                
                // Get account to be deleted
                Account account = accountDAO.getAccountById(Integer.parseInt(accountId));
                if (account == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/accounts");
                    return;
                }
                
                // Get user for logging
                User accountUser = userDAO.getUserById(account.getUserId());
                
                // Delete account
                accountDAO.deleteAccount(account.getAccountId());
                
                // Log activity
                ActivityLog activityLog = new ActivityLog();
                activityLog.setUserId(adminUser.getUserId());
                activityLog.setActivityType("ACCOUNT_DELETED");
                activityLog.setDescription("Admin deleted account for user: " + accountUser.getUsername());
                activityLog.setIpAddress(request.getRemoteAddr());
                activityLog.setUserAgent(request.getHeader("User-Agent"));
                activityLogDAO.createActivityLog(activityLog);
                
                // Redirect with success message
                session.setAttribute("successMessage", "Account deleted successfully");
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/accounts");
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            try {
                List<Account> accounts = accountDAO.getAllAccounts();
                request.setAttribute("accounts", accounts);
            } catch (Exception ex) {
                // Ignore
            }
            request.getRequestDispatcher("/WEB-INF/views/admin/accounts.jsp").forward(request, response);
        }
    }
}
