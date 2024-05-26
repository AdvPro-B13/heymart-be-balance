package com.heymart.balance.controller;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.dto.CheckoutDTO;
import com.heymart.balance.model.Balance;
import com.heymart.balance.service.AuthServiceClient;
import com.heymart.balance.service.BalanceService;
import com.heymart.balance.service.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BalanceControllerTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceController balanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSupermarketBalance_AuthorizationFails() {
        // Setup
        String supermarketId = "123";
        String authorizationHeader = "Bearer invalidToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getSupermarketBalance(supermarketId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getSupermarketBalance_NotFound() {
        // Setup
        String supermarketId = "123";
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(supermarketId), anyString())).thenReturn(true);
        when(balanceService.findByOwnerId(supermarketId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getSupermarketBalance(supermarketId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void postCreateSupermarketBalance_ValidRequest() {
        // Setup
        String supermarketId = "123";
        String authorizationHeader = "Bearer validToken";
        Balance balance = new Balance();  // Assuming you have a default constructor
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(supermarketId), anyString())).thenReturn(true);
        when(balanceService.createBalance(eq(supermarketId), any())).thenReturn(CompletableFuture.completedFuture(balance));

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.postCreateSupermarketBalance(supermarketId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void postCreateSupermarketBalance_AuthorizationFails() {
        // Setup
        String supermarketId = "123";
        String authorizationHeader = "Bearer invalidToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.postCreateSupermarketBalance(supermarketId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getUserBalance_NotFound() {
        // Setup
        String userId = "user123";
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(userId), anyString())).thenReturn(true);
        when(balanceService.findByOwnerId(userId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getUserBalance(userId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getUserBalance_AuthorizationFails() {
        // Setup
        String userId = "user123";
        String authorizationHeader = "Bearer invalidToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getUserBalance(userId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void putTopupBalance_Success() {
        // Setup
        String ownerId = "owner123";
        double amount = 50.0;
        Balance balance = new Balance();
        balance.setAmount(100.0); // Assume existing balance
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(ownerId), anyString())).thenReturn(true);
        doReturn(CompletableFuture.completedFuture(Optional.of(balance))).when(balanceService).topUp(any(String.class), any(double.class));

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.putTopupBalance(ownerId, new AmountDTO(), authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void putTopupBalance_AuthorizationFails() {
        // Setup
        String ownerId = "owner123";
        double amount = 50.0;
        String authorizationHeader = "Bearer invalidToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.putTopupBalance(ownerId, new AmountDTO(), authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void putWithdrawBalance_NotEnoughFunds() {
        // Setup
        String ownerId = "owner123";
        double amount = 150.0; // Attempt to withdraw more than available
        Balance balance = new Balance();
        balance.setAmount(100.0); // Current balance
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(ownerId), anyString())).thenReturn(true);
        when(balanceService.withdraw(any(String.class), any(double.class))).thenThrow(new IllegalArgumentException());

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.putWithdrawBalance(ownerId, new AmountDTO(), authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(400, response.getStatusCodeValue()); // Assuming that the API returns a bad request or similar error on failure
    }

    @Test
    void putCheckout_Success() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(100.0, "supermarketId", "userID");
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), any(String.class))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), anyString(), anyString())).thenReturn(true);
        when(balanceService.checkout(checkoutDTO.getUserId(), checkoutDTO.getSupermarketId(), checkoutDTO.getAmount()))
                .thenReturn(CompletableFuture.completedFuture(Arrays.asList(new Balance(), new Balance())));

        CompletableFuture<ResponseEntity<List<Balance>>> future = balanceController.putCheckout(checkoutDTO, authorizationHeader);
        ResponseEntity<List<Balance>> response = future.join();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void putCheckout_Failure() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(100.0, "supermarketId", "userID");
        String authorizationHeader = "Bearer invalidToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);

        CompletableFuture<ResponseEntity<List<Balance>>> future = balanceController.putCheckout(checkoutDTO, authorizationHeader);
        ResponseEntity<List<Balance>> response = future.join();

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void putWithdrawBalance_ExceptionHandling() {
        String ownerId = "owner123";
        double amount = 50.0;
        String authorizationHeader = "Bearer validToken";
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(balanceService.withdraw(eq(ownerId), eq(amount))).thenThrow(new RuntimeException("Unexpected error"));

        CompletableFuture<ResponseEntity<Balance>> future = balanceController.putWithdrawBalance(ownerId, new AmountDTO(), authorizationHeader);
        ResponseEntity<Balance> response = future.join();

        assertEquals(400, response.getStatusCodeValue(), "Should handle exceptions and return 400 Bad Request");
    }

    @Test
    void postCreateUserBalance_Success() {
        // Setup
        String userId = "user123";
        String authorizationHeader = "Bearer validToken";
        Balance newBalance = new Balance();  // Assuming you have a default constructor or an initialized object
        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(userId), anyString())).thenReturn(true);
        when(balanceService.createBalance(eq(userId), any())).thenReturn(CompletableFuture.completedFuture(newBalance));

        // Act
        CompletableFuture<ResponseEntity<Balance>> future = balanceController.postCreateUserBalance(userId, authorizationHeader);

        // Assert
        ResponseEntity<Balance> response = future.join();
        assertEquals(200, response.getStatusCodeValue(), "Should return 200 OK when balance creation is successful");
        assertNotNull(response.getBody(), "The body should not be null when balance creation is successful");
    }
}
