package com.heymart.balance.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.heymart.balance.model.SupermarketBalance;

public interface SupermarketBalanceService {
    public SupermarketBalance createBalance(SupermarketBalance balance);
    public CompletableFuture<SupermarketBalance> findById(UUID id);
    public CompletableFuture<SupermarketBalance> topup(SupermarketBalance balance, double amount);
    public CompletableFuture<SupermarketBalance> withdraw(SupermarketBalance balance, double amount);
}
