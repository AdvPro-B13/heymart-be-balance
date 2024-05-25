package com.heymart.balance.factory;

import com.heymart.balance.model.Balance;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class UserBalanceFactory implements BalanceFactory {
    @Override
    public Balance createBalance(UUID ownerId, Balance.OwnerType ownerType) {
        if (!ownerType.equals(Balance.OwnerType.USER)) {
            throw new IllegalArgumentException("Owner type mismatch for user balance creation.");
        }
        return new Balance(ownerId, ownerType);
    }
}