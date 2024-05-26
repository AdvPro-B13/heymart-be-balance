package com.heymart.balance.service;

import com.heymart.balance.dto.UserDTO;
import com.heymart.balance.exceptions.BalanceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceClientImpl userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void verifyOwnerIdIsOwner_WhenTokenOrOwnerIdIsNull() {
        assertFalse(userService.verifyOwnerIdIsOwner(null, "ownerId", "user"),
                "Should return false if token is null");

        assertFalse(userService.verifyOwnerIdIsOwner("token", null, "user"),
                "Should return false if ownerId is null");
    }

    @Test
    void verifyOwnerIdIsOwner_WhenUserTypeIsSupermarket() {
        String token = "Bearer validToken";
        String ownerId = "supermarket123";
        UserDTO userDTO = new UserDTO("user123", "supermarket123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(userDTO, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(UserDTO.class)))
                .thenReturn(responseEntity);

        assertTrue(userService.verifyOwnerIdIsOwner(token, ownerId, "supermarket"),
                "Should return true if ownerId matches the supermarketId in the UserDTO");
    }

    @Test
    void verifyOwnerIdIsOwner_WhenUserTypeIsUser() {
        String token = "Bearer validToken";
        String ownerId = "user123";
        UserDTO userDTO = new UserDTO("user123", "supermarket123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(userDTO, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(UserDTO.class)))
                .thenReturn(responseEntity);

        assertTrue(userService.verifyOwnerIdIsOwner(token, ownerId, "user"),
                "Should return true if ownerId matches the id in the UserDTO");
    }
}
