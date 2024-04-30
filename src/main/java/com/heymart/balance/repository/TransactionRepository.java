package com.heymart.balance.repository;

import com.heymart.balance.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public Transaction save(Transaction transaction) {
        int i = 0;
        for (Transaction savedTransaction : transactions) {
            if (savedTransaction.getId().equals(transaction.getId())) {
                transactions.remove(i);
                transactions.add(i, transaction);
            }
            i += 1;
        }

        transactions.add(transaction);
        return transaction;
    }

    public Transaction findById(UUID id) {
        for (Transaction savedTransaction : transactions) {
            if (savedTransaction.getId().equals(id)) {
                return savedTransaction;
            }
        }
        return null;
    }

    public List<Transaction> findByOwnerId(UUID id) {
        List<Transaction> ownerTransactions = new ArrayList<>();
        for (Transaction savedTransaction : transactions) {
            if (savedTransaction.getOwnerId().equals(id)) {
                ownerTransactions.add(savedTransaction);
            }
        }

        return ownerTransactions;
    }
}
