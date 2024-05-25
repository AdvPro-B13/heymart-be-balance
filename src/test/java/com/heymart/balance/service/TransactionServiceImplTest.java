package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @InjectMocks
    TransactionServiceImpl transactionService;

    @Mock
    TransactionRepository transactionRepository;

    List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();

        Transaction.TransactionType withdrawalType = Transaction.TransactionType.WITHDRAWAL;
        Transaction.TransactionType topupType = Transaction.TransactionType.TOPUP;
        Transaction.OwnerType supermarketType = Transaction.OwnerType.SUPERMARKET;
        Transaction.OwnerType userType = Transaction.OwnerType.USER;

        UUID ownerId1 = UUID.randomUUID();
        Date transactionDate1 = new Date();
        double amount1 = 200.00;
        Transaction transaction1 = new Transaction(ownerId1, userType, transactionDate1, amount1, withdrawalType);
        transactions.add(transaction1);

        UUID ownerId2 = UUID.randomUUID();
        Date transactionDate2 = new Date();
        double amount2 = 100.00;
        Transaction transaction2 = new Transaction(ownerId2, supermarketType, transactionDate2, amount2, topupType);
        transactions.add(transaction2);

        // transaction with same owner as transaction1
        Date transactionDate3 = new Date();
        double amount3 = 200.00;
        Transaction transaction3 = new Transaction(ownerId1, userType, transactionDate3, amount3, topupType);
        transactions.add(transaction3);
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = transactions.get(0);

        doReturn(transaction).when(transactionRepository).save(transaction);

        CompletableFuture<Transaction> resultFuture = transactionService.createTransaction(transaction);
        try {
            Transaction result = resultFuture.get();
            verify(transactionRepository, times(1)).save(transaction);
            assertEquals(transaction, result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Test
    void TestFindByIdFound() {
        Transaction transaction = transactions.get(0);
        Optional<Transaction> found = Optional.of(transaction);

        doReturn(found).when(transactionRepository).findById(transaction.getId());
        CompletableFuture<Optional<Transaction>> resultFuture = transactionService.findById(transaction.getId());
        try {
            Optional<Transaction> result = resultFuture.get();
            assertEquals(transaction, result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void TestFindByIdNotFound() {
        Transaction transaction = transactions.get(0);

        doReturn(Optional.empty()).when(transactionRepository).findById(transaction.getId());
        CompletableFuture<Optional<Transaction>> resultFuture = transactionService.findById(transaction.getId());
        try {
            Optional<Transaction> result = resultFuture.get();
            assertEquals(Optional.empty(), result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void TestFindByOwnerIdNotEmpty() {
        Transaction transaction = transactions.get(0);
        List<Transaction> ownerTransactions = new ArrayList<>();
        ownerTransactions.add(transaction);

        doReturn(ownerTransactions).when(transactionRepository).findByOwnerId(transaction.getOwnerId());
        CompletableFuture<List<Transaction>> resultFuture = transactionService.findByOwnerId(transaction.getOwnerId());
        try {
            List<Transaction> resultTransactions = resultFuture.get();
            assertEquals(ownerTransactions, resultTransactions);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
