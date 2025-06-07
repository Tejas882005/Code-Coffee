package com.atmapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private int accountId;
    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp;

}
