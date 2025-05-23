package com.atmapp.ui;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.atmapp.dao.AccountDAO;
import com.atmapp.model.Account;
import com.atmapp.model.User;
import com.atmapp.service.TransactionService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransactionUI {
    private final Stage stage;
    private final User user;
    private final TransactionService transactionService;
    private Account account;

    public TransactionUI(Stage stage, User user) throws SQLException {
        this.stage = stage;
        this.user = user;
        this.transactionService = new TransactionService(new AccountDAO());
        this.account = new AccountDAO().loadByUserId(user.getUserId());
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, User #" + user.getCustomerNumber());
        Label balanceLabel = new Label("Checking: $" + account.getCheckingBalance() +
                                      " | Savings: $" + account.getSavingsBalance());

        Button depositCheckingButton = new Button("Deposit to Checking");
        Button withdrawCheckingButton = new Button("Withdraw from Checking");
        Button depositSavingsButton = new Button("Deposit to Savings");
        Button withdrawSavingsButton = new Button("Withdraw from Savings");
        Button logoutButton = new Button("Logout");

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        depositCheckingButton.setOnAction(event -> handleTransaction(amountField, messageLabel, "depositChecking"));
        withdrawCheckingButton.setOnAction(event -> handleTransaction(amountField, messageLabel, "withdrawChecking"));
        depositSavingsButton.setOnAction(event -> handleTransaction(amountField, messageLabel, "depositSavings"));
        withdrawSavingsButton.setOnAction(event -> handleTransaction(amountField, messageLabel, "withdrawSavings"));
        logoutButton.setOnAction(event -> {
            try {
                new LoginUI(stage).show();
            } catch (SQLException e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
        });

        layout.getChildren().addAll(welcomeLabel, balanceLabel, amountField, depositCheckingButton,
                withdrawCheckingButton, depositSavingsButton, withdrawSavingsButton, logoutButton, messageLabel);

        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("ATM Transactions");
        stage.setScene(scene);
        stage.show();
    }

    private void handleTransaction(TextField amountField, Label messageLabel, String transactionType) {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            switch (transactionType) {
                case "depositChecking" -> transactionService.depositChecking(user, amount);
                case "withdrawChecking" -> transactionService.withdrawChecking(user, amount);
                case "depositSavings" -> transactionService.depositSavings(user, amount);
                case "withdrawSavings" -> transactionService.withdrawSavings(user, amount);
            }
            account = new AccountDAO().loadByUserId(user.getUserId());
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Transaction successful! New Checking: $" + account.getCheckingBalance() +
                                " | Savings: $" + account.getSavingsBalance());
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid amount");
        } catch (SQLException | com.atmapp.exception.InsufficientFundsException e) {
            messageLabel.setText(e.getMessage());
        }
    }
}