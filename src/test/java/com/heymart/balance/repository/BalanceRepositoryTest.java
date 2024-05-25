package com.heymart.balance.repository;

import com.heymart.balance.model.Balance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BalanceRepositoryTest {
    @Autowired
    private BalanceRepository balanceRepository;
    private UUID ownerId;
    private Balance.OwnerType ownerType;

    @BeforeEach()
    void setup() {
        ownerId = UUID.randomUUID();
        ownerType = Balance.OwnerType.USER;
    }

    @AfterEach()
    void tearDown() {
        balanceRepository.deleteAll();
    }

    @Test
    void testCreateBalance() {
        Balance newBalance = new Balance(ownerId, ownerType);
        Balance createdBalance = balanceRepository.save(newBalance);

        assertNotNull(createdBalance);
        assertEquals(createdBalance.getOwnerId(), ownerId);
        assertEquals(createdBalance.getOwnerType(), ownerType);
    }

    @Test
    void testFindByOwnerIdFound() {
        Balance newBalance = new Balance(ownerId, ownerType);
        balanceRepository.save(newBalance);
        Optional<Balance> target = balanceRepository.findByOwnerId(ownerId);

        assertTrue(target.isPresent());
        assertEquals(target.get().getOwnerType(), ownerType);
    }

    @Test
    void testFindByOwnerIdNotFound() {
        UUID randomId = UUID.randomUUID();

        Optional<Balance> target = balanceRepository.findByOwnerId(randomId);
        assertTrue(target.isEmpty());
    }

    @Test
    void testFindByIdFound() {
        Balance newBalance = new Balance(ownerId, ownerType);
        newBalance = balanceRepository.save(newBalance);

        Optional<Balance> foundBalance = balanceRepository.findById(newBalance.getId());

        assertTrue(foundBalance.isPresent());
        assertEquals(foundBalance.get().getId(), newBalance.getId());
    }

    @Test
    void testFindByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Optional<Balance> emptyOptional = balanceRepository.findByOwnerId(randomId);

        assertTrue(emptyOptional.isEmpty());
    }
}
