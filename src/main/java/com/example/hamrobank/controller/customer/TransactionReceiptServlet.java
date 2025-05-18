package com.example.hamrobank.controller.customer;

import com.example.hamrobank.dao.AccountDAO;
import com.example.hamrobank.dao.AccountDAOImpl;
import com.example.hamrobank.dao.TransactionDAO;
import com.example.hamrobank.dao.TransactionDAOImpl;
import com.example.hamrobank.dao.UserDAO;
import com.example.hamrobank.dao.UserDAOImpl;
import com.example.hamrobank.model.Account;
import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.PDFGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for generating transaction receipts
 */
@WebServlet(name = "transactionReceiptServlet", urlPatterns = {"/customer/receipt"})
public class TransactionReceiptServlet extends HttpServlet {
    
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAOImpl();
        accountDAO = new AccountDAOImpl();
        userDAO = new UserDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String transactionId = request.getParameter("id");
        
        try {
            if (transactionId == null || transactionId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/customer/transactions");
                return;
            }
            
            // Get transaction
            Transaction transaction = transactionDAO.getTransactionById(Integer.parseInt(transactionId));
            
            if (transaction == null) {
                response.sendRedirect(request.getContextPath() + "/customer/transactions");
                return;
            }
            
            // Verify that the transaction involves an account owned by the user
            boolean isAuthorized = false;
            
            if (transaction.getSourceAccountId() != null) {
                Account sourceAccount = accountDAO.getAccountById(transaction.getSourceAccountId());
                if (sourceAccount != null && sourceAccount.getUserId() == user.getUserId()) {
                    isAuthorized = true;
                }
            }
            
            if (!isAuthorized && transaction.getDestinationAccountId() != null) {
                Account destAccount = accountDAO.getAccountById(transaction.getDestinationAccountId());
                if (destAccount != null && destAccount.getUserId() == user.getUserId()) {
                    isAuthorized = true;
                }
            }
            
            if (!isAuthorized) {
                response.sendRedirect(request.getContextPath() + "/customer/transactions");
                return;
            }
            
            // Generate receipt
            String receiptHtml = PDFGenerator.generateTransactionReceipt(transaction, user);
            
            // Set response headers
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            
            // Write receipt to response
            PrintWriter out = response.getWriter();
            out.println(receiptHtml);
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/customer/transactions?error=receipt");
        }
    }
}
