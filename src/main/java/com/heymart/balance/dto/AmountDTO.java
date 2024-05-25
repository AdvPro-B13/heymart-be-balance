package com.heymart.balance.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmountDTO {
    
    @Min(value = 0, message = "The amount must be positive")
    private double amount;
}
