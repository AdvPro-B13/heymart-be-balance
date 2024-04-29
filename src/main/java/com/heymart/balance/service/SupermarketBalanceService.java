package com.heymart.balance.service;

import java.util.UUID;

import com.heymart.balance.model.SupermarketBalance;

public interface SupermarketBalanceService {
    public SupermarketBalance createBalance(SupermarketBalance balance);
    public SupermarketBalance findById(UUID id);
    public SupermarketBalance topup(SupermarketBalance balance, double amount);
    public SupermarketBalance withdraw(SupermarketBalance balance, double amount);
}
