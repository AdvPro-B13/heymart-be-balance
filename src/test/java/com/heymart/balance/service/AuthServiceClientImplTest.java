package com.heymart.balance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthServiceClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthServiceClientImpl authService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void verifyUserAuthorization_WhenHeaderIsNull() {
        assertFalse(authService.verifyUserAuthorization("login", null),
                "Should return false if authorization header is null");
    }

    @Test
    void verifyUserAuthorization_WhenServerReturnsOk() {
        String action = "login";
        String authorizationHeader = "Bearer validToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"" + action + "\"}", headers);

        when(restTemplate.postForEntity(any(String.class), eq(entity), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertTrue(authService.verifyUserAuthorization(action, authorizationHeader),
                "Should return true when the server returns HttpStatus.OK");
    }

    @Test
    void verifyUserAuthorization_WhenServerReturnsUnauthorized() {
        String action = "login";
        String authorizationHeader = "Bearer invalidToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"" + action + "\"}", headers);

        when(restTemplate.postForEntity(any(String.class), eq(entity), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

        assertFalse(authService.verifyUserAuthorization(action, authorizationHeader),
                "Should return false when the server returns HttpStatus.UNAUTHORIZED");
    }

    @Test
    void verifyUserAuthorization_WhenRestTemplateThrowsException() {
        String action = "login";
        String authorizationHeader = "Bearer validToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"" + action + "\"}", headers);

        when(restTemplate.postForEntity(any(String.class), eq(entity), eq(String.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        assertFalse(authService.verifyUserAuthorization(action, authorizationHeader),
                "Should return false when restTemplate throws an exception");
    }
}
