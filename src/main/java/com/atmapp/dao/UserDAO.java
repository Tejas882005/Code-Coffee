package com.atmapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.atmapp.model.User;

public class UserDAO {
    public User findByCustomerNumber(int customerNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE customer_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getInt("customer_number"), rs.getInt("pin"));
            }
            return null;
        }
    }
}