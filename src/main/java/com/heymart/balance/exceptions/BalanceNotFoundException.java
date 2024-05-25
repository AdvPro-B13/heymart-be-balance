package com.heymart.balance.exceptions;

public class BalanceNotFoundException extends RuntimeException{
    public BalanceNotFoundException(String message) {
        super(message);
    }
}
