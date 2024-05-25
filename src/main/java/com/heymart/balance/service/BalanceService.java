package com.heymart.balance.service;

import com.heymart.balance.model.Balance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BalanceService {
    public CompletableFuture<Balance> createBalance(UUID ownerId, Balance.OwnerType ownerType);
    public CompletableFuture<Optional<Balance>> findById(UUID id);
    public CompletableFuture<Optional<Balance>> findByOwnerId(UUID ownerId);
    public CompletableFuture<Optional<Balance>> topUp(UUID ownerId, double amount);
    public CompletableFuture<Optional<Balance>> withdraw(UUID ownerId, double amount);
    public CompletableFuture<List<Balance>> checkout(UUID userId, UUID supermarketId, double amount);
}
