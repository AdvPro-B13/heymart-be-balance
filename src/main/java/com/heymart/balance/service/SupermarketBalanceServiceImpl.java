package com.heymart.balance.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.heymart.balance.model.SupermarketBalance;
import com.heymart.balance.repository.SupermarketBalanceRepository;

@EnableAsync
@Service
public class SupermarketBalanceServiceImpl implements SupermarketBalanceService {
    @Autowired
    private SupermarketBalanceRepository supermarketBalanceRepository;

    public SupermarketBalance createBalance(SupermarketBalance balance) {
        if (supermarketBalanceRepository.findById(balance.getId()) == null) {
            if (supermarketBalanceRepository.findBySupermarketId(balance.getOwner().getId()) == null) {
                supermarketBalanceRepository.save(balance);
                return balance;
            }
            return null;
        };

        return null;
    }

    @Async
    public CompletableFuture<SupermarketBalance> findById(UUID id) {
        return CompletableFuture.supplyAsync(() -> supermarketBalanceRepository.findById(id));
    }

    public SupermarketBalance findBySupermarketId(UUID id) {
        return supermarketBalanceRepository.findBySupermarketId(id);
    }
    
    public SupermarketBalance topup(SupermarketBalance balance, double amount) {
        if (supermarketBalanceRepository.findById(balance.getId()) == null) {
            return null;
        }

        double cur = balance.getBalance();
        balance.setBalance(cur + amount);
        return balance;
    }

    public SupermarketBalance withdraw(SupermarketBalance balance, double amount) {
        if (supermarketBalanceRepository.findById(balance.getId()) == null) {
            return null;
        }

        if (balance.getBalance() < amount) {
            return null;
        }

        double cur = balance.getBalance();
        balance.setBalance(cur - amount);
        return balance;
    }
}
