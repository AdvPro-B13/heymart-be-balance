package com.heymart.balance.factory;

import com.heymart.balance.model.Balance;

public interface BalanceFactory {
    Balance createBalance(String ownerId, Balance.OwnerType ownerType);
}
