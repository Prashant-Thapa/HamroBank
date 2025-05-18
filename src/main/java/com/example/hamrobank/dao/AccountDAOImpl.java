package com.example.hamrobank.dao;

import com.example.hamrobank.model.Account;
import com.example.hamrobank.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of AccountDAO interface
 */
public class AccountDAOImpl implements AccountDAO {
    private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());

    @Override
    public Account createAccount(Account account) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO accounts (user_id, account_number, account_type, balance, status) " +
                         "VALUES (?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, account.getUserId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setString(3, account.getAccountType().toString());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getStatus().toString());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                account.setAccountId(rs.getInt(1));
            } else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }

            return account;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating account", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Account getAccountById(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting account by ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts WHERE account_number = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting account by account number", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY account_id";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

            return accounts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting accounts by user ID", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateAccount(Account account) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE accounts SET user_id = ?, account_type = ?, " +
                         "balance = ?, status = ? WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, account.getUserId());
            stmt.setString(2, account.getAccountType().toString());
            stmt.setBigDecimal(3, account.getBalance());
            stmt.setString(4, account.getStatus().toString());
            stmt.setInt(5, account.getAccountId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating account", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean closeAccount(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Account.AccountStatus.CLOSED.toString());
            stmt.setInt(2, accountId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing account", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public List<Account> getAllAccounts() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts ORDER BY account_id";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

            return accounts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all accounts", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateBalance(int accountId, BigDecimal newBalance) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating account balance", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public boolean updateStatus(int accountId, Account.AccountStatus status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.toString());
            stmt.setInt(2, accountId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating account status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public List<Account> getAccountsByStatus(String status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts WHERE status = ? ORDER BY account_id";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);

            rs = stmt.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

            return accounts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting accounts by status", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Account> getAccountsByType(String accountType) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM accounts WHERE account_type = ? ORDER BY account_id";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountType);

            rs = stmt.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

            return accounts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting accounts by type", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean deleteAccount(int accountId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM accounts WHERE account_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting account", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt);
        }
    }

    @Override
    public String generateAccountNumber() throws Exception {
        Random random = new Random();
        String accountNumber;

        do {
            // Generate a 10-digit account number starting with 1
            StringBuilder sb = new StringBuilder("1");
            for (int i = 0; i < 9; i++) {
                sb.append(random.nextInt(10));
            }
            accountNumber = sb.toString();
        } while (accountNumberExists(accountNumber));

        return accountNumber;
    }

    @Override
    public boolean accountNumberExists(String accountNumber) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if account number exists", e);
            throw e;
        } finally {
            DatabaseUtil.closeResources(conn, stmt, rs);
        }
    }

    /**
     * Map a ResultSet to an Account object
     *
     * @param rs ResultSet containing account data
     * @return Account object
     * @throws SQLException if an error occurs during the mapping
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(Account.AccountStatus.valueOf(rs.getString("status")));
        account.setCreatedAt(rs.getTimestamp("created_at"));
        account.setUpdatedAt(rs.getTimestamp("updated_at"));
        return account;
    }
}
