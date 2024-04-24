package com.heymart.balance.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BalanceTest {
    UserBalance userBalance;
    SupermarketBalance supermarketBalance;

    @BeforeEach
    void setup() {
        this.userBalance = new UserBalance(new User("Udin"));
        this.supermarketBalance = new SupermarketBalance(new Supermarket("MOR Minimarket"));
        
        this.userBalance.setId(UUID.fromString("c0adc466-a41c-4fe9-bebe-b879ba3d5ec7"));
        this.userBalance.setBalance(500000.0);

        this.supermarketBalance.setId(UUID.fromString("e7271c2f-df0a-4b52-ab8b-ca59bc2aaf13"));
        this.supermarketBalance.setBalance(0.0);
    }

    @Test
    void testGetBalanceId() {
        assertEquals("c0adc466-a41c-4fe9-bebe-b879ba3d5ec7", this.userBalance.getId().toString());
        assertEquals("e7271c2f-df0a-4b52-ab8b-ca59bc2aaf13", this.supermarketBalance.getId().toString());
    }

    @Test
    void testGetProductOwner() {
        assertEquals("Udin", this.userBalance.getOwner().getName());
        assertEquals("MOR Minimarket", this.supermarketBalance.getOwner().getName());
    }

    @Test
    void testGetProductBalance() {
        assertEquals(500000, this.userBalance.getBalance());
        assertEquals(0, this.supermarketBalance.getBalance());
    }

    @Test
    void testCreateBalanceEmptyOwner() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserBalance newBalance = new UserBalance(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SupermarketBalance newBalance = new SupermarketBalance(null);
        });
    }
}
