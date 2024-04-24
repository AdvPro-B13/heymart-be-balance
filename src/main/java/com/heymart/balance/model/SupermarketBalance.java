package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

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
}
