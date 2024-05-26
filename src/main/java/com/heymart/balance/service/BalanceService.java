package com.heymart.balance.service;

import com.heymart.balance.model.Balance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BalanceService {
    public CompletableFuture<Balance> createBalance(String ownerId, Balance.OwnerType ownerType);
    public CompletableFuture<Optional<Balance>> findById(UUID id);
    public CompletableFuture<Optional<Balance>> findByOwnerId(String ownerId);
    public CompletableFuture<Optional<Balance>> topUp(String ownerId, double amount);
    public CompletableFuture<Optional<Balance>> withdraw(String ownerId, double amount);
    public CompletableFuture<List<Balance>> checkout(String userId, String supermarketId, double amount);
}
