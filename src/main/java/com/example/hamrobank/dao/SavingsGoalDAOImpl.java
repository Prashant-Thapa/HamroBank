package com.example.hamrobank.dao;

import com.example.hamrobank.model.SavingsGoal;
import com.example.hamrobank.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the SavingsGoalDAO interface
 */
public class SavingsGoalDAOImpl implements SavingsGoalDAO {
    
    private static final Logger LOGGER = Logger.getLogger(SavingsGoalDAOImpl.class.getName());
    
    @Override
    public SavingsGoal createSavingsGoal(SavingsGoal savingsGoal) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO savings_goals (user_id, account_id, name, target_amount, current_amount, " +
                         "start_date, target_date, status, icon, color) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, savingsGoal.getUserId());
            stmt.setInt(2, savingsGoal.getAccountId());
            stmt.setString(3, savingsGoal.getName());
            stmt.setBigDecimal(4, savingsGoal.getTargetAmount());
            stmt.setBigDecimal(5, savingsGoal.getCurrentAmount());
            stmt.setDate(6, savingsGoal.getStartDate());
            
            if (savingsGoal.getTargetDate() != null) {
                stmt.setDate(7, savingsGoal.getTargetDate());
            } else {
                stmt.setNull(7, Types.DATE);
            }
            
            stmt.setString(8, savingsGoal.getStatus().toString());
            stmt.setString(9, savingsGoal.getIcon());
            stmt.setString(10, savingsGoal.getColor());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating savings goal failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                savingsGoal.setGoalId(rs.getInt(1));
            } else {
                throw new SQLException("Creating savings goal failed, no ID obtained.");
            }
            
            return savingsGoal;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating savings goal", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public SavingsGoal getSavingsGoalById(int goalId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM savings_goals WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, goalId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSavingsGoal(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goal by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<SavingsGoal> getSavingsGoalsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM savings_goals WHERE user_id = ? ORDER BY target_date";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<SavingsGoal> savingsGoals = new ArrayList<>();
            while (rs.next()) {
                savingsGoals.add(mapResultSetToSavingsGoal(rs));
            }
            
            return savingsGoals;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goals by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<SavingsGoal> getSavingsGoalsByAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM savings_goals WHERE account_id = ? ORDER BY target_date";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            
            rs = stmt.executeQuery();
            
            List<SavingsGoal> savingsGoals = new ArrayList<>();
            while (rs.next()) {
                savingsGoals.add(mapResultSetToSavingsGoal(rs));
            }
            
            return savingsGoals;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting savings goals by account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateSavingsGoal(SavingsGoal savingsGoal) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE savings_goals SET name = ?, target_amount = ?, target_date = ?, " +
                         "status = ?, icon = ?, color = ? WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, savingsGoal.getName());
            stmt.setBigDecimal(2, savingsGoal.getTargetAmount());
            
            if (savingsGoal.getTargetDate() != null) {
                stmt.setDate(3, savingsGoal.getTargetDate());
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setString(4, savingsGoal.getStatus().toString());
            stmt.setString(5, savingsGoal.getIcon());
            stmt.setString(6, savingsGoal.getColor());
            stmt.setInt(7, savingsGoal.getGoalId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating savings goal", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateCurrentAmount(int goalId, BigDecimal amount) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE savings_goals SET current_amount = ? WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, goalId);
            
            int affectedRows = stmt.executeUpdate();
            
            // Check if goal is completed
            if (affectedRows > 0) {
                checkAndUpdateGoalStatus(conn, goalId);
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating savings goal current amount", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean addToCurrentAmount(int goalId, BigDecimal amount) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE savings_goals SET current_amount = current_amount + ? WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, goalId);
            
            int affectedRows = stmt.executeUpdate();
            
            // Check if goal is completed
            if (affectedRows > 0) {
                checkAndUpdateGoalStatus(conn, goalId);
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding to savings goal current amount", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean updateStatus(int goalId, SavingsGoal.Status status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE savings_goals SET status = ? WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.toString());
            stmt.setInt(2, goalId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating savings goal status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteSavingsGoal(int goalId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM savings_goals WHERE goal_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, goalId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting savings goal", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    /**
     * Check if a goal is completed and update its status if necessary
     * 
     * @param conn Database connection
     * @param goalId ID of the goal to check
     * @throws SQLException if an error occurs during the operation
     */
    private void checkAndUpdateGoalStatus(Connection conn, int goalId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT current_amount, target_amount FROM savings_goals WHERE goal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, goalId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal currentAmount = rs.getBigDecimal("current_amount");
                BigDecimal targetAmount = rs.getBigDecimal("target_amount");
                
                if (currentAmount.compareTo(targetAmount) >= 0) {
                    // Goal is completed, update status
                    String updateSql = "UPDATE savings_goals SET status = ? WHERE goal_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, SavingsGoal.Status.COMPLETED.toString());
                    updateStmt.setInt(2, goalId);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    /**
     * Map a ResultSet to a SavingsGoal object
     * 
     * @param rs ResultSet to map
     * @return SavingsGoal object
     * @throws SQLException if an error occurs during the mapping
     */
    private SavingsGoal mapResultSetToSavingsGoal(ResultSet rs) throws SQLException {
        SavingsGoal savingsGoal = new SavingsGoal();
        savingsGoal.setGoalId(rs.getInt("goal_id"));
        savingsGoal.setUserId(rs.getInt("user_id"));
        savingsGoal.setAccountId(rs.getInt("account_id"));
        savingsGoal.setName(rs.getString("name"));
        savingsGoal.setTargetAmount(rs.getBigDecimal("target_amount"));
        savingsGoal.setCurrentAmount(rs.getBigDecimal("current_amount"));
        savingsGoal.setStartDate(rs.getDate("start_date"));
        savingsGoal.setTargetDate(rs.getDate("target_date"));
        savingsGoal.setStatus(SavingsGoal.Status.fromString(rs.getString("status")));
        savingsGoal.setIcon(rs.getString("icon"));
        savingsGoal.setColor(rs.getString("color"));
        savingsGoal.setCreatedAt(rs.getTimestamp("created_at"));
        savingsGoal.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return savingsGoal;
    }
}
