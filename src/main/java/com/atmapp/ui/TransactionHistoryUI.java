package com.atmapp.ui;

import java.sql.SQLException;
import java.text.DecimalFormat;

import com.atmapp.dao.AccountDAO;
import com.atmapp.dao.TransactionDAO;
import com.atmapp.model.Transaction;
import com.atmapp.model.User;
import com.atmapp.service.TransactionService;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransactionHistoryUI {

    private final Stage stage;
    private final User user;
    private final TransactionService transactionService;
    private final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

    public TransactionHistoryUI(Stage stage, User user) throws SQLException {
        this.stage = stage;
        this.user = user;
        this.transactionService = new TransactionService(new AccountDAO(), new TransactionDAO());
    }

    public void show() {
        Text title = new Text("Transaction History");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #003087;");

        Text userLabel = new Text("Account for User #" + user.getCustomerNumber());
        userLabel.setStyle("-fx-font-size: 16px; -fx-fill: #333333;");

        TableView<Transaction> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #003087;");

        TableColumn<Transaction, String> idColumn = new TableColumn<>("Transaction ID");
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTransactionId())));
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        typeColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Transaction, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> {
            String amount = currencyFormat.format(cellData.getValue().getAmount().abs());
            String sign = cellData.getValue().getAmount().signum() > 0 ? "+" : "-";
            return new SimpleStringProperty(sign + amount);
        });
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<Transaction, String> timeColumn = new TableColumn<>("Timestamp");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp().toString()));
        timeColumn.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().addAll(idColumn, typeColumn, amountColumn, timeColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.itemProperty().addListener(new ChangeListener<Transaction>() {
                @Override
                public void changed(ObservableValue<? extends Transaction> observable, Transaction oldTransaction, Transaction newTransaction) {
                    if (newTransaction != null) {
                        String type = newTransaction.getType();
                        if (type.contains("DEPOSIT") || type.equals("TRANSFER_IN")) {
                            row.setStyle("-fx-background-color: #e8f5e9;");
                        } else if (type.contains("WITHDRAW") || type.equals("TRANSFER_OUT")) {
                            row.setStyle("-fx-background-color: #ffebee;");
                        } else {
                            row.setStyle("-fx-background-color: #ffffff;");
                        }
                    } else {
                        row.setStyle("-fx-background-color: #ffffff;");
                    }
                }
            });
            return row;
        });

        try {
            ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                    transactionService.getTransactionHistory(user.getUserId())
            );
            tableView.setItems(transactions);
            if (transactions.isEmpty()) {
                tableView.setPlaceholder(new Text("No transactions found."));
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load transaction history: " + e.getMessage());
            alert.showAndWait();
        }

        Button backButton = new Button("Back to Transactions");
        backButton.setStyle("-fx-background-color: #003087; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");
        backButton.setOnAction(e -> {
            try {
                new TransactionUI(stage, user).show();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to return to transaction screen: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(20, title, userLabel, tableView, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #e6f0fa;");

        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("Transaction History - Horizon Bank ATM");
        stage.setScene(scene);
        stage.show();
    }
}
