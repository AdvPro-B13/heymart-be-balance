package com.heymart.balance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutDTO {
    @Min(value = 1, message = "The amount must be greater than 0")
    private double amount;
    @NotNull(message = "Transaction supermarketId cannot be null")
    private String supermarketId;
    @NotNull(message = "Transaction userId cannot be null")
    private String userId;

    public CheckoutDTO(double amount, String supermarketId, String userId) {
        this.amount = amount;
        this.supermarketId = supermarketId;
        this.userId = userId;
    }

}
