package com.heymart.balance.service;

import com.heymart.balance.exceptions.BalanceNotFoundException;
import com.heymart.balance.factory.BalanceFactory;
import com.heymart.balance.factory.SupermarketBalanceFactory;
import com.heymart.balance.factory.UserBalanceFactory;
import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Balance> createBalance(String ownerId, Balance.OwnerType ownerType) {
        Optional<Balance> existingBalance = balanceRepository.findByOwnerId(ownerId);
        if (existingBalance.isPresent()) {
            throw new IllegalArgumentException("Owner already has a balance");
        }
        BalanceFactory factory = (ownerType == Balance.OwnerType.USER) ? userBalanceFactory : supermarketBalanceFactory;
        Balance balance = factory.createBalance(ownerId, ownerType);
        balance = balanceRepository.save(balance);
        return CompletableFuture.completedFuture(balance);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Optional<Balance>> findById(UUID id) {
        return CompletableFuture.completedFuture(balanceRepository.findById(id));
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Optional<Balance>> findByOwnerId(String ownerId) {
        return CompletableFuture.completedFuture(balanceRepository.findByOwnerId(ownerId));
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Optional<Balance>> topUp(String ownerId, double amount) {
        Optional<Balance> balance = balanceRepository.findByOwnerId(ownerId);
        if (balance.isEmpty()) {
            throw new BalanceNotFoundException("Balance not found for owner Id" + ownerId.toString());
        }

        Balance b = balance.get();

        b.setBalance(b.getBalance() + amount);
        balanceRepository.save(b);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setOwnerId(ownerId);
        createdTransaction.setOwnerType(Transaction.OwnerType.valueOf(b.getOwnerType().toString()));
        createdTransaction.setAmount(amount);
        createdTransaction.setTransactionType(Transaction.TransactionType.TOPUP);
        createdTransaction.setBalance(b);
        transactionService.createTransaction(createdTransaction);
        return CompletableFuture.completedFuture(balanceRepository.findById(b.getId()));

    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Optional<Balance>> withdraw(String ownerId, double amount) {
        Optional<Balance> balance = balanceRepository.findByOwnerId(ownerId);

        if (balance.isEmpty()) {
            throw new BalanceNotFoundException("Balance not found for owner Id" + ownerId.toString());
        }

        Balance b = balance.get();

        if (b.getBalance() >= amount) {
            b.setBalance(b.getBalance() - amount);
            balanceRepository.save(b);

            Transaction createdTransaction = new Transaction();
            createdTransaction.setOwnerId(ownerId);
            createdTransaction.setOwnerType(Transaction.OwnerType.valueOf(b.getOwnerType().toString()));
            createdTransaction.setAmount(amount);
            createdTransaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
            createdTransaction.setBalance(b);
            transactionService.createTransaction(createdTransaction);
            return CompletableFuture.completedFuture(balanceRepository.findById(b.getId()));
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<List<Balance>> checkout(String userId, String supermarketId, double amount) {
        Optional<Balance> userBalance = balanceRepository.findByOwnerId(userId);
        Optional<Balance> supermarketBalance = balanceRepository.findByOwnerId(supermarketId);
        List<Balance> response = new ArrayList<>();
        if (userBalance.isPresent() && supermarketBalance.isPresent()) {
            // Execute withdraw and topUp asynchronously and block until they complete
            this.withdraw(userId, amount).join();
            this.topUp(supermarketId, amount);

            // Reload balances from the database to get the updated values
            Optional<Balance> updatedUserBalance = balanceRepository.findByOwnerId(userId);
            Optional<Balance> updatedSupermarketBalance = balanceRepository.findByOwnerId(supermarketId);

            if (updatedUserBalance.isPresent() && updatedSupermarketBalance.isPresent()) {
                response.add(updatedUserBalance.get());
                response.add(updatedSupermarketBalance.get());
            }

            return CompletableFuture.completedFuture(response);
        }

        throw new BalanceNotFoundException("Balance not found for owner or supermarket");
    }
}