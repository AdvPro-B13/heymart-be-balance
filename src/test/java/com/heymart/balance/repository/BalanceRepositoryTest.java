package com.heymart.balance.repository;

import com.heymart.balance.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BalanceRepositoryTest {
    UserBalanceRepository userBalanceRepository;
    SupermarketBalanceRepository supermarketbalanceRepository;

    List<Balance> balances;

    @BeforeEach
    void setup() {
        balances = new ArrayList<>();
        userBalanceRepository = new UserBalanceRepository();
        supermarketbalanceRepository = new SupermarketBalanceRepository();

        UserBalance userBalance1 = new UserBalance(new User("udin"));
        userBalance1.setId(UUID.fromString("c48f34fb-8b2b-4cd0-b43b-4f60b2e87055"));
        balances.add(userBalance1);

        UserBalance userBalance2 = new UserBalance(new User("petot"));
        userBalance2.setId(UUID.fromString("935fb4e6-c55a-4126-9b13-61fc57c62fac"));
        balances.add(userBalance2);

        UserBalance userBalance3 = new UserBalance(new User("stardin"));
        userBalance2.setId(UUID.fromString("4208234e-9824-42d6-bbad-59ead360daf2"));
        balances.add(userBalance3);

        SupermarketBalance superBalance1 = new SupermarketBalance(new Supermarket("MOR Minimarket"));
        superBalance1.setId(UUID.fromString("48f0b15a-effb-4b26-950c-55f64c19e96e"));
        balances.add(superBalance1);

        SupermarketBalance superBalance2 = new SupermarketBalance(new Supermarket("Keiramart"));
        superBalance2.setId(UUID.fromString("b640f72c-f78f-4a69-a663-b56820b17b88"));
        balances.add(superBalance2);

        SupermarketBalance superBalance3 = new SupermarketBalance(new Supermarket("Alfamart"));
        superBalance3.setId(UUID.fromString("732378bb-27ad-4d42-b812-663a7933f6e8"));
        balances.add(superBalance3);
    }

    @Test
    void testSaveBalance() {
        UserBalance newUB = new UserBalance(new User("Udin"));
        SupermarketBalance newSB = new SupermarketBalance(new Supermarket("KeiraMart"));
        userBalanceRepository.save(newUB);
        supermarketbalanceRepository.save(newSB);

        UserBalance targetUB = userBalanceRepository.findById(newUB.getId());
        assertEquals(newUB, targetUB);

        SupermarketBalance targetSB = supermarketbalanceRepository.findById(newSB.getId());
        assertEquals(newSB, targetSB);
    }

    @Test
    void testSaveUpdate() {
        UserBalance newUB = new UserBalance(new User("Udin"));
        SupermarketBalance newSB = new SupermarketBalance(new Supermarket("KeiraMart"));
        userBalanceRepository.save(newUB);
        supermarketbalanceRepository.save(newSB);

        UserBalance newUB1 = new UserBalance(new User("Udin1"));
        SupermarketBalance newSB1 = new SupermarketBalance(new Supermarket("KeiraMart1"));
        userBalanceRepository.save(newUB1);
        supermarketbalanceRepository.save(newSB1);

        UserBalance targetUB = userBalanceRepository.findById(newUB1.getId());
        assertEquals(newUB1, targetUB);

        SupermarketBalance targetSB = supermarketbalanceRepository.findById(newSB1.getId());
        assertEquals(newSB1, targetSB);
    }

    @Test
    void testFindByIdIfIdFound() {
        for (Balance balance : balances) {
            if (balance instanceof UserBalance) {
                userBalanceRepository.save((UserBalance) balance);
            } else if (balance instanceof SupermarketBalance) {
                supermarketbalanceRepository.save((SupermarketBalance) balance);
            }
        }

        SupermarketBalance targetSB = (SupermarketBalance) balances.get(4);
        SupermarketBalance findSBResult = supermarketbalanceRepository.findById(targetSB.getId());
        assertEquals(targetSB, findSBResult);

        UserBalance targetUB = (UserBalance) balances.get(1);
        UserBalance findUBResult = userBalanceRepository.findById(targetUB.getId());
        assertEquals(targetUB, findUBResult);
    }

    @Test
    void testFindByIdIfIdNotFound() {
        for (Balance balance : balances) {
            if (balance instanceof UserBalance) {
                userBalanceRepository.save((UserBalance) balance);
            } else if (balance instanceof SupermarketBalance) {
                supermarketbalanceRepository.save((SupermarketBalance) balance);
            }
        }

        SupermarketBalance targetSB = new SupermarketBalance(new Supermarket("keiramart"));
        SupermarketBalance findSBResult = supermarketbalanceRepository.findById(targetSB.getId());
        assertNull(findSBResult);

        UserBalance targetUB = new UserBalance(new User("keira"));
        UserBalance findUBResult = userBalanceRepository.findById(targetUB.getId());
        assertNull(findUBResult);
    }
}
