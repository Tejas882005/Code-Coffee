package com.atmapp.model;

import java.math.BigDecimal;

public class Account {
    private int accountNumber;
    private int userId;
    private BigDecimal checkingBalance;
    private BigDecimal savingsBalance;

    public Account(int accountNumber, int userId, BigDecimal checkingBalance, BigDecimal savingsBalance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.checkingBalance = checkingBalance;
        this.savingsBalance = savingsBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getCheckingBalance() {
        return checkingBalance;
    }

    public void setCheckingBalance(BigDecimal checkingBalance) {
        this.checkingBalance = checkingBalance;
    }

    public BigDecimal getSavingsBalance() {
        return savingsBalance;
    }

    public void setSavingsBalance(BigDecimal savingsBalance) {
        this.savingsBalance = savingsBalance;
    }
}