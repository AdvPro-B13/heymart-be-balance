package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    public CompletableFuture<Transaction> createTransaction(Transaction transaction);
    public CompletableFuture<Optional<Transaction>> findById(UUID id);
    public CompletableFuture<List<Transaction>> findByOwnerId(String ownerId);
}
