package com.heymart.balance.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BalanceTest {
    UserBalance userBalance;
    SupermarketBalance supermarketBalance;

    @BeforeEach
    void setup() {
        this.userBalance = new UserBalance();
        this.supermarketBalance = new SupermarketBalance();
        
        this.userBalance.setBalanceId("c0adc466-a41c-4fe9-bebe-b879ba3d5ec7");
        this.userBalance.setBalanceOwner("Udin");
        this.userBalance.setBalance(500000);

        this.supermarketBalance.setBalanceId("e7271c2f-df0a-4b52-ab8b-ca59bc2aaf13");
        this.supermarketBalance.setBalanceOwner("MOR Minimarket");
        this.supermarketBalance.setBalance(0);
    }

    @Test
    void testGetBalanceId() {
        assertEquals("c0adc466-a41c-4fe9-bebe-b879ba3d5ec7", this.userBalance.getBalanceId());
        assertEquals("e7271c2f-df0a-4b52-ab8b-ca59bc2aaf13", this.supermarketBalance.getBalanceId());
    }

    @Test
    void testGetProductOwner() {
        assertEquals("Udin", this.userBalance.getBalanceOwner());
        assertEquals("MOR Minimarket", this.supermarketBalance.getBalanceOwner());
    }

    @Test
    void testGetProductBalance() {
        assertEquals(500000, this.userBalance.getBalance());
        assertEquals(0, this.supermarketBalance.getBalance());
    }


}
