package com.heymart.balance.controller;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.model.Balance;
import com.heymart.balance.service.BalanceService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/balance/")
public class BalanceController {

    @Autowired
    private BalanceService service;

    @GetMapping("/")
    public String intro() {
        return "You are currently accessing balance api";
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> getBalanceById(@PathVariable String id) {
        try {
            UUID balanceId = UUID.fromString(id);
            return service.findById(balanceId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    @GetMapping("/supermarket/{supermarketId}")
    public ResponseEntity<?> getSupermarketBalance(@PathVariable String supermarketId) {
        try {
            UUID ownerId = UUID.fromString(supermarketId);
            return service.findByOwnerId(ownerId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    @PostMapping("/supermarket/{supermarketId}")
    public ResponseEntity<?> postCreateSupermarketBalance(
            @PathVariable String supermarketId) {
        try {
            UUID ownerId = UUID.fromString(supermarketId);
            Balance newSupermarketBalance = new Balance();
            newSupermarketBalance.setOwnerId(ownerId);
            newSupermarketBalance.setOwnerType(Balance.OwnerType.SUPERMARKET);
            newSupermarketBalance.setBalance(0.0);
            Balance savedBalance = service.createBalance(newSupermarketBalance);
            return ResponseEntity.ok(savedBalance);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("invalid arguments");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBalance(@PathVariable String userId) {
        try {
            UUID ownerId = UUID.fromString(userId);
            return service.findByOwnerId(ownerId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> postCreateUserBalance(
            @PathVariable String userId) {
        try {
            UUID ownerId = UUID.fromString(userId);
            Balance newUserBalance = new Balance();
            newUserBalance.setOwnerId(ownerId);
            newUserBalance.setOwnerType(Balance.OwnerType.USER);
            newUserBalance.setBalance(0.0);
            Balance savedBalance = service.createBalance(newUserBalance);
            return ResponseEntity.ok(savedBalance);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("invalid arguments");
        }
    }

    @PutMapping("/topup/{ownerId}")
    public ResponseEntity<?> putTopupBalance(
            @PathVariable String ownerId,
            @Valid @RequestBody AmountDTO amountDTO) {

        try {
            UUID ownerUid = UUID.fromString(ownerId);
            Optional<Balance> targetBalance = service.findByOwnerId(ownerUid);
            if (targetBalance.isPresent()) {
                return service.topUp(ownerUid, amountDTO.getAmount()).
                        map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.badRequest().build());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("invalid arguments");
        }
    }

    @PutMapping("/withdraw/{ownerId}")
    public ResponseEntity<?> putWithdrawBalance(
            @PathVariable String ownerId,
            @Valid @RequestBody AmountDTO amountDTO) {

        try {
            UUID ownerUid = UUID.fromString(ownerId);
            Optional<Balance> targetBalance = service.findByOwnerId(ownerUid);
            if (targetBalance.isPresent()) {
                return service.withdraw(ownerUid, amountDTO.getAmount())
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.badRequest().build());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("invalid arguments");
        }
    }
}
