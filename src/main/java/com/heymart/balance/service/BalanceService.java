package com.heymart.balance.service;

import com.heymart.balance.model.Balance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BalanceService {
    public Balance createBalance(UUID ownerId, Balance.OwnerType ownerType);
    public Optional<Balance> findById(UUID id);
    public Optional<Balance> findByOwnerId(UUID ownerId);
    public Optional<Balance> topUp(UUID ownerId, double amount);
    public Optional<Balance> withdraw(UUID ownerId, double amount);
    public List<Balance> checkout(UUID userId, UUID supermarketId, double amount);
}
