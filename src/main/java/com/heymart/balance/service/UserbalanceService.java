package com.heymart.balance.service;

import java.util.UUID;

import com.heymart.balance.model.UserBalance;

public interface UserbalanceService {
    public UserBalance createBalance(UserBalance balance);
    public UserBalance findById(UUID id);
    public UserBalance topup(UserBalance balance, double amount);
    public UserBalance withdraw(UserBalance balance, double amount);
}
