package com.heymart.balance.repository;

import com.heymart.balance.controller.BalanceController;
import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    private UUID ownerId;
    private Transaction transaction;
    private Balance balance;

    @BeforeEach
    @Transactional
    void setUp() {
        ownerId = UUID.randomUUID();
        Balance newBalance = new Balance(ownerId, Balance.OwnerType.USER);
        balanceRepository.save(newBalance);
        Optional<Balance> target = balanceRepository.findByOwnerId(ownerId);
        balance = target.get();

        transaction = new Transaction();
        transaction.setOwnerId(ownerId);
        transaction.setOwnerType(Transaction.OwnerType.USER);
        transaction.setAmount(100.0);
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setBalance(balance);

        transactionRepository.save(transaction);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    void testSaveTransaction() {
        List<Transaction> before = transactionRepository.findByOwnerId(ownerId);
        assertThat(before).hasSize(1);

        Transaction newTransaction = new Transaction();
        newTransaction.setOwnerId(ownerId);
        newTransaction.setOwnerType(Transaction.OwnerType.USER);
        newTransaction.setAmount(100.0);
        newTransaction.setTransactionType(Transaction.TransactionType.TOPUP);
        newTransaction.setBalance(balance);

        transactionRepository.save(newTransaction);
        List<Transaction> after = transactionRepository.findByOwnerId(ownerId);
        assertThat(after).hasSize(2);
    }

    @Test
    void testFindById() {
        Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getId());
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void testFindAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(1);
    }

    @Test
    void testFindByOwnerId() {
        List<Transaction> transactions = transactionRepository.findByOwnerId(ownerId);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.get(0).getId()).isEqualTo(transaction.getId());
    }

    @Test
    void testDeleteTransaction() {
        transactionRepository.delete(transaction);
        Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getId());
        assertThat(foundTransaction).isNotPresent();
    }
}