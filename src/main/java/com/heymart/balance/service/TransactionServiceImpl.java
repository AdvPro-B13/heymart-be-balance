package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        if (transactionRepository.findById(transaction.getId()) == null) {
            transactionRepository.save(transaction);
            return transaction;
        }
        return null;
    }

    public Transaction findById(UUID id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> findByOwnerId(UUID id) {
        return transactionRepository.findByOwnerId(id);
    }
}
