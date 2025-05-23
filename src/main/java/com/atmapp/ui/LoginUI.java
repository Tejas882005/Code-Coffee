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
import javafx.scene.layout.GridPane;
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
        Text title = new Text("Login to Your Account");
        title.getStyleClass().add("title-text");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        Label customerNumberLabel = new Label("Customer Number:");
        customerNumberLabel.getStyleClass().add("label");
        TextField customerNumberField = new TextField();
        customerNumberField.getStyleClass().add("input-field");
        grid.add(customerNumberLabel, 0, 0);
        grid.add(customerNumberField, 1, 0);

        Label pinLabel = new Label("PIN:");
        pinLabel.getStyleClass().add("label");
        PasswordField pinField = new PasswordField();
        pinField.getStyleClass().add("input-field");
        grid.add(pinLabel, 0, 1);
        grid.add(pinField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");
        grid.add(loginButton, 1, 2);

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
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

        VBox mainLayout = new VBox(20, title, grid);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getStyleClass().add("login-pane");

        Scene scene = new Scene(mainLayout, 600, 400);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("ATM Login");
        stage.setScene(scene);
        stage.show();
    }
}