package com.heymart.balance.model;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }
}
