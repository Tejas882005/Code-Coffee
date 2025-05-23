package com.atmapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.atmapp.model.Account;

public class AccountDAO {

    private Connection connection;

    public AccountDAO() throws SQLException {
        this.connection = DBConnection.getInstance();
    }

    public Account loadByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getInt("account_number"),
                            rs.getInt("user_id"),
                            rs.getBigDecimal("checking_balance"),
                            rs.getBigDecimal("savings_balance")
                    );
                }
            }
        }
        return null;
    }

    public void updateAccount(Account account) throws SQLException {
        String query = "UPDATE accounts SET checking_balance = ?, savings_balance = ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBigDecimal(1, account.getCheckingBalance());
            stmt.setBigDecimal(2, account.getSavingsBalance());
            stmt.setInt(3, account.getAccountNumber());
            stmt.executeUpdate();
        }
    }

}
