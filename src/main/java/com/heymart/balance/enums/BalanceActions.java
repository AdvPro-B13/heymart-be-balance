package com.heymart.balance.enums;

import lombok.Getter;

@Getter
public enum BalanceActions {
    USER_BALANCE_CREATE("balance:ub_create"),
    SUPERMARKET_BALANCE_CRATE("balance:sb_create"),
    USER_BALANCE_READ("balance:ub_read"),
    SUPERMARKET_BALANCE_READ("balance:sb_read"),
    USER_BALANCE_UPDATE("balance:ub_update"),
    SUPERMARKET_BALANCE_UPDATE("balance:sb_update"),
    TRANSACTION_READ("transaction:read"),
    BOTH_BALANCE_UPDATE("balance:both_update");

    private final String value;
    private BalanceActions(String value) {
        this.value = value;
    }
}
