package com.heymart.balance.controller;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.dto.CheckoutDTO;
import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.BalanceService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
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
            Balance.OwnerType ownerType = Balance.OwnerType.SUPERMARKET;
            Balance savedBalance = service.createBalance(ownerId, ownerType);
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
            Balance.OwnerType ownerType = Balance.OwnerType.USER;
            Balance savedBalance = service.createBalance(ownerId, ownerType);
            return ResponseEntity.ok(savedBalance);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("invalid arguments");
        }
    }

    @PutMapping("/checkout")
    public ResponseEntity<?> putCheckout(
            @Valid @RequestBody CheckoutDTO checkoutDTO) {

        try {
            UUID supermarketId = UUID.fromString(checkoutDTO.getSupermarketId());
            UUID userId = UUID.fromString(checkoutDTO.getUserId());
            double amount = checkoutDTO.getAmount();

            Optional<Balance> userBalance = service.findByOwnerId(userId);
            Optional<Balance> supermarketBalance = service.findByOwnerId(supermarketId);

            if (userBalance.isPresent() && supermarketBalance.isPresent()) {
                List<Balance> response = new ArrayList<>();
                service.withdraw(userId, amount);
                service.topUp(supermarketId, amount);
                response.add(userBalance.get());
                response.add(supermarketBalance.get());
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
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
            return ResponseEntity.badRequest().body("invalid a  rguments");
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
