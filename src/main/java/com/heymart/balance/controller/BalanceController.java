package com.heymart.balance.controller;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.dto.CheckoutDTO;
import com.heymart.balance.exceptions.BalanceNotFoundException;
import com.heymart.balance.model.Balance;
import com.heymart.balance.service.BalanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/balance/")
public class BalanceController {

    private final BalanceService service;

    @Autowired
    public BalanceController(BalanceService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String intro() {
        return "You are currently accessing balance api";
    }

    @GetMapping("/item/{id}")
    public CompletableFuture<ResponseEntity<Balance>> getBalanceById(@PathVariable String id) {
        try {
            UUID balanceId = UUID.fromString(id);
            return service.findById(balanceId)
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @GetMapping("/supermarket/{supermarketId}")
    public CompletableFuture<ResponseEntity<Balance>> getSupermarketBalance(@PathVariable String supermarketId) {
        try {
            UUID ownerId = UUID.fromString(supermarketId);
            return service.findByOwnerId(ownerId)
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PostMapping("/supermarket/{supermarketId}")
    public CompletableFuture<ResponseEntity<Balance>> postCreateSupermarketBalance(
            @PathVariable String supermarketId) {
        try {
            UUID ownerId = UUID.fromString(supermarketId);
            Balance.OwnerType ownerType = Balance.OwnerType.SUPERMARKET;
            return service.createBalance(ownerId, ownerType)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<Balance>> getUserBalance(@PathVariable String userId) {
        try {
            UUID ownerId = UUID.fromString(userId);
            return service.findByOwnerId(ownerId)
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PostMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<Balance>> postCreateUserBalance(
            @PathVariable String userId) {
        try {
            UUID ownerId = UUID.fromString(userId);
            Balance.OwnerType ownerType = Balance.OwnerType.USER;
            return service.createBalance(ownerId, ownerType)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PutMapping("/checkout")
    public CompletableFuture<ResponseEntity<List<Balance>>> putCheckout(
            @Valid @RequestBody CheckoutDTO checkoutDTO) {

        try {
            UUID supermarketId = UUID.fromString(checkoutDTO.getSupermarketId());
            UUID userId = UUID.fromString(checkoutDTO.getUserId());
            double amount = checkoutDTO.getAmount();

           return service.checkout(userId, supermarketId, amount)
                   .thenApply(ResponseEntity::ok)
                   .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }

    }

    @PutMapping("/topup/{ownerId}")
    public CompletableFuture<ResponseEntity<Balance>> putTopupBalance(
            @PathVariable String ownerId,
            @Valid @RequestBody AmountDTO amountDTO) {

        try {
            UUID ownerUid = UUID.fromString(ownerId);
            return service.topUp(ownerUid, amountDTO.getAmount())
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.badRequest().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        } catch (BalanceNotFoundException bnf) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @PutMapping("/withdraw/{ownerId}")
    public CompletableFuture<ResponseEntity<Balance>> putWithdrawBalance(
            @PathVariable String ownerId,
            @Valid @RequestBody AmountDTO amountDTO) {

        try {
            UUID ownerUid = UUID.fromString(ownerId);
            return service.withdraw(ownerUid, amountDTO.getAmount())
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.badRequest().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        } catch (BalanceNotFoundException bnf) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
