package com.heymart.balance.controller;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.dto.CheckoutDTO;
import com.heymart.balance.enums.BalanceActions;
import com.heymart.balance.enums.OwnerTypes;
import com.heymart.balance.exceptions.BalanceNotFoundException;
import com.heymart.balance.model.Balance;
import com.heymart.balance.service.AuthServiceClient;
import com.heymart.balance.service.BalanceService;
import com.heymart.balance.service.UserServiceClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/balance/")
public class BalanceController {

    private AuthServiceClient authServiceClient;
    private UserServiceClient userServiceClient;
    private final BalanceService service;

    @Autowired
    public BalanceController(BalanceService service,
                             AuthServiceClient authServiceClient,
                             UserServiceClient userServiceClient) {

        this.authServiceClient = authServiceClient;
        this.userServiceClient = userServiceClient;
        this.service = service;
    }

    @GetMapping("")
    public String intro() {
        return "You are currently accessing balance api";
    }

    @GetMapping("/supermarket/{supermarketId}")
    public CompletableFuture<ResponseEntity<Balance>> getSupermarketBalance(
            @PathVariable String supermarketId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(supermarketId, OwnerTypes.SUPERMARKET.getType(),
                authorizationHeader, BalanceActions.SUPERMARKET_BALANCE_READ.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            return service.findByOwnerId(supermarketId)
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PostMapping("/supermarket/{supermarketId}")
    public CompletableFuture<ResponseEntity<Balance>> postCreateSupermarketBalance(
            @PathVariable String supermarketId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(supermarketId, OwnerTypes.SUPERMARKET.getType(),
                authorizationHeader, BalanceActions.SUPERMARKET_BALANCE_CRATE.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            Balance.OwnerType ownerType = Balance.OwnerType.SUPERMARKET;
            return service.createBalance(supermarketId, ownerType)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<Balance>> getUserBalance(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(userId, OwnerTypes.USER.getType(),
                authorizationHeader, BalanceActions.USER_BALANCE_READ.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            return service.findByOwnerId(userId)
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PostMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<Balance>> postCreateUserBalance(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(userId, OwnerTypes.USER.getType(),
                authorizationHeader, BalanceActions.USER_BALANCE_CREATE.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        try {
            Balance.OwnerType ownerType = Balance.OwnerType.USER;
            return service.createBalance(userId, ownerType)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @PutMapping("/checkout")
    public CompletableFuture<ResponseEntity<List<Balance>>> putCheckout(
            @Valid @RequestBody CheckoutDTO checkoutDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(checkoutDTO.getUserId(), OwnerTypes.USER.getType(),
                authorizationHeader, BalanceActions.USER_BALANCE_UPDATE.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            String supermarketId = checkoutDTO.getSupermarketId();
            String userId = checkoutDTO.getUserId();
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
            @Valid @RequestBody AmountDTO amountDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(ownerId, OwnerTypes.BOTH.getType(),
                authorizationHeader, BalanceActions.BOTH_BALANCE_UPDATE.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            return service.topUp(ownerId, amountDTO.getAmount())
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
            @Valid @RequestBody AmountDTO amountDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(ownerId, OwnerTypes.BOTH.getType(),
                authorizationHeader, BalanceActions.BOTH_BALANCE_UPDATE.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try {
            return service.withdraw(ownerId, amountDTO.getAmount())
                    .thenApply(balance -> balance.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.badRequest().build()))
                    .exceptionally(ex -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        } catch (BalanceNotFoundException bnf) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @GetMapping("/testing-valid")
    public ResponseEntity<String> testValid(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(BalanceActions.USER_BALANCE_READ.getValue(), authorizationHeader)) {
            return ResponseEntity.notFound().build();
        }

        if (!userServiceClient.verifyOwnerIdIsOwner(authorizationHeader, "1", OwnerTypes.USER.getType())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    public boolean validateRequest(String ownerId, String ownerType,
                                   String token, String action) {
        if (!authServiceClient.verifyUserAuthorization(action, token)) {
           return false;
        }
        return userServiceClient.verifyOwnerIdIsOwner(token, ownerId, ownerType);
    }
}
