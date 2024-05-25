package com.heymart.balance.factory;

import com.heymart.balance.model.Balance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BalanceFactoryTest {
    UserBalanceFactory userBalanceFactory = new UserBalanceFactory();

    SupermarketBalanceFactory supermarketBalanceFactory = new SupermarketBalanceFactory();

    private Balance.OwnerType userType = Balance.OwnerType.USER;
    private Balance.OwnerType superType = Balance.OwnerType.SUPERMARKET;

    @Test
    public void testCreateMatchingBalance() {
        UUID random1 = UUID.randomUUID();
        UUID random2 = UUID.randomUUID();
        Balance newUserBalance = userBalanceFactory.createBalance(random1, userType);
        Balance newSuperBalance = supermarketBalanceFactory.createBalance(random2, superType);

        assertNotNull(newSuperBalance);
        assertNotNull(newUserBalance);
    }

    @Test
    public void testCreateMismatchBalance() {
        UUID random1 = UUID.randomUUID();
        UUID random2 = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> userBalanceFactory.createBalance(random1, superType));
        assertThrows(IllegalArgumentException.class, () -> supermarketBalanceFactory.createBalance(random2, userType));
    }
}
