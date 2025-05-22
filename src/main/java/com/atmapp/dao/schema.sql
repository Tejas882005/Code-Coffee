CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_number INT UNIQUE,
    pin INT
);

CREATE TABLE accounts (
    account_number INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    checking_balance DECIMAL(15, 2),
    savings_balance DECIMAL(15, 2),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);