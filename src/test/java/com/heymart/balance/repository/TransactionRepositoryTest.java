package com.heymart.balance.repository;

import com.heymart.balance.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TransactionRepositoryTest {
    TransactionRepository transactionRepository;

    List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();
        transactionRepository = new TransactionRepository();

        UUID ownerId1 = UUID.randomUUID();
        Date transactionDate1 = new Date();
        double amount1 = 200.00;
        Transaction.TransactionType transactionType1 = Transaction.TransactionType.WITHDRAWAL;
        Transaction transaction1 = new Transaction(ownerId1, transactionDate1, amount1, transactionType1);
        transactions.add(transaction1);

        UUID ownerId2 = UUID.randomUUID();
        Date transactionDate2 = new Date();
        double amount2 = 100.00;
        Transaction.TransactionType transactionType2 = Transaction.TransactionType.TOPUP;
        Transaction transaction2 = new Transaction(ownerId2, transactionDate2, amount2, transactionType2);
        transactions.add(transaction2);

        // transaction with same owner as transaction1
        Date transactionDate3 = new Date();
        double amount3 = 200.00;
        Transaction.TransactionType transactionType3 = Transaction.TransactionType.WITHDRAWAL;
        Transaction transaction3 = new Transaction(ownerId1, transactionDate3, amount3, transactionType3);
        transactions.add(transaction3);
    }

    @Test
    void testSaveTransaction() {
        Transaction newTransaction = transactions.getFirst();
        transactionRepository.save(newTransaction);

        Transaction savedTransaction = transactionRepository.findById(newTransaction.getId());
        assertEquals(newTransaction, savedTransaction);
    }

    @Test
    void testFindByOwnerIdFound() {
        UUID ownerId = transactions.getFirst().getOwnerId();
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionRepository.save(transaction);

            if (transaction.getOwnerId().equals(ownerId)) {
                result.add(transaction);
            }
        }

        List<Transaction> searchedTransaction = transactionRepository.findByOwnerId(ownerId);
        assertEquals(result, searchedTransaction);

    }

    @Test
    void testFindByOwnerIdNotFound() {
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionRepository.save(transaction);
        }

        List<Transaction> searchedTransaction = transactionRepository.findByOwnerId(UUID.randomUUID());
        assertEquals(result, searchedTransaction);
    }

    @Test
    void testFindByIdFound() {
        Transaction newTransaction = transactions.getFirst();
        transactionRepository.save(newTransaction);

        Transaction searchedTransaction = transactionRepository.findById(newTransaction.getId());
        assertEquals(newTransaction, searchedTransaction);
    }

    @Test
    void testFindByIdNotFound() {
        Transaction newTransaction = transactions.getFirst();
        transactionRepository.save(newTransaction);

        Transaction searchedTransaction = transactionRepository.findById(UUID.randomUUID());
        assertNull(searchedTransaction);
    }
}
