CREATE DATABASE IF NOT EXISTS atm_db;
USE atm_db;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_number INT UNIQUE NOT NULL,
    pin INT NOT NULL
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    checking_balance DECIMAL(10,2) DEFAULT 0.00,
    savings_balance DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    type ENUM('DEPOSIT_CHECKING', 'WITHDRAW_CHECKING', 'DEPOSIT_SAVINGS', 'WITHDRAW_SAVINGS', 'TRANSFER_OUT', 'TRANSFER_IN') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

INSERT INTO users (customer_number, pin) VALUES (123456, 1234);
INSERT INTO accounts (user_id, checking_balance, savings_balance) VALUES (1, 1000.00, 500.00);