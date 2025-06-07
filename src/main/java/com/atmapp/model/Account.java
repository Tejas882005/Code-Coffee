package com.atmapp.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal checkingBalance;
    private BigDecimal savingsBalance;

    public Account(int accountId, int userId, BigDecimal checkingBalance, BigDecimal savingsBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.checkingBalance = checkingBalance;
        this.savingsBalance = savingsBalance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getCheckingBalance() {
        return checkingBalance;
    }

    public BigDecimal getSavingsBalance() {
        return savingsBalance;
    }

    public void setCheckingBalance(BigDecimal checkingBalance) {
        this.checkingBalance = checkingBalance;
    }

    public void setSavingsBalance(BigDecimal savingsBalance) {
        this.savingsBalance = savingsBalance;
    }
}
