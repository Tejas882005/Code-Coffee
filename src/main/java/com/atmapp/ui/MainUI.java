package com.atmapp.ui;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Text welcomeText = new Text("Welcome to Horizon Bank ATM");
        welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #003087; -fx-text-fill: white; -fx-font-size: 16px;");
        loginButton.setOnAction(e -> {
            try {
                new LoginUI(primaryStage).show();
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to load login screen", ex);
            }
        });

        VBox layout = new VBox(20, welcomeText, loginButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #e6f0fa; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Horizon Bank ATM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}