package com.heymart.balance.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heymart.balance.model.SupermarketBalance;
import com.heymart.balance.repository.SupermarketBalanceRepository;

@Service
public class SupermarketBalanceServiceImpl implements SupermarketBalanceService{
    @Autowired
    private SupermarketBalanceRepository supermarketBalanceRepository;

    public SupermarketBalance createBalance(SupermarketBalance balance) {
        if (supermarketBalanceRepository.findById(balance.getId()) == null) {
            supermarketBalanceRepository.save(balance);
            return balance;
        };

        return null;
    }

    public SupermarketBalance findById(UUID id) {
        return supermarketBalanceRepository.findById(id);
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
