package com.atmapp.service;

import com.atmapp.dao.AccountDAO;
import com.atmapp.exception.InsufficientFundsException;
import com.atmapp.model.Account;
import com.atmapp.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;


public class TransactionService {
    private final AccountDAO accountDAO;

    
    public TransactionService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void depositChecking(User user, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = accountDAO.loadByUserId(user.getUserId());
        if (account != null) {
            BigDecimal newBalance = account.getCheckingBalance().add(amount);
            account.setCheckingBalance(newBalance);
            accountDAO.updateAccount(account);
        } else {
            throw new IllegalArgumentException("Account not found for user");
        }
    }

   
    public void withdrawChecking(User user, BigDecimal amount) throws SQLException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        Account account = accountDAO.loadByUserId(user.getUserId());
        if (account != null) {
            BigDecimal currentBalance = account.getCheckingBalance();
            if (currentBalance.compareTo(amount) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(amount);
                account.setCheckingBalance(newBalance);
                accountDAO.updateAccount(account);
            } else {
                throw new InsufficientFundsException("Insufficient funds in checking account");
            }
        } else {
            throw new IllegalArgumentException("Account not found for user");
        }
    }

  
    public void depositSavings(User user, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = accountDAO.loadByUserId(user.getUserId());
        if (account != null) {
            BigDecimal newBalance = account.getSavingsBalance().add(amount);
            account.setSavingsBalance(newBalance);
            accountDAO.updateAccount(account);
        } else {
            throw new IllegalArgumentException("Account not found for user");
        }
    }

  
    public void withdrawSavings(User user, BigDecimal amount) throws SQLException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        Account account = accountDAO.loadByUserId(user.getUserId());
        if (account != null) {
            BigDecimal currentBalance = account.getSavingsBalance();
            if (currentBalance.compareTo(amount) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(amount);
                account.setSavingsBalance(newBalance);
                accountDAO.updateAccount(account);
            } else {
                throw new InsufficientFundsException("Insufficient funds in savings account");
            }
        } else {
            throw new IllegalArgumentException("Account not found for user");
        }
    }
}