package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        Optional<Transaction> targetTransaction = transactionRepository.findById(transaction.getId());

        if (targetTransaction.isEmpty()) {
            return transactionRepository.save(transaction);
        }
        return null;
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> findByOwnerId(UUID id) {
        return transactionRepository.findByOwnerId(id);
    }
}
