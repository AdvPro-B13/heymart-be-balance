package com.heymart.balance.controller;

import com.heymart.balance.enums.BalanceActions;
import com.heymart.balance.enums.OwnerTypes;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.AuthServiceClient;
import com.heymart.balance.service.TransactionService;
import com.heymart.balance.service.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Previous tests here...

    @Test
    public void getOwnerTransactionList_ExceptionHandling() {
        // Setup
        String ownerId = "ownerId";
        String authorizationHeader = "Bearer validToken";

        when(authServiceClient.verifyUserAuthorization(BalanceActions.TRANSACTION_READ.getValue(), authorizationHeader)).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(authorizationHeader, ownerId, OwnerTypes.BOTH.getType())).thenReturn(true);
        when(transactionService.findByOwnerId(ownerId)).thenThrow(new RuntimeException("Database error"));

        // Act
        CompletableFuture<ResponseEntity<List<Transaction>>> future = transactionController.getOwnerTransactionList(ownerId, authorizationHeader);

        // Assert
        ResponseEntity<List<Transaction>> response = future.join();
        assertEquals(400, response.getStatusCodeValue()); // Assert the controller responds with bad request on exception
    }

    @Test
    public void validateRequest_FailsOnAuthService() {
        // Setup
        String ownerId = "ownerId";
        String ownerType = OwnerTypes.BOTH.getType();
        String token = "Bearer invalidToken";
        String action = BalanceActions.TRANSACTION_READ.getValue();

        when(authServiceClient.verifyUserAuthorization(action, token)).thenReturn(false);

        // Act
        boolean result = transactionController.validateRequest(ownerId, ownerType, token, action);

        // Assert
        assertFalse(result);
    }

    @Test
    public void validateRequest_FailsOnUserService() {
        // Setup
        String ownerId = "ownerId";
        String ownerType = OwnerTypes.BOTH.getType();
        String token = "Bearer validToken";
        String action = BalanceActions.TRANSACTION_READ.getValue();

        when(authServiceClient.verifyUserAuthorization(action, token)).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(token, ownerId, ownerType)).thenReturn(false);

        // Act
        boolean result = transactionController.validateRequest(ownerId, ownerType, token, action);

        // Assert
        assertFalse(result);
    }

    @Test
    public void validateRequest_PassesAllChecks() {
        // Setup
        String ownerId = "ownerId";
        String ownerType = OwnerTypes.BOTH.getType();
        String token = "Bearer validToken";
        String action = BalanceActions.TRANSACTION_READ.getValue();

        when(authServiceClient.verifyUserAuthorization(action, token)).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(token, ownerId, ownerType)).thenReturn(true);

        // Act
        boolean result = transactionController.validateRequest(ownerId, ownerType, token, action);

        // Assert
        assertTrue(result);
    }
}
