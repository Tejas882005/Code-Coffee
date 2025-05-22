package com.atmapp.dao;

import com.atmapp.model.Account;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    
}