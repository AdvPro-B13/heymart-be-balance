package com.heymart.balance.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heymart.balance.model.UserBalance;
import com.heymart.balance.repository.UserBalanceRepository;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {
@Autowired
    private UserBalanceRepository userBalanceRepository;

    public UserBalance createBalance(UserBalance balance) {
        if (userBalanceRepository.findById(balance.getId()) == null) {
            userBalanceRepository.save(balance);
            return balance;
        };

        return null;
    }

    public UserBalance findById(UUID id) {
        return userBalanceRepository.findById(id);
    }
    
    public UserBalance topup(UserBalance balance, double amount) {
        if (userBalanceRepository.findById(balance.getId()) == null) {
            return null;
        }

        double cur = balance.getBalance();
        balance.setBalance(cur + amount);
        return balance;
    }

    public UserBalance withdraw(UserBalance balance, double amount) {
        if (userBalanceRepository.findById(balance.getId()) == null) {
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
