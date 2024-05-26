package com.heymart.balance.enums;

import lombok.Getter;

@Getter
public enum OwnerTypes {
    SUPERMARKET("Supermarket"),
    USER("User"),
    BOTH("Both");
    private final String type;
    private OwnerTypes(String type) {
        this.type = type;
    }
}
