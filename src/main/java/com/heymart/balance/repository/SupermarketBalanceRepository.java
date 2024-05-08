package com.heymart.balance.repository;

import com.heymart.balance.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public class SupermarketBalanceRepository {

    private final List<SupermarketBalance> SupermarketBalances = new ArrayList<>();

    public SupermarketBalance save(SupermarketBalance SupermarketBalance) {
        int i = 0;
        for (SupermarketBalance savedSupermarketBalance : SupermarketBalances) {
            if (savedSupermarketBalance.getId().equals(SupermarketBalance.getId())) {
                SupermarketBalances.remove(i);
                SupermarketBalances.add(i, SupermarketBalance);
                return SupermarketBalance;
            }
            i += 1;
        }
        SupermarketBalances.add(SupermarketBalance);
        return SupermarketBalance;
    }

    public SupermarketBalance findById(UUID id) {
        for (SupermarketBalance savedSupermarketBalance : SupermarketBalances) {
            if (savedSupermarketBalance.getId().equals(id)) {
                return savedSupermarketBalance;
            }
        }
        return null;
    }

    public SupermarketBalance findBySupermarketId(UUID uid) {
        for (SupermarketBalance savedSupermarketBalance : SupermarketBalances) {
            if (savedSupermarketBalance.getOwner().getId().equals(uid)) {
                return savedSupermarketBalance;
            }
        }

        return null;
    }

    public List<SupermarketBalance> getAllSupermarketBalance() {
        return new ArrayList<>(SupermarketBalances);
    }
}
