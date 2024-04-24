package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SupermarketBalance extends Balance {
    private Supermarket owner;

    public SupermarketBalance(Supermarket supermarket) {
        if (supermarket == null) {
            throw new IllegalArgumentException();
        } else {
            this.owner = supermarket;
        }
    }

    public SupermarketBalance(Supermarket supermarket, double balance) {
        if (supermarket == null) {
            throw new IllegalArgumentException();
        } else {
            this.owner = supermarket;
            super.setBalance(balance);
        }
    }
}
