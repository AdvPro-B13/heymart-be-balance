//package com.heymart.balance.controller;
//
//import com.heymart.balance.model.Balance;
//import com.heymart.balance.service.AuthServiceClient;
//import com.heymart.balance.service.BalanceService;
//import com.heymart.balance.service.UserServiceClient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class BalanceControllerTest {
//
//    @Mock
//    private AuthServiceClient authServiceClient;
//
//    @Mock
//    private UserServiceClient userServiceClient;
//
//    @Mock
//    private BalanceService balanceService;
//
//    @InjectMocks
//    private BalanceController balanceController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getSupermarketBalance_AuthorizationFails() {
//        // Setup
//        String supermarketId = "123";
//        String authorizationHeader = "Bearer invalidToken";
//        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);
//
//        // Act
//        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getSupermarketBalance(supermarketId, authorizationHeader);
//
//        // Assert
//        ResponseEntity<Balance> response = future.join();
//        assertEquals(400, response.getStatusCodeValue());
//    }
//
//    @Test
//    public void getSupermarketBalance_NotFound() {
//        // Setup
//        String supermarketId = "123";
//        String authorizationHeader = "Bearer validToken";
//        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
//        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(supermarketId), anyString())).thenReturn(true);
//        when(balanceService.findByOwnerId(supermarketId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));
//
//        // Act
//        CompletableFuture<ResponseEntity<Balance>> future = balanceController.getSupermarketBalance(supermarketId, authorizationHeader);
//
//        // Assert
//        ResponseEntity<Balance> response = future.join();
//        assertEquals(404, response.getStatusCodeValue());
//    }
//
//    @Test
//    public void postCreateSupermarketBalance_ValidRequest() {
//        // Setup
//        String supermarketId = "123";
//        String authorizationHeader = "Bearer validToken";
//        Balance balance = new Balance();  // Assuming you have a default constructor
//        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(true);
//        when(userServiceClient.verifyOwnerIdIsOwner(anyString(), eq(supermarketId), anyString())).thenReturn(true);
//        when(balanceService.createBalance(eq(supermarketId), any())).thenReturn(CompletableFuture.completedFuture(balance));
//
//        // Act
//        CompletableFuture<ResponseEntity<Balance>> future = balanceController.postCreateSupermarketBalance(supermarketId, authorizationHeader);
//
//        // Assert
//        ResponseEntity<Balance> response = future.join();
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    public void postCreateSupermarketBalance_AuthorizationFails() {
//        // Setup
//        String supermarketId = "123";
//        String authorizationHeader = "Bearer invalidToken";
//        when(authServiceClient.verifyUserAuthorization(anyString(), eq(authorizationHeader))).thenReturn(false);
//
//        // Act
//        CompletableFuture<ResponseEntity<Balance>> future = balanceController.postCreateSupermarketBalance(supermarketId, authorizationHeader);
//
//        // Assert
//        ResponseEntity<Balance> response = future.join();
//        assertEquals(400, response.getStatusCodeValue());
//    }
//}
