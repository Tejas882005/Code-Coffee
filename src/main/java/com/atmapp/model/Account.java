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

    
}