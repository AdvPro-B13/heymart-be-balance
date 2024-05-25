package com.heymart.balance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    @Min(value = 1, message = "The amount must be greater than 0")
    private double amount;

    @NotNull(message = "Transaction type cannot be null")
    private String transactionType;

    @NotNull(message = "Owner type cannot be null")
    private String ownerType;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, String transactionType, String ownerType) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.ownerType = ownerType;
    }
}
