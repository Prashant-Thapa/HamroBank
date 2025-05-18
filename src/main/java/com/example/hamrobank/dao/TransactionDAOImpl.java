package com.example.hamrobank.dao;

import com.example.hamrobank.model.Transaction;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of TransactionDAO interface
 */
public class TransactionDAOImpl implements TransactionDAO {
    private static final Logger LOGGER = Logger.getLogger(TransactionDAOImpl.class.getName());

    @Override
    public Transaction createTransaction(Transaction transaction) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO transactions (transaction_type, amount, source_account_id, " +
                         "destination_account_id, description, status, reference_number, category_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, transaction.getTransactionType().toString());
            stmt.setBigDecimal(2, transaction.getAmount());

            if (transaction.getSourceAccountId() != null) {
                stmt.setInt(3, transaction.getSourceAccountId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (transaction.getDestinationAccountId() != null) {
                stmt.setInt(4, transaction.getDestinationAccountId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, transaction.getDescription());
            stmt.setString(6, transaction.getStatus().toString());

            // Generate a reference number if not provided
            if (transaction.getReferenceNumber() == null || transaction.getReferenceNumber().isEmpty()) {
                transaction.setReferenceNumber(generateReferenceNumber());
            }
            stmt.setString(7, transaction.getReferenceNumber());

            // Set category if provided
            if (transaction.getCategoryId() != null) {
                stmt.setInt(8, transaction.getCategoryId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt(1));
            } else {
                throw new SQLException("Creating transaction failed, no ID obtained.");
            }

            return transaction;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Transaction getTransactionById(int transactionId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE t.transaction_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, transactionId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Transaction getTransactionByReferenceNumber(String referenceNumber) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE t.reference_number = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, referenceNumber);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction by reference number", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE t.source_account_id = ? OR t.destination_account_id = ? " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getTransactionsBySourceAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE t.source_account_id = ? " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by source account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getTransactionsByDestinationAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE t.destination_account_id = ? " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by destination account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateTransactionStatus(int transactionId, Transaction.TransactionStatus status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.toString());
            stmt.setInt(2, transactionId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public String generateReferenceNumber() throws Exception {
        // Generate a unique reference number using UUID and timestamp
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public List<Transaction> getAllTransactions() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transactions", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getRecentTransactions(int accountId, int limit) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql;

            // If accountId is 0, get recent transactions for all accounts
            if (accountId == 0) {
                sql = "SELECT t.*, " +
                      "src.account_number AS source_account_number, " +
                      "dst.account_number AS destination_account_number " +
                      "FROM transactions t " +
                      "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                      "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                      "ORDER BY t.transaction_date DESC LIMIT ?";

                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, limit);
            } else {
                // Get recent transactions for a specific account
                sql = "SELECT t.*, " +
                      "src.account_number AS source_account_number, " +
                      "dst.account_number AS destination_account_number " +
                      "FROM transactions t " +
                      "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                      "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                      "WHERE t.source_account_id = ? OR t.destination_account_id = ? " +
                      "ORDER BY t.transaction_date DESC LIMIT ?";

                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, accountId);
                stmt.setInt(2, accountId);
                stmt.setInt(3, limit);
            }

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent transactions", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(int accountId, Date startDate, Date endDate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE (t.source_account_id = ? OR t.destination_account_id = ?) " +
                         "AND t.transaction_date BETWEEN ? AND ? " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by date range", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Transaction> getTransactionsByCategory(int accountId, int categoryId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.*, " +
                         "src.account_number AS source_account_number, " +
                         "dst.account_number AS destination_account_number " +
                         "FROM transactions t " +
                         "LEFT JOIN accounts src ON t.source_account_id = src.account_id " +
                         "LEFT JOIN accounts dst ON t.destination_account_id = dst.account_id " +
                         "WHERE (t.source_account_id = ? OR t.destination_account_id = ?) " +
                         "AND t.category_id = ? " +
                         "ORDER BY t.transaction_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setInt(3, categoryId);

            rs = stmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

            return transactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateTransactionCategory(int transactionId, Integer categoryId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE transactions SET category_id = ? WHERE transaction_id = ?";

            stmt = conn.prepareStatement(sql);

            if (categoryId != null) {
                stmt.setInt(1, categoryId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, transactionId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    /**
     * Map a ResultSet to a Transaction object
     *
     * @param rs ResultSet containing transaction data
     * @return Transaction object
     * @throws SQLException if an error occurs during the mapping
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setAmount(rs.getBigDecimal("amount"));

        // Handle nullable fields
        int sourceAccountId = rs.getInt("source_account_id");
        if (!rs.wasNull()) {
            transaction.setSourceAccountId(sourceAccountId);
        }

        int destinationAccountId = rs.getInt("destination_account_id");
        if (!rs.wasNull()) {
            transaction.setDestinationAccountId(destinationAccountId);
        }

        transaction.setDescription(rs.getString("description"));
        transaction.setStatus(Transaction.TransactionStatus.valueOf(rs.getString("status")));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
        transaction.setReferenceNumber(rs.getString("reference_number"));

        // Set category if available
        try {
            int categoryId = rs.getInt("category_id");
            if (!rs.wasNull()) {
                transaction.setCategoryId(categoryId);
            }
        } catch (SQLException e) {
            // Ignore if category_id column is not in the result set
        }

        // Set account numbers if available
        try {
            transaction.setSourceAccountNumber(rs.getString("source_account_number"));
            transaction.setDestinationAccountNumber(rs.getString("destination_account_number"));
        } catch (SQLException e) {
            // Ignore if these columns are not in the result set
        }

        return transaction;
    }
}
