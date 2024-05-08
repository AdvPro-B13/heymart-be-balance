package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Supermarket {
    private String name;
    private UUID id;

    public Supermarket(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
