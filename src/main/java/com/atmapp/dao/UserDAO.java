package com.atmapp.dao;

import com.atmapp.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DBConnection.getInstance();
    }

    public User findByCustomerNumberAndPin(int customerNumber, int pin) throws SQLException {
        String query = "SELECT * FROM users WHERE customer_number = ? AND pin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerNumber);
            stmt.setInt(2, pin);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getInt("customer_number"), rs.getInt("pin"));
                }
            }
        }
        return null; 
    }
}