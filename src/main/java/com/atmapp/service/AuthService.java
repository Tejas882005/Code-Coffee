package com.atmapp.service;

import java.sql.SQLException;

import com.atmapp.dao.UserDAO;
import com.atmapp.exception.AuthenticationException;
import com.atmapp.model.User;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(int customerNumber, int pin) throws AuthenticationException, SQLException {
        User user = userDAO.findByCustomerNumber(customerNumber);
        if (user == null || user.getPin() != pin) {
            throw new AuthenticationException("Invalid customer number or PIN");
        }
        return user;
    }
}