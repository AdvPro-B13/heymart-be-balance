package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @InjectMocks
    TransactionServiceImpl transactionService;

    @Mock
    TransactionRepository transactionRepository;

    List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();

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
    void testCreateTransaction() {
        Transaction transaction = transactions.getFirst();

        doReturn(transaction).when(transactionRepository).save(transaction);

        Transaction result = transactionService.createTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
        assertEquals(transaction, result);
    }

    @Test
    void testCreateTransactionIfAlreadyExists() {
        Transaction transaction = transactions.getFirst();

        doReturn(transaction).when(transactionRepository).findById(transaction.getId());
        assertNull(transactionService.createTransaction(transaction));
        verify(transactionRepository, times(0)).save(transaction);
    }

    @Test
    void TestFindByIdFound() {
        Transaction transaction = transactions.getFirst();

        doReturn(transaction).when(transactionRepository).findById(transaction.getId());
        Transaction result = transactionService.findById(transaction.getId());
        assertEquals(transaction, result);
    }

    @Test
    void TestFindByIdNotFound() {
        Transaction transaction = transactions.getFirst();

        doReturn(null).when(transactionRepository).findById(transaction.getId());
        assertNull(transactionService.findById(transaction.getId()));
    }

    @Test
    void TestFindByOwnerIdNotEmpty() {
        Transaction transaction = transactions.getFirst();
        List<Transaction> ownerTransactions = new ArrayList<>();
        ownerTransactions.add(transaction);

        doReturn(ownerTransactions).when(transactionRepository).findByOwnerId(transaction.getOwnerId());
        List<Transaction> resultTransactions = transactionService.findByOwnerId(transaction.getOwnerId());
        assertEquals(ownerTransactions, resultTransactions);
    }
}
