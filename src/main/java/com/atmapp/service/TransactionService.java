package com.atmapp.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.atmapp.dao.AccountDAO;
import com.atmapp.dao.TransactionDAO;
import com.atmapp.exception.InsufficientFundsException;
import com.atmapp.model.Account;
import com.atmapp.model.Transaction;

public class TransactionService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public void depositChecking(int userId, BigDecimal amount) throws SQLException, IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDAO.loadByUserId(userId);
        account.setCheckingBalance(account.getCheckingBalance().add(amount));
        accountDAO.update(account);
        transactionDAO.save(account.getAccountId(), "DEPOSIT_CHECKING", amount);
    }

    public void withdrawChecking(int userId, BigDecimal amount) throws SQLException, IllegalArgumentException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDAO.loadByUserId(userId);
        if (account.getCheckingBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in checking account");
        }
        account.setCheckingBalance(account.getCheckingBalance().subtract(amount));
        accountDAO.update(account);
        transactionDAO.save(account.getAccountId(), "WITHDRAW_CHECKING", amount.negate());
    }

    public void depositSavings(int userId, BigDecimal amount) throws SQLException, IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDAO.loadByUserId(userId);
        account.setSavingsBalance(account.getSavingsBalance().add(amount));
        accountDAO.update(account);
        transactionDAO.save(account.getAccountId(), "DEPOSIT_SAVINGS", amount);
    }

    public void withdrawSavings(int userId, BigDecimal amount) throws SQLException, IllegalArgumentException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDAO.loadByUserId(userId);
        if (account.getSavingsBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in savings account");
        }
        account.setSavingsBalance(account.getSavingsBalance().subtract(amount));
        accountDAO.update(account);
        transactionDAO.save(account.getAccountId(), "WITHDRAW_SAVINGS", amount.negate());
    }

    public void transfer(int userId, BigDecimal amount, boolean fromCheckingToSavings) throws SQLException, IllegalArgumentException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDAO.loadByUserId(userId);
        if (fromCheckingToSavings) {
            if (account.getCheckingBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in checking account");
            }
            account.setCheckingBalance(account.getCheckingBalance().subtract(amount));
            account.setSavingsBalance(account.getSavingsBalance().add(amount));
            transactionDAO.save(account.getAccountId(), "TRANSFER_OUT", amount.negate());
            transactionDAO.save(account.getAccountId(), "TRANSFER_IN", amount);
        } else {
            if (account.getSavingsBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in savings account");
            }
            account.setSavingsBalance(account.getSavingsBalance().subtract(amount));
            account.setCheckingBalance(account.getCheckingBalance().add(amount));
            transactionDAO.save(account.getAccountId(), "TRANSFER_OUT", amount.negate());
            transactionDAO.save(account.getAccountId(), "TRANSFER_IN", amount);
        }
        accountDAO.update(account);
    }

    public List<Transaction> getTransactionHistory(int userId) throws SQLException {
        Account account = accountDAO.loadByUserId(userId);
        return transactionDAO.findByAccountId(account.getAccountId());
    }
}