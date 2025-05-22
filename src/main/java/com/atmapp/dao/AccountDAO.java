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

   
    
}