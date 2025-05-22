package com.atmapp.model;

public class User {
    private int userId;
    private int customerNumber;
    private int pin;

    public User(int userId, int customerNumber, int pin) {
        this.userId = userId;
        this.customerNumber = customerNumber;
        this.pin = pin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}