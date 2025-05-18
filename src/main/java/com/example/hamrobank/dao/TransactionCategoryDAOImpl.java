package com.example.hamrobank.dao;

import com.example.hamrobank.model.TransactionCategory;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the TransactionCategoryDAO interface
 */
public class TransactionCategoryDAOImpl implements TransactionCategoryDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionCategoryDAOImpl.class.getName());
    
    @Override
    public TransactionCategory createCategory(TransactionCategory category) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO transaction_categories (name, description, icon, color, parent_category_id, is_system) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getIcon());
            stmt.setString(4, category.getColor());
            
            if (category.getParentCategoryId() != null) {
                stmt.setInt(5, category.getParentCategoryId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setBoolean(6, category.isSystem());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                category.setCategoryId(rs.getInt(1));
            } else {
                throw new SQLException("Creating category failed, no ID obtained.");
            }
            
            return category;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public TransactionCategory getCategoryById(int categoryId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_categories WHERE category_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCategory(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction category by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<TransactionCategory> getAllCategories() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_categories ORDER BY name";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<TransactionCategory> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
            return categories;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transaction categories", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<TransactionCategory> getSystemCategories() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_categories WHERE is_system = TRUE ORDER BY name";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<TransactionCategory> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
            return categories;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting system transaction categories", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<TransactionCategory> getUserCategories() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_categories WHERE is_system = FALSE ORDER BY name";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<TransactionCategory> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
            return categories;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user transaction categories", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateCategory(TransactionCategory category) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE transaction_categories SET name = ?, description = ?, icon = ?, " +
                         "color = ?, parent_category_id = ? WHERE category_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getIcon());
            stmt.setString(4, category.getColor());
            
            if (category.getParentCategoryId() != null) {
                stmt.setInt(5, category.getParentCategoryId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setInt(6, category.getCategoryId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteCategory(int categoryId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // First, check if this is a system category
            String checkSql = "SELECT is_system FROM transaction_categories WHERE category_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, categoryId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getBoolean("is_system")) {
                LOGGER.log(Level.WARNING, "Cannot delete system category with ID: " + categoryId);
                return false;
            }
            
            // Update transactions that use this category to use null category
            String updateSql = "UPDATE transactions SET category_id = NULL WHERE category_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, categoryId);
            updateStmt.executeUpdate();
            
            // Now delete the category
            String sql = "DELETE FROM transaction_categories WHERE category_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction category", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    /**
     * Map a ResultSet to a TransactionCategory object
     * 
     * @param rs ResultSet to map
     * @return TransactionCategory object
     * @throws SQLException if an error occurs during the mapping
     */
    private TransactionCategory mapResultSetToCategory(ResultSet rs) throws SQLException {
        TransactionCategory category = new TransactionCategory();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setIcon(rs.getString("icon"));
        category.setColor(rs.getString("color"));
        
        int parentCategoryId = rs.getInt("parent_category_id");
        if (!rs.wasNull()) {
            category.setParentCategoryId(parentCategoryId);
        }
        
        category.setSystem(rs.getBoolean("is_system"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        
        return category;
    }
}
