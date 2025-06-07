package com.atmapp.dao;

import com.atmapp.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public Account loadByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("checking_balance"),
                    rs.getBigDecimal("savings_balance")
                );
            }
            return null;
        }
    }

    public void update(Account account) throws SQLException {
        String sql = "UPDATE accounts SET checking_balance = ?, savings_balance = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, account.getCheckingBalance());
            stmt.setBigDecimal(2, account.getSavingsBalance());
            stmt.setInt(3, account.getAccountId());
            stmt.executeUpdate();
        }
    }
}