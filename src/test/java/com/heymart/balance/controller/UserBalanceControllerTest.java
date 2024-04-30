package com.heymart.balance.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.heymart.balance.model.User;
import com.heymart.balance.model.UserBalance;
import com.heymart.balance.service.UserBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@WebMvcTest(UserBalanceController.class)
class UserBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBalanceService userBalanceService;

    @Test
    void testGetBalanceById() throws Exception {
        UUID id = UUID.randomUUID();
        UserBalance balance = new UserBalance(new User("testUser"));
        balance.setId(id);
        when(userBalanceService.findById(id)).thenReturn(balance);

        mockMvc.perform(get("/api/user-balance/{id}/", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));  // Assuming your UserBalance has a getter for Id that serializes it.
    }

    @Test
    void testPutTopupBalance_Success() throws Exception {
        UUID id = UUID.randomUUID();
        UserBalance balance = new UserBalance(new User("testUser"));
        balance.setId(id);
        balance.setBalance(10000.0); // initial balance

        double topupAmount = 5000.0;
        balance.setBalance(balance.getBalance() + topupAmount); // new balance after top-up

        when(userBalanceService.findById(id)).thenReturn(balance);
        when(userBalanceService.topup(any(UserBalance.class), anyDouble())).thenReturn(balance);

        mockMvc.perform(put("/api/user-balance/{id}/topup/", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":" + topupAmount + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(15000.0));
    }

    @Test
    void testPutTopupBalance_NotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userBalanceService.findById(id)).thenReturn(null);

        mockMvc.perform(put("/api/user-balance/{id}/topup/", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":5000.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutTopupBalance_BadRequest_InvalidUUID() throws Exception {
        String invalidId = "invalid-uuid";

        mockMvc.perform(put("/api/user-balance/{id}/topup/", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":5000.0}"))
                .andExpect(status().isBadRequest());
    }

        @Test
        void testPutWithdrawBalance_Success() throws Exception {
            UUID id = UUID.randomUUID();
            UserBalance balance = new UserBalance(new User("testUser"));
            balance.setId(id);
            balance.setBalance(10000.0); // initial balance

            double withdrawAmount = 5000.0;
            balance.setBalance(balance.getBalance() - withdrawAmount); // new balance after withdrawal

            when(userBalanceService.findById(id)).thenReturn(balance);
            when(userBalanceService.withdraw(any(UserBalance.class), anyDouble())).thenReturn(balance);

            mockMvc.perform(put("/api/user-balance/{id}/withdraw/", id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"amount\":" + withdrawAmount + "}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.balance").value(5000.0));
        }

    @Test
    void testPutWithdrawBalance_NotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userBalanceService.findById(id)).thenReturn(null);

        mockMvc.perform(put("/api/user-balance/{id}/withdraw/", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":5000.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutWithdrawBalance_InsufficientFunds() throws Exception {
        UUID id = UUID.randomUUID();
        UserBalance balance = new UserBalance(new User("testUser"));
        balance.setId(id);
        balance.setBalance(3000.0); // initial balance

        double withdrawAmount = 5000.0; // More than the balance
        when(userBalanceService.findById(id)).thenReturn(balance);
        when(userBalanceService.withdraw(any(UserBalance.class), eq(withdrawAmount))).thenReturn(null); // Simulate insufficient funds

        mockMvc.perform(put("/api/user-balance/{id}/withdraw/", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":" + withdrawAmount + "}"))
                .andExpect(status().isNotFound()); // Assuming that your controller returns HttpStatus.NOT_FOUND for insufficient funds
    }

        @Test
    void testGetBalanceByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userBalanceService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/user-balance/{id}/", id.toString()))
                .andExpect(status().isNotFound());
    }
}