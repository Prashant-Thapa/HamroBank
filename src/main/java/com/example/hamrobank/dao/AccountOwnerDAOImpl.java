package com.example.hamrobank.dao;

import com.example.hamrobank.model.AccountOwner;
import com.example.hamrobank.model.User;
import com.example.hamrobank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the AccountOwnerDAO interface
 */
public class AccountOwnerDAOImpl implements AccountOwnerDAO {

    private static final Logger LOGGER = Logger.getLogger(AccountOwnerDAOImpl.class.getName());

    @Override
    public boolean addAccountOwner(AccountOwner accountOwner) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Check if the owner already exists
            if (isAccountOwner(accountOwner.getAccountId(), accountOwner.getUserId())) {
                return false;
            }

            String sql = "INSERT INTO account_owners (account_id, user_id, permission_level, is_primary) " +
                         "VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountOwner.getAccountId());
            stmt.setInt(2, accountOwner.getUserId());
            stmt.setString(3, accountOwner.getPermissionLevel());
            stmt.setBoolean(4, accountOwner.isPrimary());

            int affectedRows = stmt.executeUpdate();

            // If this is the primary owner, ensure no other primary owners exist
            if (accountOwner.isPrimary()) {
                ensureSinglePrimaryOwner(conn, accountOwner.getAccountId(), accountOwner.getUserId());
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding account owner", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public List<AccountOwner> getAccountOwnersByAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM account_owners WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            List<AccountOwner> accountOwners = new ArrayList<>();
            while (rs.next()) {
                accountOwners.add(mapResultSetToAccountOwner(rs));
            }

            return accountOwners;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting account owners by account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<AccountOwner> getAccountOwnersByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM account_owners WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            List<AccountOwner> accountOwners = new ArrayList<>();
            while (rs.next()) {
                accountOwners.add(mapResultSetToAccountOwner(rs));
            }

            return accountOwners;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting account owners by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public AccountOwner getPrimaryOwner(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM account_owners WHERE account_id = ? AND is_primary = TRUE";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccountOwner(rs);
            }

            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting primary owner", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean isAccountOwner(int accountId, int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM account_owners WHERE account_id = ? AND user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, userId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user is account owner", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updatePermissionLevel(int accountId, int userId, String permissionLevel) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE account_owners SET permission_level = ? WHERE account_id = ? AND user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, permissionLevel);
            stmt.setInt(2, accountId);
            stmt.setInt(3, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating permission level", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean setPrimaryOwner(int accountId, int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Check if the user is an account owner
            if (!isAccountOwner(accountId, userId)) {
                return false;
            }

            // Set the user as primary owner
            String sql = "UPDATE account_owners SET is_primary = TRUE WHERE account_id = ? AND user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();

            // Ensure no other primary owners exist
            if (affectedRows > 0) {
                ensureSinglePrimaryOwner(conn, accountId, userId);
                return true;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error setting primary owner", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean removeAccountOwner(int accountId, int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Check if the user is the primary owner
            AccountOwner primaryOwner = getPrimaryOwner(accountId);
            if (primaryOwner != null && primaryOwner.getUserId() == userId) {
                // Cannot remove the primary owner
                return false;
            }

            String sql = "DELETE FROM account_owners WHERE account_id = ? AND user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing account owner", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public List<User> getUsersByAccountId(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT u.* FROM users u " +
                         "JOIN account_owners ao ON u.user_id = ao.user_id " +
                         "WHERE ao.account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                user.setProfilePicture(rs.getString("profile_picture"));
                user.setActive(rs.getBoolean("is_active"));
                user.setCreatedAt(rs.getTimestamp("created_at"));

                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by account ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    /**
     * Ensure that only one primary owner exists for an account
     *
     * @param conn Database connection
     * @param accountId ID of the account
     * @param primaryUserId ID of the user who should be the primary owner
     * @throws SQLException if an error occurs during the operation
     */
    private void ensureSinglePrimaryOwner(Connection conn, int accountId, int primaryUserId) throws SQLException {
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE account_owners SET is_primary = FALSE " +
                         "WHERE account_id = ? AND user_id != ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, primaryUserId);

            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Map a ResultSet to an AccountOwner object
     *
     * @param rs ResultSet to map
     * @return AccountOwner object
     * @throws SQLException if an error occurs during the mapping
     */
    private AccountOwner mapResultSetToAccountOwner(ResultSet rs) throws SQLException {
        AccountOwner accountOwner = new AccountOwner();
        accountOwner.setAccountId(rs.getInt("account_id"));
        accountOwner.setUserId(rs.getInt("user_id"));
        accountOwner.setPermissionLevel(rs.getString("permission_level"));
        accountOwner.setPrimary(rs.getBoolean("is_primary"));
        accountOwner.setCreatedAt(rs.getTimestamp("created_at"));

        return accountOwner;
    }
}
