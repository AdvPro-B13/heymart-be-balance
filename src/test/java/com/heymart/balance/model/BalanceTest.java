package com.heymart.balance.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {
    Balance balance;

    @Test
    void testBalanceSettersAndGetters() {
        UUID balanceId = UUID.randomUUID();
        String ownerId = UUID.randomUUID().toString();
        Balance.OwnerType ownerType = Balance.OwnerType.USER;
        Double amount = 40.0;

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        List<Transaction> transactions = List.of(transaction);

        balance = new Balance();

        balance.setId(balanceId);
        balance.setOwnerId(ownerId);
        balance.setOwnerType(ownerType);
        balance.setBalance(amount);
        balance.setTransactions(transactions);

        assertAll("Ensure correct Values",
            () -> assertEquals(balance.getId(), balanceId),
            () -> assertEquals(balance.getOwnerId(), ownerId),
            () -> assertEquals(balance.getOwnerType(), ownerType),
            () -> assertEquals(balance.getBalance(), amount),
            () -> assertEquals(balance.getTransactions(), transactions)
        );
    }

    @Test
    void testBalanceConstructor() {
        String ownerId = UUID.randomUUID().toString();
        Balance.OwnerType ownerType = Balance.OwnerType.USER;

        Balance newConstructedBalance = new Balance(ownerId, ownerType);

        assertAll("constructor",
            () -> assertNotNull(newConstructedBalance.getOwnerId()),
            () -> assertNotNull(newConstructedBalance.getOwnerType())
        );
    }
}
