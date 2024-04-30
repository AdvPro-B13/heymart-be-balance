package com.heymart.balance.controller;

import com.heymart.balance.dto.TransactionDTO;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void getTransactionById_whenTransactionExists() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(id, new Date(), 100.0, Transaction.TransactionType.TOPUP);
        transaction.setId(id);
        
        given(transactionService.findById(id)).willReturn(transaction);
        
        mockMvc.perform(get("/api/transaction/item/" + id.toString() + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void getTransactionById_whenTransactionDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(transactionService.findById(id)).willReturn(null);
        
        mockMvc.perform(get("/api/transaction/item/" + id.toString() + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postOwnerTransaction_createsTransaction() throws Exception {
        UUID ownerId = UUID.randomUUID();
        TransactionDTO dto = new TransactionDTO(100.0, "TOPUP");
        Transaction transaction = new Transaction(ownerId, new Date(), 100.0, Transaction.TransactionType.TOPUP);
        
        given(transactionService.createTransaction(any(Transaction.class))).willReturn(transaction);

        mockMvc.perform(post("/api/transaction/owner/" + ownerId.toString() + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"transactionType\":\"TOPUP\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    public void getOwnerTransactionList_returnsTransactions() throws Exception {
        UUID ownerId = UUID.randomUUID();
        List<Transaction> transactions = Collections.singletonList(
            new Transaction(ownerId, new Date(), 200.0, Transaction.TransactionType.WITHDRAWAL)
        );

        given(transactionService.findByOwnerId(ownerId)).willReturn(transactions);

        mockMvc.perform(get("/api/transaction/owner/" + ownerId.toString() + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ownerId").value(ownerId.toString()));
    }
}
