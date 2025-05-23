package com.atmapp.ui;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            LoginUI loginUI = new LoginUI(primaryStage);
            loginUI.show();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}