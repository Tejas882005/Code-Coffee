package com.atmapp.ui;

import java.sql.SQLException;

import com.atmapp.dao.UserDAO;
import com.atmapp.exception.AuthenticationException;
import com.atmapp.model.User;
import com.atmapp.service.AuthService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUI {
    private final Stage stage;
    private final AuthService authService;

    public LoginUI(Stage stage) throws SQLException {
        this.stage = stage;
        this.authService = new AuthService(new UserDAO());
    }

    public void show() {
        Text title = new Text("Login to Horizon Bank ATM");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField customerNumberField = new TextField();
        customerNumberField.setPromptText("Customer Number");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #003087; -fx-text-fill: white; -fx-font-size: 14px;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            try {
                int customerNumber = Integer.parseInt(customerNumberField.getText());
                int pin = Integer.parseInt(pinField.getText());
                User user = authService.authenticate(customerNumber, pin);
                new TransactionUI(stage, user).show();
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter valid numbers");
            } catch (AuthenticationException | SQLException ex) {
                messageLabel.setText(ex.getMessage());
            }
        });

        VBox layout = new VBox(15, title, customerNumberField, pinField, loginButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #e6f0fa;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}