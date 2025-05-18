package com.example.hamrobank.dao;

import com.example.hamrobank.model.Budget;
import com.example.hamrobank.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the BudgetDAO interface
 */
public class BudgetDAOImpl implements BudgetDAO {
    
    private static final Logger LOGGER = Logger.getLogger(BudgetDAOImpl.class.getName());
    
    @Override
    public Budget createBudget(Budget budget) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO budgets (user_id, name, amount, period, category_id, start_date, end_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, budget.getUserId());
            stmt.setString(2, budget.getName());
            stmt.setBigDecimal(3, budget.getAmount());
            stmt.setString(4, budget.getPeriod());
            
            if (budget.getCategoryId() != null) {
                stmt.setInt(5, budget.getCategoryId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setDate(6, budget.getStartDate());
            
            if (budget.getEndDate() != null) {
                stmt.setDate(7, budget.getEndDate());
            } else {
                stmt.setNull(7, Types.DATE);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating budget failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                budget.setBudgetId(rs.getInt(1));
            } else {
                throw new SQLException("Creating budget failed, no ID obtained.");
            }
            
            return budget;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating budget", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public Budget getBudgetById(int budgetId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM budgets WHERE budget_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, budgetId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBudget(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting budget by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Budget> getBudgetsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM budgets WHERE user_id = ? ORDER BY start_date DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<Budget> budgets = new ArrayList<>();
            while (rs.next()) {
                budgets.add(mapResultSetToBudget(rs));
            }
            
            return budgets;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting budgets by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Budget> getBudgetsByCategory(int userId, int categoryId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM budgets WHERE user_id = ? AND category_id = ? ORDER BY start_date DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            
            rs = stmt.executeQuery();
            
            List<Budget> budgets = new ArrayList<>();
            while (rs.next()) {
                budgets.add(mapResultSetToBudget(rs));
            }
            
            return budgets;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting budgets by category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Budget> getActiveBudgets(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM budgets WHERE user_id = ? AND start_date <= CURRENT_DATE() " +
                         "AND (end_date IS NULL OR end_date >= CURRENT_DATE()) ORDER BY start_date DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<Budget> budgets = new ArrayList<>();
            while (rs.next()) {
                budgets.add(mapResultSetToBudget(rs));
            }
            
            return budgets;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active budgets", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateBudget(Budget budget) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE budgets SET name = ?, amount = ?, period = ?, category_id = ?, " +
                         "start_date = ?, end_date = ? WHERE budget_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, budget.getName());
            stmt.setBigDecimal(2, budget.getAmount());
            stmt.setString(3, budget.getPeriod());
            
            if (budget.getCategoryId() != null) {
                stmt.setInt(4, budget.getCategoryId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setDate(5, budget.getStartDate());
            
            if (budget.getEndDate() != null) {
                stmt.setDate(6, budget.getEndDate());
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.setInt(7, budget.getBudgetId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating budget", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteBudget(int budgetId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM budgets WHERE budget_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, budgetId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting budget", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public Map<Integer, BigDecimal> getSpendingByCategory(int userId, Date startDate, Date endDate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT t.category_id, SUM(t.amount) as total_amount " +
                         "FROM transactions t " +
                         "JOIN accounts a ON t.source_account_id = a.account_id " +
                         "WHERE a.user_id = ? AND t.transaction_date BETWEEN ? AND ? " +
                         "AND t.category_id IS NOT NULL " +
                         "GROUP BY t.category_id";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            
            rs = stmt.executeQuery();
            
            Map<Integer, BigDecimal> spendingByCategory = new HashMap<>();
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                BigDecimal amount = rs.getBigDecimal("total_amount");
                spendingByCategory.put(categoryId, amount);
            }
            
            return spendingByCategory;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting spending by category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public Map<Integer, BigDecimal[]> getBudgetVsActual(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Get current date for period calculations
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfMonth = currentDate.withDayOfMonth(1);
            Date sqlStartOfMonth = Date.valueOf(startOfMonth);
            Date sqlCurrentDate = Date.valueOf(currentDate);
            
            // Get active budgets
            List<Budget> activeBudgets = getActiveBudgets(userId);
            
            // Get spending by category for the current month
            Map<Integer, BigDecimal> spendingByCategory = getSpendingByCategory(userId, sqlStartOfMonth, sqlCurrentDate);
            
            // Calculate budget vs actual
            Map<Integer, BigDecimal[]> budgetVsActual = new HashMap<>();
            
            for (Budget budget : activeBudgets) {
                int budgetId = budget.getBudgetId();
                BigDecimal budgetAmount = budget.getAmount();
                
                // Get actual spending for this budget's category
                BigDecimal actualSpending = BigDecimal.ZERO;
                if (budget.getCategoryId() != null && spendingByCategory.containsKey(budget.getCategoryId())) {
                    actualSpending = spendingByCategory.get(budget.getCategoryId());
                }
                
                // Store budget amount and actual spending
                budgetVsActual.put(budgetId, new BigDecimal[]{budgetAmount, actualSpending});
            }
            
            return budgetVsActual;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting budget vs actual", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Map a ResultSet to a Budget object
     * 
     * @param rs ResultSet to map
     * @return Budget object
     * @throws SQLException if an error occurs during the mapping
     */
    private Budget mapResultSetToBudget(ResultSet rs) throws SQLException {
        Budget budget = new Budget();
        budget.setBudgetId(rs.getInt("budget_id"));
        budget.setUserId(rs.getInt("user_id"));
        budget.setName(rs.getString("name"));
        budget.setAmount(rs.getBigDecimal("amount"));
        budget.setPeriod(rs.getString("period"));
        
        int categoryId = rs.getInt("category_id");
        if (!rs.wasNull()) {
            budget.setCategoryId(categoryId);
        }
        
        budget.setStartDate(rs.getDate("start_date"));
        budget.setEndDate(rs.getDate("end_date"));
        budget.setCreatedAt(rs.getTimestamp("created_at"));
        budget.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return budget;
    }
}
