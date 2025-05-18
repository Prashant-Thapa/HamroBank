package com.example.hamrobank.util;

import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating PDF receipts
 * 
 * Note: This is a simplified version that generates HTML content that can be converted to PDF.
 * In a real application, you would use a library like iText or Apache PDFBox to generate actual PDFs.
 */
public class PDFGenerator {
    
    /**
     * Generate a transaction receipt as HTML
     * 
     * @param transaction Transaction to generate receipt for
     * @param user User who performed the transaction
     * @return HTML content for the receipt
     */
    public static String generateTransactionReceipt(Transaction transaction, User user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Transaction Receipt</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }");
        html.append(".receipt { max-width: 800px; margin: 0 auto; border: 1px solid #ccc; padding: 20px; }");
        html.append(".header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 10px; margin-bottom: 20px; }");
        html.append(".logo { font-size: 24px; font-weight: bold; color: #0056b3; }");
        html.append(".info-row { display: flex; justify-content: space-between; margin-bottom: 10px; }");
        html.append(".label { font-weight: bold; width: 40%; }");
        html.append(".value { width: 60%; }");
        html.append(".footer { margin-top: 30px; text-align: center; font-size: 12px; color: #666; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='receipt'>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<div class='logo'>Hamro Bank</div>");
        html.append("<div>Transaction Receipt</div>");
        html.append("</div>");
        
        // Transaction details
        html.append("<div class='info-row'>");
        html.append("<div class='label'>Reference Number:</div>");
        html.append("<div class='value'>").append(transaction.getReferenceNumber()).append("</div>");
        html.append("</div>");
        
        html.append("<div class='info-row'>");
        html.append("<div class='label'>Date:</div>");
        html.append("<div class='value'>").append(dateFormat.format(transaction.getTransactionDate())).append("</div>");
        html.append("</div>");
        
        html.append("<div class='info-row'>");
        html.append("<div class='label'>Transaction Type:</div>");
        html.append("<div class='value'>").append(transaction.getTransactionType()).append("</div>");
        html.append("</div>");
        
        html.append("<div class='info-row'>");
        html.append("<div class='label'>Amount:</div>");
        html.append("<div class='value'>$").append(transaction.getAmount()).append("</div>");
        html.append("</div>");
        
        if (transaction.getSourceAccountNumber() != null) {
            html.append("<div class='info-row'>");
            html.append("<div class='label'>From Account:</div>");
            html.append("<div class='value'>").append(transaction.getSourceAccountNumber()).append("</div>");
            html.append("</div>");
        }
        
        if (transaction.getDestinationAccountNumber() != null) {
            html.append("<div class='info-row'>");
            html.append("<div class='label'>To Account:</div>");
            html.append("<div class='value'>").append(transaction.getDestinationAccountNumber()).append("</div>");
            html.append("</div>");
        }
        
        html.append("<div class='info-row'>");
        html.append("<div class='label'>Status:</div>");
        html.append("<div class='value'>").append(transaction.getStatus()).append("</div>");
        html.append("</div>");
        
        if (transaction.getDescription() != null && !transaction.getDescription().isEmpty()) {
            html.append("<div class='info-row'>");
            html.append("<div class='label'>Description:</div>");
            html.append("<div class='value'>").append(transaction.getDescription()).append("</div>");
            html.append("</div>");
        }
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Thank you for banking with Hamro Bank.</p>");
        html.append("<p>This is an electronic receipt. No signature is required.</p>");
        html.append("<p>For any queries, please contact our customer service at support@hamrobank.com</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
}
