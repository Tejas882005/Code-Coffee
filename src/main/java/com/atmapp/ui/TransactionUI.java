package com.atmapp.ui;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.atmapp.dao.AccountDAO;
import com.atmapp.dao.TransactionDAO;
import com.atmapp.exception.InsufficientFundsException;
import com.atmapp.model.Account;
import com.atmapp.model.Transaction;
import com.atmapp.model.User;
import com.atmapp.service.TransactionService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        this.transactionService = new TransactionService(new AccountDAO(), new TransactionDAO());
        this.account = new AccountDAO().loadByUserId(user.getUserId());
    }

    public void show() {
        Text title = new Text("Account Transactions");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Text welcomeLabel = new Text("Welcome, User #" + user.getCustomerNumber());
        welcomeLabel.setStyle("-fx-font-size: 16px;");

        balanceLabel = new Label("Checking: $" + account.getCheckingBalance() +
                " | Savings: $" + account.getSavingsBalance());
        balanceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

        Button depositCheckingButton = new Button("Deposit to Checking");
        Button withdrawCheckingButton = new Button("Withdraw from Checking");
        Button depositSavingsButton = new Button("Deposit to Savings");
        Button withdrawSavingsButton = new Button("Withdraw from Savings");
        Button transferToSavingsButton = new Button("Transfer to Savings");
        Button transferToCheckingButton = new Button("Transfer to Checking");
        Button historyButton = new Button("View History");
        Button logoutButton = new Button("Logout");

        depositCheckingButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        withdrawCheckingButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        depositSavingsButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        withdrawSavingsButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        transferToSavingsButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        transferToCheckingButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        historyButton.setStyle("-fx-background-color: #005566; -fx-text-fill: white;");
        logoutButton.setStyle("-fx-background-color: #003087; -fx-text-fill: white;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        depositCheckingButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "depositChecking"));
        withdrawCheckingButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "withdrawChecking"));
        depositSavingsButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "depositSavings"));
        withdrawSavingsButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "withdrawSavings"));
        transferToSavingsButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "transferToSavings"));
        transferToCheckingButton.setOnAction(e -> handleTransaction(amountField, messageLabel, "transferToChecking"));
        historyButton.setOnAction(e -> showTransactionHistory(messageLabel));
        logoutButton.setOnAction(e -> {
            try {
                new LoginUI(stage).show();
            } catch (SQLException ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        HBox checkingButtons = new HBox(10, depositCheckingButton, withdrawCheckingButton);
        HBox savingsButtons = new HBox(10, depositSavingsButton, withdrawSavingsButton);
        HBox transferButtons = new HBox(10, transferToSavingsButton, transferToCheckingButton);
        checkingButtons.setAlignment(Pos.CENTER);
        savingsButtons.setAlignment(Pos.CENTER);
        transferButtons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, title, welcomeLabel, balanceLabel, amountField, checkingButtons, savingsButtons,
                transferButtons, historyButton, logoutButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #e6f0fa;");

        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("ATM Transactions");
        stage.setScene(scene);
        stage.show();
    }

    private void handleTransaction(TextField amountField, Label messageLabel, String transactionType) {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            switch (transactionType) {
                case "depositChecking" -> transactionService.depositChecking(user.getUserId(), amount);
                case "withdrawChecking" -> transactionService.withdrawChecking(user.getUserId(), amount);
                case "depositSavings" -> transactionService.depositSavings(user.getUserId(), amount);
                case "withdrawSavings" -> transactionService.withdrawSavings(user.getUserId(), amount);
                case "transferToSavings" -> transactionService.transfer(user.getUserId(), amount, true);
                case "transferToChecking" -> transactionService.transfer(user.getUserId(), amount, false);
            }
            account = new AccountDAO().loadByUserId(user.getUserId());
            balanceLabel.setText("Checking: $" + account.getCheckingBalance() +
                    " | Savings: $" + account.getSavingsBalance());
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Transaction successful!");
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Invalid amount");
        } catch (IllegalArgumentException | InsufficientFundsException | SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText(e.getMessage());
        }
    }

    private void showTransactionHistory(Label messageLabel) {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(user.getUserId());
            StringBuilder history = new StringBuilder("Recent Transactions:\n");
            for (Transaction t : transactions) {
                history.append(String.format("%s: %s $%.2f at %s\n",
                        t.getType(), t.getAmount().compareTo(BigDecimal.ZERO) > 0 ? "+" : "",
                        t.getAmount().abs(), t.getTimestamp()));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transaction History");
            alert.setHeaderText(null);
            alert.setContentText(history.toString());
            alert.showAndWait();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Error fetching history: " + e.getMessage());
        }
    }
}