package com.heymart.balance.service;

import com.heymart.balance.factory.BalanceFactory;
import com.heymart.balance.factory.SupermarketBalanceFactory;
import com.heymart.balance.factory.UserBalanceFactory;
import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BalanceServiceImpl implements BalanceService{

    private final BalanceRepository balanceRepository;
    private final TransactionService transactionService;
    private final BalanceFactory userBalanceFactory;
    private final BalanceFactory supermarketBalanceFactory;

    @Autowired BalanceServiceImpl(BalanceRepository balanceRepository,
                                  TransactionService transactionService) {
        this.balanceRepository = balanceRepository;
        this.transactionService = transactionService;
        this.userBalanceFactory = new UserBalanceFactory();
        this.supermarketBalanceFactory = new SupermarketBalanceFactory();
    }

    @Transactional
    public Balance createBalance(UUID ownerId, Balance.OwnerType ownerType) {
        Optional<Balance> existingBalance = balanceRepository.findByOwnerId(ownerId);
        if (existingBalance.isPresent()) {
            throw new IllegalArgumentException("Owner already has a balance");
        }
        BalanceFactory factory = (ownerType == Balance.OwnerType.USER) ? userBalanceFactory : supermarketBalanceFactory;
        Balance balance = factory.createBalance(ownerId, ownerType);
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

    @Transactional
    public List<Balance> checkout(UUID userId, UUID supermarketId, double amount) {
        Optional<Balance> userBalance = balanceRepository.findByOwnerId(userId);
        Optional<Balance> supermarketBalance = balanceRepository.findByOwnerId(supermarketId);
        if (userBalance.isPresent() && supermarketBalance.isPresent()) {
            List<Balance> response = new ArrayList<>();
            this.withdraw(userId, amount);
            this.topUp(supermarketId, amount);
            response.add(userBalance.get());
            response.add(supermarketBalance.get());
            return response;
        }

        return new ArrayList<>();
    }
}
