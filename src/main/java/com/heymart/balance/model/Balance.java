package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Balance {
    private UUID id;
    private Double balance;

    public Balance() {
        this.id = UUID.randomUUID();
        this.balance = 0.0;
    }
}

