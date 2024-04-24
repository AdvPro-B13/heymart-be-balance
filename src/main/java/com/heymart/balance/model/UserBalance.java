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

    public UserBalance(User user, double balance) {
        if (user == null) {
            throw new IllegalArgumentException();
        } else {
            this.owner = user;
            super.setBalance(balance);
        }
    }
}
