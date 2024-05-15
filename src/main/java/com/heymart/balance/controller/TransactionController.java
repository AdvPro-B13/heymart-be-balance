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

@CrossOrigin(origins = "http://localhost:3000/")
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
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return service.findById(uuid)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }

    @GetMapping("/owner/{ownerId}/")
    public ResponseEntity<?> getOwnerTransactionList(@PathVariable String ownerId) {
        try {
            UUID uuid = UUID.fromString(ownerId);
            List<Transaction> result = service.findByOwnerId(uuid);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    @PostMapping("/owner/{ownerId}/")
    public ResponseEntity<?> postOwnerTransaction(
            @PathVariable String ownerId,
            @Valid @RequestBody TransactionDTO transactionDTO) {
        try {
            UUID uuid = UUID.fromString(ownerId);
            Transaction transaction = convertToEntity(transactionDTO, uuid);
            System.out.println("Creating transaction with ownerType: " + transaction.getOwnerType());
            Transaction savedTransaction = service.createTransaction(transaction);
            return ResponseEntity.ok(savedTransaction);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
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
