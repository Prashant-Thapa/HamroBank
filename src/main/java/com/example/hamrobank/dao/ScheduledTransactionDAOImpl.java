package com.example.hamrobank.dao;

import com.example.hamrobank.model.ScheduledTransaction;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the ScheduledTransactionDAO interface
 */
public class ScheduledTransactionDAOImpl implements ScheduledTransactionDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ScheduledTransactionDAOImpl.class.getName());
    
    @Override
    public ScheduledTransaction createScheduledTransaction(ScheduledTransaction scheduledTransaction) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO scheduled_transactions (user_id, source_account_id, destination_account_id, " +
                         "amount, description, frequency, start_date, end_date, next_execution_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, scheduledTransaction.getUserId());
            stmt.setInt(2, scheduledTransaction.getSourceAccountId());
            stmt.setInt(3, scheduledTransaction.getDestinationAccountId());
            stmt.setBigDecimal(4, scheduledTransaction.getAmount());
            stmt.setString(5, scheduledTransaction.getDescription());
            stmt.setString(6, scheduledTransaction.getFrequency().toString());
            stmt.setDate(7, scheduledTransaction.getStartDate());
            
            if (scheduledTransaction.getEndDate() != null) {
                stmt.setDate(8, scheduledTransaction.getEndDate());
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setDate(9, scheduledTransaction.getNextExecutionDate());
            stmt.setString(10, scheduledTransaction.getStatus().toString());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating scheduled transaction failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                scheduledTransaction.setScheduledTxId(rs.getInt(1));
            } else {
                throw new SQLException("Creating scheduled transaction failed, no ID obtained.");
            }
            
            return scheduledTransaction;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating scheduled transaction", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public ScheduledTransaction getScheduledTransactionById(int scheduledTxId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM scheduled_transactions WHERE scheduled_tx_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduledTxId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToScheduledTransaction(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transaction by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ScheduledTransaction> getScheduledTransactionsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM scheduled_transactions WHERE user_id = ? ORDER BY next_execution_date";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
            while (rs.next()) {
                scheduledTransactions.add(mapResultSetToScheduledTransaction(rs));
            }
            
            return scheduledTransactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transactions by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ScheduledTransaction> getScheduledTransactionsByAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM scheduled_transactions WHERE source_account_id = ? OR destination_account_id = ? " +
                         "ORDER BY next_execution_date";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            
            rs = stmt.executeQuery();
            
            List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
            while (rs.next()) {
                scheduledTransactions.add(mapResultSetToScheduledTransaction(rs));
            }
            
            return scheduledTransactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting scheduled transactions by account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<ScheduledTransaction> getDueScheduledTransactions() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM scheduled_transactions WHERE status = 'ACTIVE' AND next_execution_date <= CURRENT_DATE()";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
            while (rs.next()) {
                scheduledTransactions.add(mapResultSetToScheduledTransaction(rs));
            }
            
            return scheduledTransactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting due scheduled transactions", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateScheduledTransaction(ScheduledTransaction scheduledTransaction) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE scheduled_transactions SET source_account_id = ?, destination_account_id = ?, " +
                         "amount = ?, description = ?, frequency = ?, start_date = ?, end_date = ?, " +
                         "next_execution_date = ?, status = ? WHERE scheduled_tx_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduledTransaction.getSourceAccountId());
            stmt.setInt(2, scheduledTransaction.getDestinationAccountId());
            stmt.setBigDecimal(3, scheduledTransaction.getAmount());
            stmt.setString(4, scheduledTransaction.getDescription());
            stmt.setString(5, scheduledTransaction.getFrequency().toString());
            stmt.setDate(6, scheduledTransaction.getStartDate());
            
            if (scheduledTransaction.getEndDate() != null) {
                stmt.setDate(7, scheduledTransaction.getEndDate());
            } else {
                stmt.setNull(7, Types.DATE);
            }
            
            stmt.setDate(8, scheduledTransaction.getNextExecutionDate());
            stmt.setString(9, scheduledTransaction.getStatus().toString());
            stmt.setInt(10, scheduledTransaction.getScheduledTxId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating scheduled transaction", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateNextExecutionDate(int scheduledTxId, java.sql.Date nextExecutionDate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE scheduled_transactions SET next_execution_date = ? WHERE scheduled_tx_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, nextExecutionDate);
            stmt.setInt(2, scheduledTxId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating next execution date", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateStatus(int scheduledTxId, ScheduledTransaction.Status status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE scheduled_transactions SET status = ? WHERE scheduled_tx_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.toString());
            stmt.setInt(2, scheduledTxId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating scheduled transaction status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteScheduledTransaction(int scheduledTxId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM scheduled_transactions WHERE scheduled_tx_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduledTxId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting scheduled transaction", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    /**
     * Map a ResultSet to a ScheduledTransaction object
     * 
     * @param rs ResultSet to map
     * @return ScheduledTransaction object
     * @throws SQLException if an error occurs during the mapping
     */
    private ScheduledTransaction mapResultSetToScheduledTransaction(ResultSet rs) throws SQLException {
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setScheduledTxId(rs.getInt("scheduled_tx_id"));
        scheduledTransaction.setUserId(rs.getInt("user_id"));
        scheduledTransaction.setSourceAccountId(rs.getInt("source_account_id"));
        scheduledTransaction.setDestinationAccountId(rs.getInt("destination_account_id"));
        scheduledTransaction.setAmount(rs.getBigDecimal("amount"));
        scheduledTransaction.setDescription(rs.getString("description"));
        scheduledTransaction.setFrequency(ScheduledTransaction.Frequency.fromString(rs.getString("frequency")));
        scheduledTransaction.setStartDate(rs.getDate("start_date"));
        scheduledTransaction.setEndDate(rs.getDate("end_date"));
        scheduledTransaction.setNextExecutionDate(rs.getDate("next_execution_date"));
        scheduledTransaction.setStatus(ScheduledTransaction.Status.fromString(rs.getString("status")));
        scheduledTransaction.setCreatedAt(rs.getTimestamp("created_at"));
        scheduledTransaction.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return scheduledTransaction;
    }
}
