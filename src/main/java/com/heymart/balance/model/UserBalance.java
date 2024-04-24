package com.heymart.balance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBalance extends Balance {
    private User owner;
}
