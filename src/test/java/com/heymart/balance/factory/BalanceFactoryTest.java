package com.heymart.balance.factory;

import com.heymart.balance.model.Balance;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BalanceFactoryTest {
    UserBalanceFactory userBalanceFactory = new UserBalanceFactory();

    SupermarketBalanceFactory supermarketBalanceFactory = new SupermarketBalanceFactory();

    private final Balance.OwnerType userType = Balance.OwnerType.USER;
    private final Balance.OwnerType superType = Balance.OwnerType.SUPERMARKET;

    @Test
    void testCreateMatchingBalance() {
        String random1 = UUID.randomUUID().toString();
        String random2 = UUID.randomUUID().toString();
        Balance newUserBalance = userBalanceFactory.createBalance(random1, userType);
        Balance newSuperBalance = supermarketBalanceFactory.createBalance(random2, superType);

        assertNotNull(newSuperBalance);
        assertNotNull(newUserBalance);
    }

    @Test
    void testCreateMismatchBalance() {
        String random1 = UUID.randomUUID().toString();
        String random2 = UUID.randomUUID().toString();

        assertThrows(IllegalArgumentException.class, () -> userBalanceFactory.createBalance(random1, superType));
        assertThrows(IllegalArgumentException.class, () -> supermarketBalanceFactory.createBalance(random2, userType));
    }
}
