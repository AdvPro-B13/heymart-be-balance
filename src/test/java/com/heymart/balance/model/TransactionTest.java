package com.heymart.balance.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.Date;

public class TransactionTest {
    Transaction transaction;

    @Test
    public void testTransactionGettersAndSetters() {
        UUID transactionId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Date transactionDate = new Date();
        double amount = 150.75;
        Transaction.TransactionType transactionType = Transaction.TransactionType.TOPUP;

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setOwnerId(ownerId);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);

        assertAll("Ensure correct values",
            () -> assertEquals(transactionId, transaction.getId()),
            () -> assertEquals(ownerId, transaction.getOwnerId()),
            () -> assertEquals(transactionDate, transaction.getTransactionDate()),
            () -> assertEquals(amount, transaction.getAmount()),
            () -> assertEquals(transactionType, transaction.getTransactionType())
        );
    }

    @Test
    public void testTransactionConstructor() {
        UUID ownerId = UUID.randomUUID();
        Date transactionDate = new Date();
        double amount = 200.00;
        Transaction.TransactionType transactionType = Transaction.TransactionType.WITHDRAWAL;

        Transaction transaction = new Transaction(ownerId, transactionDate, amount, transactionType);

        assertAll("Constructor",
            () -> assertNotNull(transaction.getId()),
            () -> assertNotNull(transaction.getOwnerId()),
            () -> assertNotNull(transaction.getTransactionDate()),
            () -> assertEquals(200.00, transaction.getAmount()),
            () -> assertEquals(Transaction.TransactionType.WITHDRAWAL, transaction.getTransactionType())
        );
    }
}
