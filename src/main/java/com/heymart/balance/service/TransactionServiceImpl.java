package com.heymart.balance.service;

import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        return CompletableFuture.completedFuture(transactionRepository.save(transaction));
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Optional<Transaction>> findById(UUID id) {
        return CompletableFuture.completedFuture(transactionRepository.findById(id));
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Transaction>> findByOwnerId(String id) {
        return CompletableFuture.completedFuture(transactionRepository.findByOwnerId(id));
    }
}
