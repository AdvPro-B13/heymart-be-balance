package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketBalance extends Balance {
    private Supermarket owner;
}
