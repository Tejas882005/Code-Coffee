package com.atmapp.ui;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.atmapp.dao.AccountDAO;
import com.atmapp.exception.InsufficientFundsException;
import com.atmapp.model.Account;
import com.atmapp.model.User;
import com.atmapp.service.TransactionService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransactionUI {

    private final Stage stage;
    private final User user;
    private final TransactionService transactionService;
    private Account account;
    private Label balanceLabel;

    public TransactionUI(Stage stage, User user) throws SQLException {
        this.stage = stage;
        this.user = user;
        this.transactionService = new TransactionService(new AccountDAO());
        this.account = new AccountDAO().loadByUserId(user.getUserId());
    }

    public void show() {
        Text title = new Text("Account Transactions");
        title.getStyleClass().add("title-text");

        Text welcomeLabel = new Text("Welcome, User #" + user.getCustomerNumber());
        welcomeLabel.getStyleClass().add("welcome-text");
        balanceLabel = new Label("Checking: $" + account.getCheckingBalance()
                + " | Savings: $" + account.getSavingsBalance());
        balanceLabel.getStyleClass().add("balance-label");

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.getStyleClass().add("input-field");

        Button depositCheckingButton = new Button("Deposit to Checking");
        Button withdrawCheckingButton = new Button("Withdraw from Checking");
        Button depositSavingsButton = new Button("Deposit to Savings");
        Button withdrawSavingsButton = new Button("Withdraw from Savings");
        Button logoutButton = new Button("Logout");

        depositCheckingButton.getStyleClass().add("transaction-button");
        withdrawCheckingButton.getStyleClass().add("transaction-button");
        depositSavingsButton.getStyleClass().add("transaction-button");
        withdrawSavingsButton.getStyleClass().add("transaction-button");
        logoutButton.getStyleClass().add("secondary-button");

        HBox checkingButtons = new HBox(10, depositCheckingButton, withdrawCheckingButton);
        checkingButtons.setAlignment(Pos.CENTER);
        HBox savingsButtons = new HBox(10, depositSavingsButton, withdrawSavingsButton);
        savingsButtons.setAlignment(Pos.CENTER);

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

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

        VBox layout = new VBox(15, title, welcomeLabel, balanceLabel, amountField, checkingButtons, savingsButtons, logoutButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("transaction-pane");
        layout.setStyle("-fx-background-color:rgb(183, 160, 216);"); 
        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("ATM Transactions");
        stage.setScene(scene);
        stage.show();
    }

    private void handleTransaction(TextField amountField, Label messageLabel, String transactionType) {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            switch (transactionType) {
                case "depositChecking" ->
                    transactionService.depositChecking(user, amount);
                case "withdrawChecking" ->
                    transactionService.withdrawChecking(user, amount);
                case "depositSavings" ->
                    transactionService.depositSavings(user, amount);
                case "withdrawSavings" ->
                    transactionService.withdrawSavings(user, amount);
            }
            account = new AccountDAO().loadByUserId(user.getUserId());
            balanceLabel.setText("Checking: $" + account.getCheckingBalance()
                    + " | Savings: $" + account.getSavingsBalance());
            messageLabel.getStyleClass().remove("warning-label");
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Transaction successful!");
        } catch (NumberFormatException e) {
            messageLabel.getStyleClass().remove("warning-label");
            messageLabel.setText("Invalid amount");
        } catch (InsufficientFundsException e) {
            messageLabel.getStyleClass().add("warning-label");
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Warning: " + e.getMessage());
        } catch (SQLException e) {
            messageLabel.getStyleClass().remove("warning-label");
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }
}
