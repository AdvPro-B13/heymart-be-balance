package com.heymart.balance.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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

    @Async
    public CompletableFuture<SupermarketBalance> topup(SupermarketBalance balance, double amount){
        return CompletableFuture.supplyAsync(() -> {
            SupermarketBalance cur = supermarketBalanceRepository.findById(balance.getId());
            if (cur == null) {
                return null;
            }

            double curbal = cur.getBalance();
            cur.setBalance(curbal + amount);
            return cur;
        });
    }

    @Async
    public CompletableFuture<SupermarketBalance> withdraw(SupermarketBalance balance, double amount) {
        return CompletableFuture.supplyAsync(() -> {
            SupermarketBalance cur = supermarketBalanceRepository.findById(balance.getId());

            if (cur == null) {
                return null;
            }

            if (balance.getBalance() < amount) {
                return null;
            }

            double curbal = cur.getBalance();
            cur.setBalance(curbal - amount);
            return cur;
        });
    }
}
