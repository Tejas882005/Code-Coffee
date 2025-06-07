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

    public int getCustomerNumber() {
        return customerNumber;
    }

    public int getPin() {
        return pin;
    }
}
