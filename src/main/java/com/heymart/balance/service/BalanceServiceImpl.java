package com.heymart.balance.service;

import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Service
public class BalanceServiceImpl implements BalanceService{

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    TransactionService transactionService;

    @Transactional
    public Balance createBalance(Balance balance) {
        Optional<Balance> existingBalance = balanceRepository.findByOwnerId(balance.getOwnerId());
        if (existingBalance.isPresent()) {
            throw new IllegalArgumentException("Owner already has a balance");
        }
        return balanceRepository.save(balance);
    }

    @Override
    public Optional<Balance> findById(UUID id) {
        return balanceRepository.findById(id);
    }

    @Override
    public Optional<Balance> findByOwnerId(UUID ownerId) {
        return balanceRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public Optional<Balance> topUp(UUID ownerId, double amount) {
        Optional<Balance> balance = findByOwnerId(ownerId);
        return balance.map(b -> {
            b.setBalance(b.getBalance() + amount);

            Transaction createdTransaction = new Transaction();
            createdTransaction.setOwnerId(ownerId);
            createdTransaction.setOwnerType(Transaction.OwnerType.valueOf(b.getOwnerType().toString()));
            createdTransaction.setAmount(amount);
            createdTransaction.setTransactionType(Transaction.TransactionType.TOPUP);
            createdTransaction.setBalance(b);
            transactionService.createTransaction(createdTransaction);

            return balanceRepository.save(b);
        });
    }

    @Transactional
    public Optional<Balance> withdraw(UUID ownerId, double amount) {
        Optional<Balance> balance = findByOwnerId(ownerId);
        return balance.map(b -> {
            if (b.getBalance() >= amount) {
                b.setBalance(b.getBalance() - amount);

                Transaction createdTransaction = new Transaction();
                createdTransaction.setOwnerId(ownerId);
                createdTransaction.setOwnerType(Transaction.OwnerType.valueOf(b.getOwnerType().toString()));
                createdTransaction.setAmount(amount);
                createdTransaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
                createdTransaction.setBalance(b);
                transactionService.createTransaction(createdTransaction);

                return balanceRepository.save(b);
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        });
    }
}
