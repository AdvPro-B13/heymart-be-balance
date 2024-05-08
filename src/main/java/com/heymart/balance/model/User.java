package com.heymart.balance.model;

import lombok.Setter;
import lombok.Getter;

import java.util.UUID;

@Setter
@Getter
public class User {
    private String name;
    private UUID id;

    public User(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
