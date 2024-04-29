package com.heymart.balance.repository;

import com.heymart.balance.model.UserBalance;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserBalanceRepository {

    private final List<UserBalance> UserBalances = new ArrayList<>();

    public UserBalance save(UserBalance UserBalance) {
        int i = 0;
        for (UserBalance savedUserBalance : UserBalances) {
            if (savedUserBalance.getId().equals(UserBalance.getId())) {
                UserBalances.remove(i);
                UserBalances.add(i, UserBalance);
                return UserBalance;
            }
            i += 1;
        }
        UserBalances.add(UserBalance);
        return UserBalance;
    }

    public UserBalance findById(UUID id) {
        for (UserBalance savedUserBalance : UserBalances) {
            if (savedUserBalance.getId().equals(id)) {
                return savedUserBalance;
            }
        }
        return null;
    }

    public List<UserBalance> getAllUserBalance() {
        return new ArrayList<>(UserBalances);
    }
}
