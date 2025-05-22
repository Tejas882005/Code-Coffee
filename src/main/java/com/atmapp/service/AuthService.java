package com.atmapp.service;

import com.atmapp.dao.UserDAO;
import com.atmapp.exception.AuthenticationException;
import com.atmapp.model.User;
import java.sql.SQLException;


public class AuthService {
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public User authenticate(int customerNumber, int pin) throws AuthenticationException {
        try {
            User user = userDAO.findByCustomerNumberAndPin(customerNumber, pin);
            if (user != null) {
                return user;
            } else {
                throw new AuthenticationException("Invalid customer number or PIN");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during authentication", e);
        }
    }
}