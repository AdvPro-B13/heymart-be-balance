package com.heymart.balance.controller;

import com.heymart.balance.dto.TransactionDTO;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.BalanceService;
import com.heymart.balance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }


    @GetMapping("/")
    public String home() {
        return "You are accessing transaction balance api";
    }

    @GetMapping("/item/{id}/")
    public CompletableFuture<ResponseEntity<Transaction>> getTransactionById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return service.findById(uuid)
                    .thenApply(transaction -> transaction.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex ->  ResponseEntity.badRequest().build());

        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @GetMapping("/owner/{ownerId}/")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getOwnerTransactionList(@PathVariable String ownerId) {
        try {
            UUID uuid = UUID.fromString(ownerId);
            return service.findByOwnerId(uuid)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex ->  ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    private Transaction convertToEntity(TransactionDTO dto, UUID ownerId) {
        Transaction transaction = new Transaction();
        transaction.setOwnerId(ownerId);
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType(Transaction.TransactionType.valueOf(dto.getTransactionType().toUpperCase()));
        transaction.setOwnerType(Transaction.OwnerType.valueOf(dto.getOwnerType().toUpperCase()));
        transaction.setTransactionDate(new Date());
        return transaction;
    }
}
