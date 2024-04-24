package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBalance extends Balance {
    private User owner;

    public UserBalance(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        } else {
            owner = user;
        }
    }
}
