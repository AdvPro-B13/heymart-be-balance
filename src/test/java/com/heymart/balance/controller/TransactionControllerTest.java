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

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Previous tests here...

    @Test
    void getOwnerTransactionList_ExceptionHandling() {
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
    void validateRequest_FailsOnAuthService() {
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
    void validateRequest_FailsOnUserService() {
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
    void validateRequest_PassesAllChecks() {
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

    @Test
    void getTransactionById_ValidIdNotFound() {
        // Setup
        String id = UUID.randomUUID().toString();
        String authorizationHeader = "Bearer validToken";
        when(transactionService.findById(UUID.fromString(id))).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // Act
        CompletableFuture<ResponseEntity<Transaction>> result = transactionController.getTransactionById(id, authorizationHeader);

        // Assert
        assertEquals(404, result.join().getStatusCodeValue(), "Should return 404 Not Found when transaction is not found");
    }

    @Test
    void getTransactionById_ValidIdFound() {
        // Setup
        String id = UUID.randomUUID().toString();
        String authorizationHeader = "Bearer validToken";
        Transaction transaction = new Transaction();
        transaction.setOwnerId(id);// Assuming constructor or setters to set up a mock transaction
        when(transactionService.findById(UUID.fromString(id))).thenReturn(CompletableFuture.completedFuture(Optional.of(transaction)));
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), anyString(), anyString())).thenReturn(true);

        // Act
        CompletableFuture<ResponseEntity<Transaction>> result = transactionController.getTransactionById(id, authorizationHeader);

        // Assert
        assertEquals(200, result.join().getStatusCodeValue(), "Should return 200 OK when transaction is found");
        assertNotNull(result.join().getBody(), "Transaction body should not be null");
    }

    @Test
    void getOwnerTransactionList_ValidRequest() {
        // Setup
        String ownerId = "ownerId";
        String authorizationHeader = "Bearer validToken";
        List<Transaction> transactionList = Arrays.asList(new Transaction(), new Transaction());
        when(authServiceClient.verifyUserAuthorization(BalanceActions.TRANSACTION_READ.getValue(), authorizationHeader)).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(authorizationHeader, ownerId, OwnerTypes.BOTH.getType())).thenReturn(true);
        when(transactionService.findByOwnerId(ownerId)).thenReturn(CompletableFuture.completedFuture(transactionList));

        // Act
        CompletableFuture<ResponseEntity<List<Transaction>>> future = transactionController.getOwnerTransactionList(ownerId, authorizationHeader);

        // Assert
        ResponseEntity<List<Transaction>> response = future.join();
        assertEquals(200, response.getStatusCodeValue(), "Should return 200 OK when transactions are found");
        assertEquals(2, response.getBody().size(), "Should return the correct number of transactions");
    }

    @Test
    void getOwnerTransactionList_EmptyList() {
        // Setup
        String ownerId = "ownerId";
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(BalanceActions.TRANSACTION_READ.getValue(), authorizationHeader)).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(authorizationHeader, ownerId, OwnerTypes.BOTH.getType())).thenReturn(true);
        when(transactionService.findByOwnerId(ownerId)).thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        // Act
        CompletableFuture<ResponseEntity<List<Transaction>>> future = transactionController.getOwnerTransactionList(ownerId, authorizationHeader);

        // Assert
        ResponseEntity<List<Transaction>> response = future.join();
        assertEquals(200, response.getStatusCodeValue(), "Should return 200 OK even if the list is empty");
        assertTrue(response.getBody().isEmpty(), "Transaction list should be empty");
    }
}
