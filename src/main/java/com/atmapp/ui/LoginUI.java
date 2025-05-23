package com.atmapp.ui;

import com.atmapp.dao.UserDAO;
import com.atmapp.service.AuthService;
import com.atmapp.model.User;
import com.atmapp.exception.AuthenticationException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginUI {
    private final Stage stage;
    private final AuthService authService;

    public LoginUI(Stage stage) throws SQLException {
        this.stage = stage;
        this.authService = new AuthService(new UserDAO());
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label customerNumberLabel = new Label("Customer Number:");
        TextField customerNumberField = new TextField();
        grid.add(customerNumberLabel, 0, 0);
        grid.add(customerNumberField, 1, 0);

        Label pinLabel = new Label("PIN:");
        PasswordField pinField = new PasswordField();
        grid.add(pinLabel, 0, 1);
        grid.add(pinField, 1, 1);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 2);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 3, 2, 1);

        loginButton.setOnAction(event -> {
            try {
                int customerNumber = Integer.parseInt(customerNumberField.getText());
                int pin = Integer.parseInt(pinField.getText());
                User user = authService.authenticate(customerNumber, pin);
               
                TransactionUI transactionUI = new TransactionUI(stage, user);
                transactionUI.show();
            } catch (NumberFormatException e) {
                errorLabel.setText("Please enter valid numbers");
            } catch (AuthenticationException e) {
                errorLabel.setText(e.getMessage());
            } catch (SQLException e) {
                errorLabel.setText("Database error: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("ATM Login");
        stage.setScene(scene);
        stage.show();
    }
}