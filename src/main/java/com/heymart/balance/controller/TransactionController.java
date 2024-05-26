package com.heymart.balance.controller;

import com.heymart.balance.enums.BalanceActions;
import com.heymart.balance.enums.OwnerTypes;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.AuthServiceClient;
import com.heymart.balance.service.TransactionService;
import com.heymart.balance.service.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;
    private AuthServiceClient authServiceClient;
    private UserServiceClient userServiceClient;

    @Autowired
    public TransactionController(TransactionService service,
                                 AuthServiceClient authServiceClient,
                                 UserServiceClient userServiceClient) {

        this.authServiceClient = authServiceClient;
        this.userServiceClient = userServiceClient;
        this.service = service;
    }


    @GetMapping("")
    public String home() {
        return "You are accessing transaction balance api";
    }

    @GetMapping("/item/{id}")
    public CompletableFuture<ResponseEntity<Transaction>> getTransactionById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        try {
            UUID uuid = UUID.fromString(id);
            Optional<Transaction> found = service.findById(uuid).join();

            if (found.isPresent()) {
                boolean validationResult = validateRequest(found.get().getOwnerId(), OwnerTypes.BOTH.getType(),
                        authorizationHeader, BalanceActions.TRANSACTION_READ.getValue());
                if (!validationResult) {
                    return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
                }

                return CompletableFuture.completedFuture(ResponseEntity.ok(found.get()));
            }

            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());

        } catch (Exception ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getOwnerTransactionList(
            @PathVariable String ownerId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean validationResult = validateRequest(ownerId, OwnerTypes.BOTH.getType(),
                authorizationHeader, BalanceActions.TRANSACTION_READ.getValue());
        if (!validationResult) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
        try {
            return service.findByOwnerId(ownerId)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex ->  ResponseEntity.badRequest().build());
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    public boolean validateRequest(String ownerId, String ownerType,
                                   String token, String action) {
        if (!authServiceClient.verifyUserAuthorization(action, token)) {
            return false;
        }
        return userServiceClient.verifyOwnerIdIsOwner(token, ownerId, ownerType);
    }
}
