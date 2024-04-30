package com.heymart.balance.controller;

import com.heymart.balance.dto.TransactionDTO;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/")
    public String home() {
        return "You are accessing transaction balance api";
    }

    @GetMapping("/item/{id}/")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Transaction transaction = service.findById(uuid);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/owner/{ownerId}/")
    public ResponseEntity<List<Transaction>> getOwnerTransactionList(@PathVariable String ownerId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(ownerId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        List<Transaction> result = service.findByOwnerId(uuid);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/owner/{ownerId}/")
    public ResponseEntity<Transaction> postOwnerTransaction(
            @PathVariable String ownerId,
            @Valid @RequestBody TransactionDTO transactionDTO) {

        UUID uuid;
        try {
            uuid = UUID.fromString(ownerId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setOwnerId(uuid);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(Transaction.TransactionType.valueOf(transactionDTO.getTransactionType().toUpperCase()));
        transaction.setTransactionDate(new Date());

        transaction = service.createTransaction(transaction);
        return ResponseEntity.ok(transaction);
    }
}
