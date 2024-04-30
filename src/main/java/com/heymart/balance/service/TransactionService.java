package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    public Transaction createTransaction(Transaction transaction);
    public Transaction findById(UUID id);
    public List<Transaction> findByOwnerId(UUID ownerId);
}
