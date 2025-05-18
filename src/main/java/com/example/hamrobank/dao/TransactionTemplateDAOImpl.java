package com.example.hamrobank.dao;

import com.example.hamrobank.model.TransactionTemplate;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the TransactionTemplateDAO interface
 */
public class TransactionTemplateDAOImpl implements TransactionTemplateDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionTemplateDAOImpl.class.getName());
    
    @Override
    public TransactionTemplate createTemplate(TransactionTemplate template) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO transaction_templates (user_id, name, source_account_id, destination_account_id, " +
                         "amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, template.getUserId());
            stmt.setString(2, template.getName());
            stmt.setInt(3, template.getSourceAccountId());
            stmt.setInt(4, template.getDestinationAccountId());
            stmt.setBigDecimal(5, template.getAmount());
            stmt.setString(6, template.getDescription());
            
            if (template.getCategoryId() != null) {
                stmt.setInt(7, template.getCategoryId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction template failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                template.setTemplateId(rs.getInt(1));
            } else {
                throw new SQLException("Creating transaction template failed, no ID obtained.");
            }
            
            return template;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction template", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public TransactionTemplate getTemplateById(int templateId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_templates WHERE template_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, templateId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTemplate(rs);
            }
            
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction template by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<TransactionTemplate> getTemplatesByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_templates WHERE user_id = ? ORDER BY name";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            List<TransactionTemplate> templates = new ArrayList<>();
            while (rs.next()) {
                templates.add(mapResultSetToTemplate(rs));
            }
            
            return templates;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction templates by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<TransactionTemplate> getTemplatesBySourceAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM transaction_templates WHERE source_account_id = ? ORDER BY name";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            
            rs = stmt.executeQuery();
            
            List<TransactionTemplate> templates = new ArrayList<>();
            while (rs.next()) {
                templates.add(mapResultSetToTemplate(rs));
            }
            
            return templates;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction templates by source account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public boolean updateTemplate(TransactionTemplate template) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE transaction_templates SET name = ?, source_account_id = ?, destination_account_id = ?, " +
                         "amount = ?, description = ?, category_id = ? WHERE template_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, template.getName());
            stmt.setInt(2, template.getSourceAccountId());
            stmt.setInt(3, template.getDestinationAccountId());
            stmt.setBigDecimal(4, template.getAmount());
            stmt.setString(5, template.getDescription());
            
            if (template.getCategoryId() != null) {
                stmt.setInt(6, template.getCategoryId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setInt(7, template.getTemplateId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction template", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    @Override
    public boolean deleteTemplate(int templateId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM transaction_templates WHERE template_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, templateId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction template", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }
    
    /**
     * Map a ResultSet to a TransactionTemplate object
     * 
     * @param rs ResultSet to map
     * @return TransactionTemplate object
     * @throws SQLException if an error occurs during the mapping
     */
    private TransactionTemplate mapResultSetToTemplate(ResultSet rs) throws SQLException {
        TransactionTemplate template = new TransactionTemplate();
        template.setTemplateId(rs.getInt("template_id"));
        template.setUserId(rs.getInt("user_id"));
        template.setName(rs.getString("name"));
        template.setSourceAccountId(rs.getInt("source_account_id"));
        template.setDestinationAccountId(rs.getInt("destination_account_id"));
        template.setAmount(rs.getBigDecimal("amount"));
        template.setDescription(rs.getString("description"));
        
        int categoryId = rs.getInt("category_id");
        if (!rs.wasNull()) {
            template.setCategoryId(categoryId);
        }
        
        template.setCreatedAt(rs.getTimestamp("created_at"));
        template.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return template;
    }
}
