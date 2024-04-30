package com.heymart.balance.model;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
    private UUID id;
    private UUID ownerId;
    private Date transactionDate;
    private double amount;
    private TransactionType transactionType;

    public enum TransactionType {
        TOPUP, WITHDRAWAL
    }

    public Transaction() {

    }

    public Transaction(UUID ownerId, Date transactionDate, double amount, TransactionType transactionType) {
        this.id = UUID.randomUUID();
        this.ownerId = ownerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
