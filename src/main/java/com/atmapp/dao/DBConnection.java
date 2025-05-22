package com.atmapp.dao;

import com.atmapp.util.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {

    }

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            ConfigLoader config = ConfigLoader.getInstance();
            String url = config.getProperty("db.url");
            String username = config.getProperty("db.username");
            String password = config.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
}