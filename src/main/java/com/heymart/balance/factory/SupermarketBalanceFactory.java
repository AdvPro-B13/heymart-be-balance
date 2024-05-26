package com.heymart.balance.factory;

import com.heymart.balance.model.Balance;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class SupermarketBalanceFactory implements BalanceFactory {
    @Override
    public Balance createBalance(String ownerId, Balance.OwnerType ownerType) {
        if (!ownerType.equals(Balance.OwnerType.SUPERMARKET)) {
            throw new IllegalArgumentException(("Owner type mismatch for supermarket balance creation"));
        }
        return new Balance(ownerId, ownerType);
    }
}