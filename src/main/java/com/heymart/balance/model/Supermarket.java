package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Supermarket {
    private String name;

    public Supermarket(String name) {
        this.name = name;
    }
}
